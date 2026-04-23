package com.github.drakescraft-labs.slimefun4.implementation.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.EnderPearl;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

/**
 * {@link EnderBoots} are a pair of boots which negate damage caused
 * by throwing an {@link EnderPearl}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class EnderBoots extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public EnderBoots(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }
}
