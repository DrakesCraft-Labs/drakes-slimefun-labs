package org.metamechanists.kinematiccore.api;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;


// Adapted from Dough's BlockPosition
public record ChunkPosition(long position) {
    public ChunkPosition(int x, int z) {
        this((((long) x) << 32) | (z & 0xFFFFFFFFL));
    }

    public ChunkPosition(@NotNull Chunk chunk) {
        this(chunk.getX(), chunk.getZ());
    }

    public ChunkPosition(@NotNull Location location) {
        this(location.getChunk());
    }

    public ChunkPosition(@NotNull Block block) {
        this(block.getChunk());
    }

    public int getX() {
        return (int) (position >> 32);
    }

    public int getZ() {
        return (int) position;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(position);
    }
}
