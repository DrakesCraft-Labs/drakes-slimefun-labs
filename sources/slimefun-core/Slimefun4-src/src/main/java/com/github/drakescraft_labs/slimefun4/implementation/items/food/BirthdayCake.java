package com.github.drakescraft_labs.slimefun4.implementation.items.food;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;

/**
 * The {@link BirthdayCake} is a seasonal item which only appears in october.
 * This is just a nod to me, TheBusyBiscuit, being born on October 26th.
 * 
 * @author TheBusyBiscuit
 * 
 */
public class BirthdayCake extends SlimefunItem implements NotPlaceable {

    @ParametersAreNonnullByDefault
    public BirthdayCake(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}
