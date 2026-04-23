package dev.j3fftw.headlimiter.blocklimiter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.blocks.ChunkPosition;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;

import dev.j3fftw.headlimiter.HeadLimiter;

public final class BlockLimiter {

    private static BlockLimiter instance;
    private final HashSet<Group> groups = new HashSet<>();
    private final Map<ChunkPosition, ChunkContent> contentMap = new HashMap<>();

    public BlockLimiter(@Nonnull HeadLimiter headLimiter) {
        Preconditions.checkArgument(instance == null, "Cannot create a new instance of the BlockLimiter");
        instance = this;
        new BlockListener(headLimiter);
        headLimiter.getServer().getScheduler().runTaskLater(headLimiter, this::loadBlockStorage, 1);
    }

    private void loadBlockStorage() {
        for (World world : Bukkit.getWorlds()) {
            BlockStorage worldStorage = BlockStorage.getStorage(world);
            if (worldStorage == null) {
                return;
            } else {
                for (Map.Entry<Location, Config> entry : worldStorage.getRawStorage().entrySet()) {
                    Location location = entry.getKey();
                    String id = entry.getValue().getString("id");
                    ChunkPosition chunkPosition = new ChunkPosition(location);
                    ChunkContent content = contentMap.get(chunkPosition);
                    if (content == null) {
                        content = new ChunkContent();
                        content.incrementAmount(id);
                        contentMap.put(chunkPosition, content);
                    } else {
                        content.incrementAmount(id);
                    }
                }
            }

        }
    }

    @Nullable
    public ChunkContent getChunkContent(@Nonnull ChunkPosition chunkPosition) {
        return contentMap.get(chunkPosition);
    }

    public Group getGroupByItem(@Nonnull SlimefunItem slimefunItem) {
        return getGroupByItem(slimefunItem.getId());
    }

    @Nullable
    public Group getGroupByItem(@Nonnull String itemId) {
        for (Group group : this.groups) {
            if (group.contains(itemId)) {
                return group;
            }
        }
        return null;
    }

    public int getPlayerLimitByItem(@Nonnull Player player, @Nonnull SlimefunItem slimefunItem) {
        return getPlayerLimitByItem(player, slimefunItem.getId());
    }

    public int getPlayerLimitByItem(@Nonnull Player player, @Nonnull String itemId) {
        Group group = getGroupByItem(itemId);
        if (group == null) {
            return -1;
        } else {
            return group.getPermissibleAmount(player);
        }
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setChunkContent(@Nonnull ChunkPosition chunkPosition, @Nonnull ChunkContent content) {
        contentMap.put(chunkPosition, content);
    }

    @Nonnull
    public static BlockLimiter getInstance() {
        return instance;
    }
}
