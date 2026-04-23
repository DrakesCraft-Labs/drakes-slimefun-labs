package me.mmmjjkx.betterChests.items.chests;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class OnlyOutputChest extends SimpleChest {
    public OnlyOutputChest(int size, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, item, recipeType, recipe);
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }
}
