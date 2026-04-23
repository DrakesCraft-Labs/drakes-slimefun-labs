package com.github.drakescraft-labs.slimefun4.implementation.items.misc;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.listeners.VillagerTradingListener;

/**
 * The {@link SyntheticEmerald} is an almost normal emerald.
 * It can even be used to trade with {@link Villager Villagers}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see VillagerTradingListener
 *
 */
public class SyntheticEmerald extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public SyntheticEmerald(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        setUseableInWorkbench(true);
    }

}
