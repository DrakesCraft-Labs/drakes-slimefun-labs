import os
from PIL import Image

def get_cell_rectangle(width, height, index):
    column = index % 4
    row = index // 4
    left = int(round(column * width / 4.0))
    right = int(round((column + 1) * width / 4.0))
    top = int(round(row * height / 2.0))
    bottom = int(round((row + 1) * height / 2.0))
    return (left, top, right, bottom)

def is_magenta(color):
    r, g, b, a = color
    if a == 0:
        return True
    # Aggressive check for any magenta/purple background or mixed edge pixel:
    # R and B should be dominant over G
    return (r - g) > 20 and (b - g) > 20

def remove_background_floodfill(cell):
    # Convert cell to RGBA
    cell = cell.convert("RGBA")
    w, h = cell.size
    pixels = cell.load()
    
    visited = set()
    queue = []
    
    # Add all border pixels to queue
    for x in range(w):
        queue.append((x, 0))
        queue.append((x, h - 1))
        visited.add((x, 0))
        visited.add((x, h - 1))
    for y in range(h):
        queue.append((0, y))
        queue.append((w - 1, y))
        visited.add((0, y))
        visited.add((w - 1, y))
        
    while queue:
        x, y = queue.pop(0)
        color = pixels[x, y]
        if is_magenta(color):
            pixels[x, y] = (0, 0, 0, 0)
            
            for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nx, ny = x + dx, y + dy
                if 0 <= nx < w and 0 <= ny < h and (nx, ny) not in visited:
                    visited.add((nx, ny))
                    queue.append((nx, ny))
                    
    return cell

def save_scaled_cell(source, box, dest_path):
    cell = source.crop(box)
    cell = cell.resize((32, 32), Image.Resampling.NEAREST)
    cell.save(dest_path)

def save_transparent_tool(source, box, dest_path):
    cell = source.crop(box)
    cell = remove_background_floodfill(cell)
    
    # Find bounding box of non-transparent pixels
    w, h = cell.size
    pixels = cell.load()
    min_x, min_y = w, h
    max_x, max_y = -1, -1
    
    for y in range(h):
        for x in range(w):
            if pixels[x, y][3] > 0:
                min_x = min(min_x, x)
                min_y = min(min_y, y)
                max_x = max(max_x, x)
                max_y = max(max_y, y)
                
    if max_x < 0:
        raise ValueError(f"No tool found in cell for {dest_path}")
        
    crop_w = max_x - min_x + 1
    crop_h = max_y - min_y + 1
    side = max(crop_w, crop_h) + 24
    
    # Center in transparent square
    square = Image.new("RGBA", (side, side), (0, 0, 0, 0))
    dest_x = (side - crop_w) // 2
    dest_y = (side - crop_h) // 2
    
    tool_crop = cell.crop((min_x, min_y, max_x + 1, max_y + 1))
    square.paste(tool_crop, (dest_x, dest_y))
    
    # Resize to 32x32
    output = square.resize((32, 32), Image.Resampling.NEAREST)
    output.save(dest_path)

def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    pack_root = os.path.abspath(os.path.join(script_dir, "..", "..", "Jackstar-Slimefun-Drakecraft"))
    
    block_sheets = {
        "01-planks-a.png": [
            "oak_planks", "spruce_planks", "birch_planks", "jungle_planks",
            "acacia_planks", "dark_oak_planks", "mangrove_planks", "cherry_planks"
        ],
        "02-planks-b-terrain-a.png": [
            "bamboo_planks", "crimson_planks", "warped_planks", "dirt",
            "grass_block_top", "grass_block_side", "stone", "cobblestone"
        ],
        "03-terrain-b-ores-a.png": [
            "deepslate", "cobbled_deepslate", "blackstone", "coal_ore",
            "iron_ore", "copper_ore", "gold_ore", "redstone_ore"
        ],
        "04-ores-b.png": [
            "emerald_ore", "lapis_ore", "diamond_ore", "deepslate_coal_ore",
            "deepslate_iron_ore", "deepslate_copper_ore",
            "deepslate_gold_ore", "deepslate_redstone_ore"
        ],
        "05-ores-c.png": [
            "deepslate_emerald_ore", "deepslate_lapis_ore",
            "deepslate_diamond_ore", "nether_quartz_ore",
            "nether_gold_ore", "ancient_debris"
        ]
    }
    
    tool_sheets = {
        "06-tools-wood-stone.png": [
            "wooden_pickaxe", "wooden_sword", "wooden_axe", "wooden_hoe",
            "stone_pickaxe", "stone_sword", "stone_axe", "stone_hoe"
        ],
        "07-tools-iron-gold.png": [
            "iron_pickaxe", "iron_sword", "iron_axe", "iron_hoe",
            "golden_pickaxe", "golden_sword", "golden_axe", "golden_hoe"
        ],
        "08-tools-diamond-netherite.png": [
            "diamond_pickaxe", "diamond_sword", "diamond_axe", "diamond_hoe",
            "netherite_pickaxe", "netherite_sword", "netherite_axe", "netherite_hoe"
        ]
    }
    
    block_output = os.path.join(pack_root, "assets", "minecraft", "textures", "block")
    item_output = os.path.join(pack_root, "assets", "minecraft", "textures", "item")
    
    os.makedirs(block_output, exist_ok=True)
    os.makedirs(item_output, exist_ok=True)
    
    # Process blocks
    for sheet_name, names in block_sheets.items():
        sheet_path = os.path.join(script_dir, sheet_name)
        img = Image.open(sheet_path)
        w, h = img.size
        for i, name in enumerate(names):
            dest = os.path.join(block_output, f"{name}.png")
            box = get_cell_rectangle(w, h, i)
            save_scaled_cell(img, box, dest)
            print(f"[BLOCK] {name}")
            
    # Process tools
    for sheet_name, names in tool_sheets.items():
        sheet_path = os.path.join(script_dir, sheet_name)
        img = Image.open(sheet_path)
        w, h = img.size
        for i, name in enumerate(names):
            dest = os.path.join(item_output, f"{name}.png")
            box = get_cell_rectangle(w, h, i)
            save_transparent_tool(img, box, dest)
            print(f"[TOOL] {name}")
            
    print(f"[SUCCESS] 62 textures processed and saved to {pack_root}")

if __name__ == "__main__":
    main()
