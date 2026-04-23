package com.github.drakescraft-labs.infinityexpansion.items.gear;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.infinityexpansion.categories.Groups;
import com.github.drakescraft-labs.infinityexpansion.items.blocks.InfinityWorkbench;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.attributes.Soulbound;

/**
 * tools
 *
 * @author Mooy1
 */
public final class InfinityTool extends SlimefunItem implements Soulbound, NotPlaceable {

    public InfinityTool(SlimefunItemStack stack, ItemStack[] recipe) {
        super(Groups.INFINITY_CHEAT, stack, InfinityWorkbench.TYPE, recipe);
    }

}