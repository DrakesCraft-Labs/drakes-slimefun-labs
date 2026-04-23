package com.github.drakescraft_labs.slimefun4.implementation.items.gps;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.gps.Waypoint;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * The {@link GPSMarkerTool} allows you to create a {@link Waypoint} at your current
 * {@link Location}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class GPSMarkerTool extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    @ParametersAreNonnullByDefault
    public GPSMarkerTool(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            if (e.getClickedBlock().isPresent()) {
                Block b = e.getClickedBlock().get().getRelative(e.getClickedFace());
                Location l = b.getLocation();
                l.setYaw(e.getPlayer().getLocation().getYaw());
                Slimefun.getGPSNetwork().createWaypoint(e.getPlayer(), l);
            }
        };
    }
}
