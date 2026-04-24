package net.guizhanss.ultimategenerators2.implementation.items.machines

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.implementation.items.abstracts.AMachine
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

class BiomassExtractionMachine(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AMachine(itemGroup, item, recipeType, recipe) {
    override val progressBar = ItemStack(Material.SLIME_BALL)

    override fun registerDefaultRecipes() {
        registerRecipe(8, ItemStack(Material.SLIME_BALL, 2))
        registerRecipe(8, ItemStack(Material.NETHER_WART_BLOCK, 2))
        Tag.LEAVES.values.forEach(Consumer { material: Material? ->
            registerRecipe(
                16, ItemStack(
                    material!!, 32
                )
            )
        })
        registerRecipe(16, ItemStack(Material.WHEAT_SEEDS, 16))
        registerRecipe(16, ItemStack(Material.BEETROOT_SEEDS, 16))
        registerRecipe(16, ItemStack(Material.MELON_SEEDS, 16))
        registerRecipe(16, ItemStack(Material.PUMPKIN_SEEDS, 16))
    }

    private fun registerRecipe(ticks: Int, input: ItemStack) {
        registerRecipe(ticks, arrayOf(input, ItemStack(Material.BUCKET)), UGItems.BIOMASS_BUCKET)
    }
}
