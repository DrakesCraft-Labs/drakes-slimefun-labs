$ErrorActionPreference = "Stop"

$scriptsDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = Split-Path -Parent (Split-Path -Parent $scriptsDir)
$source = Join-Path $root "sources\\slimefun-core\\Slimefun4-src"
$plugins = Join-Path $root "builds\\compiled-jars\\slimefun-core"

if (-not (Test-Path $source)) {
    throw "No se encontro el codigo fuente en $source"
}

if (-not (Test-Path $plugins)) {
    New-Item -ItemType Directory -Path $plugins | Out-Null
}

Push-Location $source
try {
    mvn clean package "-Dmaven.test.skip=true"

    $target = Join-Path $source "target"
    if (-not (Test-Path $target)) {
        throw "La compilacion no genero la carpeta target"
    }

    $jar = Get-ChildItem -Path $target -Filter "*.jar" |
        Where-Object { $_.Name -notlike "*sources.jar" -and $_.Name -notlike "*original-*" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if ($null -eq $jar) {
        throw "No se encontro el jar compilado en target"
    }

    Copy-Item -LiteralPath $jar.FullName -Destination (Join-Path $plugins $jar.Name) -Force
    Write-Host "Jar generado y copiado a $plugins\\$($jar.Name)"
}
finally {
    Pop-Location
}
