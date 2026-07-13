package com.github.drakescraft_labs.slimefun4.core.services;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Local compatibility facade for the retired analytics endpoint.
 *
 * <p>Callers may continue to record timings without triggering HTTP traffic or
 * retaining player/server data outside the process.</p>
 */
public final class AnalyticsService {

    public AnalyticsService(JavaPlugin plugin) {
        // API compatibility only.
    }

    public void start() {
        // Intentionally empty: Drake builds do not run analytics.
    }

    public void recordPlayerProfileDataTime(@Nonnull String backend, boolean load, long nanoseconds) {
        // Intentionally empty: retain the public contract without exporting data.
    }
}
