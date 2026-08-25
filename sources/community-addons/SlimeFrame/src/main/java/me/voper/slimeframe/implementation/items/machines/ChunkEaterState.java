package me.voper.slimeframe.implementation.items.machines;

import com.google.common.collect.PeekingIterator;

/**
 * Small, side-effect-free state checks for Chunk Eater runtime recovery.
 */
final class ChunkEaterState {

    private ChunkEaterState() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Treats a missing volatile iterator exactly like a completed machine rather than crashing a tick.
     */
    static boolean isMissingOrExhausted(PeekingIterator<?> iterator) {
        return iterator == null || !iterator.hasNext();
    }
}
