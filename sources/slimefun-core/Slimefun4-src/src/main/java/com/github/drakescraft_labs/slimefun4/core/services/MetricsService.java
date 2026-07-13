package com.github.drakescraft_labs.slimefun4.core.services;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

/**
 * Compatibility facade for the retired MetricsModule integration.
 *
 * <p>The Drake distribution never downloads modules or sends metrics. Keeping
 * this type prevents binary breakage for callers compiled against Slimefun's
 * public API while making the no-network policy explicit and testable.</p>
 */
public final class MetricsService {

    public MetricsService(@Nonnull Slimefun plugin) {
        // Kept for API compatibility. The plugin is deliberately not retained.
    }

    /** Starts no external service. */
    public void start() {
        // Intentionally empty: no automatic updates and no telemetry.
    }

    /** Releases no external resources. */
    public void cleanUp() {
        // Intentionally empty.
    }

    /** Automatic metrics updates are not supported in Drake builds. */
    public boolean checkForUpdate(@Nullable String currentVersion) {
        return false;
    }

    /** No MetricsModule is downloaded or loaded. */
    @Nullable
    public String getVersion() {
        return null;
    }

    /** Automatic metrics updates are permanently disabled. */
    public boolean hasAutoUpdates() {
        return false;
    }
}
