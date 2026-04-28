package io.github.addoncommunity.galactifun.api.worlds.populators.relics;

import java.util.Random;

import javax.annotation.Nonnull;


import org.bukkit.Location;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;

import io.github.addoncommunity.galactifun.base.BaseItems;
import io.github.addoncommunity.galactifun.util.Util;
import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class FallenSatellitePopulator extends BlockPopulator {
    public FallenSatellitePopulator(double chance) {
        this.chance = chance;
    }


    private final double chance;

    @Override
    public void populate(@Nonnull WorldInfo worldInfo, @Nonnull Random random, int cx, int cz, @Nonnull LimitedRegion region) {
        if (random.nextDouble() * 100 < chance) {
            Vector v = random.nextBoolean() ? new Vector(1, 0, 0) : new Vector(0, 0, 1);
            int x = (cx << 4) + random.nextInt(16);
            int z = (cz << 4) + random.nextInt(16);
            Location l = Util.getHighestBlockAt(region, x, z).add(0, 1, 0);
            setSlimefunBlock(region, l, BaseItems.FALLEN_SATELLITE_RELIC.item());
            if (random.nextBoolean()) {
                setSlimefunBlock(region, l.add(v), BaseItems.BROKEN_SOLAR_PANEL_RELIC.item());
                v.multiply(-1);
                l.add(v);
            }
            if (random.nextBoolean()) {
                setSlimefunBlock(region, l.add(v), BaseItems.BROKEN_SOLAR_PANEL_RELIC.item());
            }
        }
    }

    protected void setSlimefunBlock(LimitedRegion region, Location l, SlimefunItemStack item) {
        region.setType(l, item.getType());
        Location copy = l.clone();
        copy.setWorld(region.getWorld());
        Scheduler.run(() -> BlockStorage.addBlockInfo(copy, "id", item.getItemId()));
    }

}
