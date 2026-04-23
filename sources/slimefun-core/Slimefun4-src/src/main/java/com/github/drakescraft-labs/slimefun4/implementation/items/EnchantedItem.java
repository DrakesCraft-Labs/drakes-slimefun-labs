package com.github.drakescraft-labs.slimefun4.implementation.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;

/**
 * The {@link EnchantedItem} is an enchanted {@link SlimefunItem}.
 * By default, this class sets items to be not disenchantable.
 * 
 * @author Fury_Phoenix
 *
 */
public class EnchantedItem extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public EnchantedItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        disenchantable = false;
    }

}
