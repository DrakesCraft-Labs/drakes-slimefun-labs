param(
    [string]$Profile = "foundation",
    [string]$MinecraftVersion = "1.21.1",
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
$serverJar = Join-Path $serverDir "paper-$MinecraftVersion.jar"
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

function Assert-SmokeLog {
    param($ProfileConfig)

    if (-not (Test-Path $logPath)) {
        throw "No se genero log de servidor en $logPath"
    }

    $log = Get-Content $logPath -Raw
    $fatalPatterns = @(
        "Could not load 'plugins/.*\.jar'",
        "Error occurred while enabling",
        "Error occurred while disabling",
        "Plugin .* generated an exception",
        "java\.lang\.NoClassDefFoundError",
        "java\.lang\.ClassNotFoundException",
        "\[SEVERE\]"
    )

    foreach ($pattern in $fatalPatterns) {
        if ($log -match $pattern) {
            throw "Smoke runtime fallo: el log contiene '$pattern'. Revisa $logPath"
        }
    }

    foreach ($plugin in @($ProfileConfig.expectedPlugins)) {
        $pluginName = [regex]::Escape($plugin)
        if ($log -notmatch "\[$pluginName\] (Loading|Enabling|Disabling) " -and $log -notmatch "Enabled plugin '$pluginName'") {
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

if (-not $NoBuild) {
    & (Join-Path $scriptDir "build-smoke-artifacts.ps1") -Profile $Profile -Clean:$Clean
    if ($LASTEXITCODE -ne 0) {
        throw "No se pudieron construir artifacts smoke."
    }
}

Initialize-ServerDirectory
Invoke-PaperDownload
Copy-SmokePlugins

Write-Host "==> Iniciando servidor smoke ($Profile)" -ForegroundColor Cyan
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
    if (-not (Wait-ForLogText -Text "Done (" -Seconds $TimeoutSeconds)) {
        throw "El servidor no llego a estado Done en $TimeoutSeconds segundos."
    }

    Start-Sleep -Seconds 5
}
finally {
    Stop-SmokeServer -Process $process
}

Assert-SmokeLog -ProfileConfig $profileConfig
Write-Host "Smoke runtime OK. Log: $logPath" -ForegroundColor Green
