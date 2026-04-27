package org.metamechanists.kinematiccore.internal.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class EntityTicker implements Runnable {
    private static final int INTERVAL = 1;
    private static final Map<UUID, EntityErrorTracker> errorTrackers = new HashMap<>();

    private static void handleError(UUID uuid, @NotNull KinematicEntity<?, ?> kinematicEntity, Exception e) {
        if (kinematicEntity.schema() != null) {
            KinematicCore.instance().getLogger().severe("Failed to tick " + kinematicEntity.schema().id());
        } else {
            KinematicCore.instance().getLogger().severe("Failed to tick " + uuid);
        }

        errorTrackers.computeIfAbsent(uuid, k -> new EntityErrorTracker()).incrementErrors();
        e.printStackTrace();
    }

    @Override
    public void run() {
        long tick = Bukkit.getCurrentTick();
        for (Map.Entry<String, Set<UUID>> kinematicEntityId : KinematicEntity.loaded().entrySet()) {
            for (UUID uuid : kinematicEntityId.getValue()) {
                KinematicEntity<?, ?> kinematicEntity = KinematicEntity.get(uuid);

                try {
                    assert kinematicEntity != null;
                    if (kinematicEntity.schema() == null) {
                        // Prevent entities with unregistered schemas being ticked
                        continue;
                    }
                    kinematicEntity.tick(tick);
                } catch (Exception e) {
                    handleError(uuid, kinematicEntity, e);
                }
            }
        }

        errorTrackers.forEach((k, v) -> v.tick());
        errorTrackers.entrySet()
                .removeIf(pair -> EntityStorage.instance().get(pair.getKey()) == null);
        errorTrackers.entrySet()
                .removeIf(pair -> pair.getValue().shouldRemoveTracker());
        errorTrackers.entrySet()
                .stream()
                .filter(pair -> pair.getValue().shouldDeleteEntity())
                .forEach(pair -> {
                    KinematicCore.instance().getLogger().severe("Removed the entity " + pair.getKey()
                            + " because it threw " + EntityErrorTracker.MAX_ERRORS
                            + " errors in under " + EntityErrorTracker.TRACKER_TIME + " ticks");
                    Entity entity = Bukkit.getEntity(pair.getKey());
                    assert entity != null;
                    entity.remove();
                });
    }

    public static void init() {
        Bukkit.getServer().getScheduler().runTaskTimer(KinematicCore.instance(), new EntityTicker(), 0, INTERVAL);
    }
}