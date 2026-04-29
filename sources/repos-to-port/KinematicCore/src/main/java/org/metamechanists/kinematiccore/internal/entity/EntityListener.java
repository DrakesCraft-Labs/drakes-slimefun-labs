package org.metamechanists.kinematiccore.internal.entity;


import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;


public class EntityListener implements Listener {
    public static void init() {
        Bukkit.getPluginManager().registerEvents(new EntityListener(), KinematicCore.instance());
    }

    @EventHandler
    public static void onRightClick(@NotNull PlayerInteractEntityEvent event) {
        KinematicEntity<?, ?> kinematicEntity = EntityStorage.instance().get(event.getRightClicked().getUniqueId());
        if (kinematicEntity != null) {
            try {
                kinematicEntity.onRightClick(event.getPlayer());
            } catch (RuntimeException exception) {
                KinematicCore.instance().getLogger().warning(
                        "Failed handling right-click for kinematic entity "
                                + kinematicEntity.uuid() + " (" + kinematicEntity.getClass().getSimpleName() + ")"
                );
                KinematicCore.instance().getLogger().warning(
                        "Interaction ignored to prevent server-side exception spam."
                );
            }
        }
    }
}
