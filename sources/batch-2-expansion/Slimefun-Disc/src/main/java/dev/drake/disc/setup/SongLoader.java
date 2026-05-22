package dev.drake.disc.setup;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;

import org.bukkit.plugin.java.JavaPlugin;

import dev.drake.disc.NBSParser;
import dev.drake.disc.NBSParser.NBSong;
import dev.drake.disc.SlimefunDisc;

public final class SongLoader {

    private static final Map<String, NBSong> songs = new HashMap<>();

    private SongLoader() {}

    public static void extractBundledSongs() {
        JavaPlugin plugin = SlimefunDisc.getInstance();
        File songsFolder = new File(plugin.getDataFolder(), "songs");

        if (!songsFolder.exists()) {
            songsFolder.mkdirs();
        }

        try {
            URI uri = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            Path jarPath;
            if (uri.getScheme().equals("jar")) {
                jarPath = Path.of(URI.create(uri.toString().substring(0, uri.toString().indexOf('!'))));
            } else {
                jarPath = Path.of(uri);
            }

            if (Files.isDirectory(jarPath)) {
                Path songsDir = jarPath.resolve("songs");
                if (Files.exists(songsDir)) {
                    try (Stream<Path> stream = Files.list(songsDir)) {
                        stream.filter(p -> p.toString().toLowerCase().endsWith(".nbs"))
                            .forEach(p -> {
                                File target = new File(songsFolder, p.getFileName().toString());
                                if (!target.exists()) {
                                    try {
                                        Files.copy(p, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                        plugin.getLogger().info("Extracted bundled song: " + p.getFileName());
                                    } catch (IOException e) {
                                        plugin.getLogger().log(Level.WARNING, "Failed to extract " + p.getFileName(), e);
                                    }
                                }
                            });
                    }
                }
                return;
            }

            try (FileSystem fs = FileSystems.newFileSystem(jarPath, (ClassLoader) null)) {
                Path songsInJar = fs.getPath("songs");
                if (!Files.exists(songsInJar)) return;

                try (Stream<Path> stream = Files.list(songsInJar)) {
                    stream.filter(p -> p.toString().toLowerCase().endsWith(".nbs"))
                        .forEach(p -> {
                            String filename = p.getFileName().toString();
                            File target = new File(songsFolder, filename);
                            if (!target.exists()) {
                                try {
                                    Files.copy(p, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    plugin.getLogger().info("Extracted bundled song: " + filename);
                                } catch (IOException e) {
                                    plugin.getLogger().log(Level.WARNING, "Failed to extract " + filename, e);
                                }
                            }
                        });
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Could not extract bundled songs", e);
        }
    }

    public static NBSong loadSong(String key, String filename) {
        JavaPlugin plugin = SlimefunDisc.getInstance();
        File songsFolder = new File(plugin.getDataFolder(), "songs");

        File songFile = new File(songsFolder, filename);
        if (!songFile.exists()) {
            plugin.getLogger().warning("Song file not found: " + filename + " (for key: " + key + ")");
            return null;
        }

        try {
            NBSong song = NBSParser.parse(songFile);
            songs.put(key.toLowerCase(), song);
            plugin.getLogger().info("Loaded song: " + song.getName() + " (from " + filename + ")");
            return song;
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to parse " + filename, e);
            return null;
        }
    }

    public static NBSong getSong(String key) {
        return songs.get(key.toLowerCase());
    }

    public static Map<String, NBSong> getAllSongs() {
        return Map.copyOf(songs);
    }
}
