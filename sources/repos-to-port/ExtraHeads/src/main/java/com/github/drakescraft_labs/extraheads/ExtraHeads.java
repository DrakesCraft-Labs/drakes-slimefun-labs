package com.github.drakescraft_labs.extraheads;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import javax.annotation.Nonnull;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.extraheads.listeners.HeadListener;
import com.github.drakescraft_labs.extraheads.setup.ItemSetup;
import com.github.drakescraft_labs.extraheads.setup.Registry;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import dev.drake.dough.config.Config;
import dev.drake.dough.updater.BlobBuildUpdater;

import lombok.Getter;

public class ExtraHeads extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static ExtraHeads instance;

    private Registry registry;

    public static Registry getRegistry() {
        return getInstance().registry;
    }

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "ExtraHeads-drake");

        instance = this;

        // registry and config
        registry = new Registry(new Config(this));

        // Setting up bStats


        if (registry.getConfig().getBoolean("options.auto-update") && getPluginVersion().startsWith("Dev")) {
            new BlobBuildUpdater(this, getFile(), "ExtraHeads").start();
        }

        ItemSetup.setup();

        new HeadListener(this);
    }

    @Override
    @Nonnull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    @Nonnull
    public String getBugTrackerURL() {
        return "https://github.com/Slimefun-Addon-Community/ExtraHeads/issues";
    }
}
