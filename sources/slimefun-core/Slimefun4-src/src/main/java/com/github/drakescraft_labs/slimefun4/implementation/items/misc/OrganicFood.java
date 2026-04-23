package com.github.drakescraft_labs.slimefun4.implementation.items.misc;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.machines.FoodFabricator;
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.machines.accelerators.AnimalGrowthAccelerator;

/**
 * {@link OrganicFood} is created using a {@link FoodFabricator} and can
 * be used to fuel an {@link AnimalGrowthAccelerator}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see AnimalGrowthAccelerator
 *
 */
public class OrganicFood extends SlimefunItem {

    public static final int OUTPUT = 2;

    @ParametersAreNonnullByDefault
    public OrganicFood(ItemGroup itemGroup, SlimefunItemStack item, Material ingredient) {
        super(itemGroup, item, RecipeType.FOOD_FABRICATOR, new ItemStack[] { SlimefunItems.TIN_CAN, new ItemStack(ingredient), null, null, null, null, null, null, null }, new SlimefunItemStack(item, OUTPUT));
    }
}
