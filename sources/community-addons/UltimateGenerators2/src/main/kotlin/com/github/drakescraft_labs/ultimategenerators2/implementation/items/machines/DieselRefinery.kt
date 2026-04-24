package net.guizhanss.ultimategenerators2.implementation.items.machines

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.implementation.items.abstracts.AMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class DieselRefinery(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AMachine(itemGroup, item, recipeType, recipe) {
    override val progressBar = ItemStack(Material.FLINT_AND_STEEL)

    override fun registerDefaultRecipes() {
        registerRecipe(80, SlimefunItems.OIL_BUCKET, UGItems.DIESEL_BUCKET)
    }
}
