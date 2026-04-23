package me.voper.slimeframe.implementation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft-labs.slimefun4.implementation.items.misc.AlloyIngot;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.attributes.FreezingItem;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.components.PrimeComponents;
import me.voper.slimeframe.implementation.items.components.UtilsComponents;
import me.voper.slimeframe.implementation.items.gear.CryogenicArmorPiece;
import me.voper.slimeframe.implementation.items.generators.CumulativeGenerator;
import me.voper.slimeframe.implementation.items.machines.*;
import me.voper.slimeframe.implementation.items.multiblocks.Foundry;
import me.voper.slimeframe.implementation.items.relics.Relic;
import me.voper.slimeframe.implementation.items.resources.*;
import me.voper.slimeframe.implementation.items.tools.*;

import net.md_5.bungee.api.ChatColor;

public final class SFrameItems {

    public static final Set<SlimefunItem> ALLOYS = new HashSet<>();
    public static final HashMap<SlimefunItemStack, SlimefunItemStack> ALLOY_PLATES_MAP = new HashMap<>();

    private final SlimeFrame plugin;
    private final SettingsManager settingsManager = SlimeFrame.getSettingsManager();

    public SFrameItems(@Nonnull SlimeFrame plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        this.registerRelics();
        this.registerMultiBlocks();
        this.registerResources();
        this.registerMachines();
        this.registerGenerators();
        this.registerGear();
        this.registerUtilsAndTools();
        this.registerPrimeComponents();
        this.registerFreezingItems();
    }

    private void registerRelics() {
        Relic.create(SFrameStacks.LITH_A1).register(plugin);
        Relic.create(SFrameStacks.LITH_T1).register(plugin);
        Relic.create(SFrameStacks.LITH_O1).register(plugin);
        Relic.create(SFrameStacks.LITH_C1).register(plugin);

        Relic.create(SFrameStacks.MESO_B1).register(plugin);
        Relic.create(SFrameStacks.MESO_W1).register(plugin);
        Relic.create(SFrameStacks.MESO_S1).register(plugin);
        Relic.create(SFrameStacks.MESO_C1).register(plugin);

        Relic.create(SFrameStacks.NEO_A1).register(plugin);
        Relic.create(SFrameStacks.NEO_C1).register(plugin);
        Relic.create(SFrameStacks.NEO_D1).register(plugin);
        Relic.create(SFrameStacks.NEO_P1).register(plugin);

        Relic.create(SFrameStacks.AXI_F1).register(plugin);
        Relic.create(SFrameStacks.AXI_N1).register(plugin);
        Relic.create(SFrameStacks.AXI_G1).register(plugin);
        Relic.create(SFrameStacks.AXI_T1).register(plugin);
    }

    private void registerResources() {
        new SpecialOre(SFrameStacks.PYROL, Material.END_STONE, SpecialOre.Rarity.COMMON).register(plugin);
        new SpecialOre(SFrameStacks.TRAVORIDE, Material.STONE, SpecialOre.Rarity.COMMON).register(plugin);
        new SpecialOre(SFrameStacks.ADRAMALIUM, Material.NETHERRACK, SpecialOre.Rarity.COMMON).register(plugin);
        new SpecialOre(SFrameStacks.FERROS, new Material[]{Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE}, SpecialOre.Rarity.UNCOMMON).register(plugin);
        new SpecialOre(SFrameStacks.VENEROL, new Material[]{Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE}, SpecialOre.Rarity.UNCOMMON).register(plugin);
        new SpecialOre(SFrameStacks.NAMALON, Material.NETHER_QUARTZ_ORE, SpecialOre.Rarity.UNCOMMON).register(plugin);
        new SpecialOre(SFrameStacks.AURON, new Material[]{Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE}, SpecialOre.Rarity.RARE).register(plugin);
        new SpecialOre(SFrameStacks.HESPERON, new Material[]{Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE}, SpecialOre.Rarity.RARE).register(plugin);
        new SpecialOre(SFrameStacks.THAUMICA, Material.NETHER_GOLD_ORE, SpecialOre.Rarity.RARE).register(plugin);

        registerAlloy(SFrameStacks.PYROTIC_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.PYROL, 2), SlimefunItems.REINFORCED_ALLOY_INGOT, SFrameStacks.RUBEDO, SFrameStacks.CRYOTIC});
        registerAlloy(SFrameStacks.ADRAMAL_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.ADRAMALIUM, 2), new SlimefunItemStack(SFrameStacks.TRAVORIDE, 2), SFrameStacks.PLASTIDS, SFrameStacks.NEURODES});
        registerAlloy(SFrameStacks.TRAVOCYTE_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.TRAVORIDE, 2), SFrameStacks.PLASTIDS, new SlimefunItemStack(SFrameStacks.SALVAGE, 16)});
        registerAlloy(SFrameStacks.FERSTEEL_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.FERROS, 2), SFrameStacks.RUBEDO, SFrameStacks.PLASTIDS});
        registerAlloy(SFrameStacks.VENERDO_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.VENEROL, 2), SFrameStacks.RUBEDO, SFrameStacks.GALLIUM});
        registerAlloy(SFrameStacks.DEVOLVED_NAMALON, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.NAMALON, 2), new SlimefunItemStack(SFrameStacks.FERROS, 2), SFrameStacks.RUBEDO, SFrameStacks.NEURODES});
        registerAlloy(SFrameStacks.AUROXIUM_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.AURON, 2), SFrameStacks.MORPHICS, SlimefunItems.BLISTERING_INGOT_3});
        registerAlloy(SFrameStacks.HESPAZYM_ALLOY, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.HESPERON, 2), SFrameStacks.PLASTIDS, SFrameStacks.MORPHICS, SFrameStacks.CRYOTIC});
        registerAlloy(SFrameStacks.THAUMIC_DISTILLATE, new ItemStack[]{
                new SlimefunItemStack(SFrameStacks.THAUMICA, 2), new SlimefunItemStack(SFrameStacks.VENEROL, 2), SFrameStacks.GALLIUM, SlimefunItems.REINFORCED_ALLOY_INGOT});

        ALLOYS.stream()
                .sorted(Comparator.comparing(SlimefunItem::getId))
                .forEach(this::registerAlloyPlate);

        SlimefunItemStack[] plates = ALLOY_PLATES_MAP.values()
                .stream()
                .sorted(Comparator.comparing(SlimefunItemStack::getItemId))
                .toList()
                .toArray(new SlimefunItemStack[9]);
        new Resource(SFrameStacks.CONDENSED_PLATE, plates).register(plugin);

        new Cryotic().register(plugin);
        new CoolantCanister().register(plugin);
        new Resource(SFrameStacks.DILUTED_THERMIA, ThermiaExtractor.RECIPE_TYPE, new ItemStack[]{
                null, null, null, new SlimefunItemStack(SFrameStacks.COOLANT_CANISTER, 5)
        }).register(plugin);

        new Resource(SFrameStacks.TELLURIUM_FRAGMENT, TelluriumFragmentsSynthesizer.RECIPE_TYPE).register(plugin);
        new Resource(SFrameStacks.TELLURIUM, SFrameStacks.TELLURIUM_FRAGMENT).register(plugin);

        Resource.createRadioactive(SFrameStacks.BOOSTED_TELLURIUM, Radioactivity.VERY_DEADLY, new ItemStack[]{
                SFrameStacks.ARGON_CRYSTAL, SFrameStacks.CRYOTIC, SFrameStacks.DILUTED_THERMIA,
                SFrameStacks.CRYOTIC, SFrameStacks.TELLURIUM, SFrameStacks.CRYOTIC,
                SFrameStacks.DILUTED_THERMIA, SFrameStacks.CRYOTIC, SFrameStacks.ARGON_CRYSTAL
        }).register(plugin);

        Resource.createRadioactive(SFrameStacks.RUBEDO, Radioactivity.VERY_DEADLY, new ItemStack[]{
                XMaterial.REDSTONE_BLOCK.parseItem(), SlimefunItems.POWER_CRYSTAL, XMaterial.REDSTONE_BLOCK.parseItem(),
                SlimefunItems.POWER_CRYSTAL, SlimefunItems.PLUTONIUM, SlimefunItems.POWER_CRYSTAL,
                XMaterial.REDSTONE_BLOCK.parseItem(), SlimefunItems.POWER_CRYSTAL, XMaterial.REDSTONE_BLOCK.parseItem()
        }).register(plugin);

        Resource.createRadioactive(SFrameStacks.ARGON_CRYSTAL, Radioactivity.VERY_HIGH, new ItemStack[]{
                XMaterial.AMETHYST_SHARD.parseItem(), SlimefunItems.BOOSTED_URANIUM, SFrameStacks.TELLURIUM,
                SlimefunItems.BOOSTED_URANIUM, XMaterial.END_CRYSTAL.parseItem(), SlimefunItems.BOOSTED_URANIUM,
                SFrameStacks.TELLURIUM, SlimefunItems.BOOSTED_URANIUM, XMaterial.AMETHYST_SHARD.parseItem()
        }).register(plugin);

        new Resource(SFrameStacks.CUBIC_DIODES, new ItemStack[]{
                SlimefunItems.SILICON, SFrameStacks.MORPHICS, SlimefunItems.SILICON,
                SFrameStacks.GALLIUM, SlimefunItems.NETHER_ICE, SFrameStacks.GALLIUM,
                SlimefunItems.SILICON, SFrameStacks.MORPHICS, SlimefunItems.SILICON
        }).register(plugin);

        new Resource(SFrameStacks.PLASTIDS, new ItemStack[]{
                new ItemStack(Material.SPIDER_EYE), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.FERMENTED_SPIDER_EYE),
                new ItemStack(Material.CRIMSON_FUNGUS), new ItemStack(Material.POISONOUS_POTATO), new ItemStack(Material.WARPED_FUNGUS),
                new ItemStack(Material.FERMENTED_SPIDER_EYE), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.SPIDER_EYE)
        }).register(plugin);

        new Resource(SFrameStacks.CONTROL_MODULE, new ItemStack[]{
                SFrameStacks.FERSTEEL_ALLOY, SFrameStacks.TRAVOCYTE_ALLOY, SFrameStacks.FERSTEEL_ALLOY,
                SFrameStacks.CUBIC_DIODES, SlimefunItems.PROGRAMMABLE_ANDROID, SFrameStacks.CUBIC_DIODES,
                SFrameStacks.FERSTEEL_ALLOY, SFrameStacks.TRAVOCYTE_ALLOY, SFrameStacks.FERSTEEL_ALLOY
        }).register(plugin);

        new Resource(SFrameStacks.GALLIUM, Recycler.RECIPE_TYPE, new ItemStack[]{
                null, null, null, null, new SlimefunItemStack(SlimefunItems.ADVANCED_CIRCUIT_BOARD, 8)
        }).register(plugin);

        new Resource(SFrameStacks.SALVAGE, Recycler.RECIPE_TYPE, new ItemStack[]{
                null, null, null, null, new CustomItemStack(Material.FURNACE, ChatColor.AQUA + "Any Machine or Generator")
        }, new SlimefunItemStack(SFrameStacks.SALVAGE, 8)).register(plugin);

        new Resource(SFrameStacks.PRISMATIC_ENERGIZED_CORE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON), SFrameStacks.ARGON_CRYSTAL, getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON),
                SFrameStacks.DILUTED_THERMIA, SFrameStacks.CONDENSED_PLATE, SFrameStacks.DILUTED_THERMIA,
                getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON), SFrameStacks.ARGON_CRYSTAL, getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON)
        }).register(plugin);

        new MobDropItem(Groups.RESOURCES, SFrameStacks.MORPHICS)
                .setMobChanceMap(settingsManager.getDropChanceMap(SettingsManager.ConfigField.MORPHICS_CHANCE))
                .register(plugin);

        new MobDropItem(Groups.RESOURCES, SFrameStacks.NEURAL_SENSORS)
                .setMobChanceMap(settingsManager.getDropChanceMap(SettingsManager.ConfigField.NEURAL_SENSORS))
                .register(plugin);

        new MobDropItem(Groups.RESOURCES, SFrameStacks.NEURODES)
                .setMobChanceMap(settingsManager.getDropChanceMap(SettingsManager.ConfigField.NEURODES_CHANCE))
                .register(plugin);

        new MobDropItem(Groups.RESOURCES, SFrameStacks.OROKIN_CELL)
                .setMobChanceMap(settingsManager.getDropChanceMap(SettingsManager.ConfigField.OROKIN_CELL_CHANCE))
                .register(plugin);

    }

    private void registerMultiBlocks() {
        new Foundry().register(plugin);
    }

    private void registerMachines() {
        new ArtificialMangrove(Groups.MACHINES, SFrameStacks.ARTIFICIAL_MANGROVE, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.FERSTEEL_ALLOY, SFrameStacks.ADRAMAL_ALLOY, SFrameStacks.FERSTEEL_ALLOY,
                SFrameStacks.TRAVOCYTE_ALLOY, XMaterial.MUD.parseItem(), SFrameStacks.TRAVOCYTE_ALLOY,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new ArtificialMangrove(Groups.MACHINES, SFrameStacks.ADV_ARTIFICIAL_MANGROVE, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.HESPAZYM_ALLOY, SFrameStacks.HESPAZYM_ALLOY, SFrameStacks.HESPAZYM_ALLOY,
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.ARTIFICIAL_MANGROVE, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY),
                SlimefunItems.ELECTRIC_MOTOR, SFrameStacks.CONTROL_MODULE, SlimefunItems.ELECTRIC_MOTOR
        }).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new ArtificialMangrove(Groups.MACHINES, SFrameStacks.PRIME_ARTIFICIAL_MANGROVE, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SFrameStacks.OROKIN_CELL, null,
                getAlloyPlate(SFrameStacks.PYROTIC_ALLOY), SFrameStacks.ADV_ARTIFICIAL_MANGROVE, getAlloyPlate(SFrameStacks.PYROTIC_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_ARTIFICIAL_MANGROVE), PrimeComponents.createCoreModule(SFrameStacks.PRIME_ARTIFICIAL_MANGROVE), PrimeComponents.createPowerCell(SFrameStacks.PRIME_ARTIFICIAL_MANGROVE)
        }).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

        new AutoTrader(Groups.MACHINES, SFrameStacks.AUTO_TRADER, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.THAUMIC_DISTILLATE), SlimefunItems.VILLAGER_RUNE, getAlloyPlate(SFrameStacks.THAUMIC_DISTILLATE),
                getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), XMaterial.CARTOGRAPHY_TABLE.parseItem(), getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY),
                SFrameStacks.DILUTED_THERMIA, SFrameStacks.CONTROL_MODULE, SFrameStacks.DILUTED_THERMIA
        }).setEnergyPerTick(1024).register(plugin);

        new BasaltGenerator(Groups.MACHINES, SFrameStacks.BASALT_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SlimefunItems.FREEZER_2, SlimefunItems.FREEZER_2, SlimefunItems.FREEZER_2,
                SlimefunItems.ELECTRIFIED_CRUCIBLE_2, XMaterial.BASALT.parseItem(), SlimefunItems.ELECTRIFIED_CRUCIBLE_2,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setProduction(1).setEnergyPerTick(128).register(plugin);

        new BasaltGenerator(Groups.MACHINES, SFrameStacks.ADV_BASALT_GEN, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), getAlloyPlate(SFrameStacks.VENERDO_ALLOY), getAlloyPlate(SFrameStacks.VENERDO_ALLOY),
                SFrameStacks.CRYOTIC, SFrameStacks.BASALT_GENERATOR, SFrameStacks.CRYOTIC,
                SlimefunItems.ELECTRIC_MOTOR, SFrameStacks.CONTROL_MODULE, SlimefunItems.ELECTRIC_MOTOR
        }).setProduction(4).setEnergyPerTick(256).register(plugin);

        new BasaltGenerator(Groups.MACHINES, SFrameStacks.PRIME_BASALT_GEN, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SFrameStacks.OROKIN_CELL, null,
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.ADV_BASALT_GEN, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_BASALT_GEN), PrimeComponents.createCoreModule(SFrameStacks.PRIME_BASALT_GEN), PrimeComponents.createPowerCell(SFrameStacks.PRIME_BASALT_GEN)
        }).setProduction(64).setEnergyPerTick(512).register(plugin);

        new ChunkEater(SFrameStacks.CHUNK_EATER, new ItemStack[]{
                SFrameStacks.ARGON_CRYSTAL, SlimefunItems.ENERGIZED_CAPACITOR, SFrameStacks.ARGON_CRYSTAL,
                SFrameStacks.PYROTIC_ALLOY, XMaterial.SCULK.parseItem(), SFrameStacks.PYROTIC_ALLOY,
                SlimefunItems.HEATING_COIL, SFrameStacks.CONTROL_MODULE, SlimefunItems.HEATING_COIL
        }).setEnergyPerTick(1024).register(plugin);

        new ChunkEater(SFrameStacks.ADV_CHUNK_EATER, new ItemStack[]{
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SlimefunItems.ENERGIZED_CAPACITOR, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY),
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SFrameStacks.CHUNK_EATER, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY),
                SlimefunItems.PROGRAMMABLE_ANDROID_MINER, SFrameStacks.CONTROL_MODULE, SlimefunItems.PROGRAMMABLE_ANDROID_MINER
        }).setCollectDestroyedBlocks().setEnergyPerTick(2048).register(plugin);

        new ChunkEater(SFrameStacks.PRIME_CHUNK_EATER, new ItemStack[]{
                SFrameStacks.RUBEDO, SFrameStacks.OROKIN_CELL, SFrameStacks.RUBEDO,
                SFrameStacks.BOOSTED_TELLURIUM, SFrameStacks.ADV_CHUNK_EATER, SFrameStacks.BOOSTED_TELLURIUM,
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_CHUNK_EATER), PrimeComponents.createCoreModule(SFrameStacks.PRIME_CHUNK_EATER), PrimeComponents.createPowerCell(SFrameStacks.PRIME_CHUNK_EATER)
        }).setLayerMode().setCollectDestroyedBlocks().setEnergyPerTick(4096).register(plugin);

        new ConcreteGenerator(Groups.MACHINES, SFrameStacks.CONCRETE_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.FERSTEEL_ALLOY, SlimefunItems.RAINBOW_CONCRETE, SFrameStacks.FERSTEEL_ALLOY,
                SlimefunItems.ELECTRIC_MOTOR, XMaterial.BRICKS.parseItem(), SlimefunItems.ELECTRIC_MOTOR,
                SFrameStacks.CUBIC_DIODES, SlimefunItems.BIG_CAPACITOR, SFrameStacks.CUBIC_DIODES
        }).setEnergyPerTick(128).register(plugin);

        new ConcreteGenerator(Groups.MACHINES, SFrameStacks.ADV_CONCRETE_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.FERSTEEL_ALLOY, SFrameStacks.FERSTEEL_ALLOY, SFrameStacks.FERSTEEL_ALLOY,
                getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY), SFrameStacks.CONCRETE_GENERATOR, getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY),
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new ConcreteGenerator(Groups.MACHINES, SFrameStacks.PRIME_CONCRETE_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SFrameStacks.OROKIN_CELL, null,
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SFrameStacks.ADV_CONCRETE_GENERATOR, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_CONCRETE_GENERATOR), PrimeComponents.createCoreModule(SFrameStacks.PRIME_CONCRETE_GENERATOR), PrimeComponents.createPowerCell(SFrameStacks.PRIME_CONCRETE_GENERATOR)
        }).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

        new CryoticExtractor(Groups.MACHINES, SFrameStacks.CRYOTIC_EXTRACTOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.FERSTEEL_ALLOY, SFrameStacks.RUBEDO, SFrameStacks.FERSTEEL_ALLOY,
                SFrameStacks.TRAVOCYTE_ALLOY, XMaterial.BEACON.parseItem(), SFrameStacks.TRAVOCYTE_ALLOY,
                SlimefunItems.HEATING_COIL, SlimefunItems.BIG_CAPACITOR, SlimefunItems.HEATING_COIL
        }).setEnergyPerTick(512).register(plugin);

        new CryoticExtractor(Groups.MACHINES, SFrameStacks.ADV_CRYOTIC_EXTRACTOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY),
                SFrameStacks.TELLURIUM, SFrameStacks.CRYOTIC_EXTRACTOR, SFrameStacks.TELLURIUM,
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProcessingSpeed(4).setEnergyPerTick(1024).register(plugin);

        new CryoticExtractor(Groups.MACHINES, SFrameStacks.PRIME_CRYOTIC_EXTRACTOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.OROKIN_CELL, SFrameStacks.OROKIN_CELL, SFrameStacks.OROKIN_CELL,
                SFrameStacks.GALLIUM, SFrameStacks.ADV_CRYOTIC_EXTRACTOR, SFrameStacks.GALLIUM,
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_CRYOTIC_EXTRACTOR), PrimeComponents.createCoreModule(SFrameStacks.PRIME_CRYOTIC_EXTRACTOR), PrimeComponents.createPowerCell(SFrameStacks.PRIME_CRYOTIC_EXTRACTOR)
        }).setProcessingSpeed(20).setEnergyPerTick(2048).register(plugin);

        new DustGenerator(Groups.MACHINES, SFrameStacks.DUST_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SlimefunItems.ELECTRIC_DUST_WASHER_3, SlimefunItems.ELECTRIC_GOLD_PAN_3, SlimefunItems.ELECTRIC_ORE_GRINDER_3,
                SFrameStacks.BOOSTED_TELLURIUM, XMaterial.FURNACE.parseItem(), SFrameStacks.BOOSTED_TELLURIUM,
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setEnergyPerTick(256).register(plugin);

        new DustGenerator(Groups.MACHINES, SFrameStacks.ADV_DUST_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.THAUMIC_DISTILLATE, SFrameStacks.THAUMIC_DISTILLATE, SFrameStacks.THAUMIC_DISTILLATE,
                getAlloyPlate(SFrameStacks.PYROTIC_ALLOY), SFrameStacks.DUST_GENERATOR, getAlloyPlate(SFrameStacks.PYROTIC_ALLOY),
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProduction(4).setProcessingSpeed(2).setEnergyPerTick(512).register(plugin);

        new DustGenerator(Groups.MACHINES, SFrameStacks.PRIME_DUST_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.CONDENSED_PLATE, SFrameStacks.OROKIN_CELL, SFrameStacks.CONDENSED_PLATE,
                SFrameStacks.CONDENSED_PLATE, SFrameStacks.ADV_DUST_GENERATOR, SFrameStacks.CONDENSED_PLATE,
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_DUST_GENERATOR), PrimeComponents.createCoreModule(SFrameStacks.PRIME_DUST_GENERATOR), PrimeComponents.createPowerCell(SFrameStacks.PRIME_DUST_GENERATOR)
        }).setProduction(32).setProcessingSpeed(4).setEnergyPerTick(1024).register(plugin);

        new FlowerGenerator(Groups.MACHINES, SFrameStacks.FLOWER_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.PYROTIC_ALLOY, SFrameStacks.PYROTIC_ALLOY, SFrameStacks.PYROTIC_ALLOY,
                SFrameStacks.ADRAMAL_ALLOY, SFrameStacks.NEURODES, SFrameStacks.ADRAMAL_ALLOY,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new FlowerGenerator(Groups.MACHINES, SFrameStacks.ADV_FLOWER_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SlimefunItems.REINFORCED_ALLOY_INGOT, SFrameStacks.BOOSTED_TELLURIUM, SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.REINFORCED_PLATE, SFrameStacks.FLOWER_GENERATOR, SlimefunItems.REINFORCED_PLATE,
                SlimefunItems.PROGRAMMABLE_ANDROID_FARMER, SFrameStacks.CONTROL_MODULE, SlimefunItems.PROGRAMMABLE_ANDROID_FARMER
        }).setProduction(8).setEnergyPerTick(256).register(plugin);

        new FlowerGenerator(Groups.MACHINES, SFrameStacks.PRIME_FLOWER_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SFrameStacks.OROKIN_CELL, null,
                getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY), SFrameStacks.ADV_FLOWER_GENERATOR, getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_FLOWER_GENERATOR), PrimeComponents.createCoreModule(SFrameStacks.PRIME_FLOWER_GENERATOR), PrimeComponents.createPowerCell(SFrameStacks.PRIME_FLOWER_GENERATOR)
        }).setProduction(32).setEnergyPerTick(512).register(plugin);

        new GlassGenerator(Groups.MACHINES, SFrameStacks.GLASS_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.AUROXIUM_ALLOY, SlimefunItems.RAINBOW_GLASS, SFrameStacks.AUROXIUM_ALLOY,
                SlimefunItems.GOLD_24K, XMaterial.GLASS.parseItem(), SlimefunItems.GOLD_24K,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new GlassGenerator(Groups.MACHINES, SFrameStacks.ADV_GLASS_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.RUBEDO, SFrameStacks.RUBEDO, SFrameStacks.RUBEDO,
                getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY), SFrameStacks.GLASS_GENERATOR, getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY),
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProduction(2).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new GlassGenerator(Groups.MACHINES, SFrameStacks.PRIME_GLASS_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.OROKIN_CELL, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY),
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.ADV_GLASS_GENERATOR, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_GLASS_GENERATOR), PrimeComponents.createCoreModule(SFrameStacks.PRIME_GLASS_GENERATOR), PrimeComponents.createPowerCell(SFrameStacks.PRIME_GLASS_GENERATOR)
        }).setProduction(6).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

        new Putrifier(SFrameStacks.PUTRIFIER, new ItemStack[]{
                SFrameStacks.PYROTIC_ALLOY, SFrameStacks.BOOSTED_TELLURIUM, SFrameStacks.PYROTIC_ALLOY,
                XMaterial.SOUL_SOIL.parseItem(), XMaterial.SOUL_SAND.parseItem(), XMaterial.SOUL_SOIL.parseItem(),
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new Putrifier(SFrameStacks.ADV_PUTRIFIER, new ItemStack[]{
                SFrameStacks.RUBEDO, SFrameStacks.BOOSTED_TELLURIUM, SFrameStacks.RUBEDO,
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), SFrameStacks.PUTRIFIER, getAlloyPlate(SFrameStacks.VENERDO_ALLOY),
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProduction(5).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new Putrifier(SFrameStacks.PRIME_PUTRIFIER, new ItemStack[]{
                SFrameStacks.BOOSTED_TELLURIUM, SFrameStacks.OROKIN_CELL, SFrameStacks.BOOSTED_TELLURIUM,
                getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY), SFrameStacks.ADV_PUTRIFIER, getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_PUTRIFIER), PrimeComponents.createCoreModule(SFrameStacks.PRIME_PUTRIFIER), PrimeComponents.createPowerCell(SFrameStacks.PRIME_PUTRIFIER)
        }).setProduction(15).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

        new Recycler(Groups.MACHINES, SFrameStacks.RECYCLER, Foundry.RECIPE_TYPE, new ItemStack[]{
                SlimefunItems.REINFORCED_ALLOY_INGOT, XMaterial.PISTON.parseItem(), SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.ELECTRIC_FURNACE_3, SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.HEATING_COIL, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.HEATING_COIL
        }).setEnergyPerTick(512).register(plugin);

        new SulfateProducer(SFrameStacks.SULFATE_PRODUCER, new ItemStack[]{
                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.BOOSTED_URANIUM, SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.REINFORCED_ALLOY_INGOT, XMaterial.PISTON.parseItem(), SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new SulfateProducer(SFrameStacks.ADV_SULFATE_PRODUCER, new ItemStack[]{
                SFrameStacks.THAUMIC_DISTILLATE, SFrameStacks.THAUMIC_DISTILLATE, SFrameStacks.THAUMIC_DISTILLATE,
                SFrameStacks.RUBEDO, SFrameStacks.SULFATE_PRODUCER, SFrameStacks.RUBEDO,
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProduction(2).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new SulfateProducer(SFrameStacks.PRIME_SULFATE_PRODUCER, new ItemStack[]{
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), SFrameStacks.OROKIN_CELL, getAlloyPlate(SFrameStacks.VENERDO_ALLOY),
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), SFrameStacks.ADV_SULFATE_PRODUCER, getAlloyPlate(SFrameStacks.VENERDO_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_SULFATE_PRODUCER), PrimeComponents.createCoreModule(SFrameStacks.PRIME_SULFATE_PRODUCER), PrimeComponents.createPowerCell(SFrameStacks.PRIME_SULFATE_PRODUCER)
        }).setProduction(8).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

        new TelluriumFragmentsSynthesizer(SFrameStacks.TELLURIUM_FRAGMENTS_SYNTHESIZER, new ItemStack[]{
                SFrameStacks.HESPAZYM_ALLOY, SlimefunItems.ENERGIZED_CAPACITOR, SFrameStacks.HESPAZYM_ALLOY,
                SFrameStacks.DILUTED_THERMIA, XMaterial.NETHER_WART_BLOCK.parseItem(), SFrameStacks.DILUTED_THERMIA,
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setEnergyPerTick(512).register(plugin);

        new TerracottaGenerator(SFrameStacks.TERRACOTTA_GENERATOR, new ItemStack[]{
                SFrameStacks.ADRAMAL_ALLOY, SlimefunItems.RAINBOW_GLAZED_TERRACOTTA, SFrameStacks.ADRAMAL_ALLOY,
                SlimefunItems.ELECTRIC_MOTOR, XMaterial.TERRACOTTA.parseItem(), SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new TerracottaGenerator(SFrameStacks.ADV_TERRACOTTA_GEN, new ItemStack[]{
                SlimefunItems.REINFORCED_PLATE, SlimefunItems.REINFORCED_PLATE, SlimefunItems.REINFORCED_PLATE,
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SFrameStacks.TERRACOTTA_GENERATOR, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY),
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new TerracottaGenerator(SFrameStacks.PRIME_TERRACOTTA_GEN, new ItemStack[]{
                SFrameStacks.RUBEDO, SFrameStacks.OROKIN_CELL, SFrameStacks.RUBEDO,
                getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), SFrameStacks.ADV_TERRACOTTA_GEN, getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_TERRACOTTA_GEN), PrimeComponents.createCoreModule(SFrameStacks.PRIME_TERRACOTTA_GEN), PrimeComponents.createPowerCell(SFrameStacks.PRIME_TERRACOTTA_GEN)
        }).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

        new ThermiaExtractor(Groups.MACHINES, SFrameStacks.THERMIA_EXTRACTOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.FLUID_PUMP, SlimefunItems.REINFORCED_ALLOY_INGOT,
                SFrameStacks.CRYOTIC, XMaterial.LODESTONE.parseItem(), SFrameStacks.CRYOTIC,
                SlimefunItems.ELECTRIC_MOTOR, SFrameStacks.CONTROL_MODULE, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(512).register(plugin);

        new TreePeeler(Groups.MACHINES, SFrameStacks.TREE_PEELER, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SlimefunItems.HEATING_COIL, null,
                SlimefunItems.CARBONADO, XMaterial.STONECUTTER.parseItem(), SlimefunItems.CARBONADO,
                SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER
        }).setEnergyPerTick(128).register(plugin);

        new TreePeeler(Groups.MACHINES, SFrameStacks.ADV_TREE_PEELER, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SlimefunItems.HEATING_COIL, null,
                SFrameStacks.PYROTIC_ALLOY, SFrameStacks.TREE_PEELER, SFrameStacks.PYROTIC_ALLOY,
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new TreePeeler(Groups.MACHINES, SFrameStacks.PRIME_TREE_PEELER, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SFrameStacks.OROKIN_CELL, null,
                getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON), SFrameStacks.ADV_TREE_PEELER, getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_TREE_PEELER), PrimeComponents.createCoreModule(SFrameStacks.PRIME_TREE_PEELER), PrimeComponents.createPowerCell(SFrameStacks.PRIME_TREE_PEELER)
        }).setProcessingSpeed(30).setEnergyPerTick(550).register(plugin);

        new WoolGenerator(Groups.MACHINES, SFrameStacks.WOOL_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.FERSTEEL_ALLOY, SlimefunItems.RAINBOW_WOOL, SFrameStacks.FERSTEEL_ALLOY,
                SFrameStacks.DEVOLVED_NAMALON, XMaterial.WHITE_WOOL.parseItem(), SFrameStacks.DEVOLVED_NAMALON,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
        }).setEnergyPerTick(128).register(plugin);

        new WoolGenerator(Groups.MACHINES, SFrameStacks.ADV_WOOL_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.VENERDO_ALLOY, SFrameStacks.ARGON_CRYSTAL, SFrameStacks.VENERDO_ALLOY,
                SFrameStacks.VENERDO_ALLOY, SFrameStacks.WOOL_GENERATOR, SFrameStacks.VENERDO_ALLOY,
                SFrameStacks.CUBIC_DIODES, SFrameStacks.CONTROL_MODULE, SFrameStacks.CUBIC_DIODES
        }).setProcessingSpeed(5).setEnergyPerTick(256).register(plugin);

        new WoolGenerator(Groups.MACHINES, SFrameStacks.PRIME_WOOL_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                null, SFrameStacks.OROKIN_CELL, null,
                getAlloyPlate(SFrameStacks.THAUMIC_DISTILLATE), SFrameStacks.ADV_WOOL_GENERATOR, getAlloyPlate(SFrameStacks.THAUMIC_DISTILLATE),
                PrimeComponents.createControlUnit(SFrameStacks.PRIME_WOOL_GENERATOR), PrimeComponents.createCoreModule(SFrameStacks.PRIME_WOOL_GENERATOR), PrimeComponents.createPowerCell(SFrameStacks.PRIME_WOOL_GENERATOR)
        }).setProcessingSpeed(30).setEnergyPerTick(512).register(plugin);

    }

    private void registerGenerators() {
        new CumulativeGenerator(SFrameStacks.GRAVITECH_ENERCELL, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.AUROXIUM_ALLOY, SlimefunItems.SOLAR_GENERATOR_4, SFrameStacks.AUROXIUM_ALLOY,
                SFrameStacks.THAUMIC_DISTILLATE, SFrameStacks.PRISMATIC_ENERGIZED_CORE, SFrameStacks.THAUMIC_DISTILLATE,
                SFrameStacks.AUROXIUM_ALLOY, SlimefunItems.SOLAR_GENERATOR_4, SFrameStacks.AUROXIUM_ALLOY
        }).setEnergyGenerated(2000).register(plugin);

        new CumulativeGenerator(SFrameStacks.ARCANE_FLUX_DYNAMO, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.PYROTIC_ALLOY), SFrameStacks.GRAVITECH_ENERCELL, getAlloyPlate(SFrameStacks.PYROTIC_ALLOY),
                getAlloyPlate(SFrameStacks.PYROTIC_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.PYROTIC_ALLOY),
                getAlloyPlate(SFrameStacks.PYROTIC_ALLOY), SFrameStacks.GRAVITECH_ENERCELL, getAlloyPlate(SFrameStacks.PYROTIC_ALLOY),
        }).setBonusEnergy(1500).setEnergyGenerated(2000).register(plugin);

        new CumulativeGenerator(SFrameStacks.SPECTRA_REACTOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.ARCANE_FLUX_DYNAMO, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY),
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY),
                getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY), SFrameStacks.ARCANE_FLUX_DYNAMO, getAlloyPlate(SFrameStacks.ADRAMAL_ALLOY)
        }).setBonusEnergy(3000).setEnergyGenerated(4000).register(plugin);

        new CumulativeGenerator(SFrameStacks.PRISMA_POWER_CORE, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SFrameStacks.SPECTRA_REACTOR, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY),
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY),
                getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY), SFrameStacks.SPECTRA_REACTOR, getAlloyPlate(SFrameStacks.TRAVOCYTE_ALLOY)
        }).setBonusEnergy(6000).setEnergyGenerated(8000).register(plugin);

        new CumulativeGenerator(SFrameStacks.VOIDLIGHT_FUSION_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY), SFrameStacks.PRISMA_POWER_CORE, getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY),
                getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY),
                getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY), SFrameStacks.PRISMA_POWER_CORE, getAlloyPlate(SFrameStacks.FERSTEEL_ALLOY)
        }).setBonusEnergy(12000).setEnergyGenerated(16000).register(plugin);

        new CumulativeGenerator(SFrameStacks.AXIOM_ENERGENESIS_ENGINE, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), SFrameStacks.VOIDLIGHT_FUSION_GENERATOR, getAlloyPlate(SFrameStacks.VENERDO_ALLOY),
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.VENERDO_ALLOY),
                getAlloyPlate(SFrameStacks.VENERDO_ALLOY), SFrameStacks.VOIDLIGHT_FUSION_GENERATOR, getAlloyPlate(SFrameStacks.VENERDO_ALLOY)
        }).setBonusEnergy(24000).setEnergyGenerated(32000).register(plugin);

        new CumulativeGenerator(SFrameStacks.CHRONOS_INFINITY_DYNAMO, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON), SFrameStacks.AXIOM_ENERGENESIS_ENGINE, getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON),
                getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON),
                getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON), SFrameStacks.AXIOM_ENERGENESIS_ENGINE, getAlloyPlate(SFrameStacks.DEVOLVED_NAMALON)
        }).setBonusEnergy(48000).setEnergyGenerated(64000).register(plugin);

        new CumulativeGenerator(SFrameStacks.PRIMORDIAL_ETERNACORE_REACTOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), SFrameStacks.CHRONOS_INFINITY_DYNAMO, getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY),
                getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY),
                getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY), SFrameStacks.CHRONOS_INFINITY_DYNAMO, getAlloyPlate(SFrameStacks.AUROXIUM_ALLOY)
        }).setBonusEnergy(96000).setEnergyGenerated(128000).register(plugin);

        new CumulativeGenerator(SFrameStacks.VOIDFORGE_CELESTIUM_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY), SFrameStacks.PRIMORDIAL_ETERNACORE_REACTOR, getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY),
                getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY), SFrameStacks.PRISMATIC_ENERGIZED_CORE, getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY),
                getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY), SFrameStacks.PRIMORDIAL_ETERNACORE_REACTOR, getAlloyPlate(SFrameStacks.HESPAZYM_ALLOY)
        }).setBonusEnergy(192000).setEnergyGenerated(256000).register(plugin);

        new CumulativeGenerator(SFrameStacks.ASTRAL_PRIME_GENERATOR, Foundry.RECIPE_TYPE, new ItemStack[]{
                SFrameStacks.CONDENSED_PLATE, SFrameStacks.VOIDFORGE_CELESTIUM_GENERATOR, SFrameStacks.CONDENSED_PLATE,
                PrimeComponents.createControlUnit(SFrameStacks.ASTRAL_PRIME_GENERATOR), PrimeComponents.createCoreModule(SFrameStacks.ASTRAL_PRIME_GENERATOR), PrimeComponents.createPowerCell(SFrameStacks.ASTRAL_PRIME_GENERATOR),
                SFrameStacks.CONDENSED_PLATE, SFrameStacks.VOIDFORGE_CELESTIUM_GENERATOR, SFrameStacks.CONDENSED_PLATE
        }).setBonusEnergy(1_000_000).setEnergyGenerated(1_000_000).register(plugin);
    }

    private void registerGear() {
        new CryogenicArmorPiece(SFrameStacks.CRYO_HELMET, new ItemStack[]{
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.SCUBA_HELMET, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH
        }, new PotionEffect[]{
                new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 1)}
        ).register(plugin);

        new CryogenicArmorPiece(SFrameStacks.CRYO_CHESTPLATE, new ItemStack[]{
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.HAZMAT_CHESTPLATE, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH
        }, new PotionEffect[]{
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1)}
        ).register(plugin);

        new CryogenicArmorPiece(SFrameStacks.CRYO_LEGGINGS, new ItemStack[]{
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.HAZMAT_LEGGINGS, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH
        }).register(plugin);

        new CryogenicArmorPiece(SFrameStacks.CRYO_BOOTS, new ItemStack[]{
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.HAZMAT_BOOTS, SlimefunItems.REINFORCED_CLOTH,
                SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH, SlimefunItems.REINFORCED_CLOTH
        }).register(plugin);
    }

    private void registerUtilsAndTools() {

        new NosamPick(SFrameStacks.NOSAM_PICK, Foundry.RECIPE_TYPE, new ItemStack[]{
                SlimefunItems.POWER_CRYSTAL, SlimefunItems.LAVA_CRYSTAL, SlimefunItems.POWER_CRYSTAL,
                null, SlimefunItems.REINFORCED_ALLOY_INGOT, null,
                null, SlimefunItems.REINFORCED_ALLOY_INGOT, null
        }, 30, 15, 5).register(plugin);

        new NosamPick(SFrameStacks.FOCUSED_NOSAM_PICK, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SFrameStacks.DILUTED_THERMIA, SFrameStacks.NEURODES, SFrameStacks.DILUTED_THERMIA,
                SlimefunItems.EARTH_RUNE, SFrameStacks.NOSAM_PICK, SlimefunItems.EARTH_RUNE,
                SFrameStacks.DILUTED_THERMIA, SFrameStacks.NEURODES, SFrameStacks.DILUTED_THERMIA
        }, 40, 20, 10).register(plugin);

        new NosamPick(SFrameStacks.PRIME_NOSAM_PICK, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SFrameStacks.NEURAL_SENSORS, SFrameStacks.OROKIN_CELL, SFrameStacks.NEURAL_SENSORS,
                UtilsComponents.createVoidShard(SFrameStacks.PRIME_NOSAM_PICK), SFrameStacks.FOCUSED_NOSAM_PICK, UtilsComponents.createTemporal(SFrameStacks.PRIME_NOSAM_PICK),
                SFrameStacks.NEURAL_SENSORS, UtilsComponents.createNeuralNexus(SFrameStacks.PRIME_NOSAM_PICK), SFrameStacks.NEURAL_SENSORS
        }, 60, 35, 25).register(plugin);

        new MerchantSoulContract(Groups.UTILS_AND_TOOLS, SFrameStacks.MERCHANT_SOUL_CONTRACT, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SFrameStacks.CRYOTIC, SFrameStacks.NEURAL_SENSORS, SFrameStacks.CRYOTIC,
                SlimefunItems.SOULBOUND_RUNE, SlimefunItems.VILLAGER_RUNE, SlimefunItems.SOULBOUND_RUNE,
                SFrameStacks.CRYOTIC, SFrameStacks.NEURAL_SENSORS, SFrameStacks.CRYOTIC
        }).register(plugin);

        new OrokinWand(Groups.UTILS_AND_TOOLS, SFrameStacks.OROKIN_WAND, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                null, null, SFrameStacks.OROKIN_CELL,
                null, XMaterial.STICK.parseItem(), null,
                XMaterial.STICK.parseItem(), null, null
        }).setMaxUseCount(64).register(plugin);

        new OrokinWand(Groups.UTILS_AND_TOOLS, SFrameStacks.PRIME_OROKIN_WAND, RecipeType.ANCIENT_ALTAR, new ItemStack[]{
                SlimefunItems.BLANK_RUNE, SFrameStacks.OROKIN_CELL, SlimefunItems.BLANK_RUNE,
                UtilsComponents.createVoidShard(SFrameStacks.PRIME_OROKIN_WAND), SFrameStacks.OROKIN_WAND, UtilsComponents.createTemporal(SFrameStacks.PRIME_OROKIN_WAND),
                SlimefunItems.BLANK_RUNE, UtilsComponents.createNeuralNexus(SFrameStacks.PRIME_OROKIN_WAND), SlimefunItems.BLANK_RUNE
        }).setMaxUseCount(1024).register(plugin);

        new InputConfigurator(Groups.UTILS_AND_TOOLS, SFrameStacks.INPUT_CONFIGURATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, SlimefunItems.CARGO_MANAGER,
                null, XMaterial.BLAZE_ROD.parseItem(), null,
                SFrameStacks.OROKIN_WAND, null, null
        }).register(plugin);

        new SelectorConfigurator(Groups.UTILS_AND_TOOLS, SFrameStacks.SELECTOR_CONFIGURATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, SFrameStacks.TELLURIUM,
                null, XMaterial.BLAZE_ROD.parseItem(), null,
                SFrameStacks.INPUT_CONFIGURATOR, null, null
        }).register(plugin);

        new ItemProjector(SFrameStacks.ITEM_PROJECTOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, SFrameStacks.NEURAL_SENSORS, null,
                SlimefunItems.POWER_CRYSTAL, new SlimefunItemStack(SFrameStacks.SALVAGE, 8), SlimefunItems.POWER_CRYSTAL,
                SlimefunItems.HOLOGRAM_PROJECTOR, SlimefunItems.HOLOGRAM_PROJECTOR, SlimefunItems.HOLOGRAM_PROJECTOR
        }).register(plugin);

        new EnergyCentral(Groups.UTILS_AND_TOOLS, SFrameStacks.ENERGY_CENTRAL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ENERGY_REGULATOR, SlimefunItems.DAMASCUS_STEEL_INGOT,
                SlimefunItems.DAMASCUS_STEEL_INGOT, XMaterial.BEACON.parseItem(), SlimefunItems.DAMASCUS_STEEL_INGOT,
                SlimefunItems.DAMASCUS_STEEL_INGOT, SFrameStacks.CONTROL_MODULE, SlimefunItems.DAMASCUS_STEEL_INGOT
        }).register(plugin);

    }

    private void registerPrimeComponents() {
        new PrimeComponents(SFrameStacks.PRIME_TREE_PEELER);
        new PrimeComponents(SFrameStacks.PRIME_FLOWER_GENERATOR);
        new PrimeComponents(SFrameStacks.PRIME_WOOL_GENERATOR);
        new PrimeComponents(SFrameStacks.PRIME_ARTIFICIAL_MANGROVE);
        new PrimeComponents(SFrameStacks.PRIME_BASALT_GEN);
        new PrimeComponents(SFrameStacks.PRIME_CRYOTIC_EXTRACTOR);
        new PrimeComponents(SFrameStacks.PRIME_CONCRETE_GENERATOR);
        new PrimeComponents(SFrameStacks.ASTRAL_PRIME_GENERATOR);
        new PrimeComponents(SFrameStacks.PRIME_SULFATE_PRODUCER);
        new PrimeComponents(SFrameStacks.PRIME_DUST_GENERATOR);
        new PrimeComponents(SFrameStacks.PRIME_CHUNK_EATER);
        new PrimeComponents(SFrameStacks.PRIME_GLASS_GENERATOR);
        new PrimeComponents(SFrameStacks.PRIME_PUTRIFIER);
        new PrimeComponents(SFrameStacks.PRIME_TERRACOTTA_GEN);
        PrimeComponents.registerAll(plugin);

        new UtilsComponents(SFrameStacks.PRIME_NOSAM_PICK);
        new UtilsComponents(SFrameStacks.PRIME_OROKIN_WAND);
        UtilsComponents.registerAll(plugin);
    }

    private void registerFreezingItems() {
        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            if (!(item instanceof FreezingItem)) continue;
            SlimeFrame.getFreezingItems().add(item);
        }
    }

    private void registerAlloy(@Nonnull SlimefunItemStack itemStack, ItemStack[] recipe) {
        AlloyIngot alloyIngot = new AlloyIngot(Groups.RESOURCES, itemStack, recipe);
        alloyIngot.register(plugin);
        ALLOYS.add(alloyIngot);
    }

    private void registerAlloyPlate(@Nonnull SlimefunItem item) {
        ItemStack stack = item.getItem();
        if (stack instanceof SlimefunItemStack sfStack) {
            this.registerAlloyPlate(sfStack);
        }
    }

    private void registerAlloyPlate(@Nonnull SlimefunItemStack itemStack) {
        this.registerAlloyPlate(itemStack, new ItemStack[]{
                itemStack, itemStack, itemStack,
                itemStack, SFrameStacks.ARGON_CRYSTAL, itemStack,
                itemStack, itemStack, itemStack
        });
    }

    private void registerAlloyPlate(@Nonnull SlimefunItemStack itemStack, ItemStack[] recipe) {
        SlimefunItemStack alloyPlate = SFrameTheme.sfStackFromTheme(
                itemStack.getItemId() + "_PLATE",
                Material.PAPER,
                SFrameStacks.RESOURCES_THEME,
                itemStack.getDisplayName() + " Plate",
                "A plate made of " + itemStack.getDisplayName()
        );

        SlimefunItem slimefunItem = new SlimefunItem(Groups.RESOURCES, alloyPlate, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        ALLOY_PLATES_MAP.put(itemStack, alloyPlate);
        slimefunItem.register(plugin);
    }

    private SlimefunItemStack getAlloyPlate(SlimefunItemStack itemStack) {
        return ALLOY_PLATES_MAP.get(itemStack);
    }

}
