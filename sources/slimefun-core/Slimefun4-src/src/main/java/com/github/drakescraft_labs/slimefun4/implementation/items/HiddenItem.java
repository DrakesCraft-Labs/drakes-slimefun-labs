package com.github.drakescraft_labs.slimefun4.implementation.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotConfigurable;

/**
 * The {@link HiddenItem} is a {@link NotConfigurable} {@link SlimefunItem}
 * that is hidden from the Slimefun guide.
 * 
 * @author char321
 * 
 */
public class HiddenItem extends SlimefunItem implements NotConfigurable {

    @ParametersAreNonnullByDefault
    public HiddenItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.setHidden(true);
    }
}
