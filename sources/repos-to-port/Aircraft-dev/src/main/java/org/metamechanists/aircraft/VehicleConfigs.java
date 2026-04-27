package org.metamechanists.aircraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Installs or refreshes bundled vehicle YAML under {@code plugins/Aircraft/vehicles/}.
 * <p>
 * Older files (e.g. scalar {@code engine.force}) break MetaLib parsing; bump
 * {@link #SCHEMA_REVISION} to push updated defaults from the jar again.
 */
public final class VehicleConfigs {
    /**
     * Bump when packaged vehicle YAML changes in a breaking way for on-disk copies.
     */
    private static final int SCHEMA_REVISION = 1;

    private static final String[] BUNDLED_VEHICLE_IDS = {
            "crude_airplane",
            "crude_airship",
            "crude_drone",
            "cessna",
            "hoverduck",
            "metacoin_ufo",
    };

    private VehicleConfigs() {}

    public static void installBundledVehicleConfigs(JavaPlugin plugin) {
        Path vehiclesDir = plugin.getDataFolder().toPath().resolve("vehicles");
        Path revPath = vehiclesDir.resolve(".schema_revision");
        int onDiskRevision = 0;
        try {
            if (Files.isRegularFile(revPath)) {
                String text = Files.readString(revPath).trim();
                if (!text.isEmpty()) {
                    onDiskRevision = Integer.parseInt(text);
                }
            }
        } catch (IOException | NumberFormatException ignored) {
            onDiskRevision = 0;
        }

        boolean forceUpdate = onDiskRevision < SCHEMA_REVISION;
        try {
            Files.createDirectories(vehiclesDir);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not create vehicles directory: " + e.getMessage());
        }

        for (String id : BUNDLED_VEHICLE_IDS) {
            String resourcePath = "vehicles/" + id + ".yml";
            try {
                if (forceUpdate) {
                    plugin.saveResource(resourcePath, true);
                } else if (!Files.isRegularFile(vehiclesDir.resolve(id + ".yml"))) {
                    plugin.saveResource(resourcePath, false);
                }
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().severe("Missing bundled vehicle resource: " + resourcePath);
            }
        }

        if (forceUpdate) {
            try {
                Files.writeString(revPath, Integer.toString(SCHEMA_REVISION));
            } catch (IOException e) {
                plugin.getLogger().warning("Could not write vehicles/.schema_revision: " + e.getMessage());
            }
            plugin.getLogger().info("Aircraft: refreshed bundled vehicle YAML (schema revision " + SCHEMA_REVISION + ").");
        }
    }
}
