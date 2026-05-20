package io.github.sefiraat.networks;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import io.github.sefiraat.networks.commands.NetworksMain;
import io.github.sefiraat.networks.managers.ListenerManager;
import io.github.sefiraat.networks.managers.SupportedPluginManager;
import io.github.sefiraat.networks.integrations.HudCallbacks;
import io.github.sefiraat.networks.integrations.NetheoPlants;
import io.github.sefiraat.networks.network.SupportedRecipes;
import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Networks extends JavaPlugin implements SlimefunAddon {

    private static Networks instance;

    private final String username;
    private final String repo;
    private final String branch;

    private ListenerManager listenerManager;
    private SupportedPluginManager supportedPluginManager;

    public Networks() {
        this.username = "Sefiraat";
        this.repo = "Networks";
        this.branch = "master";
    }

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "Networks-drake");

        instance = this;

        getLogger().info("########################################");
        getLogger().info("         Networks - By Sefiraat         ");
        getLogger().info("########################################");

        saveDefaultConfig();

        this.supportedPluginManager = new SupportedPluginManager();
        setupSlimefun();

        this.listenerManager = new ListenerManager();
        this.getCommand("networks").setExecutor(new NetworksMain());

        // Initialize recipes after all items are registered
        SupportedRecipes.setup();
    }

    @Override
    public void onDisable() {
        if (instance == null) {
            return;
        }

        Bukkit.getScheduler().cancelTasks(this);
        saveData();
        instance = null;
    }

    private void saveData() {
        getLogger().info("Saving Networks data before shutdown...");

        markNetworkInventoriesDirty();

        for (org.bukkit.World world : Bukkit.getWorlds()) {
            final BlockStorage storage = BlockStorage.getStorage(world);
            if (storage != null) {
                storage.save();
            }
        }

        BlockStorage.saveChunks();
    }

    private void markNetworkInventoriesDirty() {
        for (Location location : new HashSet<>(NetworkStorage.getAllNetworkObjects().keySet())) {
            markNetworkInventoryDirty(location);
        }

        for (org.bukkit.World world : Bukkit.getWorlds()) {
            final BlockStorage storage = BlockStorage.getStorage(world);
            if (storage == null) {
                continue;
            }

            for (Location location : storage.getRawStorage().keySet()) {
                markNetworkInventoryDirty(location);
            }
        }
    }

    private void markNetworkInventoryDirty(Location location) {
        if (location == null || location.getWorld() == null) {
            return;
        }

        final SlimefunItem item = BlockStorage.check(location);
        if (item == null || !item.getId().startsWith("NTW_") || !BlockMenuPreset.isInventory(item.getId())) {
            return;
        }

        final BlockMenu menu = BlockStorage.getInventory(location);
        if (menu != null) {
            menu.markDirty();
        }
    }



    public void setupSlimefun() {
        getLogger().info("[Networks] --- Starting Slimefun Setup (v2.2) ---");

        // Step 1: Initialize ItemStacks
        NetworksSlimefunItemStacks.setup();
        getLogger().info("[Networks] NetworksSlimefunItemStacks setup complete.");

        // Step 2: Initialize SlimefunItems
        getLogger().info("[Networks] Calling NetworkSlimefunItems.setup()...");
        NetworkSlimefunItems.setup();
        getLogger().info("[Networks] NetworkSlimefunItems setup complete.");

        if (supportedPluginManager.isNetheopoiesis()) {
            try {
                NetheoPlants.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().severe("Netheopoiesis must be updated to meet Networks' requirements.");
            }
        }
        if (supportedPluginManager.isSlimeHud()) {
            try {
                HudCallbacks.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().severe("SlimeHUD must be updated to meet Networks' requirements.");
            }
        }
    }



    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues/", this.username, this.repo);
    }

    @Nonnull
    public static PluginManager getPluginManager() {
        return Networks.getInstance().getServer().getPluginManager();
    }

    public static Networks getInstance() {
        return Networks.instance;
    }

    public static SupportedPluginManager getSupportedPluginManager() {
        return Networks.getInstance().supportedPluginManager;
    }

    public static ListenerManager getListenerManager() {
        return Networks.getInstance().listenerManager;
    }
}
