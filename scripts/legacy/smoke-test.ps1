param(
    [ValidateSet("quick", "core", "extended")]
    [string]$Profile = "quick",
    [switch]$Offline
)

$ErrorActionPreference = "Stop"

$scriptsDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = Split-Path -Parent (Split-Path -Parent $scriptsDir)

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

    throw "No se encontro Maven ni en PATH ni en rutas fallback conocidas."
}

function Invoke-SmokeBuild {
    param(
        [string]$Maven,
        [string]$Module
    )

    $args = @("-pl", $Module, "-am", "-DskipTests", "package")
    if ($Offline) {
        $args = @("-o") + $args
    }

    Write-Host ""
    Write-Host "==> Smoke test: $Module" -ForegroundColor Cyan
    & $Maven @args

    if ($LASTEXITCODE -ne 0) {
        throw "Fallo el smoke test para $Module"
    }
}

$profiles = @{
    quick = @(
        "sources/dough-core",
        "sources/slimefun-core/Slimefun4-src",
        "sources/repos-to-port/PrivateStorage",
        "sources/repos-to-port/ElectricSpawners"
    )
    core = @(
        "sources/dough-core",
        "sources/slimefun-core/Slimefun4-src",
        "sources/batch-2-expansion/SefiLib",
        "sources/batch-2-expansion/InfinityLib",
        "sources/batch-2-expansion/Networks_Better_Compatibility",
        "sources/batch-2-expansion/Cultivation_Updated"
    )
    extended = @(
        "sources/dough-core",
        "sources/slimefun-core/Slimefun4-src",
        "sources/batch-2-expansion/SefiLib",
        "sources/batch-2-expansion/InfinityLib",
        "sources/batch-2-expansion/Networks_Better_Compatibility",
        "sources/batch-2-expansion/Cultivation_Updated",
        "sources/batch-2-expansion/SlimeTinker",
        "sources/batch-2-expansion/SMG",
        "sources/batch-2-expansion/Supreme",
        "sources/batch-2-expansion/TranscEndence",
        "sources/repos-to-port/PrivateStorage",
        "sources/repos-to-port/ElectricSpawners",
        "sources/repos-to-port/SFMobDrops",
        "sources/repos-to-port/GlobalWarming"
    )
}

$maven = Resolve-MavenCommand
$modules = $profiles[$Profile]

Write-Host "Smoke test profile: $Profile" -ForegroundColor Green
Write-Host "Repo root: $root"
Write-Host "Maven: $maven"

Push-Location $root
try {
    foreach ($module in $modules) {
        Invoke-SmokeBuild -Maven $maven -Module $module
    }

    Write-Host ""
    Write-Host "Smoke test completado sin errores." -ForegroundColor Green
}
finally {
    Pop-Location
}
