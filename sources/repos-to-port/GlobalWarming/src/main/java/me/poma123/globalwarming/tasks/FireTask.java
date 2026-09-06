/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 */
package me.poma123.globalwarming.tasks;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.ParametersAreNonnullByDefault;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.tasks.MechanicTask;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class FireTask
extends MechanicTask {
    private final ThreadLocalRandom rnd = ThreadLocalRandom.current();
    private final double minimumTemperature;
    private final double chance;
    private final int fireAmount;

    @ParametersAreNonnullByDefault
    public FireTask(double minimumTemperature, double chance, int fireAmount) {
        this.minimumTemperature = minimumTemperature;
        this.chance = chance;
        this.fireAmount = fireAmount;
    }

    private void fire(World world) {
        if (world != null) {
            Chunk[] loadedChunks = world.getLoadedChunks();
            int count = loadedChunks.length;
            for (int i = 0; i < this.fireAmount; ++i) {
                int index = this.rnd.nextInt(count);
                Chunk chunk = loadedChunks[index];
                int x = (chunk.getX() << 4) + this.rnd.nextInt(16);
                int z = (chunk.getZ() << 4) + this.rnd.nextInt(16);
                Block current = world.getHighestBlockAt(x, z).getRelative(BlockFace.UP);
                if (!(GlobalWarmingPlugin.getTemperatureManager().getTemperatureAtLocation(current.getLocation()).getCelsiusValue() >= this.minimumTemperature)) continue;
                current.setType(Material.FIRE);
            }
        }
    }

    @Override
    public void run() {
        Set<String> enabledWorlds = GlobalWarmingPlugin.getRegistry().getEnabledWorlds();
        for (String worldName : enabledWorlds) {
            double random;
            World w = Bukkit.getWorld((String)worldName);
            if (w == null || !GlobalWarmingPlugin.getRegistry().isWorldEnabled(w.getName()) || w.getEnvironment() != World.Environment.NORMAL || w.getPlayers().isEmpty() || w.hasStorm() || w.isThundering() || w.getLoadedChunks().length <= 0 || !((random = this.rnd.nextDouble()) < this.chance)) continue;
            this.fire(w);
        }
    }
}

