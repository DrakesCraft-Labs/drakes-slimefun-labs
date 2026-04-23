package com.github.drakescraft-labs.ecopower.generators;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.electric.generators.SolarGenerator;

/**
 * The {@link LunarGenerator} is a {@link SolarGenerator} which <strong>only works at night!</strong>.
 * 
 * @author TheBusyBiscuit
 *
 */
public class LunarGenerator extends SolarGenerator {

    public LunarGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int energy) {
        super(itemGroup, 0, energy, item, recipeType, recipe);
    }

}
