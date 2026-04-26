package com.github.drakescraft_labs.bump.core.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Reemplazo de Guizhan {@code AddonConfig} que no depende de {@code AbstractAddon#getInstance()}.
 */
public final class BumpPluginYaml extends YamlConfiguration {

    private final JavaPlugin plugin;
    private final String relativePath;
    private final File file;

    public BumpPluginYaml(@Nonnull JavaPlugin plugin, @Nonnull String relativePath) {
        this.plugin = plugin;
        this.relativePath = relativePath.replace('\\', '/');
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        this.file = new File(plugin.getDataFolder(), relativePath);
        File parent = this.file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try {
            loadFromDisk();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar " + relativePath, e);
        }
    }

    private void loadFromDisk() throws IOException {
        try (InputStream defStream = plugin.getResource(relativePath)) {
            if (defStream != null) {
                YamlConfiguration defCfg = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defStream, StandardCharsets.UTF_8));
                setDefaults(defCfg);
            }
        }
        if (!file.exists()) {
            try (InputStream again = plugin.getResource(relativePath)) {
                if (again != null) {
                    plugin.saveResource(relativePath, false);
                } else {
                    if (!file.createNewFile()) {
                        throw new IOException("No se pudo crear " + file);
                    }
                }
            }
        }
        if (file.exists()) {
            try {
                load(file);
            } catch (InvalidConfigurationException e) {
                throw new IOException(e);
            }
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar " + relativePath, e);
        }
    }

    public void reload() {
        try {
            loadFromDisk();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
