package com.github.drakescraft-labs.slimefun4.implementation.items.electric.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;

import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;

public class Refinery extends AContainer implements RecipeDisplayItem {

    public Refinery(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(40, SlimefunItems.OIL_BUCKET, SlimefunItems.FUEL_BUCKET);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public String getMachineIdentifier() {
        return "REFINERY";
    }

}
