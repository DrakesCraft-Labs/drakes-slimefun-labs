package com.github.jackstar.coronalis.discovery;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.jackstar.coronalis.Coronalis;
import com.github.drakescraft_labs.slimefun4.api.events.ResearchUnlockEvent;

/**
 * Bonus de XP al desbloquear investigaciones del addon.
 */
public final class DiscoveryListener implements Listener {

    private final DiscoveryService discoveryService;

    public DiscoveryListener(@Nonnull DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResearchUnlock(@Nonnull ResearchUnlockEvent event) {
        discoveryService.onResearchUnlocked(
            event.getPlayer(),
            event.getResearch().getKey(),
            event.getResearch().getName(event.getPlayer())
        );
    }
}
