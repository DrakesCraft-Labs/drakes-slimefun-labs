package com.github.drakescraft_labs.villagertrade.implementation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;

import com.github.drakescraft_labs.villagertrade.VillagerTrade;
import com.github.drakescraft_labs.villagertrade.api.trades.TradeItem;

public final class TradeInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.isCancelled()) {
            return;
        }

        final Inventory clickedInventory = e.getClickedInventory();
        final Inventory topInventory = e.getView().getTopInventory();

        if (clickedInventory != null && topInventory.getType() == InventoryType.MERCHANT) {
            SlimefunItem sfItem;
            if (clickedInventory.getType() == InventoryType.MERCHANT) {
                sfItem = SlimefunItem.getByItem(e.getCursor());
            } else {
                sfItem = SlimefunItem.getByItem(e.getCurrentItem());
            }

            if (sfItem == null) {
                return;
            }

            for (TradeItem tradeItem : VillagerTrade.getRegistry().getSlimefunTradeInputs()) {
                if (sfItem.getId().equals(tradeItem.getId())) {
                    e.setCancelled(false);
                    return;
                }
            }
        }
    }
}
