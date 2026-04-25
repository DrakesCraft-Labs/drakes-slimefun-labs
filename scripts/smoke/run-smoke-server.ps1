param(
    [string]$Profile = "foundation",
    [string]$MinecraftVersion = "",
    [int]$TimeoutSeconds = 90,
    [switch]$NoBuild,
    [switch]$Clean
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = Split-Path -Parent (Split-Path -Parent $scriptDir)
$profilePath = Join-Path $scriptDir "smoke-profiles.json"
$profileRoot = Join-Path (Join-Path $root ".smoke") $Profile
$pluginsSource = Join-Path $profileRoot "artifacts/plugins"
$serverDir = Join-Path $profileRoot "server"
$logPath = Join-Path $serverDir "logs/latest.log"

function Get-ProfileConfig {
    $profiles = (Get-Content $profilePath -Raw | ConvertFrom-Json).profiles
    if (-not $profiles.PSObject.Properties.Name.Contains($Profile)) {
        $available = $profiles.PSObject.Properties.Name -join ", "
        throw "Perfil smoke '$Profile' no existe. Perfiles disponibles: $available"
    }

    return $profiles.$Profile
}

function Invoke-PaperDownload {
    if (Test-Path $serverJar) {
        return
    }

    Write-Host "==> Descargando Paper $MinecraftVersion" -ForegroundColor Cyan
    $buildsUri = "https://api.papermc.io/v2/projects/paper/versions/$MinecraftVersion/builds"
    $builds = Invoke-RestMethod -Uri $buildsUri
    $latest = $builds.builds | Sort-Object build -Descending | Select-Object -First 1
    if (-not $latest) {
        throw "No se encontro build de Paper para $MinecraftVersion"
    }

    $downloadName = $latest.downloads.application.name
    $downloadUri = "https://api.papermc.io/v2/projects/paper/versions/$MinecraftVersion/builds/$($latest.build)/downloads/$downloadName"
    Invoke-WebRequest -Uri $downloadUri -OutFile $serverJar
}

function Initialize-ServerDirectory {
    if ($Clean -and (Test-Path $serverDir)) {
        Remove-Item $serverDir -Recurse -Force
    }

    New-Item -ItemType Directory -Force -Path $serverDir | Out-Null
    New-Item -ItemType Directory -Force -Path (Join-Path $serverDir "plugins") | Out-Null
    "eula=true" | Set-Content (Join-Path $serverDir "eula.txt") -Encoding ASCII
    @"
online-mode=false
motd=DrakesCraft smoke test
enable-command-block=false
allow-flight=true
view-distance=4
simulation-distance=4
spawn-protection=0
"@ | Set-Content (Join-Path $serverDir "server.properties") -Encoding ASCII
}

function Copy-SmokePlugins {
    if (-not (Test-Path $pluginsSource)) {
        throw "No existen artifacts smoke en $pluginsSource. Ejecuta build-smoke-artifacts.ps1 primero."
    }

    $pluginDir = Join-Path $serverDir "plugins"
    Get-ChildItem $pluginDir -Filter "*.jar" -File -ErrorAction SilentlyContinue | Remove-Item -Force
    Copy-Item (Join-Path $pluginsSource "*.jar") $pluginDir -Force
}

function Wait-ForLogText {
    param(
        [string]$Text,
        [int]$Seconds
    )

    $deadline = (Get-Date).AddSeconds($Seconds)
    while ((Get-Date) -lt $deadline) {
        if ((Test-Path $logPath) -and ((Get-Content $logPath -Raw -ErrorAction SilentlyContinue) -match [regex]::Escape($Text))) {
            return $true
        }
        Start-Sleep -Seconds 1
    }

    return $false
}

function Stop-SmokeServer {
    param($Process)

    if ($Process -and -not $Process.HasExited) {
        try {
            $Process.StandardInput.WriteLine("stop")
            if (-not $Process.WaitForExit(30000)) {
                $Process.Kill($true)
            }
        } catch {
            if (-not $Process.HasExited) {
                $Process.Kill($true)
            }
        }
    }
}

function Ensure-ProtocolLibSmoke {
    param([string]$PluginDir)

    $dest = Join-Path $PluginDir "ProtocolLib.jar"
    if ((Test-Path $dest) -and ((Get-Item $dest).Length -gt 10000)) {
        return
    }

    $urls = @(
        "https://github.com/dmulloy2/ProtocolLib/releases/download/5.3.0/ProtocolLib.jar",
        "https://repo.dmulloy2.net/repository/public/com/comphenix/protocol/ProtocolLib/5.3.0/ProtocolLib-5.3.0.jar"
    )

    foreach ($u in $urls) {
        try {
            Write-Host "==> Descargando ProtocolLib (fallback pwsh): $u" -ForegroundColor Cyan
            Invoke-WebRequest -Uri $u -OutFile $dest -UseBasicParsing
            if ((Test-Path $dest) -and ((Get-Item $dest).Length -gt 10000)) {
                return
            }
        } catch {
            Write-Warning "ProtocolLib download fallo: $($_.Exception.Message)"
        }
    }

    Write-Warning "No se pudo colocar ProtocolLib.jar en $PluginDir (SoundMuffler puede no cargar)."
}

function Assert-SmokeLog {
    param($ProfileConfig)

    if (-not (Test-Path $logPath)) {
        throw "No se genero log de servidor en $logPath"
    }

    $log = Get-Content $logPath -Raw
    $fatalPatterns = @(
        "Could not load 'plugins/.*\.jar'",
        "Could not load plugin '",
        "Error occurred while enabling",
        "Error occurred while disabling",
        "Plugin .* generated an exception",
        "java\.lang\.NoClassDefFoundError",
        "\[SEVERE\]"
    )

    foreach ($pattern in $fatalPatterns) {
        if ($log -match $pattern) {
            throw "Smoke runtime fallo: el log contiene '$pattern'. Revisa $logPath"
        }
    }

    $strictPlugins = $false
    if ($ProfileConfig.PSObject.Properties.Name -contains "strictExpectedPlugins") {
        $strictPlugins = [bool]$ProfileConfig.strictExpectedPlugins
    }

    foreach ($plugin in @($ProfileConfig.expectedPlugins)) {
        $pluginName = [regex]::Escape($plugin)
        if ($log -notmatch "\[$pluginName\] (Loading|Enabling|Disabling) " -and $log -notmatch "Enabled plugin '$pluginName'") {
            if ($strictPlugins) {
                throw "Smoke runtime fallo: no aparecio carga/enabling para el plugin esperado '$plugin'."
            }
            Write-Warning "No se encontro marcador claro de carga para plugin esperado '$plugin'."
        }
    }

    foreach ($marker in @($ProfileConfig.requiredLogMarkers)) {
        if ($log -notmatch [regex]::Escape($marker)) {
            throw "Smoke runtime fallo: no aparecio el marcador requerido '$marker' en consola."
        }
    }
}

$profileConfig = Get-ProfileConfig

$mc = $MinecraftVersion
if ([string]::IsNullOrWhiteSpace($mc)) {
    if ($profileConfig.PSObject.Properties.Name -contains "minecraftVersion" -and $profileConfig.minecraftVersion) {
        $mc = [string]$profileConfig.minecraftVersion
    } else {
        $mc = "1.21.1"
    }
}
$MinecraftVersion = $mc
$serverJar = Join-Path $serverDir "paper-$MinecraftVersion.jar"
Write-Host "==> Paper Minecraft version: $MinecraftVersion (jar: $serverJar)" -ForegroundColor DarkGray

if (-not $NoBuild) {
    & (Join-Path $scriptDir "build-smoke-artifacts.ps1") -Profile $Profile -Clean:$Clean
    if ($LASTEXITCODE -ne 0) {
        throw "No se pudieron construir artifacts smoke."
    }
}

$manifestPath = Join-Path $profileRoot "artifact-manifest.json"
if (Test-Path $manifestPath) {
    $mj = Get-Content $manifestPath -Raw | ConvertFrom-Json
    if ($mj.PSObject.Properties.Name -contains "expectedPlugins" -and $mj.expectedPlugins) {
        $names = @($mj.expectedPlugins)
        if ($names.Count -gt 0) {
            $profileConfig | Add-Member -MemberType NoteProperty -Name expectedPlugins -Value $names -Force
        }
    }
}

$waitSeconds = $TimeoutSeconds
if ($profileConfig.PSObject.Properties.Name -contains "startupTimeoutSeconds" -and $profileConfig.startupTimeoutSeconds) {
    $minFromProfile = [int]$profileConfig.startupTimeoutSeconds
    if ($minFromProfile -gt $waitSeconds) {
        $waitSeconds = $minFromProfile
    }
}

Initialize-ServerDirectory
Invoke-PaperDownload
Copy-SmokePlugins

$pluginDirForDeps = Join-Path $serverDir "plugins"
$fetchDeps = Join-Path $scriptDir "fetch_smoke_optional_deps.py"
if (Test-Path $fetchDeps) {
    $python = Get-Command python -ErrorAction SilentlyContinue
    if (-not $python) {
        $python = Get-Command python3 -ErrorAction SilentlyContinue
    }
    if (-not $python) {
        $python = Get-Command py -ErrorAction SilentlyContinue
    }
    if ($python) {
        Write-Host "==> Dependencias opcionales smoke (ProtocolLib, etc.)" -ForegroundColor Cyan
        & $python.Source $fetchDeps $pluginDirForDeps
    } else {
        Write-Warning "Python no encontrado: se usara descarga directa de ProtocolLib (pwsh)."
    }
}

Ensure-ProtocolLibSmoke -PluginDir $pluginDirForDeps

Write-Host "==> Iniciando servidor smoke ($Profile, MC $MinecraftVersion, timeout ${waitSeconds}s)" -ForegroundColor Cyan
$psi = [System.Diagnostics.ProcessStartInfo]::new()
$psi.FileName = "java"
$psi.Arguments = "-Xms512M -Xmx2G -jar `"$serverJar`" --nogui"
$psi.WorkingDirectory = $serverDir
$psi.RedirectStandardInput = $true
$psi.RedirectStandardOutput = $false
$psi.RedirectStandardError = $false
$psi.UseShellExecute = $false

$process = [System.Diagnostics.Process]::Start($psi)
try {
    if (-not (Wait-ForLogText -Text "Done (" -Seconds $waitSeconds)) {
        throw "El servidor no llego a estado Done en $waitSeconds segundos."
    }

    $postReadySleep = 5
    if ($profileConfig.PSObject.Properties.Name -contains "postReadySleepSeconds" -and $profileConfig.postReadySleepSeconds) {
        $postReadySleep = [int]$profileConfig.postReadySleepSeconds
    }
    Start-Sleep -Seconds $postReadySleep
}
finally {
    Stop-SmokeServer -Process $process
}

Assert-SmokeLog -ProfileConfig $profileConfig
Write-Host "Smoke runtime OK. Log: $logPath" -ForegroundColor Green
