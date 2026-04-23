package dev.j3fftw.headlimiter.blocklimiter;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.drakescraft_labs.slimefun4.api.events.SlimefunBlockBreakEvent;
import com.github.drakescraft_labs.slimefun4.api.events.SlimefunBlockPlaceEvent;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.blocks.ChunkPosition;

import dev.j3fftw.headlimiter.HeadLimiter;

public class BlockListener implements Listener {

    public BlockListener(@Nonnull HeadLimiter headLimiter) {
        headLimiter.getServer().getPluginManager().registerEvents(this, headLimiter);
    }

    @EventHandler
    public void onSlimefunItemPlaced(@Nonnull SlimefunBlockPlaceEvent event) {
        SlimefunItem slimefunItem = event.getSlimefunItem();
        String slimefunItemId = slimefunItem.getId();
        int definedLimit = BlockLimiter.getInstance().getPlayerLimitByItem(event.getPlayer(), slimefunItem);

        if (definedLimit == -1) {
            // No limit has been set, nothing required for HeadLimiter
            return;
        }

        ChunkPosition chunkPosition = new ChunkPosition(event.getBlockPlaced().getChunk());
        ChunkContent content = BlockLimiter.getInstance().getChunkContent(chunkPosition);

        if (content == null) {
            // Content is null so no blocks are currently in this chunk, lets set one up - event can continue
            content = new ChunkContent();
            content.incrementAmount(slimefunItemId);
            BlockLimiter.getInstance().setChunkContent(chunkPosition, content);
        } else if (content.getGroupTotal(slimefunItemId) < definedLimit) {
            // This chunk can take more of the specified item type
            content.incrementAmount(slimefunItemId);
        } else {
            // Chunk has hit its limit for this type, time to deny the placement
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place any more of this item within this chunk.");
        }
    }

    @EventHandler
    public void onSlimefunItemBroken(@Nonnull SlimefunBlockBreakEvent event) {
        SlimefunItem slimefunItem = event.getSlimefunItem();
        String slimefunItemId = slimefunItem.getId();
        int definedLimit = BlockLimiter.getInstance().getPlayerLimitByItem(event.getPlayer(), slimefunItem);
        if (definedLimit == -1) {
            // No limit has been set, nothing required for HeadLimiter
            return;
        }

        ChunkPosition chunkPosition = new ChunkPosition(event.getBlockBroken().getChunk());
        ChunkContent content = BlockLimiter.getInstance().getChunkContent(chunkPosition);

        if (content == null) {
            // Content is null so no blocks are currently in this chunk, shouldn't be possible, but never mind
            return;
        }

        // This chunk can take more of the specified item type
        content.decrementAmount(slimefunItemId);

    }

}
