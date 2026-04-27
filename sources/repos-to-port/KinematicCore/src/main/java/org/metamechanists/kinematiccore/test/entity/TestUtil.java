package org.metamechanists.kinematiccore.test.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;

import java.util.Random;
import java.util.concurrent.CompletableFuture;


@SuppressWarnings("WeakerAccess")
public final class TestUtil {
    private static final long EXTRA_MILLISECONDS_TO_WAIT = 100;
    private static final Random random = new Random();

    private TestUtil() {}

    public static Location findUnloadedChunk(@NotNull World world) {
        WorldBorder border = world.getWorldBorder();
        Location location;
        int max = (int) Math.min(border.getSize(), 10000);
        do  {
            location = border.getCenter().clone().add(random.nextInt(max), 1000, random.nextInt(max));
        } while (location.isChunkLoaded());
        return location;
    }

    public static boolean isChunkLoaded(@NotNull Location location) {
        // Not sure if isChunkLoaded will always return true if force loaded - I assume so but doing both to be sure
        return location.getWorld().isChunkLoaded(location.getBlockX() / 16, location.getBlockZ() / 16)
            || location.getWorld().isChunkForceLoaded(location.getBlockX() / 16, location.getBlockZ() / 16);
    }

    public static void loadChunk(@NotNull Location location) {
        runSync(() -> location.getWorld().setChunkForceLoaded(location.getBlockX() / 16, location.getBlockZ() / 16, true));

        try {
            Thread.sleep(EXTRA_MILLISECONDS_TO_WAIT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert isChunkLoaded(location);
    }

    public static void unloadChunk(@NotNull Location location) {
        runSync(() -> {
            location.getWorld().setChunkForceLoaded(location.getBlockX() / 16, location.getBlockZ() / 16, false);
            location.getWorld().unloadChunk(location.getBlockX() / 16, location.getBlockZ() / 16);
        });

        try {
            Thread.sleep(EXTRA_MILLISECONDS_TO_WAIT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert !isChunkLoaded(location);
    }

    public static void runSync(@NotNull Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(KinematicCore.instance(), () -> {
            try {
                runnable.run();
            } catch (Exception | AssertionError e) {
                future.completeExceptionally(e);
            }
            future.complete(null);
        });

        future.join();
    }
}