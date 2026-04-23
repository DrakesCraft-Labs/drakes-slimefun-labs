package com.github.drakescraft-labs.slimefun4.utils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;

import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotResult;

/**
 * Utility class for PaperLib-related features that are not available in the standard PaperLib.
 * This mainly provides a synchronous way to get a block state snapshot.
 *
 * @author TheBusyBiscuit
 */
public final class PaperLibUtils {

    private PaperLibUtils() {}

    /**
     * Returns a {@link BlockStateSnapshotResult} for the given {@link Block}.
     * This is a synchronous operation.
     *
     * @param block
     *            The {@link Block} to get the state from
     * @param useSnapshot
     *            Whether to use a snapshot or not
     * @return A {@link BlockStateSnapshotResult}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {
        return new BlockStateSnapshotResult(useSnapshot, block.getState(useSnapshot));
    }
}
