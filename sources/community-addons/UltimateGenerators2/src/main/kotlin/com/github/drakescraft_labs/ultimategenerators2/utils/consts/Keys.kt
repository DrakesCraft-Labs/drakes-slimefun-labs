package net.guizhanss.ultimategenerators2.utils.consts

import net.guizhanss.ultimategenerators2.UltimateGenerators2
import org.bukkit.NamespacedKey

object Keys {
    val ITEMGROUP_MAIN = "main".createKey()
    val ITEMGROUP_MISC = "misc".createKey()
    val ITEMGROUP_MACHINES = "machines".createKey()
    val ITEMGROUP_ELECTRICITY_STORAGE = "electricity_storage".createKey()
    val ITEMGROUP_SIMPLE_GENERATORS = "simple_generators".createKey()
    val ITEMGROUP_MODULAR_GENERATORS = "modular_generators".createKey()
    val CANNOT_CRAFT = "cannot_craft".createKey()
    val DIESEL_REFINERY = "diesel_refinery".createKey()
    val BIOMASS_EXTRACTION_MACHINE = "biomass_extraction_machine".createKey()
    val BIOFUEL_REFINERY = "biofuel_refinery".createKey()
    val HEAVY_WATER_REFINING_MACHINE = "heavy_water_refining_machine".createKey()

    const val ENHANCED_ENDER_CRYSTAL = "UG2_ENHANCED_ENDER_CRYSTAL"

    private fun String.createKey() = NamespacedKey(UltimateGenerators2.getInstance(), this)
}

