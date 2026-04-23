package com.github.drakescraft-labs.slimefun4.implementation.items.misc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import com.github.drakescraft-labs.slimefun4.implementation.items.cargo.ReactorAccessPort;
import com.github.drakescraft-labs.slimefun4.implementation.items.electric.reactors.NetherStarReactor;
import com.github.drakescraft-labs.slimefun4.implementation.items.electric.reactors.NuclearReactor;
import com.github.drakescraft-labs.slimefun4.implementation.items.electric.reactors.Reactor;

/**
 * A {@link CoolantCell} is an {@link ItemStack} that is used to cool a {@link Reactor}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Reactor
 * @see ReactorAccessPort
 * @see NuclearReactor
 * @see NetherStarReactor
 *
 */
public class CoolantCell extends UnplaceableBlock {

    @ParametersAreNonnullByDefault
    public CoolantCell(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        this(itemGroup, item, recipeType, recipe, null);
    }

    @ParametersAreNonnullByDefault
    public CoolantCell(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

}
