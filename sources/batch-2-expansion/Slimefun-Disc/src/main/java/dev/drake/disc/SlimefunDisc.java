package dev.drake.disc;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

import dev.drake.disc.setup.ItemSetup;
import dev.drake.disc.setup.SongLoader;

public final class SlimefunDisc extends JavaPlugin implements SlimefunAddon {

    private static SlimefunDisc instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        SongLoader.extractBundledSongs();

        getLogger().info("Registering discs with Slimefun...");
        ItemSetup.INSTANCE.init();

        Slimefun.logger().info(() -> "Slimefun-Disc enabled.");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static SlimefunDisc getInstance() {
        return instance;
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Chagui68/Slimefun6-Drakes-Fusion/issues";
    }
}