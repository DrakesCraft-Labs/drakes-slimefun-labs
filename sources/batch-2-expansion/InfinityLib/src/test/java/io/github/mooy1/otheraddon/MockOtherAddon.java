package com.github.drakescraft_labs.otheraddon;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import dev.drake.infinitylib.core.Environment;
import dev.drake.infinitylib.core.MockAddon;

public final class MockOtherAddon extends MockAddon {

    public MockOtherAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

}
