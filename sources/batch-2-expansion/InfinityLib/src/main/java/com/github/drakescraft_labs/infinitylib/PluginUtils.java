package com.github.drakescraft_labs.infinitylib;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Compatibility class for InfinityLib PluginUtils.
 */
public final class PluginUtils {

    private PluginUtils() {}

    public static void setup(String prefix, JavaPlugin plugin, String repo, File file) {
        // Simplified setup for legacy compatibility
        plugin.getLogger().info("PluginUtils setup called for " + prefix);
    }
}
