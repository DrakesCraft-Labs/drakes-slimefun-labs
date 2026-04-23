package com.github.drakescraft_labs.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.github.drakescraft_labs.slimefun4.api.items.ItemHandler;
import com.github.drakescraft_labs.slimefun4.core.SlimefunRegistry;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemDropHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

/**
 * Listens to the {@link PlayerDropItemEvent} to call any {@link ItemDropHandler}.
 *
 * @author TheBusyBiscuit
 * 
 * @see ItemDropHandler
 */
public class ItemDropListener implements Listener {

    public ItemDropListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        SlimefunRegistry registry = Slimefun.getRegistry();

        for (ItemHandler handler : registry.getGlobalItemHandlers(ItemDropHandler.class)) {
            if (((ItemDropHandler) handler).onItemDrop(e, e.getPlayer(), e.getItemDrop())) {
                return;
            }
        }
    }
}
