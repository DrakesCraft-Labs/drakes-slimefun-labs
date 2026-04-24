package net.guizhanss.ultimategenerators2.implementation.setup

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.generators.SolarGenerator
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.implementation.items.capacitors.HiddenCapacitor
import net.guizhanss.ultimategenerators2.implementation.items.capacitors.UGCapacitor
import net.guizhanss.ultimategenerators2.implementation.items.generators.simple.BioFuelGenerator
import net.guizhanss.ultimategenerators2.implementation.items.generators.simple.DieselGenerator
import net.guizhanss.ultimategenerators2.implementation.items.generators.simple.DragonBreathGenerator
import net.guizhanss.ultimategenerators2.implementation.items.generators.simple.EndlessGenerator
import net.guizhanss.ultimategenerators2.implementation.items.generators.simple.ReactionGenerator
import net.guizhanss.ultimategenerators2.implementation.items.machines.BioFuelRefinery
import net.guizhanss.ultimategenerators2.implementation.items.machines.BiomassExtractionMachine
import net.guizhanss.ultimategenerators2.implementation.items.machines.DieselRefinery
import net.guizhanss.ultimategenerators2.implementation.items.machines.HeavyWaterRefiningMachine
import net.guizhanss.ultimategenerators2.implementation.items.misc.FakePotionItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object UGItemSetup {
    fun setup(plugin: UltimateGenerators2) {
        // <editor-fold defaultstate="collapsed" desc="Misc">
        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.SOLID_STORAGE_EXPANSION,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null, SlimefunItems.CARGO_OUTPUT_NODE, null,
                SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ANDROID_INTERFACE_ITEMS, SlimefunItems.ALUMINUM_INGOT,
                SlimefunItems.ALUMINUM_INGOT, ItemStack(Material.GLASS), SlimefunItems.ALUMINUM_INGOT
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.LIQUID_STORAGE_EXPANSION,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null, SlimefunItems.CARGO_OUTPUT_NODE, null,
                SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ANDROID_INTERFACE_FUEL, SlimefunItems.ALUMINUM_INGOT,
                SlimefunItems.ALUMINUM_INGOT, ItemStack(Material.GLASS), SlimefunItems.ALUMINUM_INGOT
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.DIESEL_BUCKET,
            UGRecipeTypes.DIESEL_REFINERY,
            centerRecipe(SlimefunItems.OIL_BUCKET)
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.BIOMASS_BUCKET,
            UGRecipeTypes.BIOMASS_EXTRACTION_MACHINE,
            arrayOfNulls(9)
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.BIOFUEL_BUCKET,
            UGRecipeTypes.BIOFUEL_REFINERY,
            centerRecipe(UGItems.BIOMASS_BUCKET)
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.ENDER_LUMP_4,
            RecipeType.ANCIENT_ALTAR,
            arrayOf(
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3,
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3,
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3, SlimefunItems.ENDER_LUMP_3
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.RUNE_COMPLEX_ENDER,
            RecipeType.ANCIENT_ALTAR,
            arrayOf(
                SlimefunItems.ENDER_RUNE, SlimefunItems.GOLD_24K, SlimefunItems.ENDER_RUNE,
                UGItems.ENDER_LUMP_4, SlimefunItems.STONE_CHUNK, UGItems.ENDER_LUMP_4,
                SlimefunItems.ENDER_RUNE, SlimefunItems.GOLD_24K, SlimefunItems.ENDER_RUNE
            )
        ).register(plugin)

        FakePotionItem(
            UGItemGroups.MISC,
            UGItems.ENDER_CRYSTAL_ENHANCER,
            RecipeType.ANCIENT_ALTAR,
            arrayOf(
                ItemStack(Material.GLASS), ItemStack(Material.GLASS), ItemStack(Material.GLASS),
                ItemStack(Material.GLASS), UGItems.ENDER_LUMP_4, ItemStack(Material.GLASS),
                ItemStack(Material.GLASS), ItemStack(Material.GLASS), ItemStack(Material.GLASS)
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.HEAVY_WATER_BUCKET,
            UGRecipeTypes.HEAVY_WATER_REFINING_MACHINE,
            centerRecipe(ItemStack(Material.WATER_BUCKET))
        ).register(plugin)

        FakePotionItem(
            UGItemGroups.MISC,
            UGItems.NEUTRON_MODERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                ItemStack(Material.GLASS), UGItems.HEAVY_WATER_BUCKET, ItemStack(Material.GLASS),
                UGItems.HEAVY_WATER_BUCKET, SlimefunItems.CARBON_CHUNK, UGItems.HEAVY_WATER_BUCKET,
                ItemStack(Material.GLASS), UGItems.HEAVY_WATER_BUCKET, ItemStack(Material.GLASS)
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.MISC,
            UGItems.THERMAL_NEUTRON_REACTOR_COOLANT_CELL,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null, null, UGItems.HEAVY_WATER_BUCKET,
                null, SlimefunItems.REACTOR_COOLANT_CELL, null,
                UGItems.HEAVY_WATER_BUCKET, null, null
            )
        ).register(plugin)
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Machines">
        DieselRefinery(
            UGItemGroups.MACHINES,
            UGItems.DIESEL_REFINERY,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.HEATING_COIL, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HEATING_COIL,
                SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS,
                SlimefunItems.HARDENED_GLASS, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HARDENED_GLASS
            )
        ).setCapacity(160)
            .setEnergyConsumption(16)
            .register(plugin)

        BiomassExtractionMachine(
            UGItemGroups.MACHINES,
            UGItems.BIOMASS_EXTRACTION_MACHINE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.ALUMINUM_INGOT, ItemStack(Material.PISTON), SlimefunItems.ALUMINUM_INGOT,
                SlimefunItems.HARDENED_GLASS, SlimefunItems.HEATING_COIL, SlimefunItems.HARDENED_GLASS,
                SlimefunItems.HEATING_COIL, ItemStack(Material.HOPPER), SlimefunItems.HEATING_COIL
            )
        ).setCapacity(30)
            .setEnergyConsumption(3)
            .register(plugin)

        BioFuelRefinery(
            UGItemGroups.MACHINES,
            UGItems.BIOFUEL_REFINERY,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.HEATING_COIL, SlimefunItems.PLASTIC_SHEET, SlimefunItems.HEATING_COIL,
                SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS,
                SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL
            )
        ).setCapacity(180)
            .setEnergyConsumption(18)
            .register(plugin)

        HeavyWaterRefiningMachine(
            UGItemGroups.MACHINES,
            UGItems.HEAVY_WATER_REFINING_MACHINE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.HARDENED_GLASS, null, SlimefunItems.HARDENED_GLASS,
                SlimefunItems.HEATING_COIL, SlimefunItems.ELECTRIC_DUST_WASHER_2, SlimefunItems.HEATING_COIL,
                SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.BASIC_CIRCUIT_BOARD
            )
        ).setCapacity(90)
            .setEnergyConsumption(9)
            .register(plugin)

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Electricity Storage">
        SlimefunItem(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.ELECTRICITY_STORAGE_UNIT,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS,
                SlimefunItems.ALUMINUM_INGOT, SlimefunItems.BATTERY, SlimefunItems.ALUMINUM_INGOT,
                SlimefunItems.HARDENED_GLASS, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.HARDENED_GLASS
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.ADVANCED_BATTERY,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.BATTERY, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.BATTERY,
                SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT,
                SlimefunItems.BATTERY, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.BATTERY
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.ALPHA_BATTERY,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.ADVANCED_BATTERY, SlimefunItems.ADVANCED_CIRCUIT_BOARD, UGItems.ADVANCED_BATTERY,
                SlimefunItems.COPPER_INGOT, UGItems.ELECTRICITY_STORAGE_UNIT, SlimefunItems.COPPER_INGOT,
                UGItems.ADVANCED_BATTERY, SlimefunItems.ADVANCED_CIRCUIT_BOARD, UGItems.ADVANCED_BATTERY
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.BETA_BATTERY,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.ALPHA_BATTERY, SlimefunItems.ADVANCED_CIRCUIT_BOARD, UGItems.ALPHA_BATTERY,
                SlimefunItems.COPPER_INGOT, UGItems.ELECTRICITY_STORAGE_UNIT, SlimefunItems.COPPER_INGOT,
                UGItems.ALPHA_BATTERY, SlimefunItems.ADVANCED_CIRCUIT_BOARD, UGItems.ALPHA_BATTERY
            )
        ).register(plugin)

        SlimefunItem(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.GAMMA_BATTERY,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.BETA_BATTERY, SlimefunItems.ADVANCED_CIRCUIT_BOARD, UGItems.BETA_BATTERY,
                SlimefunItems.COPPER_INGOT, UGItems.ELECTRICITY_STORAGE_UNIT, SlimefunItems.COPPER_INGOT,
                UGItems.BETA_BATTERY, SlimefunItems.ADVANCED_CIRCUIT_BOARD, UGItems.BETA_BATTERY
            )
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.GLASS_ELECTRICITY_TRANSMITTER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null, SlimefunItems.ALUMINUM_INGOT, null,
                SlimefunItems.ALUMINUM_INGOT, UGItems.ELECTRICITY_STORAGE_UNIT, SlimefunItems.ALUMINUM_INGOT,
                null, SlimefunItems.ALUMINUM_INGOT, null
            ),
            16,
            UltimateGenerators2.configService().glassElectricityTransmitterCraftAmount
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.BASIC_ELECTRICITY_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BATTERY,
                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.BATTERY
            ),
            640
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.ADVANCED_ELECTRICITY_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.ADVANCED_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, UGItems.BASIC_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, UGItems.ADVANCED_BATTERY
            ),
            2560
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.ALPHA_ELECTRICITY_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.ALPHA_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, UGItems.ADVANCED_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, UGItems.ALPHA_BATTERY
            ),
            5120
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.BETA_ELECTRICITY_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.BETA_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, UGItems.ALPHA_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, UGItems.BETA_BATTERY
            ),
            40960
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.GAMMA_ELECTRICITY_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.GAMMA_BATTERY, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, UGItems.BETA_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.REDSTONE_ALLOY, SlimefunItems.REDSTONE_ALLOY, UGItems.GAMMA_BATTERY
            ),
            327680
        ).register(plugin)

        UGCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.LAMBDA_ELECTRICITY_STORAGE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE,
                UGItems.GAMMA_ELECTRICITY_STORAGE, SlimefunItems.REDSTONE_ALLOY, UGItems.GAMMA_ELECTRICITY_STORAGE,
                UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE, UGItems.GAMMA_ELECTRICITY_STORAGE
            ),
            2621440
        ).register(plugin)

        HiddenCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.KAPA_ELECTRICITY_STORAGE,
            UGRecipeTypes.CANNOT_CRAFT,
            arrayOfNulls(9),
            20971520
        ).register(plugin)

        HiddenCapacitor(
            UGItemGroups.ELECTRICITY_STORAGE,
            UGItems.PHI_ELECTRICITY_STORAGE,
            UGRecipeTypes.CANNOT_CRAFT,
            arrayOfNulls(9),
            167772160
        ).register(plugin)

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Simple Generators">
        EndlessGenerator(
            UGItemGroups.SIMPLE_GENERATORS,
            UGItems.ENDLESS_GENERATOR,
            UGRecipeTypes.CANNOT_CRAFT,
            arrayOfNulls(9)
        ).register(plugin)

        DieselGenerator(
            UGItemGroups.SIMPLE_GENERATORS,
            UGItems.DIESEL_GENERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null, SlimefunItems.DURALUMIN_INGOT, null,
                SlimefunItems.DURALUMIN_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DURALUMIN_INGOT,
                SlimefunItems.HEATING_COIL, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.HEATING_COIL
            )
        ).setCapacity(256)
            .setEnergyProduction(18)
            .register(plugin)

        BioFuelGenerator(
            UGItemGroups.SIMPLE_GENERATORS,
            UGItems.BIOFUEL_GENERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.HEATING_COIL, SlimefunItems.BILLON_INGOT, SlimefunItems.HEATING_COIL,
                SlimefunItems.BILLON_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BILLON_INGOT,
                SlimefunItems.HEATING_COIL, SlimefunItems.BILLON_INGOT, SlimefunItems.HEATING_COIL
            )
        ).setCapacity(128)
            .setEnergyProduction(18)
            .register(plugin)

        DragonBreathGenerator(
            UGItemGroups.SIMPLE_GENERATORS,
            UGItems.DRAGON_BREATH_GENERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.HEATING_COIL, SlimefunItems.HARDENED_GLASS, SlimefunItems.HEATING_COIL,
                SlimefunItems.PLASTIC_SHEET, SlimefunItems.ENDER_RUNE, SlimefunItems.PLASTIC_SHEET,
                SlimefunItems.ENDER_LUMP_3, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.ADVANCED_CIRCUIT_BOARD
            )
        ).setCapacity(256)
            .setEnergyProduction(9)
            .register(plugin)

        ReactionGenerator(
            UGItemGroups.SIMPLE_GENERATORS,
            UGItems.REACTION_GENERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.LEAD_INGOT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.LEAD_INGOT,
                SlimefunItems.HEATING_COIL, SlimefunItems.HARDENED_GLASS, SlimefunItems.HEATING_COIL,
                SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.BASIC_CIRCUIT_BOARD
            )
        ).setCapacity(256)
            .setEnergyProduction(8)
            .register(plugin)

        SolarGenerator(
            UGItemGroups.SIMPLE_GENERATORS,
            1024, 512,
            UGItems.QUANTUM_SOLAR_GENERATOR,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4,
                SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.SOLAR_GENERATOR_4,
                SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4, SlimefunItems.SOLAR_GENERATOR_4
            ),
            65536
        ).register(plugin)

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Modular Generators">
//        new EnderCrystalStabilizer(
//            UGItemGroups.MODULAR_GENERATORS,
//            UGItems.ENDER_CRYSTAL_GENERATOR_STABILIZER,
//            RecipeType.ENHANCED_CRAFTING_TABLE,
//            new ItemStack[] {
//
//            }
//        ).setCapacity(90)
//            .setEnergyConsumption(9)
//            .register(plugin);
        // </editor-fold>
    }

    private fun centerRecipe(item: ItemStack) = arrayOf(
        null, null, null,
        null, item, null,
        null, null, null
    )
}
