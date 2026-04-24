package com.github.drakescraft_labs.bump.implementation.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.bump.core.attributes.AppraisableItem;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

/**
 * A {@link RandomEquipment} is an item that is an {@link AppraisableItem}.
 *
 * @author ybw0014
 */
public final class RandomEquipment extends SlimefunItem implements AppraisableItem {

    @ParametersAreNonnullByDefault
    public RandomEquipment(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }
}
