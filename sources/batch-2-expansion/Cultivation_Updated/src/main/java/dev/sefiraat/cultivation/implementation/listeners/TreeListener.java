package dev.sefiraat.cultivation.implementation.listeners;

import dev.sefiraat.cultivation.api.events.CultivationTreeGrowEvent;
import dev.sefiraat.cultivation.api.slimefun.items.trees.CultivationTree;
import dev.drake.sefilib.misc.ParticleUtils;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import javax.annotation.Nonnull;

public class TreeListener implements Listener {

    @EventHandler
    public void onTreeGrows(@Nonnull StructureGrowEvent event) {
        Location location = event.getLocation();
        SlimefunItem slimefunItem = BlockStorage.check(location);
        if (slimefunItem instanceof CultivationTree tree) {
            event.setCancelled(true);
            if (tree.testBuild(location)) {
                CultivationTreeGrowEvent growEvent = new CultivationTreeGrowEvent(
                    location,
                    event.getPlayer(),
                    tree.getTreeDesign(),
                    slimefunItem
                );
                Bukkit.getPluginManager().callEvent(growEvent);
                if (growEvent.isCancelled()) {
                    return;
                }
                BlockStorage.clearBlockInfo(location);
                tree.grow(location);
            } else {
                ParticleUtils.displayParticleRandomly(
                    location,
                    1,
                    5,
                    new Particle.DustOptions(Color.BLACK, 1)
                );
            }
        }
    }
}
