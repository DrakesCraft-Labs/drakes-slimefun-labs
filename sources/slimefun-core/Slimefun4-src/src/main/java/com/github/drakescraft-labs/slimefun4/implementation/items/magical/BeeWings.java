package com.github.drakescraft-labs.slimefun4.implementation.items.magical;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.listeners.BeeWingsListener;
import com.github.drakescraft-labs.slimefun4.implementation.tasks.player.BeeWingsTask;

/**
 * The {@link BeeWings} are a special form of the elytra which gives you a slow falling effect
 * when you approach the ground.
 *
 * @author beSnow
 * @author TheBusyBiscuit
 * 
 * @see BeeWingsListener
 * @see BeeWingsTask
 * 
 */
public class BeeWings extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public BeeWings(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

}
