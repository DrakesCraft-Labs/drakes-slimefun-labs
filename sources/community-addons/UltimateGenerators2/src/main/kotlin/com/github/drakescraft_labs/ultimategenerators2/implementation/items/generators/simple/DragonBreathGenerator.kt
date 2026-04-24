package net.guizhanss.ultimategenerators2.implementation.items.generators.simple

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import net.guizhanss.ultimategenerators2.implementation.items.abstracts.AGenerator
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import javax.annotation.ParametersAreNonnullByDefault

class DragonBreathGenerator @ParametersAreNonnullByDefault constructor(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AGenerator(itemGroup, item, recipeType, recipe) {
    override val progressBar = ItemStack(Material.END_CRYSTAL)

    override fun registerDefaultFuelTypes() {
        registerFuel(60, ItemStack(Material.DRAGON_BREATH))
    }
}
