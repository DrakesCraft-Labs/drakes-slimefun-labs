param(
    [string]$Nombre,
    [switch]$Listar
)

$ErrorActionPreference = "Stop"

$scriptsDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = Split-Path -Parent (Split-Path -Parent $scriptsDir)
$catalogoPath = Join-Path $root "catalog\\slimefun\\audit\\addons-catalogo-oficial.csv"
$downloadsPath = Join-Path $root "deploy\\purpur-1.20.6\\plugins-confirmed\\slimefun-upstream-ready"

if (-not (Test-Path $catalogoPath)) {
    throw "No se encontro el catalogo: $catalogoPath"
}

if (-not (Test-Path $downloadsPath)) {
    New-Item -ItemType Directory -Path $downloadsPath | Out-Null
}

$catalogo = Import-Csv $catalogoPath

if ($Listar) {
    $catalogo | Select-Object tipo, nombre, owner_repo | Format-Table -AutoSize
    exit 0
}

if ([string]::IsNullOrWhiteSpace($Nombre)) {
    throw "Debes indicar -Nombre o usar -Listar"
}

$addon = $catalogo | Where-Object { $_.nombre -eq $Nombre } | Select-Object -First 1

if ($null -eq $addon) {
    throw "No se encontro el addon '$Nombre' en el catalogo"
}

$repo = $addon.owner_repo
$apiUrl = "https://api.github.com/repos/$repo/releases/latest"
$headers = @{
    "User-Agent" = "DrakesSlimefunAddonDownloader"
    "Accept" = "application/vnd.github+json"
}

$release = Invoke-RestMethod -Uri $apiUrl -Headers $headers
$asset = $release.assets | Where-Object { $_.name -like "*.jar" } | Select-Object -First 1

if ($null -eq $asset) {
    throw "La ultima release de $repo no tiene un asset .jar descargable"
}

$destino = Join-Path $downloadsPath $asset.name
Invoke-WebRequest -Uri $asset.browser_download_url -Headers $headers -OutFile $destino

Write-Host "Addon descargado:" $asset.name
Write-Host "Repo:" $repo
Write-Host "Release:" $release.tag_name
Write-Host "Destino:" $destino
