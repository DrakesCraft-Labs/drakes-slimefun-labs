package com.github.drakescraft_labs.slimefun4.implementation.listeners;

import java.util.logging.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Fuerza guardado de configuración al deshabilitar plugins.
 * Esto añade una red de seguridad para reinicios/apagados donde algún addon
 * no persiste su config explícitamente en onDisable.
 */
public final class PluginShutdownSaveListener implements Listener {

    private final JavaPlugin owner;

    public PluginShutdownSaveListener(JavaPlugin owner) {
        this.owner = owner;
        owner.getServer().getPluginManager().registerEvents(this, owner);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent event) {
        if (!(event.getPlugin() instanceof JavaPlugin plugin)) {
            return;
        }

        // Evita autollamadas redundantes del propio Slimefun.
        if (plugin == owner) {
            return;
        }

        try {
            plugin.saveConfig();
        } catch (Exception ex) {
            owner.getLogger().log(
                Level.FINE,
                "No se pudo persistir config de " + plugin.getName() + " al deshabilitar.",
                ex
            );
        }
    }
}

