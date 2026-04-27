package org.metamechanists.kinematiccore.internal.entity;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;


public class EntityStorageListener implements Listener {
    public static void init() {
        Bukkit.getPluginManager().registerEvents(new EntityStorageListener(), KinematicCore.instance());
    }

    @EventHandler
    private static void onEntityLoad(@NotNull EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
             EntityStorage.instance().load(entity.getUniqueId());
        }
    }

    // TODO: This currently does not differentiate between unloaded and dead entities because the API is fucking broken and
    // isDead does not actually return if the entity is dead (for some reason). When this PR
    // https://github.com/PaperMC/Paper/pull/10149#issuecomment-2403735366
    // is merged, this can be fixed, but currently this fires WHEN ENTITIES ARE KILLED meaning it 'competes' with onEntityDeath
    @EventHandler
    private static void onEntityUnload(@NotNull EntityRemoveFromWorldEvent event) {
        KinematicEntity<?, ?> kinematicEntity = EntityStorage.instance().get(event.getEntity().getUniqueId());
        if (kinematicEntity == null) {
            return;
        }

        EntityStorage.instance().unload(kinematicEntity);
        EntityStorage.instance().save(kinematicEntity);
    }

    @EventHandler
    private static void onEntityDeath(@NotNull EntityDeathEvent event) {
        KinematicEntity<?, ?> kinematicEntity = KinematicEntity.get(event.getEntity().getUniqueId());
        if (kinematicEntity == null) {
            return;
        }

        EntityStorage.instance().delete(kinematicEntity);
    }
}
