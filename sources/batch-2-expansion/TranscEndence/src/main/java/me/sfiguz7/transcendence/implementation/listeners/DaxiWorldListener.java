package me.sfiguz7.transcendence.implementation.listeners;

import me.sfiguz7.transcendence.TranscEndence;
import me.sfiguz7.transcendence.implementation.items.items.Daxi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DaxiWorldListener implements Listener {

    public DaxiWorldListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        TranscEndence.getInstance().getServer().getScheduler().runTask(
            TranscEndence.getInstance(),
            () -> Daxi.reapplyEffects(event.getPlayer())
        );
    }
}
