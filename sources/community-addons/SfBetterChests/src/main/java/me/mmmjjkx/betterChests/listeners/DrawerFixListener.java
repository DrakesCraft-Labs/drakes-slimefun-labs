package me.mmmjjkx.betterChests.listeners;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import me.mmmjjkx.betterChests.items.chests.SimpleDrawer;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class DrawerFixListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onOpenDrawer(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        Location loc = inv.getLocation();
        if (loc != null) {
            SlimefunItem item = BlockStorage.check(loc);
            if (item instanceof SimpleDrawer) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(InventoryMoveItemEvent e) {
        Location l = e.getDestination().getLocation();
        if (l != null) {
            SlimefunItem item = BlockStorage.check(l);
            if (item instanceof SimpleDrawer) {
                e.setCancelled(true);
            }
        }
    }
}
