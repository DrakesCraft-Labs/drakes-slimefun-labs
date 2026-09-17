package com.github.drakescraft_labs.gcereborn.setup;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.items.chicken.ChickenTypes;
import com.github.drakescraft_labs.gcereborn.items.chicken.ExpandedChickenSpecies;
import com.github.drakescraft_labs.gcereborn.items.chicken.PocketChicken;
import com.github.drakescraft_labs.gcereborn.items.common.ChickenNet;
import com.github.drakescraft_labs.gcereborn.items.common.ResourceEgg;
import com.github.drakescraft_labs.gcereborn.items.machines.BioCloningVat;
import com.github.drakescraft_labs.gcereborn.items.machines.ExcitationChamber;
import com.github.drakescraft_labs.gcereborn.items.machines.GeneticSequencer;
import com.github.drakescraft_labs.gcereborn.items.machines.GrowthChamber;
import com.github.drakescraft_labs.gcereborn.items.machines.IndustrialRoost;
import com.github.drakescraft_labs.gcereborn.items.machines.MutagenicSplicer;
import com.github.drakescraft_labs.gcereborn.items.machines.PrivateCoop;
import com.github.drakescraft_labs.gcereborn.items.machines.RestorationChamber;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Items {

    public static void setup(@Nonnull GeneticChickengineering plugin) {
        // === HERRAMIENTAS Y BASE ===
        new PocketChicken(
            Groups.MAIN,
            GCEItems.POCKET_CHICKEN,
            RecipeTypes.FROM_NET,
            new ItemStack[9]
        ).register(plugin);

        new ChickenNet(
            Groups.MAIN,
            GCEItems.CHICKEN_NET,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                null, new ItemStack(Material.STRING), new ItemStack(Material.STRING),
                null, new ItemStack(Material.STICK), new ItemStack(Material.STRING),
                null, new ItemStack(Material.STICK), null
            }
        ).register(plugin);

        new ResourceEgg(
            Groups.MAIN,
            GCEItems.WATER_EGG,
            RecipeTypes.FROM_CHICKEN,
            Material.WATER,
            GeneticChickengineering.getConfigService().isNetherWaterEnabled()
        ).register(plugin);

        new ResourceEgg(
            Groups.MAIN,
            GCEItems.LAVA_EGG,
            RecipeTypes.FROM_CHICKEN,
            Material.LAVA,
            true
        ).register(plugin);

        // === CONSUMIBLES BIOTECNOLÓGICOS Y PIENSOS ===
        new SlimefunItem(
            Groups.BIOTECH,
            GCEItems.MUTAGENIC_SERUM,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.NETHER_WART), SlimefunItems.URANIUM, new ItemStack(Material.NETHER_WART),
                new ItemStack(Material.SLIME_BALL), new ItemStack(Material.GLASS_BOTTLE), new ItemStack(Material.SLIME_BALL),
                SlimefunItems.MAGIC_LUMP_2, new ItemStack(Material.EXPERIENCE_BOTTLE), SlimefunItems.MAGIC_LUMP_2
            }
        ).register(plugin);

        new SlimefunItem(
            Groups.BIOTECH,
            GCEItems.NUTRIENT_GEL,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.WHEAT), new ItemStack(Material.SUGAR), new ItemStack(Material.WHEAT),
                new ItemStack(Material.SLIME_BALL), new ItemStack(Material.POTION), new ItemStack(Material.SLIME_BALL),
                new ItemStack(Material.BONE_MEAL), new ItemStack(Material.EGG), new ItemStack(Material.BONE_MEAL)
            }
        ).register(plugin);

        new SlimefunItem(
            Groups.BIOTECH,
            GCEItems.RAPID_GROWTH_FEED,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.BONE_MEAL), new ItemStack(Material.WHEAT_SEEDS),
                SlimefunItems.GOLD_DUST, new ItemStack(Material.SUGAR), SlimefunItems.GOLD_DUST,
                new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.BONE_MEAL), new ItemStack(Material.WHEAT_SEEDS)
            }
        ).register(plugin);

        new SlimefunItem(
            Groups.BIOTECH,
            GCEItems.FORTIFIED_VITA_FEED,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.PUMPKIN_SEEDS), new ItemStack(Material.GOLDEN_CARROT), new ItemStack(Material.PUMPKIN_SEEDS),
                SlimefunItems.MAGNESIUM_DUST, new ItemStack(Material.HONEY_BOTTLE), SlimefunItems.MAGNESIUM_DUST,
                new ItemStack(Material.PUMPKIN_SEEDS), new ItemStack(Material.GOLDEN_CARROT), new ItemStack(Material.PUMPKIN_SEEDS)
            }
        ).register(plugin);

        new SlimefunItem(
            Groups.BIOTECH,
            GCEItems.EXCITATION_CATALYST,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.REDSTONE), new ItemStack(Material.GLOWSTONE_DUST),
                SlimefunItems.TIN_DUST, SlimefunItems.GOLD_24K, SlimefunItems.TIN_DUST,
                new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.REDSTONE), new ItemStack(Material.GLOWSTONE_DUST)
            }
        ).register(plugin);

        // === MAQUINARIA BÁSICA ===
        new GeneticSequencer(
            Groups.MAIN,
            GCEItems.GENETIC_SEQUENCER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.OAK_PLANKS), null, new ItemStack(Material.OAK_PLANKS),
                new ItemStack(Material.COBBLESTONE), new ItemStack(Material.OBSERVER), new ItemStack(Material.COBBLESTONE),
                new ItemStack(Material.COBBLESTONE), SlimefunItems.ADVANCED_CIRCUIT_BOARD, new ItemStack(Material.COBBLESTONE)
            }
        ).setCapacity(180).setEnergyConsumption(3).setProcessingSpeed(1).register(plugin);

        new ExcitationChamber(
            Groups.MAIN,
            GCEItems.EXCITATION_CHAMBER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.BLACKSTONE), SlimefunItems.SMALL_CAPACITOR, new ItemStack(Material.BLACKSTONE),
                new ItemStack(Material.CHAIN), null, new ItemStack(Material.CHAIN),
                new ItemStack(Material.STONE), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.STONE)
            }
        ).setCapacity(250).setEnergyConsumption(5).setProcessingSpeed(1).register(plugin);

        new ExcitationChamber(
            Groups.MAIN,
            GCEItems.EXCITATION_CHAMBER_2,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                SlimefunItems.LEAD_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.LEAD_INGOT,
                SlimefunItems.BLISTERING_INGOT_3, GCEItems.EXCITATION_CHAMBER, SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.LEAD_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.LEAD_INGOT
            }
        ).setCapacity(1000).setEnergyConsumption(10).setProcessingSpeed(2).register(plugin);

        new ExcitationChamber(
            Groups.MAIN,
            GCEItems.EXCITATION_CHAMBER_3,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                SlimefunItems.MAGIC_LUMP_3, SlimefunItems.NUCLEAR_REACTOR, SlimefunItems.MAGIC_LUMP_3,
                SlimefunItems.REINFORCED_PLATE, GCEItems.EXCITATION_CHAMBER_2, SlimefunItems.REINFORCED_PLATE,
                SlimefunItems.MAGIC_LUMP_3, SlimefunItems.URANIUM, SlimefunItems.MAGIC_LUMP_3
            }
        ).setCapacity(5000).setEnergyConsumption(50).setProcessingSpeed(10).register(plugin);

        // === NUEVA MAQUINARIA AVANZADA ===
        new ExcitationChamber(
            Groups.BIOTECH,
            GCEItems.EXCITATION_CHAMBER_4,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                SlimefunItems.BOOSTED_URANIUM, SlimefunItems.REINFORCED_PLATE, SlimefunItems.BOOSTED_URANIUM,
                new ItemStack(Material.NETHERITE_INGOT), GCEItems.EXCITATION_CHAMBER_3, new ItemStack(Material.NETHERITE_INGOT),
                SlimefunItems.BOOSTED_URANIUM, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BOOSTED_URANIUM
            }
        ).setCapacity(15000).setEnergyConsumption(125).setProcessingSpeed(25).register(plugin);

        new BioCloningVat(
            Groups.BIOTECH,
            GCEItems.BIO_CLONING_VAT,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.GLASS), SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.GLASS),
                SlimefunItems.HEATING_COIL, new ItemStack(Material.BREWING_STAND), SlimefunItems.HEATING_COIL,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BIG_CAPACITOR, SlimefunItems.ELECTRIC_MOTOR
            }
        ).setCapacity(8000).setEnergyConsumption(60).setProcessingSpeed(1).register(plugin);

        new MutagenicSplicer(
            Groups.BIOTECH,
            GCEItems.MUTAGENIC_SPLICER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.DAMASCUS_STEEL_INGOT,
                SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.ENCHANTING_TABLE), SlimefunItems.REINFORCED_PLATE,
                SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.BIG_CAPACITOR, SlimefunItems.HARDENED_METAL_INGOT
            }
        ).setCapacity(10000).setEnergyConsumption(75).setProcessingSpeed(1).register(plugin);

        new IndustrialRoost(
            Groups.BIOTECH,
            GCEItems.INDUSTRIAL_ROOST,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.IRON_TRAPDOOR), new ItemStack(Material.OAK_PLANKS),
                SlimefunItems.HEATING_COIL, new ItemStack(Material.BARREL), SlimefunItems.HEATING_COIL,
                new ItemStack(Material.HOPPER), SlimefunItems.SMALL_CAPACITOR, new ItemStack(Material.HOPPER)
            }
        ).setCapacity(3000).setEnergyConsumption(15).setProcessingSpeed(1).register(plugin);

        new PrivateCoop(
            Groups.MAIN,
            GCEItems.PRIVATE_COOP,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.BIRCH_PLANKS), new ItemStack(Material.BIRCH_PLANKS), new ItemStack(Material.BIRCH_PLANKS),
                new ItemStack(Material.JUKEBOX), new ItemStack(Material.RED_BED), new ItemStack(Material.POPPY),
                new ItemStack(Material.BIRCH_PLANKS), SlimefunItems.HEATING_COIL, new ItemStack(Material.BIRCH_PLANKS)
            }
        ).setCapacity(30).setEnergyConsumption(1).setProcessingSpeed(1).register(plugin);

        if (GeneticChickengineering.getConfigService().isPainEnabled()) {
            new RestorationChamber(
                Groups.MAIN,
                GCEItems.RESTORATION_CHAMBER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {
                    new ItemStack(Material.PINK_TERRACOTTA), new ItemStack(Material.PINK_TERRACOTTA), new ItemStack(Material.PINK_TERRACOTTA),
                    SlimefunItems.BANDAGE, new ItemStack(Material.WHITE_BED), SlimefunItems.MEDICINE,
                    new ItemStack(Material.PINK_TERRACOTTA), SlimefunItems.HEATING_COIL, new ItemStack(Material.PINK_TERRACOTTA)
                }
            ).setCapacity(30).setEnergyConsumption(2).setProcessingSpeed(1).register(plugin);
        }

        if (GeneticChickengineering.getConfigService().isGrowthChamberEnabled()) {
            new GrowthChamber(
                Groups.MAIN,
                GCEItems.GROWTH_CHAMBER,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {
                    SlimefunItems.GOLD_24K, SlimefunItems.TIN_CAN, SlimefunItems.GOLD_24K,
                    SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.HAY_BLOCK), SlimefunItems.ELECTRIC_MOTOR,
                    SlimefunItems.LEAD_INGOT, SlimefunItems.FOOD_FABRICATOR, SlimefunItems.LEAD_INGOT
                }
            ).setCapacity(200).setEnergyConsumption(20).setProcessingSpeed(1).register(plugin);
        }

        // Registrar las 64 gallinas clásicas
        ChickenTypes.registerChickens();

        // Registrar las 21 nuevas especies expandidas en el directorio de la guía
        for (ExpandedChickenSpecies species : ExpandedChickenSpecies.values()) {
            ChickenUtils.createExpandedProductDisplay(species);
        }
    }
}
