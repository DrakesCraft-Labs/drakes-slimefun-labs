package com.github.drakescraft_labs.simpleutils;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import dev.drake.infinitylib.core.AbstractAddon;


import com.github.drakescraft_labs.simpleutils.implementation.Items;

public final class SimpleUtils extends AbstractAddon {

    public SimpleUtils(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "Mooy1", "SimpleUtils", "master", "auto-update");
    }

    public SimpleUtils() {
        super("Mooy1", "SimpleUtils", "master", "auto-update");
    }

    @Override
    protected void enable() {
        Items.setup(this);

        String ixInstalled = String.valueOf(getServer().getPluginManager().isPluginEnabled("InfinityExpansion"));
        String autoUpdates = String.valueOf(autoUpdatesEnabled());


    }

    @Override
    protected void disable() {

    }

}
