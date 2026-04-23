# Script para generar la lista de proyectos para Maven -pl basado en carpetas
param (
    [string]$BatchName
)

$Modules = @()

switch ($BatchName) {
    "foundation" {
        $Modules = "sources/dough-core", "sources/slimefun-core/Slimefun4-src", "sources/batch-2-expansion/InfinityLib", "sources/batch-2-expansion/SefiLib"
    }
    "standard" {
        $Modules = Get-ChildItem -Path "sources/repos-to-port" -Directory | ForEach-Object { "sources/repos-to-port/" + $_.Name }
    }
    "expansion" {
        $Modules = Get-ChildItem -Path "sources/batch-2-expansion" -Directory | Where-Object { $_.Name -notin "InfinityLib", "SefiLib" } | ForEach-Object { "sources/batch-2-expansion/" + $_.Name }
    }
    "community" {
        $Modules = Get-ChildItem -Path "sources/community-addons" -Directory | ForEach-Object { "sources/community-addons/" + $_.Name }
    }
}

$ProjectList = $Modules -join ","
Write-Output $ProjectList
