package net.guizhanss.ultimategenerators2.implementation.items.capacitors

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import org.bukkit.inventory.ItemStack

open class HiddenCapacitor : UGCapacitor {
    constructor(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<ItemStack?>,
        capacity: Int
    ) : super(
        itemGroup, item, recipeType, recipe, capacity
    )

    constructor(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<ItemStack?>,
        capacity: Int,
        craftAmount: Int
    ) : super(
        itemGroup, item, recipeType, recipe, capacity, craftAmount
    )

    override fun postRegister() {
        isHidden = true
    }
}
