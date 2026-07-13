package com.github.drakescraft_labs.slimefun4.core.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;

import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.core.debug.Debug;
import com.github.drakescraft_labs.slimefun4.core.debug.TestCase;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

/**
 * This Service is responsible for automatically saving {@link Player} and {@link Block}
 * data.
 * 
 * @author TheBusyBiscuit
 *
 */
public class AutoSavingService implements Listener {

    private int interval;
    private Slimefun plugin;
    private int dynamicIntervalSeconds;
    private int dynamicThreshold;
    private int dynamicBatchSize;

    /**
     * This method starts the {@link AutoSavingService} with the given interval.
     * 
     * @param plugin
     *            The current instance of Slimefun
     * @param interval
     *            The interval in which to run this task
     */
    public void start(@Nonnull Slimefun plugin, int interval) {
        this.interval = interval;
        this.plugin = plugin;
        this.dynamicIntervalSeconds = Math.max(0, plugin.getConfig().getInt("performance.dynamic-block-autosave.interval-seconds", 30));
        this.dynamicThreshold = Math.max(1, plugin.getConfig().getInt("performance.dynamic-block-autosave.threshold", 2048));
        this.dynamicBatchSize = Math.max(1, plugin.getConfig().getInt("performance.dynamic-block-autosave.batch-size", 500));

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllPlayers, 2000L, interval * 60L * 20L);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::saveAllBlocks, 2000L, interval * 60L * 20L);
        if (dynamicIntervalSeconds > 0) {
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::saveDirtyBlocksBatch, dynamicIntervalSeconds * 20L, dynamicIntervalSeconds * 20L);
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * This method saves every {@link PlayerProfile} in memory and removes profiles
     * that were marked for deletion.
     */
    private void saveAllPlayers() {
        Iterator<PlayerProfile> iterator = PlayerProfile.iterator();
        int players = 0;

        Debug.log(TestCase.PLAYER_PROFILE_DATA, "Saving all players data");

        while (iterator.hasNext()) {
            try {
                PlayerProfile profile = iterator.next();

                if (profile.isDirty()) {
                    players++;
                    profile.save();

                    Debug.log(TestCase.PLAYER_PROFILE_DATA, "Saved data for {} ({})",
                        profile.getPlayer() != null ? profile.getPlayer().getName() : "Unknown", profile.getUUID()
                    );
                }

                // Remove the PlayerProfile from memory if the player has left the server (marked from removal)
                // and they're still not on the server
                // At this point, we've already saved their profile so we can safely remove it
                // without worry for having a data sync issue (e.g. data is changed but then we try to re-load older data)
                if (profile.isMarkedForDeletion() && profile.getPlayer() == null) {
                    iterator.remove();

                    Debug.log(TestCase.PLAYER_PROFILE_DATA, "Removed data from memory for {}",
                        profile.getUUID()
                    );
                }
            } catch (Throwable t) {
                Slimefun.logger().log(Level.SEVERE, "Error saving player profile during auto-save", t);
            }
        }

        if (players > 0) {
            Slimefun.logger().log(Level.INFO, "Auto-saved all player data for {0} player(s)!", players);
        }
    }

    /**
     * This method saves the data of every {@link Block} marked dirty by {@link BlockStorage}.
     */
    private void saveAllBlocks() {
        Set<BlockStorage> worlds = new HashSet<>();

        for (World world : Bukkit.getWorlds()) {
            try {
                BlockStorage storage = BlockStorage.getStorage(world);

                if (storage != null) {
                    storage.computeChanges();

                    if (storage.getChanges() > 0) {
                        worlds.add(storage);
                    }
                }
            } catch (Throwable t) {
                Slimefun.logger().log(Level.SEVERE, "Error computing block changes for world " + world.getName() + " during auto-save", t);
            }
        }

        if (!worlds.isEmpty()) {
            Slimefun.logger().log(Level.INFO, "Auto-saving block data... (Next auto-save: {0}m)", interval);

            for (BlockStorage storage : worlds) {
                try {
                    storage.save();
                } catch (Throwable t) {
                    Slimefun.logger().log(Level.SEVERE, "Error saving block data for world " + (storage.getWorld() != null ? storage.getWorld().getName() : "unknown") + " during auto-save", t);
                }
            }
        }

        try {
            BlockStorage.saveChunks();
        } catch (Throwable t) {
            Slimefun.logger().log(Level.SEVERE, "Error saving chunks during auto-save", t);
        }
    }

    /**
     * This drains large BlockStorage queues in small batches. It avoids letting
     * dirty block data pile up for the full periodic auto-save interval while
     * also avoiding one huge disk I/O spike when busy Slimefun worlds are active.
     */
    private void saveDirtyBlocksBatch() {
        for (World world : Bukkit.getWorlds()) {
            try {
                BlockStorage storage = BlockStorage.getStorage(world);

                if (storage == null) {
                    continue;
                }

                storage.computeChanges();
                int queued = storage.getChanges();

                if (queued >= dynamicThreshold) {
                    Slimefun.logger().log(Level.FINE, "Dynamic block auto-save: draining up to {0} of {1} queued change(s) for world \"{2}\".", new Object[] { dynamicBatchSize, queued, world.getName() });
                    storage.save(dynamicBatchSize);
                }
            } catch (Throwable t) {
                Slimefun.logger().log(Level.SEVERE, "Error draining dynamic block auto-save for world " + world.getName(), t);
            }
        }
    }

    /**
     * Listen to WorldSaveEvent to automatically write BlockStorage data to disk.
     * This ensures that Slimefun data is aligned with Minecraft world saves,
     * preventing rollback exploits and desync issues.
     */
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        BlockStorage storage = BlockStorage.getStorage(world);

        if (storage == null || plugin == null) {
            return;
        }

        Runnable saveTask = () -> {
            try {
                storage.computeChanges();
                if (storage.getChanges() > 0) {
                    Slimefun.logger().log(Level.INFO, "Saving BlockStorage for world \"{0}\" on WorldSaveEvent ({1} changes queued)", new Object[] { world.getName(), storage.getChanges() });
                    storage.save();
                }
            } catch (Throwable t) {
                Slimefun.logger().log(Level.SEVERE, "Error saving block data for world " + world.getName() + " on WorldSaveEvent", t);
            }
        };

        // Durante el apagado el scheduler rechaza tareas nuevas (IllegalPluginAccessException)
        // y perderíamos el guardado más importante: el del save-all final. Se ejecuta en línea.
        if (plugin.isEnabled()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, saveTask);
        } else {
            saveTask.run();
        }
    }

    /**
     * Guardado síncrono al apagar el servidor: todos los perfiles en memoria (no solo los marcados dirty)
     * y el mismo volcado de bloques que el auto-save periódico, más {@link BlockStorage#saveChunks()}.
     * Debe ejecutarse en el hilo principal durante {@code onDisable}.
     */
    public void shutdownSave() {
        Slimefun.logger().log(Level.INFO, "Slimefun: ejecutando guardado de apagado (jugadores, bloques y chunks)...");
        try {
            saveAllPlayersShutdown();
        } catch (Throwable t) {
            Slimefun.logger().log(Level.SEVERE, "Error fatal al guardar los perfiles de jugadores en el apagado", t);
        }
        try {
            saveAllBlocksShutdown();
        } catch (Throwable t) {
            Slimefun.logger().log(Level.SEVERE, "Error fatal al guardar los bloques de BlockStorage en el apagado", t);
        }
        try {
            BlockStorage.saveChunks();
        } catch (Throwable t) {
            Slimefun.logger().log(Level.SEVERE, "Error fatal al guardar los chunks de BlockStorage en el apagado", t);
        }
    }

    private void saveAllPlayersShutdown() {
        Iterator<PlayerProfile> iterator = PlayerProfile.iterator();
        int players = 0;

        while (iterator.hasNext()) {
            PlayerProfile profile = iterator.next();
            try {
                profile.save();
                players++;
            } catch (Throwable t) {
                Slimefun.logger().log(Level.SEVERE, "Error al guardar el perfil del jugador " + profile.getUUID() + " en el apagado", t);
            }
        }

        if (players > 0) {
            Slimefun.logger().log(Level.INFO, "Slimefun: guardados {0} perfil(es) de jugador en el apagado.", players);
        }
    }

    private void saveAllBlocksShutdown() {
        int worlds = 0;

        for (World world : Bukkit.getWorlds()) {
            BlockStorage storage = BlockStorage.getStorage(world);

            if (storage != null) {
                try {
                    int passes = storage.flushUntilClean(5);
                    storage.computeChanges();
                    if (storage.getChanges() > 0) {
                        Slimefun.logger().log(Level.SEVERE, "Slimefun: quedaron {0} cambios sin persistir en el mundo '{1}' tras {2} intentos.", new Object[] { storage.getChanges(), world.getName(), passes });
                    }
                    worlds++;
                } catch (Throwable t) {
                    Slimefun.logger().log(Level.SEVERE, "Error al guardar BlockStorage para el mundo '" + world.getName() + "' en el apagado", t);
                }
            }
        }

        if (worlds > 0) {
            Slimefun.logger().log(Level.INFO, "Slimefun: volcado de BlockStorage revisado en {0} mundo(s) al apagar.", worlds);
        }
    }

}
