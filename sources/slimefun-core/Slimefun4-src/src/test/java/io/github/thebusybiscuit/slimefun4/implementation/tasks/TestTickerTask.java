package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.drakescraft_labs.slimefun4.implementation.tasks.TickerTask;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

class TestTickerTask {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testTickerLocationsTolerateConcurrentMutation() {
        World world = server.addSimpleWorld("ticker_world");
        TickerTask task = new TickerTask();

        Location first = new Location(world, 1, 64, 1);
        Location second = new Location(world, 2, 64, 1);
        Location third = new Location(world, 3, 64, 1);

        task.enableTicker(first);
        task.enableTicker(second);

        Chunk chunk = world.getChunkAt(0, 0);
        Set<Location> locations = task.getLocations(chunk);
        Iterator<Location> iterator = locations.iterator();

        Assertions.assertTrue(iterator.hasNext());
        task.disableTicker(first);
        task.enableTicker(third);

        Assertions.assertDoesNotThrow(() -> {
            while (iterator.hasNext()) {
                iterator.next();
            }
        });
    }
}
