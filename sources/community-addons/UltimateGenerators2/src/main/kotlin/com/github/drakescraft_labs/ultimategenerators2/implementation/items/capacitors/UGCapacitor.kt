package net.guizhanss.ultimategenerators2.implementation.items.capacitors

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.Capacitor
import dev.drake.dough.items.CustomItemStack
import org.bukkit.inventory.ItemStack

open class UGCapacitor : Capacitor {
    constructor(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<ItemStack?>,
        capacity: Int
    ) : super(itemGroup, capacity, item, recipeType, recipe)

    constructor(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<ItemStack?>,
        capacity: Int,
        craftAmount: Int
    ) : super(itemGroup, capacity, item, recipeType, recipe) {
        setRecipeOutput(CustomItemStack(item, craftAmount))
    }
}
