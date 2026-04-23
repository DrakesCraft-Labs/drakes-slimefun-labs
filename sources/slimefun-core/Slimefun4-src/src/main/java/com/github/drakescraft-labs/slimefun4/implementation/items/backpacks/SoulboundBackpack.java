package com.github.drakescraft-labs.slimefun4.implementation.items.backpacks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.Soulbound;

/**
 * This implementation of {@link SlimefunBackpack} is also {@link Soulbound}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SoulboundBackpack extends SlimefunBackpack implements Soulbound {

    @ParametersAreNonnullByDefault
    public SoulboundBackpack(int size, ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(size, itemGroup, item, recipeType, recipe);
    }

}
