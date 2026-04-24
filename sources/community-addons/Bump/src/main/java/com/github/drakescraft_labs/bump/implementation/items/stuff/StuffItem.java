package com.github.drakescraft_labs.bump.implementation.items.stuff;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.bump.implementation.groups.BumpItemGroups;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;

/**
 * {@link UnplaceableBlock} items in stuff category.
 *
 * @author ybw0014
 */
public class StuffItem extends UnplaceableBlock {
    public StuffItem(SlimefunItemStack itemStack, RecipeType recipeType, ItemStack[] recipe) {
        super(BumpItemGroups.STUFF, itemStack, recipeType, recipe);
    }
}
