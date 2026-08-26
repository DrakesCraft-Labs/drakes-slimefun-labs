package io.ncbpfluffybear.fluffymachines.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

class EventsTest {

    @Test
    void warpParticlesSupplyDragonBreathFloatData() {
        AtomicReference<Object[]> spawnArguments = new AtomicReference<>();
        Player player = (Player) Proxy.newProxyInstance(
            Player.class.getClassLoader(),
            new Class<?>[] {Player.class},
            (proxy, method, arguments) -> {
                if (method.getName().equals("spawnParticle") && arguments != null && arguments.length == 4) {
                    spawnArguments.set(arguments);
                }
                return null;
            }
        );
        Location location = new Location(null, 1, 2, 3);

        Events.spawnWarpParticles(player, location);

        Object[] arguments = spawnArguments.get();
        assertSame(Particle.DRAGON_BREATH, arguments[0]);
        assertSame(location, arguments[1]);
        assertEquals(10, arguments[2]);
        assertEquals(1.0F, arguments[3]);
    }
}
