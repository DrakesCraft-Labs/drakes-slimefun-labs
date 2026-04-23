package com.github.drakescraft_labs.slimefun4.implementation.items.teleporter;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.gps.GPSNetwork;
import com.github.drakescraft_labs.slimefun4.api.gps.TeleportationManager;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

/**
 * The Teleporter is a {@link SlimefunItem} that can be placed down and allows a {@link Player}
 * to display to any of his waypoints he set via his {@link GPSNetwork}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see GPSNetwork
 * @see TeleportationManager
 *
 */
public class Teleporter extends SimpleSlimefunItem<BlockPlaceHandler> {

    @ParametersAreNonnullByDefault
    public Teleporter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "owner", e.getPlayer().getUniqueId().toString());
            }
        };
    }

}
