package com.github.drakescraft_labs.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

import com.github.drakescraft_labs.slimefun4.core.attributes.NotHopperable;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

/**
 * This {@link Listener} prevents item from being transferred to
 * and from {@link AContainer} using a hopper.
 *
 * @author CURVX
 *
 * @see NotHopperable
 *
 */
public class HopperListener implements Listener {

    public HopperListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHopperInsert(InventoryMoveItemEvent e) {
        Location loc = e.getDestination().getLocation();

        if (loc != null && e.getSource().getType() == InventoryType.HOPPER && BlockStorage.check(loc) instanceof NotHopperable) {
            e.setCancelled(true);
        }
    }
}
