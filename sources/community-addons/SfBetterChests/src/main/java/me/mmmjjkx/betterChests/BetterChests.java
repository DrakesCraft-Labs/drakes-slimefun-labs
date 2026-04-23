package me.mmmjjkx.betterChests;

import io.github.schntgaispock.slimehud.SlimeHUD;
import io.github.schntgaispock.slimehud.util.HudBuilder;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.libraries.dough.updater.BlobBuildUpdater;
import me.mmmjjkx.betterChests.items.BCItems;
import me.mmmjjkx.betterChests.items.chests.SimpleDrawer;
import me.mmmjjkx.betterChests.items.chests.ie.IEStorageCache;
import me.mmmjjkx.betterChests.items.chests.ie.IEStorageUnit;
import me.mmmjjkx.betterChests.listeners.DrawerFixListener;
import me.mmmjjkx.betterChests.utils.LanguageManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class BetterChests extends JavaPlugin implements SlimefunAddon {
    public static BetterChests INSTANCE;

    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        saveConfig();

        languageManager = new LanguageManager(this);

        BCItems.registerItems();

        if (getServer().getPluginManager().isPluginEnabled("SlimeHUD")) {
            SlimeHUD.getHudController().registerCustomHandler(SimpleDrawer.class, req -> {
                SimpleDrawer d = (SimpleDrawer) req.getSlimefunItem();
                Location loc = req.getLocation();
                String itemName = LegacyComponentSerializer.legacyAmpersand().serialize(SimpleDrawer.getItemName(d.getStoringItem(loc)));
                return "&7| &f" + itemName + " &7| &f" + HudBuilder.getAbbreviatedNumber(d.getStoringItemCount(loc)) + "/" + HudBuilder.getAbbreviatedNumber(d.getCapacity());
            });

            SlimeHUD.getHudController().registerCustomHandler(IEStorageUnit.class, req -> {
                IEStorageUnit d = (IEStorageUnit) req.getSlimefunItem();
                IEStorageCache cache = d.getCache(req.getLocation());
                Location loc = req.getLocation();
                String itemName = LegacyComponentSerializer.legacyAmpersand().serialize(SimpleDrawer.getItemName(d.getDisplayingItem(loc.getBlock())));
                return "&7| &f" + itemName + " &7| &f" + HudBuilder.getAbbreviatedNumber(cache.getStored()) + "/" + HudBuilder.getAbbreviatedNumber(d.getCapacity());
            });
        }

        getServer().getPluginManager().registerEvents(new DrawerFixListener(), this);

        getLogger().info("BetterChests has been enabled.");

        if (getConfig().getBoolean("options.auto-update")) {
            BlobBuildUpdater updater = new BlobBuildUpdater(this, getFile(), "BetterChests");
            updater.start();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("BetterChests has been disabled.");
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/lijinhong11/SfBetterChests/issues";
    }

    public LanguageManager getLang() {
        return languageManager;
    }
}
