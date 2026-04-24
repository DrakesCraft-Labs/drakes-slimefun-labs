package com.github.drakescraft_labs.galactifun.base.items;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.base.BaseItems;
import com.github.drakescraft_labs.galactifun.util.Util;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

public final class LaunchPadFloor extends SlimefunItem {

    public LaunchPadFloor(ItemGroup category, SlimefunItemStack itemStack, RecipeType type, ItemStack[] recipe) {
        super(category, itemStack, type, recipe);

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                for (BlockFace face : Util.SURROUNDING_FACES) {
                    Block b = e.getBlock().getRelative(face);
                    if (BlockStorage.check(b, BaseItems.LAUNCH_PAD_CORE.getItemId()) && !LaunchPadCore.canBreak(e.getPlayer(), b)) {
                        e.setCancelled(true);
                    }
                }
            }
        });
    }

}
