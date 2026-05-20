package io.github.sefiraat.networks;

import com.balugaq.netex.utils.Converter;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import dev.drake.dough.updater.BlobBuildUpdater;
import io.github.sefiraat.networks.commands.NetworksMain;
import io.github.sefiraat.networks.integrations.HudCallbacks;
import io.github.sefiraat.networks.integrations.NetheoPlants;
import io.github.sefiraat.networks.managers.ListenerManager;
import io.github.sefiraat.networks.managers.SupportedPluginManager;
import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.sefiraat.networks.utils.NetworkUtils;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Networks extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static final Set<Location> controllersSet = new HashSet<>();

    private static Networks instance;

    private final String username;
    private final String repo;

    private ListenerManager listenerManager;
    private SupportedPluginManager supportedPluginManager;

    public Networks() {
        this.username = "Sefiraat";
        this.repo = "Networks";
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

    @Override
  public void onEnable() {
    instance = this;

    getLogger().info("########################################");
    getLogger().info(" Networks - By Sefiraat ");
    getLogger().info(" Changed by mmmjjkx ");
    getLogger().info("########################################");

    saveDefaultConfig();
    tryUpdate();

    this.supportedPluginManager = new SupportedPluginManager();

    // Delay setupSlimefun() to ensure Slimefun is loaded first
    Bukkit.getScheduler().runTaskLater(this, this::setupSlimefun, 1L);

    this.listenerManager = new ListenerManager();
    this.getCommand("networks").setExecutor(new NetworksMain());

        // Fix dupe bug which break the network controller data without player interaction
        Bukkit.getScheduler().runTaskTimer(
                this,
                () -> {
                    Set<Location> wrongs = new HashSet<>();
                    Set<Location> controllers = new HashSet<>(
                            NetworkController.getNetworks().keySet());
                    for (Location controller : controllers) {
                        if (!(BlockStorage.check(controller) instanceof NetworkController)) {
                            wrongs.add(controller);
                        }
                    }

                    for (Location wrong : wrongs) {
                        NetworkUtils.clearNetwork(wrong);
                    }
                },
                5, Slimefun.getTickerTask().getTickRate()
        );

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Location c : controllersSet) {
                if (BlockStorage.check(c) instanceof NetworkController) {
                    Block block = c.getBlock();

                    Bukkit.getScheduler().runTask(this, () -> {
                        block.getWorld().dropItemNaturally(block.getLocation(), Converter.getItem(NetworksSlimefunItemStacks.NETWORK_CONTROLLER));
                        block.setType(Material.AIR);
                    });

                    BlockStorage.clearBlockInfo(c);
                    NetworkUtils.clearNetwork(c);
                }
            }

 controllersSet.clear();
 }, 5, 10);
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

    public void tryUpdate() {
        if (getConfig().getBoolean("auto-update") && getPluginMeta().getVersion().startsWith("Dev")) {
            new BlobBuildUpdater(this, getFile(), "Networks", "Dev").start();
        }
    }

    public void setupSlimefun() {
        NetworkSlimefunItems.setup();
        // TODO: Re-enable NetheoPlants integration once ported
        // if (supportedPluginManager.isNetheopoiesis()) {
        //     try {
        //         NetheoPlants.setup();
        //     } catch (NoClassDefFoundError e) {
        //         getLogger().severe("Netheopoiesis must be updated to meet Networks' requirements.");
        //     }
        // }
        if (supportedPluginManager.isSlimeHud()) {
            try {
                HudCallbacks.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().severe("SlimeHUD must be updated to meet Networks' requirements.");
            }
        }
    }

    public void setupMetrics() {
        final Metrics metrics = new Metrics(this, 13644);

        AdvancedPie networksChart = new AdvancedPie("networks", () -> {
            Map<String, Integer> networksMap = new HashMap<>();
            networksMap.put("Number of networks", NetworkController.getNetworks().size());
            return networksMap;
        });

        metrics.addCustomChart(networksChart);
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
}
