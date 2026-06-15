param(
    [string]$PackRoot = (Join-Path $PSScriptRoot "..\..\Jackstar-Slimefun-Drakecraft")
)

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing

$blockSheets = [ordered]@{
    "01-planks-a.png" = @(
        "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
        "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks"
    )
    "02-planks-b-terrain-a.png" = @(
        "bamboo_planks", "crimson_planks", "warped_planks", "dirt",
        "grass_block_top", "grass_block_side", "stone", "cobblestone"
    )
    "03-terrain-b-ores-a.png" = @(
        "deepslate", "cobbled_deepslate", "blackstone", "coal_ore",
        "iron_ore", "copper_ore", "gold_ore", "redstone_ore"
    )
    "04-ores-b.png" = @(
        "emerald_ore", "lapis_ore", "diamond_ore", "deepslate_coal_ore",
        "deepslate_iron_ore", "deepslate_copper_ore",
        "deepslate_gold_ore", "deepslate_redstone_ore"
    )
    "05-ores-c.png" = @(
        "deepslate_emerald_ore", "deepslate_lapis_ore",
        "deepslate_diamond_ore", "nether_quartz_ore",
        "nether_gold_ore", "ancient_debris"
    )
}

$toolSheets = [ordered]@{
    "06-tools-wood-stone.png" = @(
        "wooden_pickaxe", "wooden_sword", "wooden_axe", "wooden_hoe",
        "stone_pickaxe", "stone_sword", "stone_axe", "stone_hoe"
    )
    "07-tools-iron-gold.png" = @(
        "iron_pickaxe", "iron_sword", "iron_axe", "iron_hoe",
        "golden_pickaxe", "golden_sword", "golden_axe", "golden_hoe"
    )
    "08-tools-diamond-netherite.png" = @(
        "diamond_pickaxe", "diamond_sword", "diamond_axe", "diamond_hoe",
        "netherite_pickaxe", "netherite_sword", "netherite_axe", "netherite_hoe"
    )
}

function Get-CellRectangle {
    param(
        [System.Drawing.Bitmap]$Image,
        [int]$Index
    )

    $column = $Index % 4
    $row = [math]::Floor($Index / 4)
    $left = [math]::Round($column * $Image.Width / 4)
    $right = [math]::Round(($column + 1) * $Image.Width / 4)
    $top = [math]::Round($row * $Image.Height / 2)
    $bottom = [math]::Round(($row + 1) * $Image.Height / 2)
    return [System.Drawing.Rectangle]::new(
        $left,
        $top,
        $right - $left,
        $bottom - $top
    )
}

function Save-ScaledCell {
    param(
        [System.Drawing.Bitmap]$Source,
        [System.Drawing.Rectangle]$Cell,
        [string]$Destination
    )

    $output = [System.Drawing.Bitmap]::new(
        32,
        32,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )
    $graphics = [System.Drawing.Graphics]::FromImage($output)
    try {
        $graphics.InterpolationMode =
            [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
        $graphics.PixelOffsetMode =
            [System.Drawing.Drawing2D.PixelOffsetMode]::Half
        $graphics.DrawImage(
            $Source,
            [System.Drawing.Rectangle]::new(0, 0, 32, 32),
            $Cell,
            [System.Drawing.GraphicsUnit]::Pixel
        )
        $output.Save($Destination, [System.Drawing.Imaging.ImageFormat]::Png)
    } finally {
        $graphics.Dispose()
        $output.Dispose()
    }
}

function Save-TransparentTool {
    param(
        [System.Drawing.Bitmap]$Source,
        [System.Drawing.Rectangle]$Cell,
        [string]$Destination
    )

    $isolated = [System.Drawing.Bitmap]::new(
        $Cell.Width,
        $Cell.Height,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )
    $graphics = [System.Drawing.Graphics]::FromImage($isolated)
    try {
        $graphics.DrawImage(
            $Source,
            [System.Drawing.Rectangle]::new(0, 0, $Cell.Width, $Cell.Height),
            $Cell,
            [System.Drawing.GraphicsUnit]::Pixel
        )
    } finally {
        $graphics.Dispose()
    }

    $minX = $isolated.Width
    $minY = $isolated.Height
    $maxX = -1
    $maxY = -1

    for ($y = 0; $y -lt $isolated.Height; $y++) {
        for ($x = 0; $x -lt $isolated.Width; $x++) {
            $color = $isolated.GetPixel($x, $y)
            $isChroma = (
                $color.R -gt 170 -and
                $color.B -gt 160 -and
                $color.G -lt 135 -and
                ($color.R - $color.G) -gt 65 -and
                ($color.B - $color.G) -gt 55
            )
            if ($isChroma) {
                $isolated.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
                continue
            }
            $minX = [math]::Min($minX, $x)
            $minY = [math]::Min($minY, $y)
            $maxX = [math]::Max($maxX, $x)
            $maxY = [math]::Max($maxY, $y)
        }
    }

    if ($maxX -lt 0) {
        $isolated.Dispose()
        throw "No se encontró herramienta en $Destination"
    }

    $cropWidth = $maxX - $minX + 1
    $cropHeight = $maxY - $minY + 1
    $side = [math]::Max($cropWidth, $cropHeight) + 24
    $square = [System.Drawing.Bitmap]::new(
        $side,
        $side,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )
    $squareGraphics = [System.Drawing.Graphics]::FromImage($square)
    try {
        $squareGraphics.Clear([System.Drawing.Color]::Transparent)
        $destinationX = [math]::Floor(($side - $cropWidth) / 2)
        $destinationY = [math]::Floor(($side - $cropHeight) / 2)
        $squareGraphics.DrawImage(
            $isolated,
            [System.Drawing.Rectangle]::new(
                $destinationX,
                $destinationY,
                $cropWidth,
                $cropHeight
            ),
            [System.Drawing.Rectangle]::new(
                $minX,
                $minY,
                $cropWidth,
                $cropHeight
            ),
            [System.Drawing.GraphicsUnit]::Pixel
        )
    } finally {
        $squareGraphics.Dispose()
        $isolated.Dispose()
    }

    $output = [System.Drawing.Bitmap]::new(
        32,
        32,
        [System.Drawing.Imaging.PixelFormat]::Format32bppArgb
    )
    $outputGraphics = [System.Drawing.Graphics]::FromImage($output)
    try {
        $outputGraphics.Clear([System.Drawing.Color]::Transparent)
        $outputGraphics.InterpolationMode =
            [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
        $outputGraphics.PixelOffsetMode =
            [System.Drawing.Drawing2D.PixelOffsetMode]::Half
        $outputGraphics.CompositingMode =
            [System.Drawing.Drawing2D.CompositingMode]::SourceCopy
        $outputGraphics.DrawImage(
            $square,
            [System.Drawing.Rectangle]::new(0, 0, 32, 32),
            [System.Drawing.Rectangle]::new(0, 0, $side, $side),
            [System.Drawing.GraphicsUnit]::Pixel
        )
        $output.Save($Destination, [System.Drawing.Imaging.ImageFormat]::Png)
    } finally {
        $outputGraphics.Dispose()
        $output.Dispose()
        $square.Dispose()
    }
}

$blockOutput = Join-Path $PackRoot "assets\minecraft\textures\block"
$itemOutput = Join-Path $PackRoot "assets\minecraft\textures\item"
[System.IO.Directory]::CreateDirectory($blockOutput) | Out-Null
[System.IO.Directory]::CreateDirectory($itemOutput) | Out-Null

foreach ($entry in $blockSheets.GetEnumerator()) {
    $sourcePath = Join-Path $PSScriptRoot $entry.Key
    $source = [System.Drawing.Bitmap]::new([string]$sourcePath)
    try {
        for ($index = 0; $index -lt $entry.Value.Count; $index++) {
            $destination = Join-Path $blockOutput "$($entry.Value[$index]).png"
            Save-ScaledCell $source (Get-CellRectangle $source $index) $destination
            Write-Output "[BLOCK] $($entry.Value[$index])"
        }
    } finally {
        $source.Dispose()
    }
}

foreach ($entry in $toolSheets.GetEnumerator()) {
    $sourcePath = Join-Path $PSScriptRoot $entry.Key
    $source = [System.Drawing.Bitmap]::new([string]$sourcePath)
    try {
        for ($index = 0; $index -lt $entry.Value.Count; $index++) {
            $destination = Join-Path $itemOutput "$($entry.Value[$index]).png"
            Save-TransparentTool $source (Get-CellRectangle $source $index) $destination
            Write-Output "[TOOL] $($entry.Value[$index])"
        }
    } finally {
        $source.Dispose()
    }
}

Write-Output "[SUCCESS] 62 texturas Coronalis generadas en $PackRoot"
