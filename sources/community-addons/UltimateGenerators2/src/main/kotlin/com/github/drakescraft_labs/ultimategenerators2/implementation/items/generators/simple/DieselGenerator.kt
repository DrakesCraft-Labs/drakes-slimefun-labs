package net.guizhanss.ultimategenerators2.implementation.items.generators.simple

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.implementation.items.abstracts.AGenerator
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class DieselGenerator(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AGenerator(itemGroup, item, recipeType, recipe) {
    override val progressBar = ItemStack(Material.FLINT_AND_STEEL)

    override fun registerDefaultFuelTypes() {
        registerFuel(180, UGItems.DIESEL_BUCKET)
    }
}
