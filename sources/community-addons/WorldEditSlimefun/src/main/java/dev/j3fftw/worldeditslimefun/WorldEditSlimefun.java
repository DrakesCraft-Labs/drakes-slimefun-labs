package dev.j3fftw.worldeditslimefun;

import dev.j3fftw.worldeditslimefun.commands.WorldEditSlimefunCommands;
import dev.j3fftw.worldeditslimefun.listeners.RegistryListener;
import dev.j3fftw.worldeditslimefun.slimefun.Items;
import dev.j3fftw.worldeditslimefun.listeners.WandListener;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.libraries.dough.updater.BlobBuildUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;

public final class WorldEditSlimefun extends JavaPlugin implements SlimefunAddon {

    private static WorldEditSlimefun instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        if (getConfig().getBoolean("auto-update", true) && getDescription().getVersion().startsWith("DEV - ")) {
            new BlobBuildUpdater(this, getFile(), "WorldEditSlimefun", "Dev").start();
        }



        Items.init(this);
        WorldEditSlimefunCommands.init(this);

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new WandListener(), this);
        manager.registerEvents(new RegistryListener(), this);
    }

    @Override
    public void onDisable() {}

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Slimefun-Addon-Community/WorldEditSlimefun/issues";
    }

    public static WorldEditSlimefun getInstance() {
        return instance;
    }
}
