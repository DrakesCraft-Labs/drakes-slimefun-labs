package io.github.thebusybiscuit.slimefunluckyblocks;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

/**
 * Listens for chunk population events and randomly spawns Lucky Blocks
 * in newly generated chunks.
 *
 * @author TheBusyBiscuit
 */
public class WorldGenerator implements Listener {

    private final SlimefunLuckyBlocks plugin;
    private final List<String> blacklist;
    private final int chance;

    /**
     * Creates a new {@link WorldGenerator} and registers it as a listener.
     *
     * @param plugin the {@link SlimefunLuckyBlocks} plugin instance
     */
    public WorldGenerator(@Nonnull SlimefunLuckyBlocks plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        blacklist = plugin.getCfg().getStringList("world-blacklist");
        chance = plugin.getCfg().getInt("chance");
    }

    @EventHandler
    public void onRandomSpawn(@Nonnull ChunkPopulateEvent e) {
        if (blacklist.contains(e.getWorld().getName())) {
            return;
        }

        Random random = ThreadLocalRandom.current();

        if (random.nextInt(100) < chance) {
            int x = e.getChunk().getX() * 16 + random.nextInt(16);
            int z = e.getChunk().getZ() * 16 + random.nextInt(16);
            int y = e.getWorld().getHighestBlockYAt(x, z);

            Block current = e.getWorld().getBlockAt(x, y, z);
            if (!current.getType().isSolid() && current.getRelative(BlockFace.DOWN).getType().isSolid()) {
                plugin.spawnLuckyBlock(current);
            }
        }
    }

}
