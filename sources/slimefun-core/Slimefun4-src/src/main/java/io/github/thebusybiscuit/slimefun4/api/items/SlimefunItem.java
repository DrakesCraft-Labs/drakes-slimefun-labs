package io.github.thebusybiscuit.slimefun4.api.items;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

@Deprecated(forRemoval = false)
public class SlimefunItem extends com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem {

    public SlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public SlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    public SlimefunItem(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item);
    }
}
