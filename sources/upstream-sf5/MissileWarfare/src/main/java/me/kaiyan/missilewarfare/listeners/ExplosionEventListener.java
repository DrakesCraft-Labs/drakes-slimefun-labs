package me.kaiyan.missilewarfare.listeners;

import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Objects;

/**
 * Listens for explosion events triggered by missile entities and
 * tracks the number of blocks destroyed for metrics.
 *
 * @author MissileWarfare contributors
 */
public class ExplosionEventListener implements Listener {

    /**
     * Handles entity explosion events to count blocks destroyed by missiles.
     *
     * @param event the entity explode event
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (Objects.equals(event.getEntity().getCustomName(), "MissileHolder")) {
            MissileWarfare.blocksExploded += event.blockList().size();
        }
    }
}
