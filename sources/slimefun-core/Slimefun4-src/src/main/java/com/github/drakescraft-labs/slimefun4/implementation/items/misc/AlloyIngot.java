package com.github.drakescraft-labs.slimefun4.implementation.items.misc;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.multiblocks.Smeltery;

/**
 * An {@link AlloyIngot} is a blend of different metals and resources.
 * These ingots can be crafted using a {@link Smeltery}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Smeltery
 *
 */
public class AlloyIngot extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public AlloyIngot(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, RecipeType.SMELTERY, recipe);
    }

}
