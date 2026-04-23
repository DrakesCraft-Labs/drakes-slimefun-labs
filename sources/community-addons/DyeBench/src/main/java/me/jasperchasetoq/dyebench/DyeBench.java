package me.jasperchasetoq.dyebench;

import com.github.drakescraft-labs.slimefun4.api.SlimefunAddon;
import dev.drake.dough.updater.GitHubBuildsUpdater;

import me.jasperchasetoq.dyebench.setup.DyeBenchItemSetup;

import org.bstats.bukkit.Metrics;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;

public class DyeBench extends JavaPlugin implements SlimefunAddon {


    @Override
    public void onEnable() {
        DyeBenchItemSetup.setup(this);
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        if (getConfig().getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "JasperChaseTOQ/DyeBench/master").start();

            int pluginId = 15656;
            Metrics metrics = new Metrics(this, pluginId);
        }
    }
    @Override
    public void onDisable() {
    }
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/JasperChaseTOQ/DyeBench/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    private static DyeBench instance;

    public DyeBench() {
        instance = this;
    }

    public static DyeBench getInstance() {
        return instance;
    }
}
