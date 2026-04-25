param(
    [string]$Profile = "foundation",
    [switch]$Clean
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = Split-Path -Parent (Split-Path -Parent $scriptDir)
$profilePath = Join-Path $scriptDir "smoke-profiles.json"
$smokeRoot = Join-Path $root ".smoke"
$profileRoot = Join-Path $smokeRoot $Profile
$pluginsDir = Join-Path $profileRoot "artifacts/plugins"

function Resolve-MavenCommand {
    $mvn = Get-Command mvn -ErrorAction SilentlyContinue
    if ($mvn) {
        return $mvn.Source
    }

    $fallbacks = @(
        "C:\Users\pablo\Tools\apache-maven-3.9.9\bin\mvn.cmd",
        "C:\Users\pablo\Tools\apache-maven-3.9.9\bin\mvn.bat"
    )

    foreach ($candidate in $fallbacks) {
        if (Test-Path $candidate) {
            return $candidate
        }
    }

    throw "No se encontro Maven en PATH ni en rutas fallback conocidas."
}

function Get-ProfileConfig {
    if (-not (Test-Path $profilePath)) {
        throw "No existe $profilePath"
    }

    $profiles = (Get-Content $profilePath -Raw | ConvertFrom-Json).profiles
    if (-not $profiles.PSObject.Properties.Name.Contains($Profile)) {
        $available = $profiles.PSObject.Properties.Name -join ", "
        throw "Perfil smoke '$Profile' no existe. Perfiles disponibles: $available"
    }

    return $profiles.$Profile
}

function Find-ModuleJar {
    param([string]$Module)

    $modulePath = Join-Path $root $Module
    $candidates = @()

    $target = Join-Path $modulePath "target"
    if (Test-Path $target) {
        $candidates += Get-ChildItem $target -Filter "*.jar" -File |
            Where-Object {
                $_.Name -notmatch "^(original-|.*-(sources|javadoc|tests)\.jar$)"
            }
    }

    $libs = Join-Path $modulePath "build/libs"
    if (Test-Path $libs) {
        $candidates += Get-ChildItem $libs -Filter "*.jar" -File |
            Where-Object {
                $_.Name -notmatch "(-sources|-javadoc|-plain)\.jar$"
            }
    }

    if (-not $candidates -or $candidates.Count -eq 0) {
        throw "No se encontro jar generado para $Module"
    }

    return ($candidates | Sort-Object LastWriteTimeUtc -Descending | Select-Object -First 1)
}

function Get-RootReactorModules {
    $pomFile = Join-Path $root "pom.xml"
    $text = [System.IO.File]::ReadAllText($pomFile)
    $rx = [regex]::new("(?m)^\s*<module>\s*([^<]+?)\s*</module>\s*$")
    $list = [System.Collections.Generic.List[string]]::new()
    foreach ($m in $rx.Matches($text)) {
        $rel = ($m.Groups[1].Value.Trim() -replace '\\', '/')
        $list.Add($rel) | Out-Null
    }
    if ($list.Count -eq 0) {
        throw "No se pudieron leer <module> del pom raiz."
    }
    return $list.ToArray()
}

function Normalize-ModulePath {
    param([string]$Path)
    return ($Path -replace '\\', '/').TrimEnd('/')
}

function Test-ModuleExcluded {
    param(
        [string]$Module,
        [string[]]$Excludes
    )
    $norm = Normalize-ModulePath $Module
    foreach ($ex in $Excludes) {
        if ($norm -eq (Normalize-ModulePath $ex)) {
            return $true
        }
    }
    return $false
}

function Read-PluginNameFromModule {
    param([string]$Module)
    $yp = Join-Path (Join-Path $root $Module) "src/main/resources/plugin.yml"
    if (-not (Test-Path $yp)) {
        return $null
    }
    foreach ($line in Get-Content $yp) {
        if ($line -match '^\s*name:\s*(.+)\s*$') {
            return $matches[1].Trim().Trim("'").Trim('"')
        }
    }
    return $null
}

function Test-JarContainsDoughConfig {
    param([string]$JarPath)
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $zip = [System.IO.Compression.ZipFile]::OpenRead($JarPath)
    try {
        foreach ($e in $zip.Entries) {
            $n = $e.FullName.Replace('\', '/')
            if ($n -eq 'dev/drake/dough/config/Config.class') {
                return $true
            }
        }
        return $false
    }
    finally {
        $zip.Dispose()
    }
}

function Merge-DoughIntoAddonJar {
    param(
        [string]$DoughJarPath,
        [string]$TargetJarPath
    )
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $readDough = [System.IO.Compression.ZipFile]::OpenRead($DoughJarPath)
    try {
        $writeTarget = [System.IO.Compression.ZipFile]::Open($TargetJarPath, [System.IO.Compression.ZipArchiveMode]::Update)
        try {
            foreach ($entry in $readDough.Entries) {
                $name = $entry.FullName.Replace('\', '/')
                if ($name.StartsWith('META-INF/', [System.StringComparison]::OrdinalIgnoreCase)) {
                    continue
                }
                if (-not $name.StartsWith('dev/drake/dough/', [System.StringComparison]::OrdinalIgnoreCase)) {
                    continue
                }
                $existing = $writeTarget.GetEntry($name)
                if ($null -ne $existing) {
                    $existing.Delete()
                }
                $dest = $writeTarget.CreateEntry($name, [System.IO.Compression.CompressionLevel]::Optimal)
                $in = $entry.Open()
                try {
                    $out = $dest.Open()
                    try {
                        $in.CopyTo($out)
                    }
                    finally {
                        $out.Dispose()
                    }
                }
                finally {
                    $in.Dispose()
                }
            }
        }
        finally {
            $writeTarget.Dispose()
        }
    }
    finally {
        $readDough.Dispose()
    }
}

$profileConfig = Get-ProfileConfig

if ($Clean -and (Test-Path $profileRoot)) {
    Remove-Item $profileRoot -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $pluginsDir | Out-Null

Push-Location $root
try {
    $mvn = Resolve-MavenCommand
    $reactorAll = $false
    if ($profileConfig.PSObject.Properties.Name -contains "mavenReactorAll") {
        $reactorAll = [bool]$profileConfig.mavenReactorAll
    }

    $excludeMavenModules = @()
    if ($profileConfig.PSObject.Properties.Name -contains "excludeMavenModules") {
        $excludeMavenModules = @($profileConfig.excludeMavenModules)
    }

    if ($reactorAll) {
        Write-Host "==> Maven reactor completo (smoke profile $Profile)" -ForegroundColor Cyan
        & $mvn -B -DskipTests package
        if ($LASTEXITCODE -ne 0) {
            throw "Fallo Maven package reactor completo para perfil smoke '$Profile'"
        }
    }
    else {
        $mavenModules = @($profileConfig.mavenModules)
        if ($mavenModules.Count -gt 0) {
            Write-Host "==> Maven smoke package: $($mavenModules -join ', ')" -ForegroundColor Cyan
            & $mvn -B -DskipTests package -pl ($mavenModules -join ",") -am
            if ($LASTEXITCODE -ne 0) {
                throw "Fallo Maven package para perfil smoke '$Profile'"
            }
        }
    }

    $gradleTasks = @($profileConfig.gradleTasks)
    if ($gradleTasks.Count -gt 0) {
        Write-Host "==> Gradle smoke build: $($gradleTasks -join ', ')" -ForegroundColor Cyan
        & .\gradlew --no-daemon @gradleTasks
        if ($LASTEXITCODE -ne 0) {
            throw "Fallo Gradle para perfil smoke '$Profile'"
        }
    }

    Get-ChildItem $pluginsDir -Filter "*.jar" -File -ErrorAction SilentlyContinue | Remove-Item -Force

    $copied = @()
    $expectedPlugins = [System.Collections.Generic.List[string]]::new()

    if ($reactorAll) {
        $allMods = Get-RootReactorModules
        foreach ($module in $allMods) {
            if (Test-ModuleExcluded -Module $module -Excludes $excludeMavenModules) {
                continue
            }
            $jar = Find-ModuleJar -Module $module
            $destination = Join-Path $pluginsDir $jar.Name
            Copy-Item $jar.FullName $destination -Force
            $copied += [pscustomobject]@{
                module = $module
                jar = $jar.FullName
                copiedTo = $destination
            }
            $pn = Read-PluginNameFromModule -Module $module
            if ($pn) {
                $expectedPlugins.Add($pn) | Out-Null
            }
            Write-Host "   + $($jar.Name)" -ForegroundColor Green
        }
    }
    else {
        $mavenModules = @($profileConfig.mavenModules)
        foreach ($module in $mavenModules) {
            $jar = Find-ModuleJar -Module $module
            $destination = Join-Path $pluginsDir $jar.Name
            Copy-Item $jar.FullName $destination -Force
            $copied += [pscustomobject]@{
                module = $module
                jar = $jar.FullName
                copiedTo = $destination
            }
            $pn = Read-PluginNameFromModule -Module $module
            if ($pn) {
                $expectedPlugins.Add($pn) | Out-Null
            }
            Write-Host "   + $($jar.Name)" -ForegroundColor Green
        }
    }

    $mergeDough = $false
    if ($profileConfig.PSObject.Properties.Name -contains "mergeDoughIntoAddonJars") {
        $mergeDough = [bool]$profileConfig.mergeDoughIntoAddonJars
    }
    if ($mergeDough) {
        $doughJar = Find-ModuleJar -Module "sources/dough-core"
        Write-Host "==> Inyectando paquete dev.drake.dough desde $($doughJar.Name) en addons que no lo incluyen" -ForegroundColor Cyan
        foreach ($jarFile in (Get-ChildItem $pluginsDir -Filter "*.jar" -File)) {
            if ($jarFile.Name -match '(?i)slimefun') {
                continue
            }
            if (Test-JarContainsDoughConfig -JarPath $jarFile.FullName) {
                continue
            }
            Write-Host "   merge dough -> $($jarFile.Name)" -ForegroundColor Yellow
            Merge-DoughIntoAddonJar -DoughJarPath $doughJar.FullName -TargetJarPath $jarFile.FullName
        }
    }

    $manifest = [ordered]@{
        profile = $Profile
        generatedAt = (Get-Date).ToString("o")
        pluginsDir = $pluginsDir
        artifacts = $copied
    }
    if ($expectedPlugins.Count -gt 0) {
        $manifest["expectedPlugins"] = @($expectedPlugins | Select-Object -Unique)
    }

    ($manifest | ConvertTo-Json -Depth 10) | Set-Content (Join-Path $profileRoot "artifact-manifest.json") -Encoding UTF8
    Write-Host "Smoke artifacts listos en $pluginsDir ($($copied.Count) jars)" -ForegroundColor Green
}
finally {
    Pop-Location
}
