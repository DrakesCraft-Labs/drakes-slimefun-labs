# Coronalis Vanilla 32x - Manifiesto de producción

## Objetivo

Reemplazar texturas vanilla con una identidad coherente con Coronalis:
metal oscuro, cobre envejecido, violeta profundo y acentos cian. Resolución
final: 32x32. Los archivos de esta carpeta son fuentes, no se incluyen en el
ZIP publicado por el workflow actual porque viven fuera de la raíz del pack.

## Estado

- [x] Lámina 01: tablones I
- [x] Lámina 02: tablones II + terreno I
- [x] Lámina 03: terreno II + minerales I
- [x] Lámina 04: minerales II
- [x] Lámina 05: minerales III
- [x] Lámina 06: herramientas madera + piedra
- [x] Lámina 07: herramientas hierro + oro
- [x] Lámina 08: herramientas diamante + netherita
- [x] Recorte 32x32
- [x] Integración en `assets/minecraft/textures`
- [x] ZIP y verificación visual (Desplegado en producción)

Checkpoint de fuentes generado: 2026-06-15. `contact-sheet.png` permite revisar
las ocho láminas sin abrirlas individualmente.

## Orden de celdas

Cada lámina usa una cuadrícula 4x2, de izquierda a derecha y de arriba abajo.

### 01-planks-a

1. `oak_planks`
2. `spruce_planks`
3. `birch_planks`
4. `jungle_planks`
5. `acacia_planks`
6. `dark_oak_planks`
7. `mangrove_planks`
8. `cherry_planks`

### 02-planks-b-terrain-a

1. `bamboo_planks`
2. `crimson_planks`
3. `warped_planks`
4. `dirt`
5. `grass_block_top`
6. `grass_block_side`
7. `stone`
8. `cobblestone`

### 03-terrain-b-ores-a

1. `deepslate`
2. `cobbled_deepslate`
3. `blackstone`
4. `coal_ore`
5. `iron_ore`
6. `copper_ore`
7. `gold_ore`
8. `redstone_ore`

### 04-ores-b

1. `emerald_ore`
2. `lapis_ore`
3. `diamond_ore`
4. `deepslate_coal_ore`
5. `deepslate_iron_ore`
6. `deepslate_copper_ore`
7. `deepslate_gold_ore`
8. `deepslate_redstone_ore`

### 05-ores-c

1. `deepslate_emerald_ore`
2. `deepslate_lapis_ore`
3. `deepslate_diamond_ore`
4. `nether_quartz_ore`
5. `nether_gold_ore`
6. `ancient_debris`
7. celda vacía
8. celda vacía

### 06-tools-wood-stone

1. `wooden_pickaxe`
2. `wooden_sword`
3. `wooden_axe`
4. `wooden_hoe`
5. `stone_pickaxe`
6. `stone_sword`
7. `stone_axe`
8. `stone_hoe`

### 07-tools-iron-gold

1. `iron_pickaxe`
2. `iron_sword`
3. `iron_axe`
4. `iron_hoe`
5. `golden_pickaxe`
6. `golden_sword`
7. `golden_axe`
8. `golden_hoe`

### 08-tools-diamond-netherite

1. `diamond_pickaxe`
2. `diamond_sword`
3. `diamond_axe`
4. `diamond_hoe`
5. `netherite_pickaxe`
6. `netherite_sword`
7. `netherite_axe`
8. `netherite_hoe`

## Reglas de recorte

1. Conservar cada PNG fuente original.
2. Separar la cuadrícula en cuatro columnas y dos filas.
3. Bloques: recortar cada celda completa y escalar a 32x32 sin suavizado.
4. Herramientas: retirar el croma, centrar el objeto en un lienzo cuadrado
   transparente y escalar a 32x32 con `NearestNeighbor`.
5. No sobrescribir una textura final sin revisar primero una vista previa.
6. Validar dimensiones, canal alfa, nombres exactos y `git diff`.

## Libro Coronalis

`Items.java` ya define:

- Título: `Manual Coronalis`
- Autor NBT: `DrakesCraft Array Labs`

Pendiente: añadir el autor al lore y una página visible de créditos para que
también aparezca dentro del contenido del libro.
