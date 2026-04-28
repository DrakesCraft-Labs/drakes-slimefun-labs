package io.github.mooy1.infinityexpansion.items.materials;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.machines.VoidHarvester;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

public final class Materials {

    private Materials() {}


    public static final SlimefunItemStack INFINITE_CIRCUIT = new SlimefunItemStack(
            "INFINITE_MACHINE_CIRCUIT",
            Material.DIAMOND,
            "&bInfinite &6Machine Circuit",
            "&7Machine Component"
    );
    public static final SlimefunItemStack INFINITE_CORE = new SlimefunItemStack(
            "INFINITE_MACHINE_CORE",
            Material.DIAMOND_BLOCK,
            "&bInfinite Machine Core",
            "&7Machine Component"
    );
    public static final SlimefunItemStack MAGSTEEL_PLATE = new SlimefunItemStack(
            "MAGSTEEL_PLATE",
            Material.NETHERITE_SCRAP,
            "&4MagSteel Plate",
            "&7Machine Component"
    );
    public static final SlimefunItemStack MACHINE_PLATE = new SlimefunItemStack(
            "MACHINE_PLATE",
            Material.PAPER,
            "&fMachine Plate",
            "&7Machine Component"
    );
    public static final SlimefunItemStack MACHINE_CIRCUIT = new SlimefunItemStack(
            "MACHINE_CIRCUIT",
            Material.GOLD_INGOT,
            "&6Machine Circuit",
            "&7Machine Component"
    );

    public static final SlimefunItemStack MACHINE_CORE = new SlimefunItemStack(
            "MACHINE_CORE",
            Material.IRON_BLOCK,
            "&fMachine Core",
            "&7Machine Component"
    );
    public static final SlimefunItemStack VOID_BIT = new SlimefunItemStack(
            "VOID_BIT",
            Material.IRON_NUGGET,
            "&8Void Bit",
            "&7&oIt feels... empty"
    );
    public static final SlimefunItemStack VOID_DUST = new SlimefunItemStack(
            "VOID_DUST",
            Material.GUNPOWDER,
            "&8Void Dust",
            "&7&oIts starting to take form..."
    );
    public static final SlimefunItemStack VOID_INGOT = new SlimefunItemStack(
            "VOID_INGOT",
            Material.NETHERITE_INGOT,
            "&8Void Ingot",
            "&7&oThe emptiness of the cosmos",
            "&7&oin the palm of your hand"
    );
    public static final SlimefunItemStack COBBLE_1 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_1",
            Material.ANDESITE,
            "&7Single Compressed Cobblestone",
            "&89 cobblestone combined"
    );
    public static final SlimefunItemStack COBBLE_2 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_2",
            Material.ANDESITE,
            "&7Double Compressed Cobblestone",
            "&881 cobblestone combined"
    );
    public static final SlimefunItemStack COBBLE_3 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_3",
            Material.STONE,
            "&7Triple Compressed Cobblestone",
            "&8729 cobblestone combined"
    );
    public static final SlimefunItemStack COBBLE_4 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_4",
            Material.STONE,
            "&7Quadruple Compressed Cobblestone",
            "&86,561 cobblestone combined"
    );
    public static final SlimefunItemStack COBBLE_5 = new SlimefunItemStack(
            "COMPRESSED_COBBLESTONE_5",
            Material.POLISHED_ANDESITE,
            "&7Quintuple Compressed Cobblestone",
            "&859,049 cobblestone combined"
    );
    public static final SlimefunItemStack MAGSTEEL = new SlimefunItemStack(
            "MAGSTEEL",
            Material.BRICK,
            "&4MagSteel"
    );
    public static final SlimefunItemStack MAGNONIUM = new SlimefunItemStack(
            "MAGNONIUM",
            Material.NETHER_BRICK,
            "&5Magnonium"
    );
    public static final SlimefunItemStack TITANIUM = new SlimefunItemStack(
            "TITANIUM",
            Material.IRON_INGOT,
            "&7Titanium"
    );
    public static final SlimefunItemStack MYTHRIL = new SlimefunItemStack(
            "MYTHRIL",
            Material.IRON_INGOT,
            "&bMythril"
    );
    public static final SlimefunItemStack ADAMANTITE = new SlimefunItemStack(
            "ADAMANTITE",
            Material.BRICK,
            "&dAdamantite"
    );
    public static final SlimefunItemStack INFINITE_INGOT = new SlimefunItemStack(
            "INFINITE_INGOT",
            Material.IRON_INGOT,
            "&bInfinity Ingot", // &dI&cn&6f&ei&an&bi&3t&9y &fIngot
            "&7&oThe fury of the cosmos",
            "&7&oin the palm of your hand"
    );
    public static final SlimefunItemStack FORTUNE_SINGULARITY = new SlimefunItemStack(
            "FORTUNE_SINGULARITY",
            Material.NETHER_STAR,
            "&6Fortune Singularity"
    );
    public static final SlimefunItemStack EARTH_SINGULARITY = new SlimefunItemStack(
            "EARTH_SINGULARITY",
            Material.NETHER_STAR,
            "&aEarth Singularity"
    );
    public static final SlimefunItemStack METAL_SINGULARITY = new SlimefunItemStack(
            "METAL_SINGULARITY",
            Material.NETHER_STAR,
            "&8Metal Singularity"
    );
    public static final SlimefunItemStack MAGIC_SINGULARITY = new SlimefunItemStack(
            "MAGIC_SINGULARITY",
            Material.NETHER_STAR,
            "&dMagic Singularity"
    );
    public static final SlimefunItemStack ENDER_ESSENCE = new SlimefunItemStack(
            "END_ESSENCE",
            Material.BLAZE_POWDER,
            "&5Ender Essence",
            "&8&oFrom the depths of the end..."
    );


    public static final SlimefunItemStack COPPER_SINGULARITY = new SlimefunItemStack(
            "COPPER_SINGULARITY",
            Material.BRICKS,
            "&6Copper Singularity"
    );
    public static final SlimefunItemStack ZINC_SINGULARITY = new SlimefunItemStack(
            "ZINC_SINGULARITY",
            Material.IRON_BLOCK,
            "&7Zinc Singularity"
    );
    public static final SlimefunItemStack TIN_SINGULARITY = new SlimefunItemStack(
            "TIN_SINGULARITY",
            Material.IRON_BLOCK,
            "&7Tin Singularity"
    );
    public static final SlimefunItemStack ALUMINUM_SINGULARITY = new SlimefunItemStack(
            "ALUMINUM_SINGULARITY",
            Material.IRON_BLOCK,
            "&7Aluminum Singularity"
    );
    public static final SlimefunItemStack SILVER_SINGULARITY = new SlimefunItemStack(
            "SILVER_SINGULARITY",
            Material.IRON_BLOCK,
            "&7Silver Singularity"
    );
    public static final SlimefunItemStack MAGNESIUM_SINGULARITY = new SlimefunItemStack(
            "MAGNESIUM_SINGULARITY",
            Material.NETHER_BRICKS,
            "&5Magnesium Singularity"
    );
    public static final SlimefunItemStack LEAD_SINGULARITY = new SlimefunItemStack(
            "LEAD_SINGULARITY",
            Material.IRON_BLOCK,
            "&8Lead Singularity"
    );
    public static final SlimefunItemStack GOLD_SINGULARITY = new SlimefunItemStack(
            "GOLD_SINGULARITY",
            Material.GOLD_BLOCK,
            "&6Gold Singularity"
    );
    public static final SlimefunItemStack IRON_SINGULARITY = new SlimefunItemStack(
            "IRON_SINGULARITY",
            Material.IRON_BLOCK,
            "&7Iron Singularity"
    );
    public static final SlimefunItemStack DIAMOND_SINGULARITY = new SlimefunItemStack(
            "DIAMOND_SINGULARITY",
            Material.DIAMOND_BLOCK,
            "&bDiamond Singularity"
    );
    public static final SlimefunItemStack EMERALD_SINGULARITY = new SlimefunItemStack(
            "EMERALD_SINGULARITY",
            Material.EMERALD_BLOCK,
            "&aEmerald Singularity"
    );
    public static final SlimefunItemStack NETHERITE_SINGULARITY = new SlimefunItemStack(
            "NETHERITE_SINGULARITY",
            Material.NETHERITE_BLOCK,
            "&4Netherite Singularity"
    );
    public static final SlimefunItemStack COAL_SINGULARITY = new SlimefunItemStack(
            "COAL_SINGULARITY",
            Material.COAL_BLOCK,
            "&8Coal Singularity"
    );
    public static final SlimefunItemStack REDSTONE_SINGULARITY = new SlimefunItemStack(
            "REDSTONE_SINGULARITY",
            Material.REDSTONE_BLOCK,
            "&cRedstone Singularity"
    );
    public static final SlimefunItemStack LAPIS_SINGULARITY = new SlimefunItemStack(
            "LAPIS_SINGULARITY",
            Material.LAPIS_BLOCK,
            "&9Lapis Singularity"
    );
    public static final SlimefunItemStack QUARTZ_SINGULARITY = new SlimefunItemStack(
            "QUARTZ_SINGULARITY",
            Material.QUARTZ_BLOCK,
            "&fQuartz Singularity"
    );
    public static final SlimefunItemStack INFINITY_SINGULARITY = new SlimefunItemStack(
            "INFINITY_SINGULARITY",
            Material.SMOOTH_QUARTZ,
            "&bInfinity Singularity"
    );
    public static final SlimefunItemStack BASIC_STRAINER = new SlimefunItemStack(
            "BASIC_STRAINER",
            Material.FISHING_ROD,
            "&9Basic Strainer",
            "&7Collects materials from flowing water",
            "",
            LoreBuilder.speed(1)
    );
    public static final SlimefunItemStack ADVANCED_STRAINER = new SlimefunItemStack(
            "ADVANCED_STRAINER",
            Material.FISHING_ROD,
            "&cAdvanced Strainer",
            "&7Collects materials from flowing water",
            "",
            LoreBuilder.speed(4)
    );
    public static final SlimefunItemStack REINFORCED_STRAINER = new SlimefunItemStack(
            "REINFORCED_STRAINER",
            Material.FISHING_ROD,
            "&fReinforced Strainer",
            "&7Collects materials from flowing water",
            "",
            LoreBuilder.speed(20)
    );

    public static void setup(InfinityExpansion plugin) {
        new EnderEssence(Groups.MAIN_MATERIALS, ENDER_ESSENCE, new NamespacedKey(plugin, "ender_essence")).register(plugin);
        registerEnhanced(COBBLE_1, new ItemStack[] {
                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.COBBLESTONE), new ItemStack(Material.COBBLESTONE),
                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.COBBLESTONE), new ItemStack(Material.COBBLESTONE),
                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.COBBLESTONE), new ItemStack(Material.COBBLESTONE)
        });
        registerEnhanced(COBBLE_2, new ItemStack[] {
                COBBLE_1.item(), COBBLE_1.item(), COBBLE_1.item(),
                COBBLE_1.item(), COBBLE_1.item(), COBBLE_1.item(),
                COBBLE_1.item(), COBBLE_1.item(), COBBLE_1.item()
        });
        registerEnhanced(COBBLE_3, new ItemStack[] {
                COBBLE_2.item(), COBBLE_2.item(), COBBLE_2.item(),
                COBBLE_2.item(), COBBLE_2.item(), COBBLE_2.item(),
                COBBLE_2.item(), COBBLE_2.item(), COBBLE_2.item()
        });
        registerEnhanced(COBBLE_4, new ItemStack[] {
                COBBLE_3.item(), COBBLE_3.item(), COBBLE_3.item(),
                COBBLE_3.item(), COBBLE_3.item(), COBBLE_3.item(),
                COBBLE_3.item(), COBBLE_3.item(), COBBLE_3.item()
        });
        registerEnhanced(COBBLE_5, new ItemStack[] {
                COBBLE_4.item(), COBBLE_4.item(), COBBLE_4.item(),
                COBBLE_4.item(), COBBLE_4.item(), COBBLE_4.item(),
                COBBLE_4.item(), COBBLE_4.item(), COBBLE_4.item()
        });
        registerEnhanced(VOID_DUST, new ItemStack[] {
                VOID_BIT.item(), VOID_BIT.item(), VOID_BIT.item(),
                VOID_BIT.item(), VOID_BIT.item(), VOID_BIT.item(),
                VOID_BIT.item(), VOID_BIT.item(), VOID_BIT.item()
        });
        registerEnhanced(VOID_INGOT, new ItemStack[] {
                VOID_DUST.item(), VOID_DUST.item(), VOID_DUST.item(),
                VOID_DUST.item(), VOID_DUST.item(), VOID_DUST.item(),
                VOID_DUST.item(), VOID_DUST.item(), VOID_DUST.item()
        });
        registerSmeltery(INFINITE_INGOT, EARTH_SINGULARITY.item(), MYTHRIL.item(), FORTUNE_SINGULARITY.item(), MAGIC_SINGULARITY.item(), VOID_INGOT.item(), METAL_SINGULARITY.item());
        registerSmeltery(FORTUNE_SINGULARITY, GOLD_SINGULARITY.item(), DIAMOND_SINGULARITY.item(), EMERALD_SINGULARITY.item(), NETHERITE_SINGULARITY.item(), ADAMANTITE.item());
        registerSmeltery(MAGIC_SINGULARITY, REDSTONE_SINGULARITY.item(), LAPIS_SINGULARITY.item(), QUARTZ_SINGULARITY.item(), MAGNESIUM_SINGULARITY.item(), MAGNONIUM.item());
        registerSmeltery(EARTH_SINGULARITY, COBBLE_4.item(), COAL_SINGULARITY.item(), IRON_SINGULARITY.item(), COPPER_SINGULARITY.item(), LEAD_SINGULARITY.item());
        registerSmeltery(METAL_SINGULARITY, SILVER_SINGULARITY.item(), ALUMINUM_SINGULARITY.item(), TIN_SINGULARITY.item(), ZINC_SINGULARITY.item(), TITANIUM.item());
        registerSmeltery(MAGSTEEL, SlimefunItems.MAGNESIUM_INGOT.item(), SlimefunItems.STEEL_INGOT.item(), SlimefunItems.MAGNESIUM_DUST.item());
        registerSmeltery(TITANIUM, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.DAMASCUS_STEEL_INGOT.item(), SlimefunItems.HARDENED_METAL_INGOT.item());
        registerSmeltery(MYTHRIL, SlimefunItems.REINFORCED_ALLOY_INGOT.item(), IRON_SINGULARITY.item(), SlimefunItems.HARDENED_METAL_INGOT.item());
        registerSmeltery(ADAMANTITE, SlimefunItems.REDSTONE_ALLOY.item(), DIAMOND_SINGULARITY.item(), MAGSTEEL.item());
        registerSmeltery(MAGNONIUM, MAGSTEEL.item(), MAGNESIUM_SINGULARITY.item(), ENDER_ESSENCE.item());
        register(VOID_BIT, VoidHarvester.TYPE, new ItemStack[0]);
        registerEnhanced(MAGSTEEL_PLATE, new ItemStack[] {
                MAGSTEEL.item(), MAGSTEEL.item(), MAGSTEEL.item(),
                MAGSTEEL.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), MAGSTEEL.item(),
                MAGSTEEL.item(), MAGSTEEL.item(), MAGSTEEL.item()
        });
        registerEnhanced(MACHINE_CIRCUIT, new ItemStack[] {
                SlimefunItems.COPPER_INGOT.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COPPER_INGOT.item(),
                SlimefunItems.COPPER_INGOT.item(), SlimefunItems.SILICON.item(), SlimefunItems.COPPER_INGOT.item(),
                SlimefunItems.COPPER_INGOT.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COPPER_INGOT.item()
        });
        new Strainer(BASIC_STRAINER, new ItemStack[] {
                new ItemStack(Material.STICK), new ItemStack(Material.STRING), new ItemStack(Material.STICK),
                new ItemStack(Material.STRING), new ItemStack(Material.STICK), new ItemStack(Material.STRING),
                new ItemStack(Material.STICK), new ItemStack(Material.STRING), new ItemStack(Material.STICK),
        }, 1).register(plugin);
        new Strainer(ADVANCED_STRAINER, new ItemStack[] {
                Materials.MAGSTEEL.item(), new ItemStack(Material.STRING), Materials.MAGSTEEL.item(),
                new ItemStack(Material.STRING), BASIC_STRAINER.item(), new ItemStack(Material.STRING),
                Materials.MAGSTEEL.item(), new ItemStack(Material.STRING), Materials.MAGSTEEL.item()
        }, 4).register(plugin);
        new Strainer(REINFORCED_STRAINER, new ItemStack[] {
                SlimefunItems.REINFORCED_ALLOY_INGOT.item(), new ItemStack(Material.STRING), SlimefunItems.REINFORCED_ALLOY_INGOT.item(),
                new ItemStack(Material.STRING), ADVANCED_STRAINER.item(), new ItemStack(Material.STRING),
                SlimefunItems.REINFORCED_ALLOY_INGOT.item(), new ItemStack(Material.STRING), SlimefunItems.REINFORCED_ALLOY_INGOT.item()
        }, 20).register(plugin);
        registerEnhanced(MACHINE_CORE, new ItemStack[] {
                TITANIUM.item(), MACHINE_CIRCUIT.item(), TITANIUM.item(),
                MACHINE_CIRCUIT.item(), MACHINE_PLATE.item(), MACHINE_CIRCUIT.item(),
                TITANIUM.item(), MACHINE_CIRCUIT.item(), TITANIUM.item()
        });
        registerEnhanced(MACHINE_PLATE, new ItemStack[] {
                SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(),
                MAGSTEEL_PLATE.item(), TITANIUM.item(), MAGSTEEL_PLATE.item(),
                SlimefunItems.REINFORCED_ALLOY_INGOT.item(), SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item()
        });
        register(Groups.INFINITY_CHEAT, INFINITE_CIRCUIT, InfinityWorkbench.TYPE, new ItemStack[] {
                MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(), MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(),
                VOID_INGOT.item(), MACHINE_CIRCUIT.item(), VOID_INGOT.item(), VOID_INGOT.item(), MACHINE_CIRCUIT.item(), VOID_INGOT.item(),
                INFINITE_INGOT.item(), VOID_INGOT.item(), MACHINE_CIRCUIT.item(), MACHINE_CIRCUIT.item(), VOID_INGOT.item(), INFINITE_INGOT.item(),
                INFINITE_INGOT.item(), VOID_INGOT.item(), MACHINE_CIRCUIT.item(), MACHINE_CIRCUIT.item(), VOID_INGOT.item(), INFINITE_INGOT.item(),
                VOID_INGOT.item(), MACHINE_CIRCUIT.item(), VOID_INGOT.item(), VOID_INGOT.item(), MACHINE_CIRCUIT.item(), VOID_INGOT.item(),
                MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(), MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(), MACHINE_CIRCUIT.item()
        });
        register(Groups.INFINITY_CHEAT, INFINITE_CORE, InfinityWorkbench.TYPE, new ItemStack[] {
                MACHINE_PLATE.item(), MACHINE_CORE.item(), INFINITE_INGOT.item(), INFINITE_INGOT.item(), MACHINE_CORE.item(), MACHINE_PLATE.item(),
                MACHINE_CORE.item(), MACHINE_PLATE.item(), MACHINE_CIRCUIT.item(), MACHINE_CIRCUIT.item(), MACHINE_PLATE.item(), MACHINE_CORE.item(),
                INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(), INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(),
                INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(), INFINITE_INGOT.item(), MACHINE_CIRCUIT.item(), INFINITE_INGOT.item(),
                MACHINE_CORE.item(), MACHINE_PLATE.item(), MACHINE_CIRCUIT.item(), MACHINE_CIRCUIT.item(), MACHINE_PLATE.item(), MACHINE_CORE.item(),
                MACHINE_PLATE.item(), MACHINE_CORE.item(), INFINITE_INGOT.item(), INFINITE_INGOT.item(), MACHINE_CORE.item(), MACHINE_PLATE.item()
        });
        new Singularity(COPPER_SINGULARITY, SlimefunItems.COPPER_INGOT.item(), 3000).register(plugin);
        new Singularity(ZINC_SINGULARITY, SlimefunItems.ZINC_INGOT.item(), 3000).register(plugin);
        new Singularity(TIN_SINGULARITY, SlimefunItems.TIN_INGOT.item(), 3000).register(plugin);
        new Singularity(ALUMINUM_SINGULARITY, SlimefunItems.ALUMINUM_INGOT.item(), 3000).register(plugin);
        new Singularity(SILVER_SINGULARITY, SlimefunItems.SILVER_INGOT.item(), 3000).register(plugin);
        new Singularity(MAGNESIUM_SINGULARITY, SlimefunItems.MAGNESIUM_INGOT.item(), 3000).register(plugin);
        new Singularity(LEAD_SINGULARITY, SlimefunItems.LEAD_INGOT.item(), 3000).register(plugin);
        new Singularity(GOLD_SINGULARITY, Material.GOLD_INGOT, 2000).register(plugin);
        new Singularity(IRON_SINGULARITY, Material.IRON_INGOT, 2000).register(plugin);
        new Singularity(DIAMOND_SINGULARITY, Material.DIAMOND, 500).register(plugin);
        new Singularity(EMERALD_SINGULARITY, Material.EMERALD, 500).register(plugin);
        new Singularity(NETHERITE_SINGULARITY, Material.NETHERITE_INGOT, 200).register(plugin);
        new Singularity(COAL_SINGULARITY, Material.COAL, 1500).register(plugin);
        new Singularity(REDSTONE_SINGULARITY, Material.REDSTONE, 1500).register(plugin);
        new Singularity(LAPIS_SINGULARITY, Material.LAPIS_LAZULI, 1500).register(plugin);
        new Singularity(QUARTZ_SINGULARITY, Material.QUARTZ, 1500).register(plugin);
        new Singularity(INFINITY_SINGULARITY, INFINITE_INGOT, 100).register(plugin);
    }

    private static void registerEnhanced(SlimefunItemStack item, ItemStack[] recipe) {
        register(item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

    private static void registerSmeltery(SlimefunItemStack itemStack, ItemStack... recipe) {
        register(itemStack, RecipeType.SMELTERY, Arrays.copyOf(recipe, 9));
    }

    private static void register(SlimefunItemStack itemStack, RecipeType type, ItemStack[] recipe) {
        register(Groups.MAIN_MATERIALS, itemStack, type, recipe);
    }

    private static void register(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        new SlimefunItem(category, item, recipeType, recipe).register(InfinityExpansion.instance());
    }

}
