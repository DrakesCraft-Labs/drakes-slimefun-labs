package dev.sefiraat.cultivation.implementation.utils;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;

import javax.annotation.Nonnull;

public final class CultivationDisplayCleanup {

    private CultivationDisplayCleanup() {
    }

    /** Removes orphaned Cultivation visuals from the exact broken block column. */
    public static void removeOrphans(@Nonnull Location blockLocation) {
        Location center = blockLocation.clone().add(0.5, 1.0, 0.5);
        for (Entity entity : blockLocation.getWorld().getNearbyEntities(center, 0.49, 2.5, 0.49)) {
            if (entity instanceof Display || entity instanceof Interaction) {
                entity.remove();
            }
        }
    }
}
