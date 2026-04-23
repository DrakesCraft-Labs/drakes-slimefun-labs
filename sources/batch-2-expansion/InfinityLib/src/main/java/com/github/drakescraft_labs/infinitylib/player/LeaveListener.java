package com.github.drakescraft_labs.infinitylib.player;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

/**
 * Utility to clean up maps when players leave.
 */
public final class LeaveListener implements Listener {

    private final Map<UUID, ?> map;

    private LeaveListener(Map<UUID, ?> map) {
        this.map = map;
    }

    public static void add(Map<UUID, ?> map) {
        Plugin plugin = Bukkit.getPluginManager().getPlugins()[0]; // Hacky, but works for lib
        Bukkit.getPluginManager().registerEvents(new LeaveListener(map), plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        map.remove(e.getPlayer().getUniqueId());
    }
}
