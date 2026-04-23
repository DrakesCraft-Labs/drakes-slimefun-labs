param(
    [switch]$DescargarCompatibles
)

$ErrorActionPreference = "Stop"

$scriptsDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$root = Split-Path -Parent (Split-Path -Parent $scriptsDir)
$catalogRoot = Join-Path $root "catalog\\slimefun\\audit"
$deployRoot = Join-Path $root "deploy\\purpur-1.20.6\\plugins-confirmed"
$catalogoPath = Join-Path $catalogRoot "addons-catalogo-oficial.csv"
$auditCsvPath = Join-Path $catalogRoot "addons-audit-1.20.6.csv"
$auditMdPath = Join-Path $catalogRoot "addons-audit-1.20.6.md"
$compatiblesDir = Join-Path $deployRoot "slimefun-upstream-ready"

if (-not (Test-Path $catalogoPath)) {
    throw "No se encontro el catalogo: $catalogoPath"
}

$headers = @{
    "User-Agent" = "DrakesSlimefunAudit"
    "Accept" = "application/json,text/html,*/*"
}

$catalogo = Import-Csv $catalogoPath
$reposIndex = Invoke-RestMethod -Uri "https://thebusybiscuit.github.io/builds/resources/repos.json" -Headers $headers -TimeoutSec 60
$repoEntries = foreach ($property in $reposIndex.PSObject.Properties) {
    $fullKey = $property.Name
    $repoPart, $branch = $fullKey -split ":", 2
    $owner, $repo = $repoPart -split "/", 2
    [PSCustomObject]@{
        FullKey = $fullKey
        RepoPart = $repoPart
        Owner = $owner
        Repo = $repo
        RepoLower = $repo.ToLowerInvariant()
        Branch = $branch
        Data = $property.Value
    }
}

$manualRepoAliases = @{
    "LuckyBlocks" = @("TheBusyBiscuit/luckyblocks-sf:master")
}

function Get-NormalizedText {
    param([string]$Text)

    if ([string]::IsNullOrWhiteSpace($Text)) {
        return ""
    }

    $normalized = $Text -replace "<[^>]+>", " "
    $normalized = $normalized -replace "&nbsp;", " "
    $normalized = $normalized -replace "\s+", " "
    return $normalized.Trim()
}

function Get-BuildSupportText {
    param(
        [object]$DependencyMap,
        [int]$BuildNumber
    )

    if ($null -eq $DependencyMap) {
        return ""
    }

    $entries = @()
    if ($DependencyMap -is [System.Collections.IDictionary]) {
        $entries = $DependencyMap.GetEnumerator()
    }
    else {
        $entries = $DependencyMap.PSObject.Properties | ForEach-Object {
            [PSCustomObject]@{
                Key = $_.Name
                Value = $_.Value
            }
        }
    }

    $selectedValue = $null
    $selectedBuild = -1

    foreach ($entry in $entries) {
        $entryBuild = [int]$entry.Key
        if ($entryBuild -le $BuildNumber -and $entryBuild -gt $selectedBuild) {
            $selectedBuild = $entryBuild
            $selectedValue = [string]$entry.Value
        }
    }

    return (Get-NormalizedText -Text $selectedValue)
}

function Test-Supports1206 {
    param([string]$SupportText)

    if ([string]::IsNullOrWhiteSpace($SupportText)) {
        return [PSCustomObject]@{
            Status = "revision-manual"
            Reason = "No hay soporte declarado"
        }
    }

    $text = $SupportText.ToLowerInvariant()

    if ($text -match "1\.20\.6" -or $text -match "1\.20\.x" -or $text -match "1\.20\+" -or $text -match "1\.19\.4\s*-\s*1\.20\.x") {
        return [PSCustomObject]@{
            Status = "compatible"
            Reason = "Soporte explicito para 1.20.x"
        }
    }

    if ($text -match "1\.(\d+)(?:\.x)?\+") {
        $startMinor = [int]$Matches[1]
        if ($startMinor -le 20) {
            return [PSCustomObject]@{
                Status = "compatible"
                Reason = "Soporte abierto que incluye 1.20.6"
            }
        }
    }

    if ($text -match "1\.16\+" -or $text -match "1\.17\+" -or $text -match "1\.18\+" -or $text -match "1\.19\+") {
        return [PSCustomObject]@{
            Status = "compatible"
            Reason = "Soporte abierto que incluye 1.20.6"
        }
    }

    if ($text -match "only 1\.14" -or $text -match "only 1\.15") {
        return [PSCustomObject]@{
            Status = "no-compatible"
            Reason = "Soporte declarado fuera de 1.20.6"
        }
    }

    $allMatches = [regex]::Matches($text, "1\.(\d+)")
    if ($allMatches.Count -gt 0) {
        $minorVersions = @()
        foreach ($match in $allMatches) {
            $minorVersions += [int]$match.Groups[1].Value
        }

        $maxMinor = ($minorVersions | Measure-Object -Maximum).Maximum
        if ($maxMinor -lt 20) {
            return [PSCustomObject]@{
                Status = "no-compatible"
                Reason = "El soporte declarado termina antes de 1.20"
            }
        }
    }

    return [PSCustomObject]@{
        Status = "revision-manual"
        Reason = "Formato de soporte no concluyente"
    }
}

function Get-DownloadFileNameFromUrl {
    param(
        [string]$Url,
        [string]$AddonName,
        [int]$BuildNumber
    )

    if ([string]::IsNullOrWhiteSpace($Url)) {
        return $null
    }

    $uri = [System.Uri]$Url
    $name = [System.IO.Path]::GetFileName($uri.AbsolutePath)
    if ([string]::IsNullOrWhiteSpace($name) -or $name -match '^\d+$' -or -not $name.EndsWith('.jar')) {
        return "$AddonName-$BuildNumber.jar"
    }

    return $name
}

function Get-MatchingRepoEntry {
    param(
        [pscustomobject]$Addon,
        [object[]]$RepoEntries
    )

    if ($manualRepoAliases.ContainsKey($Addon.nombre)) {
        foreach ($wantedKey in $manualRepoAliases[$Addon.nombre]) {
            $found = $RepoEntries | Where-Object { $_.FullKey -eq $wantedKey } | Select-Object -First 1
            if ($null -ne $found) {
                return $found
            }
        }
    }

    $exact = $RepoEntries | Where-Object { $_.RepoPart -eq $Addon.owner_repo } | Select-Object -First 1
    if ($null -ne $exact) {
        return $exact
    }

    $repoName = ($Addon.owner_repo -split "/", 2)[1]
    $sameRepo = $RepoEntries | Where-Object { $_.RepoLower -eq $repoName.ToLowerInvariant() }
    if (($sameRepo | Measure-Object).Count -ge 1) {
        return $sameRepo |
            Sort-Object @{ Expression = {
                switch ($_.Branch) {
                    "stable" { 0 }
                    "main" { 1 }
                    "master" { 2 }
                    "dev" { 3 }
                    default { 9 }
                }
            } } |
            Select-Object -First 1
    }

    $sameName = $RepoEntries | Where-Object { $_.RepoLower -eq $Addon.nombre.ToLowerInvariant() }
    if (($sameName | Measure-Object).Count -ge 1) {
        return $sameName |
            Sort-Object @{ Expression = {
                switch ($_.Branch) {
                    "stable" { 0 }
                    "main" { 1 }
                    "master" { 2 }
                    "dev" { 3 }
                    default { 9 }
                }
            } } |
            Select-Object -First 1
    }

    return $null
}

function Get-BlobBuildInfo {
    param(
        [string[]]$CandidateNames
    )

    foreach ($candidate in $CandidateNames) {
        if ([string]::IsNullOrWhiteSpace($candidate)) {
            continue
        }

        $url = "https://blob.build/project/$candidate"

        try {
            $response = Invoke-WebRequest -UseBasicParsing -Uri $url -Headers $headers -TimeoutSec 40
            $content = $response.Content

            $buildMatch = [regex]::Match(
                $content,
                'href="https://blob\.build/dl/(?<project>[^/]+)/(?<tab>[^/]+)/(?<build>\d+)".*?Supported versions <span class="text-primary">(?<support>[^<]+)',
                [System.Text.RegularExpressions.RegexOptions]::Singleline
            )

            if (-not $buildMatch.Success) {
                continue
            }

            $headerSupportMatch = [regex]::Match(
                $content,
                'Supported versions <span class="text-primary">(?<support>[^<]+)',
                [System.Text.RegularExpressions.RegexOptions]::Singleline
            )

            $supportText = if ($headerSupportMatch.Success) {
                $headerSupportMatch.Groups["support"].Value.Trim()
            }
            else {
                $buildMatch.Groups["support"].Value.Trim()
            }

            return [PSCustomObject]@{
                Source = "blob.build"
                Build = [int]$buildMatch.Groups["build"].Value
                Support = $supportText
                DownloadUrl = "https://blob.build/dl/$($buildMatch.Groups["project"].Value)/$($buildMatch.Groups["tab"].Value)/$($buildMatch.Groups["build"].Value)"
                Project = $buildMatch.Groups["project"].Value
            }
        }
        catch {
            continue
        }
    }

    return $null
}

$results = foreach ($addon in $catalogo) {
    $repoEntry = Get-MatchingRepoEntry -Addon $addon -RepoEntries $repoEntries

    if ($null -eq $repoEntry) {
        [PSCustomObject]@{
            tipo = $addon.tipo
            nombre = $addon.nombre
            owner_repo = $addon.owner_repo
            fuente = "catalogo"
            build_key = ""
            rama = ""
            ultima_build = ""
            fecha = ""
            soporte = ""
            compatibilidad_1206 = "revision-manual"
            razon = "No encontre metadata oficial de builds para este addon"
            download_url = ""
            archivo = ""
            estado_descarga = "omitido"
        }
        continue
    }

    $repoName = ($addon.owner_repo -split "/", 2)[1]
    $blobInfo = Get-BlobBuildInfo -CandidateNames @($addon.nombre, $repoName)

    $buildDirectory = if ($repoEntry.Data.options.custom_directory) {
        [string]$repoEntry.Data.options.custom_directory
    }
    else {
        "{0}/{1}/{2}" -f $repoEntry.Owner, $repoEntry.Repo, $repoEntry.Branch
    }

    $buildsUrl = "https://thebusybiscuit.github.io/builds/$buildDirectory/builds.json"
    $builds = Invoke-RestMethod -Uri $buildsUrl -Headers $headers -TimeoutSec 60
    $lastSuccessful = [int]$builds.last_successful
    $buildData = $builds.PSObject.Properties[$lastSuccessful.ToString()].Value
    $supportMap = $repoEntry.Data.dependencies.'Minecraft Version(s)'
    $supportText = Get-BuildSupportText -DependencyMap $supportMap -BuildNumber $lastSuccessful
    $downloadUrl = "https://thebusybiscuit.github.io/builds/$buildDirectory/$($repoEntry.Repo)-$lastSuccessful.jar"
    $sourceName = "builds-oficiales"

    if ($null -ne $blobInfo) {
        $lastSuccessful = $blobInfo.Build
        $supportText = $blobInfo.Support
        $downloadUrl = $blobInfo.DownloadUrl
        $sourceName = $blobInfo.Source
    }

    $compatibility = Test-Supports1206 -SupportText $supportText

    $estadoDescarga = "omitido"
    $fileName = Get-DownloadFileNameFromUrl -Url $downloadUrl -AddonName $addon.nombre -BuildNumber $lastSuccessful

    if ($DescargarCompatibles -and $compatibility.Status -eq "compatible") {
        if (-not (Test-Path $compatiblesDir)) {
            New-Item -ItemType Directory -Path $compatiblesDir | Out-Null
        }
        $destinationPath = Join-Path $compatiblesDir $fileName
        Invoke-WebRequest -Uri $downloadUrl -Headers $headers -OutFile $destinationPath -TimeoutSec 120
        $estadoDescarga = "descargado"
    }

    [PSCustomObject]@{
        tipo = $addon.tipo
        nombre = $addon.nombre
        owner_repo = $addon.owner_repo
        fuente = $sourceName
        build_key = $repoEntry.FullKey
        rama = $repoEntry.Branch
        ultima_build = $lastSuccessful
        fecha = $buildData.date
        soporte = $supportText
        compatibilidad_1206 = $compatibility.Status
        razon = $compatibility.Reason
        download_url = $downloadUrl
        archivo = $fileName
        estado_descarga = $estadoDescarga
    }
}

$results | Sort-Object compatibilidad_1206, nombre | Export-Csv -NoTypeInformation -Encoding UTF8 $auditCsvPath

$compatibles = $results | Where-Object { $_.compatibilidad_1206 -eq "compatible" } | Sort-Object nombre
$noCompatibles = $results | Where-Object { $_.compatibilidad_1206 -eq "no-compatible" } | Sort-Object nombre
$manuales = $results | Where-Object { $_.compatibilidad_1206 -eq "revision-manual" } | Sort-Object nombre

$md = [System.Collections.Generic.List[string]]::new()
$md.Add("# Auditoria Slimefun para Purpur 1.20.6")
$md.Add("")
$md.Add("Fecha de generacion: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss zzz')")
$md.Add("")
$md.Add("Fuente principal: https://thebusybiscuit.github.io/builds/resources/repos.json")
$md.Add("")
$md.Add("## Compatibles")
$md.Add("")
if ($compatibles.Count -eq 0) {
    $md.Add("No se encontro ningun addon con soporte declarado para 1.20.6.")
}
else {
    foreach ($item in $compatibles) {
        $md.Add("- $($item.nombre): build $($item.ultima_build), soporte '$($item.soporte)', fecha '$($item.fecha)'")
        $md.Add("  download: $($item.download_url)")
    }
}
$md.Add("")
$md.Add("## No compatibles")
$md.Add("")
if ($noCompatibles.Count -eq 0) {
    $md.Add("No se encontro ningun addon marcado explicitamente como incompatible.")
}
else {
    foreach ($item in $noCompatibles) {
        $md.Add("- $($item.nombre): build $($item.ultima_build), soporte '$($item.soporte)', razon '$($item.razon)'")
        $md.Add("  download: $($item.download_url)")
    }
}
$md.Add("")
$md.Add("## Revision manual")
$md.Add("")
if ($manuales.Count -eq 0) {
    $md.Add("No quedaron addons pendientes de revision manual.")
}
else {
    foreach ($item in $manuales) {
        $md.Add("- $($item.nombre): $($item.razon)")
    }
}

Set-Content -Path $auditMdPath -Value $md -Encoding UTF8

$results | Sort-Object compatibilidad_1206, nombre | Format-Table nombre, compatibilidad_1206, ultima_build, soporte -AutoSize
