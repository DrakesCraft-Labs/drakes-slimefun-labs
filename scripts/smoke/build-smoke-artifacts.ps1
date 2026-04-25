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

$profileConfig = Get-ProfileConfig

if ($Clean -and (Test-Path $profileRoot)) {
    Remove-Item $profileRoot -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $pluginsDir | Out-Null

Push-Location $root
try {
    $mavenModules = @($profileConfig.mavenModules)
    if ($mavenModules.Count -gt 0) {
        $mvn = Resolve-MavenCommand
        Write-Host "==> Maven smoke package: $($mavenModules -join ', ')" -ForegroundColor Cyan
        & $mvn -B -DskipTests package -pl ($mavenModules -join ",") -am
        if ($LASTEXITCODE -ne 0) {
            throw "Fallo Maven package para perfil smoke '$Profile'"
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
    foreach ($module in $mavenModules) {
        $jar = Find-ModuleJar -Module $module
        $destination = Join-Path $pluginsDir $jar.Name
        Copy-Item $jar.FullName $destination -Force
        $copied += [pscustomobject]@{
            module = $module
            jar = $jar.FullName
            copiedTo = $destination
        }
        Write-Host "   + $($jar.Name)" -ForegroundColor Green
    }

    $manifest = [pscustomobject]@{
        profile = $Profile
        generatedAt = (Get-Date).ToString("o")
        pluginsDir = $pluginsDir
        artifacts = $copied
    }

    $manifest | ConvertTo-Json -Depth 8 | Set-Content (Join-Path $profileRoot "artifact-manifest.json") -Encoding UTF8
    Write-Host "Smoke artifacts listos en $pluginsDir" -ForegroundColor Green
}
finally {
    Pop-Location
}
