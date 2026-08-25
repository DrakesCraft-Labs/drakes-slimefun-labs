package io.github.sefiraat.crystamaehistoria.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigManagerTest {

    /**
     * Player research must still persist even when the addon base configuration
     * cannot be accessed during the plugin shutdown order.
     */
    @Test
    void savesPlayerStatsWithoutAccessingAddonBaseConfig(@TempDir Path temporaryDirectory) {
        YamlConfiguration playerStats = new YamlConfiguration();
        playerStats.set("player-uuid.researches.arcane", true);
        File destination = temporaryDirectory.resolve("player_stats.yml").toFile();

        boolean saved = ConfigManager.savePlayerStats(playerStats, destination, Logger.getLogger("test"));

        assertTrue(saved);
        assertTrue(YamlConfiguration.loadConfiguration(destination)
            .getBoolean("player-uuid.researches.arcane"));
    }
}
