package org.metamechanists.aircraft.vehicle.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Protector implements Listener {
    private static final Set<UUID> protectedEntities = new HashSet<>();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new Protector(), Aircraft.getInstance());
    }

    public static void protect(@NotNull Entity entity) {
        protectedEntities.add(entity.getUniqueId());
    }

    @EventHandler
    private static void onDamage(@NotNull EntityDamageEvent event) {
        if (protectedEntities.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
