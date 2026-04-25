package me.voper.slimeframe.implementation.tasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import me.voper.slimeframe.SlimeFrame;

// This class manages the particles around each CoolantRaknoid
public class CoolantRaknoidsTask implements Runnable {

    public static final Set<UUID> RAKNOIDS_UUIDS = new HashSet<>();
    private static final double MULTIPLIER = 0.90D;
    private static double angle = 0D;

    public CoolantRaknoidsTask(@Nonnull SlimeFrame plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        Iterator<UUID> iterator = RAKNOIDS_UUIDS.iterator();
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null || !entity.isValid()) {
                iterator.remove();
                continue;
            }

            Location particleLoc = entity.getLocation().clone();
            particleLoc.setX(particleLoc.getX() + Math.cos(angle) * MULTIPLIER);
            particleLoc.setZ(particleLoc.getZ() + Math.sin(angle) * MULTIPLIER);
            entity.getWorld().spawnParticle(Particle.DUST, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(87, 182, 255), 2));

            angle += 0.25D;
            if (angle > 360) {
                angle = 0D;
            }
        }
    }
}
