package me.schntgaispock.wildernether;

import java.util.Random;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;

import com.github.drakescraft_labs.infinitylib.core.AbstractAddon;
import com.github.drakescraft_labs.slimefun4.libraries.dough.updater.BlobBuildUpdater;
import me.schntgaispock.wildernether.integration.GastronomiconSetup;
import me.schntgaispock.wildernether.integration.SlimeHUDSetup;
import me.schntgaispock.wildernether.listeners.Listeners;
import me.schntgaispock.wildernether.managers.ItemManager;
import me.schntgaispock.wildernether.managers.LootManager;

/**
 * See the <a href="https://github.com/SchnTgaiSpock/Wildernether/wiki">Wildernether wiki</a> for more info about the addon
 */
public class Wildernether extends AbstractAddon {

    static Wildernether instance;

    final Random random = ThreadLocalRandom.current();

    public Wildernether() {
        super("SchnTgaiSpock", "Wildernether", "master", "options.auto-update");
    }

    public static Wildernether getInstance() {
        return instance;
    }

    public Random getRandom() {
        return this.random;
    }

    @Override
    @SuppressWarnings("unused")
    public void enable() {
        instance = this;

        getLogger().info("#=======================================#");
        getLogger().info("#    Wildernether - By SchnTgaiSpock    #");
        getLogger().info("#=======================================#");
        
        if (getConfig().getBoolean("options.auto-update")) {
            if (getDescription().getVersion().startsWith("Dev - ")) {
                new BlobBuildUpdater(this, getFile(), "Wildernether", "Dev").start();
            } else {
                getLogger().info("This is an unofficial build of Wildernether, so auto updates are disabled!");
                getLogger().info("You can download the official build here: https://blob.build/project/Wildernether");
            }
        }

        ItemManager.setup();
        LootManager.setup();
        Listeners.setup();

        // Might do more with this later idk
        Metrics metrics = new Metrics(this, 15487);

        if (getInstance().getServer().getPluginManager().isPluginEnabled("SlimeHUD")) {
            try {
                getLogger().info("SlimeHUD was found on this server!");
                getLogger().info("Setting up Wildernether for SlimeHUD...");
                SlimeHUDSetup.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().warning("This server is using an old version of SlimeHUD that is incompatitable with this version of Wildernether.");
                getLogger().warning("Please update SlimeHUD to version 1.2.0 or higher!");
            }
        }

        if (getInstance().getServer().getPluginManager().isPluginEnabled("Gastronomicon")) {
            try {
                getLogger().info("Gastronomicon was found on this server!");
                getLogger().info("Setting up Wildernether for Gastronomicon...");
                GastronomiconSetup.setup();
            } catch (NoClassDefFoundError e) {
                getLogger().warning("This server is using an old version of Gastronomicon that is incompatitable with this version of Wildernether.");
                getLogger().warning("Please update Gastronomicon to version 1.1.0 or higher!");
            }
        }
    }

    @Override
    protected void disable() {
        instance = null;
    }

    public static NamespacedKey newNamespacedKey(@Nonnull String name) {
        return new NamespacedKey(Wildernether.getInstance(), name);
    }
}
