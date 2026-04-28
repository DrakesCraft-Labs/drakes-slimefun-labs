package dev.j3fftw.litexpansion;

import dev.j3fftw.extrautils.utils.LoreBuilderDynamic;
import dev.j3fftw.litexpansion.machine.MassFabricator;
import dev.j3fftw.litexpansion.machine.Recycler;
import dev.j3fftw.litexpansion.machine.RubberSynthesizer;
import dev.j3fftw.litexpansion.machine.generators.AdvancedSolarPanel;
import dev.j3fftw.litexpansion.weapons.NanoBlade;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPlayerHead;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents the {@link Items} class.
 *
 * @author j3fftw
 */
public final class Items {

    //region Category
    public static final ItemGroup LITEXPANSION = new ItemGroup(
        new NamespacedKey(LiteXpansion.getInstance(),
            "litexpansion"
        ),
        CustomItemStack.create(VersionedPlayerHead.getItemStack(
            "3f87fc5cbb233743a82fb0fa51fe739487f29bcc01c9026621ecefad197f4fb1"),
            "\u00a77LiteXpansion"
        )
    );

    public static final SlimefunItemStack ELECTRIC_CHESTPLATE = new SlimefunItemStack(
        "ELECTRIC_CHESTPLATE",
        Material.LEATHER_CHESTPLATE, Color.TEAL,
        "\u00a79Electric Chestplate",
        "",
        "\u00a78\u21E8 \u00a77Negates all the damage dealt to player.",
        "",
        "\u00a7c\u00a7o\u00a78\u21E8 \u00a7e\u26A1 \u00a770 / 8192 J"
    );
    //endregion

    //region Armor
    public static final SlimefunItemStack NANO_BLADE = new SlimefunItemStack(
        "NANO_BLADE",
        Material.DIAMOND_SWORD,
        "\u00a72Nano Blade \u00a7c(Off)",
        "",
        "\u00a7fAn advanced piece of technology which can",
        "\u00a7fcut through organic tissue with ease.",
        "",
        "\u00a7fToggle: \u00a7aRight Click",
        "",
        "\u00a7c\u00a7o\u00a78\u21E8 \u00a7e\u26A1 \u00a770 / " + NanoBlade.CAPACITY + " J"
    );
    //endregion

    //region Weapon
    // Tools
    public static final SlimefunItemStack GLASS_CUTTER = new SlimefunItemStack(
        "GLASS_CUTTER",
        Material.GHAST_TEAR,
        "\u00a7bGlass Cutter",
        "",
        "\u00a77> \u00a7eLeft Click \u00a77- Cut glass quickly",
        "\u00a77> \u00a7eRight Click \u00a77- Cut glass slowly",
        "",
        "\u00a7c\u00a7o\u00a78\u21E8 \u00a7e\u26A1 \u00a770 / 300 J"
    );
    public static final SlimefunItemStack MINING_DRILL = new SlimefunItemStack(
        "MINING_DRILL",
        Material.IRON_SHOVEL,
        "\u00a77Mining Drill",
        "",
        "\u00a77Instantly breaks stone and stone variants",
        "\u00a77Right click to break block slower",
        "",
        "\u00a7c\u00a7o\u00a78\u21E8 \u00a7e\u26A1 \u00a770 / 1000 J"
    );
    public static final SlimefunItemStack DIAMOND_DRILL = new SlimefunItemStack(
        "DIAMOND_DRILL",
        Material.DIAMOND_SHOVEL,
        "\u00a7bDiamond Drill",
        "",
        "\u00a77Instantly breaks obsidian, stone, and stone variants",
        "\u00a77Right click to break block slower",
        "",
        "\u00a7c\u00a7o\u00a78\u21E8 \u00a7e\u26A1 \u00a770 / 1000 J"
    );
    public static final SlimefunItemStack TREETAP = new SlimefunItemStack(
        "TREETAP",
        Material.WOODEN_HOE,
        "\u00a77Treetap"
    );
    public static final SlimefunItemStack CARGO_CONFIGURATOR = new SlimefunItemStack(
        "CARGO_CONFIGURATOR",
        Material.COMPASS,
        "\u00a77Cargo Configurator",
        "",
        "\u00a77> \u00a7eRight Click \u00a77- Copy node configuration",
        "\u00a77> \u00a7eLeft Click  \u00a77- Apply node configuration",
        "\u00a77> \u00a7eShift+Right Click \u00a77- Clear node configurator"
    );
    //endregion

    //region Items
    public static final SlimefunItemStack TIN_PLATE = new SlimefunItemStack(
        "TIN_PLATE",
        Material.WHITE_CARPET,
        "\u00a77Tin Plate"
    );

    public static final SlimefunItemStack TIN_ITEM_CASING = new SlimefunItemStack(
        "TIN_ITEM_CASING",
        Material.WHITE_CARPET,
        "\u00a77Tin Item Casing"
    );

    public static final SlimefunItemStack UNINSULATED_TIN_CABLE = new SlimefunItemStack(
        "UNINSULATED_TIN_CABLE",
        Material.STRING,
        "\u00a77Uninsulated Tin Cable"
    );

    public static final SlimefunItemStack TIN_CABLE = new SlimefunItemStack(
        "TIN_CABLE",
        Material.STRING,
        "\u00a77Tin Cable"
    );

    public static final SlimefunItemStack COPPER_PLATE = new SlimefunItemStack(
        "COPPER_PLATE",
        Material.ORANGE_CARPET,
        "\u00a77Copper Plate"
    );

    public static final SlimefunItemStack COPPER_ITEM_CASING = new SlimefunItemStack(
        "COPPER_ITEM_CASING",
        Material.ORANGE_CARPET,
        "\u00a77Copper Item Casing"
    );

    public static final SlimefunItemStack MAG_THOR = new SlimefunItemStack(
        "MAG_THOR",
        Material.IRON_INGOT,
        "\u00a7b\u00a7lMag-Thor",
        "",
        "\u00a77\u00a7oAn extremely durable alloy used",
        "\u00a77\u00a7oonly in the most advanced machines"
    );

    public static final SlimefunItemStack THORIUM = new SlimefunItemStack(
        "THORIUM",
        CustomItemStack.create(VersionedPlayerHead.getItemStack(
            "b87403257c0eaa518cf186deccde137d476556ccff146d503fb2e73956582f37"),
            "Thorium"
        ),
        "\u00a78Thorium",
        "",
        LoreBuilder.radioactive(Radioactivity.HIGH),
        LoreBuilder.HAZMAT_SUIT_REQUIRED
    );

    public static final SlimefunItemStack SCRAP = new SlimefunItemStack(
        "SCRAP",
        Material.DEAD_BUSH,
        "\u00a78Scrap",
        "",
        "\u00a77Can be used to create \u00a75UU-Matter"
    );
    public static final SlimefunItemStack UU_MATTER = new SlimefunItemStack(
        "UU_MATTER",
        Material.PURPLE_DYE,
        "\u00a75UU-Matter",
        "",
        "\u00a77Can be used to create items or resources"
    );
    public static final SlimefunItemStack IRIDIUM = new SlimefunItemStack(
        "IRIDIUM",
        Material.WHITE_DYE,
        "\u00a7fIridium"
    );
    public static final SlimefunItemStack IRIDIUM_PLATE = new SlimefunItemStack(
        "IRIDIUM_PLATE",
        Material.PAPER,
        "\u00a7fIridium Plate",
        "",
        "\u00a77Used to create Iridium Armor"
    );
    public static final SlimefunItemStack THORIUM_DUST = new SlimefunItemStack(
        "THORIUM_DUST",
        Material.BLACK_DYE,
        "\u00a78Thorium Dust"
    );
    public static final SlimefunItemStack REFINED_IRON = new SlimefunItemStack(
        "REFINED_IRON",
        Material.IRON_INGOT,
        "\u00a77Refined Iron"
    );
    public static final SlimefunItemStack MACHINE_BLOCK = new SlimefunItemStack(
        "MACHINE_BLOCK",
        Material.IRON_BLOCK,
        "\u00a77Machine Block"
    );
    public static final SlimefunItemStack UNINSULATED_COPPER_CABLE = new SlimefunItemStack(
        "UNINSULATED_COPPER_CABLE",
        Material.STRING,
        "\u00a77Uninsulated Copper Cable"
    );
    public static final SlimefunItemStack COPPER_CABLE = new SlimefunItemStack(
        "COPPER_CABLE",
        Material.STRING,
        "\u00a77Copper Cable"
    );
    public static final SlimefunItemStack RUBBER = new SlimefunItemStack(
        "RUBBER",
        Material.INK_SAC,
        "\u00a77Rubber"
    );
    public static final SlimefunItemStack ELECTRONIC_CIRCUIT = new SlimefunItemStack(
        "ELECTRONIC_CIRCUIT",
        Material.COBWEB,
        "\u00a77Electronic Circuit"
    );
    public static final SlimefunItemStack ADVANCED_CIRCUIT = new SlimefunItemStack(
        "ADVANCED_CIRCUIT",
        Material.COBWEB,
        "\u00a77Advanced Circuit"
    );
    //endregion

    //region Carbon Crap
    //////////////////////////
    // Carbon Crap
    //////////////////////////
    public static final SlimefunItemStack COAL_DUST = new SlimefunItemStack(
        "COAL_DUST",
        Material.BLACK_DYE,
        "\u00a77Coal Dust"
    );
    public static final SlimefunItemStack RAW_CARBON_FIBRE = new SlimefunItemStack(
        "RAW_CARBON_FIBRE",
        Material.BLACK_DYE,
        "\u00a77Raw Carbon Fibre"
    );
    public static final SlimefunItemStack RAW_CARBON_MESH = new SlimefunItemStack(
        "RAW_CARBON_MESH",
        Material.BLACK_DYE,
        "\u00a77Raw Carbon Mesh"
    );
    public static final SlimefunItemStack CARBON_PLATE = new SlimefunItemStack(
        "CARBON_PLATE",
        Material.BLACK_CARPET,
        "\u00a77Carbon Plate"
    );
    public static final SlimefunItemStack ADVANCED_ALLOY = new SlimefunItemStack(
        "ADVANCED_ALLOY",
        Material.PAPER,
        "\u00a77Advanced Alloy"
    );
    //endregion

    public static final SlimefunItemStack ADVANCED_MACHINE_BLOCK = new SlimefunItemStack(
        "ADVANCED_MACHINE_BLOCK",
        Material.DIAMOND_BLOCK,
        "\u00a77Advanced Machine Block"
    );

    public static final SlimefunItemStack LAPOTRON_CRYSTAL = new SlimefunItemStack(
        "LAPOTRON_CRYSTAL",
        Material.DIAMOND,
        "\u00a77Lapotron Crystal"
    );
    public static final SlimefunItemStack REINFORCED_STONE = new SlimefunItemStack(
        "REINFORCED_STONE",
        Material.STONE,
        "\u00a77Reinforced Stone"
    );
    public static final SlimefunItemStack REINFORCED_DOOR = new SlimefunItemStack(
        "REINFORCED_DOOR",
        Material.IRON_DOOR,
        "\u00a77Reinforced Door"
    );
    public static final SlimefunItemStack REINFORCED_GLASS = new SlimefunItemStack(
        "REINFORCED_GLASS",
        Material.GRAY_STAINED_GLASS,
        "\u00a77Reinforced Glass"
    );
    public static final SlimefunItemStack MIXED_METAL_INGOT = new SlimefunItemStack(
        "MIXED_METAL_INGOT",
        Material.IRON_INGOT,
        "\u00a77Mixed Metal Ingot"
    );
    // Machines
    public static final SlimefunItemStack RECYCLER = new SlimefunItemStack(
        "SCRAP_MACHINE",
        Material.BLACK_CONCRETE,
        "\u00a78Recycler",
        "",
        "\u00a7fProduces \u00a78Scrap \u00a7ffrom anything",
        "",
        LoreBuilderDynamic.powerBuffer(Recycler.CAPACITY),
        LoreBuilderDynamic.powerPerTick(Recycler.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack MASS_FABRICATOR_MACHINE = new SlimefunItemStack(
        "MASS_FABRICATOR_MACHINE",
        Material.PURPLE_CONCRETE,
        "\u00a75Mass Fabricator",
        "",
        "\u00a7fConverts \u00a78Scrap \u00a7fto \u00a75UU-Matter",
        "",
        LoreBuilderDynamic.powerBuffer(MassFabricator.CAPACITY),
        LoreBuilderDynamic.powerPerTick(MassFabricator.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack RUBBER_SYNTHESIZER_MACHINE = new SlimefunItemStack(
        "RUBBER_SYNTHESIZER",
        Material.ORANGE_CONCRETE,
        "\u00a76Rubber Synthesizer",
        "",
        "\u00a7fConverts Bucket of Oil to \u00a77Rubber",
        "",
        LoreBuilderDynamic.powerBuffer(RubberSynthesizer.CAPACITY),
        LoreBuilderDynamic.powerPerTick(RubberSynthesizer.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack MACERATOR = new SlimefunItemStack(
        "MACERATOR",
        Material.IRON_BLOCK,
        "\u00a77Macerator"
    );
    //// Solar panels
    public static final SlimefunItemStack ADVANCED_SOLAR_PANEL = new SlimefunItemStack(
        "ADVANCED_SOLAR_PANEL",
        Material.BLACK_GLAZED_TERRACOTTA,
        "\u00a77\u00a7lAdvanced Solar Panel",
        "\u00a79Works at Night",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedSolarPanel.ADVANCED_STORAGE),
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ADVANCED_DAY_RATE) + " (Day)",
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ADVANCED_NIGHT_RATE) + " (Night)"
    );
    public static final SlimefunItemStack HYBRID_SOLAR_PANEL = new SlimefunItemStack(
        "HYBRID_SOLAR_PANEL",
        Material.GRAY_GLAZED_TERRACOTTA,
        "\u00a7b\u00a7lHybrid Solar Panel",
        "\u00a79Works at Night",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedSolarPanel.HYBRID_STORAGE),
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.HYBRID_DAY_RATE) + " (Day + Nether)",
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.HYBRID_NIGHT_RATE) + " (Night + End)"
    );
    public static final SlimefunItemStack ULTIMATE_SOLAR_PANEL = new SlimefunItemStack(
        "ULTIMATE_SOLAR_PANEL",
        Material.PURPLE_GLAZED_TERRACOTTA,
        "\u00a75\u00a7lUltimate Solar Panel",
        "\u00a79Works at Night",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedSolarPanel.ULTIMATE_STORAGE),
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ULTIMATE_DAY_RATE) + " (Day)",
        LoreBuilderDynamic.powerPerTick(AdvancedSolarPanel.ULTIMATE_NIGHT_RATE) + " (Night)"
    );
    public static final SlimefunItemStack MULTI_FUNCTIONAL_ELECTRIC_STORAGE_UNIT = new SlimefunItemStack(
        "MULTI_FUNCTIONAL_ELECTRIC_STORAGE_UNIT",
        Material.IRON_BLOCK,
        "\u00a77MFE"
    );
    public static final SlimefunItemStack MULTI_FUNCTIONAL_STORAGE_UNIT = new SlimefunItemStack(
        "MULTI_FUNCTIONAL_STORAGE_UNIT",
        Material.DIAMOND_BLOCK,
        "\u00a77MFSU"
    );
    public static final SlimefunItemStack GOLD_PLATE = new SlimefunItemStack(
        "GOLD_PLATE",
        Material.YELLOW_CARPET,
        "\u00a77Gold Plate"
    );
    public static final SlimefunItemStack GOLD_ITEM_CASING = new SlimefunItemStack(
        "GOLD_ITEM_CASING",
        Material.YELLOW_CARPET,
        "\u00a77Gold Item Casing"
    );
    public static final SlimefunItemStack UNINSULATED_GOLD_CABLE = new SlimefunItemStack(
        "UNINSULATED_GOLD_CABLE",
        Material.STRING,
        "\u00a77Uninsulated Gold Cable"
    );
    public static final SlimefunItemStack GOLD_CABLE = new SlimefunItemStack(
        "GOLD_CABLE",
        Material.STRING,
        "\u00a77Gold Cable"
    );
    //Basic Machines
    public static final SlimefunItemStack REFINED_SMELTERY = new SlimefunItemStack(
        "REFINED_SMELTERY",
        Material.BLAST_FURNACE,
        "\u00a77Refined Smeltery"
    );

    public static final SlimefunItemStack METAL_FORGE = new SlimefunItemStack(
        "METAL_FORGE",
        Material.DISPENSER,
        "\u00a77Metal Forge"
    );

    public static final SlimefunItemStack MANUAL_MILL = new SlimefunItemStack(
        "MANUAL_MILL",
        Material.DISPENSER,
        "\u00a77Manual Mill"
    );

    public static final SlimefunItemStack GENERATOR = new SlimefunItemStack(
        "GENERATOR",
        Material.IRON_BLOCK,
        "\u00a77Generator"
    );

    public static final SlimefunItemStack RE_BATTERY = new SlimefunItemStack(
        "RE_BATTERY",
        Material.GLASS_BOTTLE,
        "\u00a77RE Battery"
    );

    public static final SlimefunItemStack ADVANCED_SOLAR_HELMET = new SlimefunItemStack(
        "ADVANCED_SOLAR_HELMET",
        Material.IRON_HELMET,
        "\u00a77Advanced Solar Helmet"
    );

    public static final SlimefunItemStack CARBONADO_SOLAR_HELMET = new SlimefunItemStack(
        "CARBONADO_SOLAR_HELMET",
        Material.GOLDEN_HELMET,
        "\u00a77Carbonado Solar Helmet"
    );

    public static final SlimefunItemStack ENERGIZED_SOLAR_HELMET = new SlimefunItemStack(
        "ENERGIZED_SOLAR_HELMET",
        Material.GOLDEN_HELMET,
        "\u00a77Energized Solar Helmet"
    );

    public static final SlimefunItemStack ADVANCEDLX_SOLAR_HELMET = new SlimefunItemStack(
        "ADVANCEDLX_SOLAR_HELMET",
        Material.DIAMOND_HELMET,
        "\u00a77Super Advanced Solar Helmet"
    );

    public static final SlimefunItemStack HYBRID_SOLAR_HELMET = new SlimefunItemStack(
        "HYBRID_SOLAR_HELMET",
        Material.DIAMOND_HELMET,
        "\u00a77Hybrid Solar Helmet"
    );

    public static final SlimefunItemStack ULTIMATE_SOLAR_HELMET = new SlimefunItemStack(
        "ULTIMATE_SOLAR_HELMET",
        Material.DIAMOND_HELMET,
        "\u00a77Ultimate Solar Helmet"
    );

    public static final SlimefunItemStack FOOD_SYNTHESIZER = new SlimefunItemStack(
        "FOOD_SYNTHESIZER",
        CustomItemStack.create(VersionedPlayerHead.getItemStack(
            "a967efe969d264f635f2c201c34381ef59c72e16ec50af7692033121e22fba9c"),
            "Food Synthesizer"),
        "\u00a7dFood Synthesizer",
        "",
        "\u00a7fKeeps you fed with artificial food.",
        "\u00a7fComes in the flavors!",
        "",
        "\u00a7c\u00a7o\u00a78\u21E8 \u00a7e\u26A1 \u00a770 / 100 J"
    );

    //////////////////////////
    // Plating
    //////////////////////////
    public static final SlimefunItemStack IRON_PLATE = new SlimefunItemStack(
        "IRON_PLATE",
        Material.WHITE_CARPET,
        "\u00a77Iron Plate"
    );

    public static final SlimefunItemStack DIAMOND_PLATE = new SlimefunItemStack(
        "DIAMOND_PLATE",
        Material.CYAN_CARPET,
        "\u00a77Diamond Plate"
    );
    public static final SlimefunItemStack THORIUM_PLATE = new SlimefunItemStack(
        "THORIUM_PLATE",
        Material.GRAY_CARPET,
        "\u00a77Thorium Plate"
    );

    public static final SlimefunItemStack POWER_UNIT = new SlimefunItemStack(
        "POWER_UNIT",
        Material.GOLDEN_HOE,
        "\u00a77Power Unit"
    );

    public static final SlimefunItemStack IRON_ITEM_CASING = new SlimefunItemStack(
        "IRON_ITEM_CASING",
        Material.GRAY_CARPET,
        "\u00a77Iron Item Casing"
    );

    //////////////////////////
    // Dust
    //////////////////////////
    public static final SlimefunItemStack LAPIS_DUST = new SlimefunItemStack(
        "LAPIS_DUST",
        Material.PURPLE_DYE,
        "\u00a77Lapiz Dust"
    );

    public static final SlimefunItemStack REDSTONE_DUST = new SlimefunItemStack(
        "REDSTONE_DUST",
        Material.RED_DYE,
        "\u00a77Redstone Dust"
    );

    public static final SlimefunItemStack DIAMOND_DUST = new SlimefunItemStack(
        "DIAMOND_DUST",
        Material.CYAN_DYE,
        "\u00a77Diamond Dust"
    );
    public static final SlimefunItemStack EMERALD_DUST = new SlimefunItemStack(
        "EMERALD_DUST",
        Material.LIME_DYE,
        "\u00a77Emerald Dust"
    );
    public static final SlimefunItemStack QUARTZ_DUST = new SlimefunItemStack(
        "QUARTZ_DUST",
        Material.WHITE_DYE,
        "\u00a77Quartz Dust"
    );
    public static final SlimefunItemStack ANCIENT_DEBRIS_DUST = new SlimefunItemStack(
        "ANCIENT_DEBRIS_DUST",
        Material.BROWN_DYE,
        "\u00a77Ancient Debris Dust"
    );

    public static final SlimefunItemStack UU_CRAFTER = new SlimefunItemStack(
        "UU_CRAFTER",
        Material.CRAFTING_TABLE,
        "\u00a77UU Crafter"
    );

    public static final SlimefunItemStack CONVERTER = new SlimefunItemStack(
        "CONVER",
        Material.COAL_BLOCK,
        "\u00a77Converter"
    );

    static {
        ADVANCED_SOLAR_HELMET.item().addEnchantment(Enchantment.UNBREAKING, 1);
        CARBONADO_SOLAR_HELMET.item().addEnchantment(Enchantment.UNBREAKING, 2);
        ENERGIZED_SOLAR_HELMET.item().addEnchantment(Enchantment.UNBREAKING, 3);
        ADVANCEDLX_SOLAR_HELMET.item().addUnsafeEnchantment(Enchantment.UNBREAKING, 4);
        HYBRID_SOLAR_HELMET.item().addUnsafeEnchantment(Enchantment.UNBREAKING, 5);
        ULTIMATE_SOLAR_HELMET.item().addUnsafeEnchantment(Enchantment.UNBREAKING, 6);
        setGlint(ADVANCED_CIRCUIT);
        setGlint(GLASS_CUTTER);
        setGlint(DIAMOND_DRILL);
        setGlint(LAPOTRON_CRYSTAL);
        setGlint(ADVANCEDLX_SOLAR_HELMET);
        setGlint(HYBRID_SOLAR_HELMET);
        setGlint(ULTIMATE_SOLAR_HELMET);
    }

    private static void setGlint(SlimefunItemStack item) {
        ItemMeta meta = item.item().getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        item.item().setItemMeta(meta);
    }

    private Items() {}
}
