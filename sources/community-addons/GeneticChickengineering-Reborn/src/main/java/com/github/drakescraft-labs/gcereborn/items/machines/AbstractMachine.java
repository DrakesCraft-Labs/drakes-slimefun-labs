package com.github.drakescraft-labs.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;

public abstract class AbstractMachine extends AContainer {

    protected static final int INFO_SLOT = 22;

    private final String identifier;

    @ParametersAreNonnullByDefault
    protected AbstractMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        identifier = item.getItemId();
    }

    @Override
    @Nonnull
    public String getMachineIdentifier() {
        return identifier;
    }
}
