package com.github.jackstar.coronalis;

import java.io.File;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.jackstar.coronalis.discovery.DiscoveryListener;
import com.github.jackstar.coronalis.discovery.DiscoveryService;
import com.github.jackstar.coronalis.commands.CoronalisCommand;
import com.github.jackstar.coronalis.implementation.items.ControlConsole;
import com.github.jackstar.coronalis.implementation.setup.ItemSetup;
import com.github.jackstar.coronalis.managers.AccessManager;
import com.github.jackstar.coronalis.managers.CosmicEventManager;
import com.github.jackstar.coronalis.managers.NetworkRegistry;
import com.github.jackstar.coronalis.managers.SoundManager;

/**
 * Addon Slimefun Coronalis — radiotelescopios, correlación de fase y catálogo XP.
 */
public class Coronalis extends JavaPlugin implements SlimefunAddon {

    private static Coronalis instance;
    private DiscoveryService discoveryService;
    private CosmicEventManager cosmicEventManager;
    private AccessManager accessManager;
    private NetworkRegistry networkRegistry;
    private SoundManager soundManager;

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "Coronalis-drake");
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        discoveryService = new DiscoveryService(this);
        cosmicEventManager = new CosmicEventManager();
        accessManager = new AccessManager();
        networkRegistry = new NetworkRegistry();
        soundManager = new SoundManager();

        log("§d==================================================");
        log("§5[Coronalis] §dSintonizando el array de fase...");

        getServer().getPluginManager().registerEvents(new DiscoveryListener(discoveryService), this);
        accessManager.register();
        if (getCommand("coronalis") != null) {
            getCommand("coronalis").setExecutor(new CoronalisCommand());
        }
        loadItems();
        cosmicEventManager.start();
        getServer().getScheduler().runTaskTimer(this, ControlConsole::tickAll, 40L, 40L);
        getServer().getScheduler().runTaskTimer(this, () -> networkRegistry.tickAll(), 80L, 80L);

        log("§5[Coronalis] §aAddon cargado.");
        log("§d==================================================");
    }

    @Override
    public void onDisable() {
        if (cosmicEventManager != null) {
            cosmicEventManager.stop();
        }
        if (soundManager != null) {
            soundManager.stopAll();
        }
        log("§5[Coronalis] §cDeshabilitado.");
        instance = null;
        discoveryService = null;
        cosmicEventManager = null;
        accessManager = null;
        networkRegistry = null;
        soundManager = null;
    }

    @Nonnull
    public DiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    @Nonnull
    public CosmicEventManager getCosmicEventManager() {
        return cosmicEventManager;
    }

    @Nonnull
    public AccessManager getAccessManager() {
        return accessManager;
    }

    @Nonnull
    public NetworkRegistry getNetworkRegistry() {
        return networkRegistry;
    }

    @Nonnull
    public SoundManager getSoundManager() {
        return soundManager;
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
