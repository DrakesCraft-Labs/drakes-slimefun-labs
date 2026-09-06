/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Keyed
 *  org.bukkit.Material
 *  org.bukkit.Tag
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockFadeEvent
 */
package me.poma123.globalwarming.tasks;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.ParametersAreNonnullByDefault;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.tasks.MechanicTask;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockFadeEvent;

public class MeltTask
extends MechanicTask {
    private final ThreadLocalRandom rnd = ThreadLocalRandom.current();
    private final double minimumTemperature;
    private final double chance;
    private final int meltAmount;

    @ParametersAreNonnullByDefault
    public MeltTask(double minimumTemperature, double chance, int meltAmount) {
        this.minimumTemperature = minimumTemperature;
        this.chance = chance;
        this.meltAmount = meltAmount;
    }

    private void melt(World world) {
        if (world != null) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            int count = loadedChunks.length;
            for (int i = 0; i < this.meltAmount; ++i) {
                int z;
                int index = this.rnd.nextInt(count);
                Chunk chunk = loadedChunks[index];
                int x = (chunk.getX() << 4) + this.rnd.nextInt(16);
                Block current = world.getHighestBlockAt(x, z = (chunk.getZ() << 4) + this.rnd.nextInt(16));
                if (!Tag.ICE.isTagged(current.getType()) || !(GlobalWarmingPlugin.getTemperatureManager().getTemperatureAtLocation(current.getLocation()).getCelsiusValue() >= this.minimumTemperature)) continue;
                BlockState state = current.getState();
                if (current.getType() == Material.ICE) {
                    state.setType(Material.WATER);
                } else {
                    state.setType(Material.AIR);
                }
                GlobalWarmingPlugin.getInstance().getServer().getPluginManager().callEvent((Event)new BlockFadeEvent(current, state));
            }
        }
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarmingPlugin.getRegistry().getEnabledWorlds();
        for (String worldName : enabledWorlds) {
            double random;
            World w = Bukkit.getWorld((String)worldName);
            if (w == null || !GlobalWarmingPlugin.getRegistry().isWorldEnabled(w.getName()) || w.getEnvironment() != World.Environment.NORMAL || w.getPlayers().isEmpty() || w.getLoadedChunks().length <= 0 || !((random = this.rnd.nextDouble()) < this.chance)) continue;
            this.melt(w);
        }
    }
}

