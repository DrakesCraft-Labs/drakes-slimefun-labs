package com.github.drakescraft_labs.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

/**
 * The {@link FarmerShoes} are just a simple pair of boots which allows a {@link Player} to walk
 * on {@link Farmland} without breaking it.
 * 
 * @author TheBusyBiscuit
 *
 */
public class FarmerShoes extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public FarmerShoes(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}
