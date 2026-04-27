package io.github.addoncommunity.galactifun.api.blocks

import io.github.addoncommunity.galactifun.units.Mass
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

open class BasicMassedBlock(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
    private val mass: Mass
) : SlimefunItem(itemGroup, item, recipeType, recipe), CustomMass {
    override fun getMass(block: Block): Mass = mass
}