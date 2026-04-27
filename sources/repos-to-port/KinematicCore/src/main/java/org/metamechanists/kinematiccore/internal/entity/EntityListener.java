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
            kinematicEntity.onRightClick(event.getPlayer());
        }
    }
}
