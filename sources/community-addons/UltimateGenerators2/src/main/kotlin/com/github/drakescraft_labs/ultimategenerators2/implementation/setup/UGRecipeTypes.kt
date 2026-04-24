package net.guizhanss.ultimategenerators2.implementation.setup

import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.utils.consts.Keys
import org.bukkit.Material

object UGRecipeTypes {
    @JvmField
    val CANNOT_CRAFT: RecipeType = RecipeType(
        Keys.CANNOT_CRAFT,
        UltimateGenerators2.localization().getItem("_UG2_CANNOT_CRAFT", Material.BARRIER)
    )

    @JvmField
    val DIESEL_REFINERY: RecipeType = RecipeType(
        Keys.DIESEL_REFINERY,
        UltimateGenerators2.localization().getItem("_UG2_DIESEL_REFINERY", UGItems.DIESEL_REFINERY)
    )

    @JvmField
    val BIOMASS_EXTRACTION_MACHINE: RecipeType = RecipeType(
        Keys.BIOMASS_EXTRACTION_MACHINE,
        UltimateGenerators2.localization()
            .getItem("_UG2_BIOMASS_EXTRACTION_MACHINE", UGItems.BIOMASS_EXTRACTION_MACHINE)
    )

    @JvmField
    val BIOFUEL_REFINERY: RecipeType = RecipeType(
        Keys.BIOFUEL_REFINERY,
        UltimateGenerators2.localization().getItem("_UG2_BIOFUEL_REFINERY", UGItems.BIOFUEL_REFINERY)
    )

    @JvmField
    val HEAVY_WATER_REFINING_MACHINE: RecipeType = RecipeType(
        Keys.HEAVY_WATER_REFINING_MACHINE,
        UltimateGenerators2.localization().getItem(
            "_UG2_HEAVY_WATER_REFINING_MACHINE",
            UGItems.HEAVY_WATER_REFINING_MACHINE
        )
    )
}
