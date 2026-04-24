package me.voper.slimeframe.implementation.listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import dev.drake.dough.items.ItemUtils;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.managers.RelicInventoryManager;
import me.voper.slimeframe.implementation.items.relics.RelicInventory;

public class RelicInventoryListener implements Listener {

    private final SlimeFrame plugin;
    private final RelicInventoryManager relicInventoryManager = SlimeFrame.getRelicInventoryManager();

    public RelicInventoryListener(SlimeFrame plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        RelicInventory relicInventory = relicInventoryManager.getRelicInventory(p);
        if (!e.getInventory().equals(relicInventory.getInventory())) return;

        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) return;

        if (clickedInventory.equals(relicInventory.getInventory())) {
            e.setCancelled(true);
            ItemStack currentItem = e.getCurrentItem();
            if (currentItem == null || currentItem.getType() == Material.AIR) return;
            ItemStack clone = currentItem.clone();

            int consume = 1;
            if (!e.isShiftClick()) {
                clone.setAmount(1);
            } else {
                consume = clone.getAmount();
            }

            HashMap<Integer, ItemStack> nonAddedItems = p.getInventory().addItem(clone);
            if (nonAddedItems.isEmpty()) {
                ItemUtils.consumeItem(currentItem, consume, false);
//                currentItem.setAmount(currentItem.getAmount() - 1);
            } else {
                p.sendMessage(ChatColor.RED + "Your inventory is full!");
            }
            return;
        }

        if (e.isShiftClick()) e.setCancelled(true);
    }

}
