package com.github.jackstar.coronalis;

import java.io.File;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.jackstar.coronalis.discovery.DiscoveryListener;
import com.github.jackstar.coronalis.discovery.DiscoveryService;
import com.github.jackstar.coronalis.implementation.setup.ItemSetup;

/**
 * Addon Slimefun Coronalis — radiotelescopios, correlación de fase y catálogo XP.
 */
public class Coronalis extends JavaPlugin implements SlimefunAddon {

    private static Coronalis instance;
    private DiscoveryService discoveryService;

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "Coronalis-drake");
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        discoveryService = new DiscoveryService(this);

        log("§d==================================================");
        log("§5[Coronalis] §dSintonizando el array de fase...");

        getServer().getPluginManager().registerEvents(new DiscoveryListener(discoveryService), this);
        loadItems();

        log("§5[Coronalis] §aAddon cargado.");
        log("§d==================================================");
    }

    @Override
    public void onDisable() {
        log("§5[Coronalis] §cDeshabilitado.");
        instance = null;
        discoveryService = null;
    }

    @Nonnull
    public DiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/JackStar6677-1/Coronalis/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    public static Coronalis instance() {
        return instance;
    }

    public static void log(@Nonnull String message) {
        instance().getLogger().info(message);
    }

    private void loadItems() {
        try {
            ItemSetup.setup(this);
        } catch (Exception | LinkageError x) {
            instance().getLogger().log(Level.SEVERE, x, () -> "Error loading Coronalis items");
        }
    }
}
