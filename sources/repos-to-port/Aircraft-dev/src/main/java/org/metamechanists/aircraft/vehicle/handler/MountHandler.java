package org.metamechanists.aircraft.vehicle.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;
import org.metamechanists.aircraft.vehicle.VehicleEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;


public final class MountHandler implements Listener {
    public static void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new MountHandler(), Aircraft.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private static void onUnmount(@NotNull EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }

        if (!(KinematicEntity.get(e.getDismounted().getUniqueId()) instanceof VehicleEntity vehicleEntity)) {
            return;
        }

        vehicleEntity.onUnmount(player);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private static void onMount(@NotNull EntityMountEvent e) {
        Entity maybePlayer = e.getEntity();
        if (!(maybePlayer instanceof Player player)) {
            return;
        }

        if (!(KinematicEntity.get(e.getMount().getUniqueId()) instanceof VehicleEntity vehicleEntity)) {
            return;
        }

        vehicleEntity.onMount(player);
    }
}
