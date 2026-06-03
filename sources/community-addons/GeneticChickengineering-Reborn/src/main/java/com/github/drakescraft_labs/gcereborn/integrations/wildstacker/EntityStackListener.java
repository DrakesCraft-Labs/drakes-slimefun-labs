package com.github.drakescraft_labs.gcereborn.integrations.wildstacker;

import com.bgsoftware.wildstacker.api.events.EntityStackEvent;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.drake.dough.data.persistent.PersistentDataAPI;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.utils.Keys;

public class EntityStackListener implements Listener {

    public EntityStackListener(GeneticChickengineering plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityStack(EntityStackEvent e) {
        if (e.getEntity().getType() != EntityType.CHICKEN || e.getTarget().getType() != EntityType.CHICKEN) {
            return;
        }
        var source = (Chicken) e.getEntity().getLivingEntity();
        var target = (Chicken) e.getTarget().getLivingEntity();

        // both have no dna data, no need to handle.
        if (!PersistentDataAPI.hasString(source, Keys.CHICKEN_DNA) && !PersistentDataAPI.hasString(target, Keys.CHICKEN_DNA)) {
            return;
        }

        // one of them has dna data, cancel merging.
        if (PersistentDataAPI.hasString(source, Keys.CHICKEN_DNA) != PersistentDataAPI.hasString(target, Keys.CHICKEN_DNA)) {
            e.setCancelled(true);
            return;
        }

        // now both have dna data, check if they are different.
        // if so, cancel merging.
        if (!PersistentDataAPI.getString(source, Keys.CHICKEN_DNA).equals(PersistentDataAPI.getString(target, Keys.CHICKEN_DNA))) {
            e.setCancelled(true);
        }
    }
}
