package io.github.mooy1.infinityexpansion.items.machines;

import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.SlimefunExtension;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.machines.MachineBlock;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

public final class Machines {

    private Machines() {}

    public static final SlimefunItemStack COBBLE_PRESS = new SlimefunItemStack(
            "COBBLE_PRESS",
            Material.SMOOTH_STONE,
            "&8Cobble Press",
            "&7Compresses cobblestone more efficiently",
            "",
            MachineLore.energyPerSecond(120)
    );
    public static final SlimefunItemStack RESOURCE_SYNTHESIZER = new SlimefunItemStack(
            "RESOURCE_SYNTHESIZER",
            Material.LODESTONE,
            "&6Resource Synthesizer",
            "&7Creates resources by combining 2 Singularities",
            "",
            MachineLore.energy(1_000_000) + "per use"
    );
    public static final SlimefunItemStack BASIC_GROWER = new SlimefunItemStack(
            "BASIC_VIRTUAL_FARM",
            Material.GRASS_BLOCK,
            "&9Basic &aVirtual Farm",
            "&7Automatically grows, harvests, and replants crops",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(18)
    );
    public static final SlimefunItemStack ADVANCED_GROWER = new SlimefunItemStack(
            "ADVANCED_VIRTUAL_FARM",
            Material.CRIMSON_NYLIUM,
            "&cAdvanced &aVirtual Farm",
            "&7Automatically grows, harvests, and replants crops",
            "",
            MachineLore.speed(5),
            MachineLore.energyPerSecond(90)
    );
    public static final SlimefunItemStack INFINITY_GROWER = new SlimefunItemStack(
            "INFINITY_VIRTUAL_FARM",
            Material.WARPED_NYLIUM,
            "&bInfinity &aVirtual Farm",
            "&7Automatically grows, harvests, and replants crops",
            "",
            MachineLore.speed(25),
            MachineLore.energyPerSecond(900)
    );
    public static final SlimefunItemStack BASIC_TREE = new SlimefunItemStack(
            "BASIC_TREE_GROWER",
            Material.STRIPPED_OAK_WOOD,
            "&9Basic &2Tree Grower",
            "&7Automatically grows, harvests, and replants trees",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(36)
    );
    public static final SlimefunItemStack ADVANCED_TREE = new SlimefunItemStack(
            "ADVANCED_TREE_GROWER",
            Material.STRIPPED_ACACIA_WOOD,
            "&cAdvanced &2Tree Grower",
            "&7Automatically grows, harvests, and replants trees",
            "",
            MachineLore.speed(5),
            MachineLore.energyPerSecond(180)
    );
    public static final SlimefunItemStack INFINITY_TREE = new SlimefunItemStack(
            "INFINITY_TREE_GROWER",
            Material.STRIPPED_WARPED_HYPHAE,
            "&bInfinity &2Tree Grower",
            "&7Automatically grows, harvests, and replants trees",
            "",
            MachineLore.speed(25),
            MachineLore.energyPerSecond(1800)
    );
    private static final int BEDROCK_ENERGY = 10_000;
    public static final SlimefunItemStack POWERED_BEDROCK = new SlimefunItemStack(
            "POWERED_BEDROCK",
            Material.NETHERITE_BLOCK,
            "&4Powered Bedrock",
            "&7When powered, transforms into a bedrock",
            "&7Will revert once unpowered or broken",
            "",
            MachineLore.energyPerSecond(BEDROCK_ENERGY)
    );
    private static final int GEO_QUARRY_INTERVAL = 400;
    private static final int GEO_QUARRY_ENERGY = 450;
    public static final SlimefunItemStack GEO_QUARRY = new SlimefunItemStack(
            "GEO_QUARRY",
            Material.QUARTZ_BRICKS,
            "&fGeo Quarry",
            "&7Slowly harvests geo resources from the void using power",
            "",
            MachineLore.energyPerSecond(GEO_QUARRY_ENERGY)
    );
    public static final SlimefunItemStack EXTREME_FREEZER = new SlimefunItemStack(
            "EXTREME_FREEZER",
            Material.LIGHT_BLUE_CONCRETE,
            "&bExtreme Freezer",
            "&7Converts ice into coolant",
            "",
            MachineLore.energyPerSecond(90)
    );
    public static final SlimefunItemStack DUST_EXTRACTOR = new SlimefunItemStack(
            "DUST_EXTRACTOR",
            Material.FURNACE,
            "&8Dust Extractor",
            "&7Converts cobble into dusts",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(240)
    );
    public static final SlimefunItemStack INFINITY_DUST_EXTRACTOR = new SlimefunItemStack(
            "INFINITY_DUST_EXTRACTOR",
            Material.FURNACE,
            "&bInfinity &8Dust Extractor",
            "&7Converts cobble into dusts",
            "",
            MachineLore.speed(16),
            MachineLore.energyPerSecond(7200)
    );
    public static final SlimefunItemStack INGOT_FORMER = new SlimefunItemStack(
            "INGOT_FORMER",
            Material.FURNACE,
            "&8Ingot Former",
            "&7Forms ingots from dusts",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(240)
    );
    public static final SlimefunItemStack INFINITY_INGOT_FORMER = new SlimefunItemStack(
            "INFINITY_INGOT_FORMER",
            Material.FURNACE,
            "&bInfinity &8Ingot Former",
            "&7Forms ingots from dusts",
            "",
            MachineLore.speed(16),
            MachineLore.energyPerSecond(7200)
    );
    public static final SlimefunItemStack URANIUM_EXTRACTOR = new SlimefunItemStack(
            "URANIUM_EXTRACTOR",
            Material.LIME_CONCRETE,
            "&aUranium Extractor",
            "&7Converts cobble into uranium",
            "",
            MachineLore.energyPerSecond(240)
    );
    public static final SlimefunItemStack DECOMPRESSOR = new SlimefunItemStack(
            "DECOMPRESSOR",
            Material.TARGET,
            "&7Decompressor",
            "&7Reduces blocks to their base material",
            "",
            MachineLore.energyPerSecond(60)
    );
    private static final int GEAR_TRANSFORMER_ENERGY = 12000;
    public static final SlimefunItemStack GEAR_TRANSFORMER = new SlimefunItemStack(
            "GEAR_TRANSFORMER",
            Material.EMERALD_BLOCK,
            "&7Gear Transformer",
            "&7Changes the material of vanilla tools and gear",
            "",
            MachineLore.energy(GEAR_TRANSFORMER_ENERGY) + "Per Use"
    );
    public static final SlimefunItemStack BASIC_COBBLE = new SlimefunItemStack(
            "BASIC_COBBLE_GEN",
            Material.SMOOTH_STONE,
            "&9Basic &8Cobble Generator",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(24)
    );
    public static final SlimefunItemStack ADVANCED_COBBLE = new SlimefunItemStack(
            "ADVANCED_COBBLE_GEN",
            Material.SMOOTH_STONE,
            "&cAdvanced &8Cobble Generator",
            "",
            MachineLore.speed(4),
            MachineLore.energyPerSecond(120)
    );
    public static final SlimefunItemStack INFINITY_COBBLE = new SlimefunItemStack(
            "INFINITY_COBBLE_GEN",
            Material.SMOOTH_STONE,
            "&cInfinity &8Cobble Generator",
            "",
            MachineLore.speed(64),
            MachineLore.energyPerSecond(800)
    );
    public static final SlimefunItemStack BASIC_OBSIDIAN = new SlimefunItemStack(
            "BASIC_OBSIDIAN_GEN",
            Material.SMOOTH_STONE,
            "&8Obsidian Generator",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(240)
    );
    public static final SlimefunItemStack SINGULARITY_CONSTRUCTOR = new SlimefunItemStack(
            "SINGULARITY_CONSTRUCTOR",
            Material.QUARTZ_BRICKS,
            "&fSingularity Constructor",
            "&7Condenses large amounts of resources",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(120)
    );
    public static final SlimefunItemStack INFINITY_CONSTRUCTOR = new SlimefunItemStack(
            "INFINITY_CONSTRUCTOR",
            Material.CHISELED_QUARTZ_BLOCK,
            "&bInfinity &fConstructor",
            "&7Quickly condenses large amounts of resources",
            "",
            MachineLore.speed(64),
            MachineLore.energyPerSecond(1200)
    );
    public static final SlimefunItemStack STONEWORKS_FACTORY = new SlimefunItemStack(
            "STONEWORKS_FACTORY",
            Material.BLAST_FURNACE,
            "&8Stoneworks Factory",
            "&7Generates cobblestone and processes it into various materials",
            "",
            MachineLore.energyPerSecond(240)
    );
    public static final SlimefunItemStack VOID_HARVESTER = new SlimefunItemStack(
            "VOID_HARVESTER",
            Material.OBSIDIAN,
            "&8Void Harvester",
            "&7Slowly harvests &8Void &7Bits from nothing...",
            "",
            MachineLore.speed(1),
            MachineLore.energyPerSecond(120)
    );
    public static final SlimefunItemStack INFINITE_VOID_HARVESTER = new SlimefunItemStack(
            "INFINITE_VOID_HARVESTER",
            Material.CRYING_OBSIDIAN,
            "&bInfinite &8Void Harvester",
            "&7Harvests &8Void &7Bits from nothing...",
            "",
            MachineLore.speed(64),
            MachineLore.energyPerSecond(12000)
    );

    public static void setup(InfinityExpansion plugin) {
        new VoidHarvester(Groups.ADVANCED_MACHINES, VOID_HARVESTER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.TITANIUM.item(), Materials.TITANIUM.item(), Materials.TITANIUM.item(),
                Materials.MACHINE_PLATE.item(), SlimefunItems.GEO_MINER.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 1).energyPerTick(120).register(plugin);
        new VoidHarvester(Groups.INFINITY_CHEAT, INFINITE_VOID_HARVESTER, InfinityWorkbench.TYPE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(),
                Materials.MAGNONIUM.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.MAGNONIUM.item(),
                Materials.MAGNONIUM.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.VOID_INGOT.item(), Materials.MAGNONIUM.item(),
                Materials.MAGNONIUM.item(), Materials.VOID_INGOT.item(), VOID_HARVESTER.item(), VOID_HARVESTER.item(), Materials.VOID_INGOT.item(), Materials.MAGNONIUM.item(),
                Materials.MAGNONIUM.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.MAGNONIUM.item(),
                Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item()
        }, 64).energyPerTick(12000).register(plugin);
        new StoneworksFactory(Groups.ADVANCED_MACHINES, STONEWORKS_FACTORY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), BASIC_COBBLE.item(), Materials.MAGSTEEL_PLATE.item(),
                SlimefunItems.ELECTRIC_FURNACE_3.item(), Materials.MACHINE_CIRCUIT.item(), SlimefunItems.ELECTRIC_ORE_GRINDER.item(),
                Materials.MAGSTEEL_PLATE.item(), SlimefunItems.ELECTRIC_PRESS.item(), Materials.MAGSTEEL_PLATE.item()
        }).energyPerTick(240).register(plugin);

        new SingularityConstructor(Groups.ADVANCED_MACHINES, SINGULARITY_CONSTRUCTOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(),
                Materials.MACHINE_PLATE.item(), SlimefunItems.CARBON_PRESS_3.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }).speed(1).energyPerTick(120).register(plugin);
        new SingularityConstructor(Groups.INFINITY_CHEAT, INFINITY_CONSTRUCTOR, InfinityWorkbench.TYPE, new ItemStack[] {
                null, Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), null,
                null, Materials.VOID_INGOT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.VOID_INGOT.item(), null,
                null, Materials.VOID_INGOT.item(), SINGULARITY_CONSTRUCTOR.item(), SINGULARITY_CONSTRUCTOR.item(), Materials.VOID_INGOT.item(), null,
                null, Materials.VOID_INGOT.item(), SINGULARITY_CONSTRUCTOR.item(), SINGULARITY_CONSTRUCTOR.item(), Materials.VOID_INGOT.item(), null,
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_INGOT.item(), null,
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item()
        }).speed(64).energyPerTick(1200).register(plugin);

        new ResourceSynthesizer(Groups.ADVANCED_MACHINES, RESOURCE_SYNTHESIZER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.ADAMANTITE.item(), Materials.ADAMANTITE.item(), Materials.ADAMANTITE.item(),
                Materials.MACHINE_PLATE.item(), SlimefunItems.REINFORCED_FURNACE.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_PLATE.item()
        }).recipes(new SlimefunItemStack[] {
                Materials.IRON_SINGULARITY, Materials.COAL_SINGULARITY, new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 32),
                Materials.IRON_SINGULARITY, Materials.REDSTONE_SINGULARITY, new SlimefunItemStack(SlimefunItems.REDSTONE_ALLOY, 32),
                Materials.DIAMOND_SINGULARITY, Materials.COAL_SINGULARITY, new SlimefunItemStack(SlimefunItems.CARBONADO, 16),
                Materials.GOLD_SINGULARITY, Materials.EMERALD_SINGULARITY, new SlimefunItemStack(SlimefunItems.BLISTERING_INGOT_3, 16),
                Materials.COPPER_SINGULARITY, Materials.ZINC_SINGULARITY, new SlimefunItemStack(SlimefunItems.ELECTRO_MAGNET, 64),
                Materials.IRON_SINGULARITY, Materials.QUARTZ_SINGULARITY, new SlimefunItemStack(SlimefunItems.SOLAR_PANEL, 64)
        }).energyPerTick(1_000_000).register(plugin);

        new PoweredBedrock(Groups.INFINITY_CHEAT, POWERED_BEDROCK, InfinityWorkbench.TYPE, new ItemStack[] {
                Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(),
                Materials.COBBLE_5.item(), Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(), Materials.COBBLE_5.item(),
                Materials.COBBLE_5.item(), Materials.VOID_INGOT.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), Materials.VOID_INGOT.item(), Materials.COBBLE_5.item(),
                Materials.COBBLE_5.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.VOID_INGOT.item(), Materials.COBBLE_5.item(),
                Materials.COBBLE_5.item(), Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(), Materials.COBBLE_5.item(),
                Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item(), Materials.COBBLE_5.item()
        }, BEDROCK_ENERGY).register(plugin);
        new MaterialGenerator(Groups.BASIC_MACHINES, BASIC_COBBLE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL.item(), new ItemStack(Material.DIAMOND_PICKAXE), Materials.MAGSTEEL.item(),
                new ItemStack(Material.WATER_BUCKET), Materials.COBBLE_2.item(), new ItemStack(Material.LAVA_BUCKET),
                Materials.MAGSTEEL.item(), Materials.MACHINE_CIRCUIT.item(), Materials.MAGSTEEL.item()
        }).material(Material.COBBLESTONE).speed(1).energyPerTick(24).register(plugin);
        new MaterialGenerator(Groups.ADVANCED_MACHINES, ADVANCED_COBBLE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), BASIC_COBBLE.item(), Materials.MAGSTEEL_PLATE.item(),
                new ItemStack(Material.WATER_BUCKET), Materials.COBBLE_3.item(), new ItemStack(Material.LAVA_BUCKET),
                Materials.MACHINE_CIRCUIT.item(), BASIC_COBBLE.item(), Materials.MACHINE_CIRCUIT.item()
        }).material(Material.COBBLESTONE).speed(4).energyPerTick(75).register(plugin);
        new MaterialGenerator(Groups.INFINITY_CHEAT, INFINITY_COBBLE, InfinityWorkbench.TYPE, new ItemStack[] {
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), ADVANCED_COBBLE.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item()
        }).material(Material.COBBLESTONE).speed(64).energyPerTick(800).register(plugin);
        new MaterialGenerator(Groups.ADVANCED_MACHINES, BASIC_OBSIDIAN, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.FLUID_PUMP.item(), SlimefunItems.PROGRAMMABLE_ANDROID_MINER.item(), SlimefunItems.FLUID_PUMP.item(),
                new ItemStack(Material.DISPENSER), Materials.VOID_INGOT.item(), new ItemStack(Material.DISPENSER),
                Materials.MACHINE_CIRCUIT.item(), ADVANCED_COBBLE.item(), Materials.MACHINE_CIRCUIT.item()
        }).material(Material.OBSIDIAN).speed(1).energyPerTick(240).register(plugin);

        EnumMap<Material, ItemStack[]> crops = new EnumMap<>(Material.class);
        crops.put(Material.WHEAT_SEEDS, new ItemStack[] { new ItemStack(Material.WHEAT, 2) });
        crops.put(Material.CARROT, new ItemStack[] { new ItemStack(Material.CARROT, 2) });
        crops.put(Material.POTATO, new ItemStack[] { new ItemStack(Material.POTATO, 2) });
        crops.put(Material.BEETROOT_SEEDS, new ItemStack[] { new ItemStack(Material.BEETROOT, 2) });
        crops.put(Material.PUMPKIN_SEEDS, new ItemStack[] { new ItemStack(Material.PUMPKIN) });
        crops.put(Material.MELON_SEEDS, new ItemStack[] { new ItemStack(Material.MELON) });
        crops.put(Material.SUGAR_CANE, new ItemStack[] { new ItemStack(Material.SUGAR_CANE, 2) });
        crops.put(Material.COCOA_BEANS, new ItemStack[] { new ItemStack(Material.COCOA_BEANS, 2) });
        crops.put(Material.CACTUS, new ItemStack[] { new ItemStack(Material.CACTUS, 2) });
        crops.put(Material.BAMBOO, new ItemStack[] { new ItemStack(Material.BAMBOO, 6) });
        crops.put(Material.CHORUS_FLOWER, new ItemStack[] { new ItemStack(Material.CHORUS_FRUIT, 6) });
        crops.put(Material.NETHER_WART, new ItemStack[] { new ItemStack(Material.NETHER_WART, 2) });

        new GrowingMachine(Groups.BASIC_MACHINES, BASIC_GROWER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                Materials.MAGSTEEL.item(), new ItemStack(Material.DIAMOND_HOE), Materials.MAGSTEEL.item(),
                Materials.MACHINE_CIRCUIT.item(), new ItemStack(Material.GRASS_BLOCK), Materials.MACHINE_CIRCUIT.item()
        }).recipes(crops).ticksPerOutput(300).energyPerTick(18).register(plugin);
        new GrowingMachine(Groups.ADVANCED_MACHINES, ADVANCED_GROWER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.HARDENED_GLASS.item(),
                Materials.MAGNONIUM.item(), BASIC_GROWER.item(), Materials.MAGNONIUM.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }).recipes(crops).ticksPerOutput(60).energyPerTick(90).register(plugin);
        new GrowingMachine(Groups.INFINITY_CHEAT, INFINITY_GROWER, InfinityWorkbench.TYPE, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                new ItemStack(Material.GLASS), null, null, null, null, new ItemStack(Material.GLASS),
                new ItemStack(Material.GLASS), null, null, null, null, new ItemStack(Material.GLASS),
                new ItemStack(Material.GLASS), new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.GRASS_BLOCK), new ItemStack(Material.GLASS),
                Materials.MACHINE_PLATE.item(), SlimefunItems.CROP_GROWTH_ACCELERATOR_2.item(), ADVANCED_GROWER.item(), ADVANCED_GROWER.item(), SlimefunItems.CROP_GROWTH_ACCELERATOR_2.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.MACHINE_PLATE.item()
        }).recipes(crops).ticksPerOutput(12).energyPerTick(900).register(plugin);

        EnumMap<Material, ItemStack[]> trees = new EnumMap<>(Material.class);

        trees.put(Material.OAK_SAPLING, new ItemStack[] {
                new ItemStack(Material.OAK_LEAVES, 8), new ItemStack(Material.OAK_LOG, 6), new ItemStack(Material.STICK), new ItemStack(Material.APPLE)
        });
        trees.put(Material.SPRUCE_SAPLING, new ItemStack[] {
                new ItemStack(Material.SPRUCE_LEAVES, 8), new ItemStack(Material.SPRUCE_LOG, 6), new ItemStack(Material.STICK, 2)
        });
        trees.put(Material.DARK_OAK_SAPLING, new ItemStack[] {
                new ItemStack(Material.DARK_OAK_LEAVES, 8), new ItemStack(Material.DARK_OAK_LOG, 6), new ItemStack(Material.APPLE)
        });
        trees.put(Material.BIRCH_SAPLING, new ItemStack[] {
                new ItemStack(Material.BIRCH_LEAVES, 8), new ItemStack(Material.BIRCH_LOG, 6)
        });
        trees.put(Material.ACACIA_SAPLING, new ItemStack[] {
                new ItemStack(Material.ACACIA_LEAVES, 8), new ItemStack(Material.ACACIA_LOG, 6)
        });
        trees.put(Material.JUNGLE_SAPLING, new ItemStack[] {
                new ItemStack(Material.JUNGLE_LEAVES, 8), new ItemStack(Material.JUNGLE_LOG, 6), new ItemStack(Material.COCOA_BEANS)
        });
        trees.put(Material.WARPED_FUNGUS, new ItemStack[] {
                new ItemStack(Material.WARPED_HYPHAE, 8), new ItemStack(Material.WARPED_STEM, 6), new ItemStack(Material.SHROOMLIGHT)
        });
        trees.put(Material.CRIMSON_FUNGUS, new ItemStack[] {
                new ItemStack(Material.CRIMSON_HYPHAE, 8), new ItemStack(Material.CRIMSON_STEM, 6), new ItemStack(Material.WEEPING_VINES)
        });

        new GrowingMachine(Groups.BASIC_MACHINES, BASIC_TREE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                Materials.MAGSTEEL.item(), new ItemStack(Material.PODZOL), Materials.MAGSTEEL.item(),
                Materials.MACHINE_CIRCUIT.item(), BASIC_GROWER.item(), Materials.MACHINE_CIRCUIT.item()
        }).recipes(trees).ticksPerOutput(600).energyPerTick(36).register(plugin);
        new GrowingMachine(Groups.ADVANCED_MACHINES, ADVANCED_TREE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.HARDENED_GLASS.item(), SlimefunItems.HARDENED_GLASS.item(),
                Materials.MAGNONIUM.item(), BASIC_TREE.item(), Materials.MAGNONIUM.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }).recipes(trees).ticksPerOutput(120).energyPerTick(180).register(plugin);
        new GrowingMachine(Groups.INFINITY_CHEAT, INFINITY_TREE, InfinityWorkbench.TYPE, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                new ItemStack(Material.GLASS), SlimefunItems.TREE_GROWTH_ACCELERATOR.item(), null, null, SlimefunItems.TREE_GROWTH_ACCELERATOR.item(), new ItemStack(Material.GLASS),
                new ItemStack(Material.GLASS), ADVANCED_TREE.item(), null, null, ADVANCED_TREE.item(), new ItemStack(Material.GLASS),
                new ItemStack(Material.GLASS), SlimefunItems.TREE_GROWTH_ACCELERATOR.item(), null, null, SlimefunItems.TREE_GROWTH_ACCELERATOR.item(), new ItemStack(Material.GLASS),
                Materials.MACHINE_PLATE.item(), new ItemStack(Material.PODZOL), new ItemStack(Material.PODZOL), new ItemStack(Material.PODZOL), new ItemStack(Material.PODZOL), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.MACHINE_PLATE.item()
        }).recipes(trees).ticksPerOutput(24).energyPerTick(1800).register(plugin);

        new MachineBlock(Groups.ADVANCED_MACHINES, EXTREME_FREEZER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.FREEZER_2.item(), SlimefunItems.FREEZER_2.item(), SlimefunItems.FREEZER_2.item(),
                new ItemStack(Material.WATER_BUCKET), SlimefunItems.FLUID_PUMP.item(), new ItemStack(Material.WATER_BUCKET),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }).addRecipe(SlimefunItems.NETHER_ICE_COOLANT_CELL.item(), new ItemStack(Material.MAGMA_BLOCK, 2))
                .ticksPerOutput(1)
                .addRecipe(SlimefunItems.REACTOR_COOLANT_CELL.item(), new ItemStack(Material.ICE, 2))
                .energyPerTick(90).register(plugin);

        RandomizedItemStack twoDust = new RandomizedItemStack(
                new SlimefunItemStack(SlimefunItems.COPPER_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.ZINC_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.TIN_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.ALUMINUM_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.LEAD_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.SILVER_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.IRON_DUST, 2).item(),
                new SlimefunItemStack(SlimefunItems.MAGNESIUM_DUST, 2).item()
        );
        new MachineBlock(Groups.ADVANCED_MACHINES, DUST_EXTRACTOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.ELECTRIC_ORE_GRINDER_2.item(), SlimefunItems.ELECTRIC_GOLD_PAN_3.item(), SlimefunItems.ELECTRIC_DUST_WASHER_3.item(),
                SlimefunItems.ELECTRIC_ORE_GRINDER_2.item(), SlimefunItems.ELECTRIC_GOLD_PAN_3.item(), SlimefunItems.ELECTRIC_DUST_WASHER_3.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item(),
        }).ticksPerOutput(1).addRecipe(twoDust, new ItemStack(Material.COBBLESTONE, 4))
                .addRecipe(twoDust, new ItemStack(Material.ANDESITE, 4))
                .addRecipe(twoDust, new ItemStack(Material.STONE, 4))
                .addRecipe(twoDust, new ItemStack(Material.DIORITE, 4))
                .addRecipe(twoDust, new ItemStack(Material.GRANITE, 4))
                .energyPerTick(240).register(plugin);

        RandomizedItemStack sixtyFourDust = new RandomizedItemStack(
                new SlimefunItemStack(SlimefunItems.COPPER_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.ZINC_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.TIN_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.ALUMINUM_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.LEAD_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.SILVER_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.GOLD_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.IRON_DUST, 64).item(),
                new SlimefunItemStack(SlimefunItems.MAGNESIUM_DUST, 64).item()
        );
        new MachineBlock(Groups.INFINITY_CHEAT, INFINITY_DUST_EXTRACTOR, InfinityWorkbench.TYPE, new ItemStack[] {
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), DUST_EXTRACTOR.item(), DUST_EXTRACTOR.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), DUST_EXTRACTOR.item(), DUST_EXTRACTOR.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), DUST_EXTRACTOR.item(), DUST_EXTRACTOR.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
        }).addRecipe(sixtyFourDust, new ItemStack(Material.COBBLESTONE, 64))
                .addRecipe(sixtyFourDust, new ItemStack(Material.ANDESITE, 64))
                .addRecipe(sixtyFourDust, new ItemStack(Material.STONE, 64))
                .addRecipe(sixtyFourDust, new ItemStack(Material.DIORITE, 64))
                .addRecipe(sixtyFourDust, new ItemStack(Material.GRANITE, 64))
                .ticksPerOutput(1).energyPerTick(7200).register(plugin);

        new MachineBlock(Groups.ADVANCED_MACHINES, COBBLE_PRESS, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), Materials.COBBLE_3.item(), Materials.MACHINE_PLATE.item(),
                SlimefunItems.ELECTRIC_PRESS_2.item(), SlimefunItems.ELECTRIC_PRESS_2.item(), SlimefunItems.ELECTRIC_PRESS_2.item(),
                Materials.MACHINE_PLATE.item(), Materials.COBBLE_3.item(), Materials.MACHINE_PLATE.item()
        }).addRecipe(new SlimefunItemStack(Materials.COBBLE_1, 8).item(), new ItemStack(Material.COBBLESTONE, 64))
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_2, 8).item(), new SlimefunItemStack(Materials.COBBLE_1, 64).item())
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_3, 8).item(), new SlimefunItemStack(Materials.COBBLE_2, 64).item())
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_4, 8).item(), new SlimefunItemStack(Materials.COBBLE_3, 64).item())
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_5, 8).item(), new SlimefunItemStack(Materials.COBBLE_4, 64).item())
                .ticksPerOutput(1).energyPerTick(120).register(plugin);

        new MachineBlock(Groups.ADVANCED_MACHINES, INGOT_FORMER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(),
                SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(), SlimefunItems.ELECTRIC_INGOT_FACTORY_2.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item(),
        }).addRecipe(new SlimefunItemStack(SlimefunItems.COPPER_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.COPPER_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.ZINC_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.ZINC_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.TIN_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.TIN_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.ALUMINUM_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.ALUMINUM_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.LEAD_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.LEAD_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.SILVER_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.SILVER_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.GOLD_24K, 1).item(), new SlimefunItemStack(SlimefunItems.GOLD_DUST, 8).item())
                .addRecipe(new ItemStack(Material.IRON_INGOT, 4), new SlimefunItemStack(SlimefunItems.IRON_DUST, 8).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.MAGNESIUM_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.MAGNESIUM_DUST, 8).item())
                .ticksPerOutput(1).energyPerTick(240).register(plugin);

        new MachineBlock(Groups.INFINITY_CHEAT, INFINITY_INGOT_FORMER, InfinityWorkbench.TYPE, new ItemStack[] {
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), INGOT_FORMER.item(), INGOT_FORMER.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), INGOT_FORMER.item(), INGOT_FORMER.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), INGOT_FORMER.item(), INGOT_FORMER.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
        }).addRecipe(new SlimefunItemStack(SlimefunItems.COPPER_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.COPPER_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.ZINC_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.ZINC_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.TIN_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.TIN_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.ALUMINUM_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.ALUMINUM_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.LEAD_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.LEAD_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.SILVER_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.SILVER_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.GOLD_24K, 10).item(), new SlimefunItemStack(SlimefunItems.GOLD_DUST, 64).item())
                .addRecipe(new ItemStack(Material.IRON_INGOT, 32), new SlimefunItemStack(SlimefunItems.IRON_DUST, 64).item())
                .addRecipe(new SlimefunItemStack(SlimefunItems.MAGNESIUM_INGOT, 32).item(), new SlimefunItemStack(SlimefunItems.MAGNESIUM_DUST, 64).item())
                .ticksPerOutput(1).energyPerTick(7200).register(plugin);

        new MachineBlock(Groups.ADVANCED_MACHINES, URANIUM_EXTRACTOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.ELECTRIC_ORE_GRINDER_2.item(), SlimefunItems.ELECTRIC_ORE_GRINDER_2.item(), SlimefunItems.ELECTRIC_ORE_GRINDER_2.item(),
                SlimefunItems.ELECTRIC_GOLD_PAN_3.item(), SlimefunItems.ELECTRIC_DUST_WASHER_3.item(), SlimefunItems.ENHANCED_AUTO_CRAFTER.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item(),
        }).addRecipe(SlimefunItems.SMALL_URANIUM.item(), new ItemStack(Material.COBBLESTONE, 4))
                .addRecipe(SlimefunItems.SMALL_URANIUM.item(), new ItemStack(Material.ANDESITE, 4))
                .addRecipe(SlimefunItems.SMALL_URANIUM.item(), new ItemStack(Material.STONE, 4))
                .addRecipe(SlimefunItems.SMALL_URANIUM.item(), new ItemStack(Material.DIORITE, 4))
                .addRecipe(SlimefunItems.SMALL_URANIUM.item(), new ItemStack(Material.GRANITE, 4))
                .ticksPerOutput(1).energyPerTick(240).register(plugin);

        new MachineBlock(Groups.ADVANCED_MACHINES, DECOMPRESSOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), Materials.MAGSTEEL_PLATE.item(), Materials.MAGSTEEL_PLATE.item(),
                new ItemStack(Material.STICKY_PISTON), SlimefunItems.ELECTRIC_PRESS_2.item(), new ItemStack(Material.STICKY_PISTON),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item(),
        }).addRecipe(new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK))
                .addRecipe(new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK))
                .addRecipe(new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK))
                .addRecipe(new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK))
                .addRecipe(new ItemStack(Material.NETHERITE_INGOT, 9), new ItemStack(Material.NETHERITE_BLOCK))
                .addRecipe(new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK))
                .addRecipe(new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK))
                .addRecipe(new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK))
                .addRecipe(new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK))
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_4, 8).item(), Materials.COBBLE_5.item())
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_3, 8).item(), Materials.COBBLE_4.item())
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_2, 8).item(), Materials.COBBLE_3.item())
                .addRecipe(new SlimefunItemStack(Materials.COBBLE_1, 8).item(), Materials.COBBLE_2.item())
                .addRecipe(new ItemStack(Material.COBBLESTONE, 8), Materials.COBBLE_1.item())
                .ticksPerOutput(1).energyPerTick(60).register(plugin);

        new GearTransformer(Groups.ADVANCED_MACHINES, GEAR_TRANSFORMER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), Materials.MACHINE_CIRCUIT.item(), Materials.MAGSTEEL_PLATE.item(),
                Materials.MACHINE_CIRCUIT.item(), new ItemStack(Material.SMITHING_TABLE), Materials.MACHINE_CIRCUIT.item(),
                Materials.MAGSTEEL_PLATE.item(), Materials.MACHINE_CIRCUIT.item(), Materials.MAGSTEEL_PLATE.item()
        }, GEAR_TRANSFORMER_ENERGY).register(plugin);

        new GeoQuarry(Groups.ADVANCED_MACHINES, GEO_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(),
                Materials.VOID_INGOT.item(), SlimefunExtension.ADVANCED_GEO_MINER.item(), Materials.VOID_INGOT.item(),
                Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(),
        }).ticksPerOutput(GEO_QUARRY_INTERVAL).energyPerTick(GEO_QUARRY_ENERGY).register(plugin);
    }

    private static final class RandomizedItemStack extends ItemStack {

        private final ItemStack[] items;

        public RandomizedItemStack(ItemStack... outputs) {
            super(outputs[0]);
            this.items = outputs;
        }

        @Nonnull
        @Override
        public ItemStack clone() {
            return this.items[ThreadLocalRandom.current().nextInt(this.items.length)].clone();
        }

    }

}
