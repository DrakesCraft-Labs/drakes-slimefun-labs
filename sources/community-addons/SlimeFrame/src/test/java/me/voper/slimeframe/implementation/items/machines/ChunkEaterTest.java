package me.voper.slimeframe.implementation.items.machines;

import com.google.common.collect.Iterators;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChunkEaterTest {

    /**
     * A server restart clears the iterator map, so the machine must take the safe recovery path.
     */
    @Test
    void missingIteratorIsHandledWithoutDereference() {
        assertTrue(ChunkEaterState.isMissingOrExhausted(null));
    }

    /**
     * A restored iterator remains usable until every pending block was processed.
     */
    @Test
    void restoredIteratorRetainsItsCompletionState() {
        var iterator = Iterators.peekingIterator(List.of("pending").iterator());

        assertFalse(ChunkEaterState.isMissingOrExhausted(iterator));
        iterator.next();
        assertTrue(ChunkEaterState.isMissingOrExhausted(iterator));
    }
}
