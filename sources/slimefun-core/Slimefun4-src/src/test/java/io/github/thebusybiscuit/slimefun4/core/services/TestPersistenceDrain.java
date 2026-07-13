package io.github.thebusybiscuit.slimefun4.core.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.github.drakescraft_labs.slimefun4.core.services.PersistenceDrain;

class TestPersistenceDrain {

    @Test
    void drainsUntilQueueIsEmpty() {
        AtomicInteger pending = new AtomicInteger(3);

        int passes = PersistenceDrain.drain(5, pending::get, () -> pending.decrementAndGet());

        assertEquals(3, passes);
        assertEquals(0, pending.get());
    }

    @Test
    void stopsAtSafetyLimitWhenWritesRemainPending() {
        AtomicInteger writes = new AtomicInteger();

        int passes = PersistenceDrain.drain(2, () -> 1, writes::incrementAndGet);

        assertEquals(2, passes);
        assertEquals(2, writes.get());
    }
}
