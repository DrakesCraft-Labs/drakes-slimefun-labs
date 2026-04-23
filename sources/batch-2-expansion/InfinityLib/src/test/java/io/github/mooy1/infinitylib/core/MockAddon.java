package com.github.drakescraft_labs.infinitylib.core;

import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

public class MockAddon extends AbstractAddon {

    private static MockAddonTest testCase;

    public static void setTestCase(MockAddonTest test) {
        testCase = test;
    }

    public MockAddon() {
        super("Mooy1", "InfinityLib", 
                testCase == MockAddonTest.BAD_GITHUB_PATH ? "[!#$" : "master",
                testCase == MockAddonTest.MISSING_KEY ? "missing" : "auto-update");
        MockBukkit.load(Slimefun.class);
    }

    public MockAddon(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file, "Mooy1", "InfinityLib",
                testCase == MockAddonTest.BAD_GITHUB_PATH ? "[!#$" : "master",
                testCase == MockAddonTest.MISSING_KEY ? "missing" : "auto-update", Environment.LIBRARY_TESTING);
        MockBukkit.load(Slimefun.class);
    }

    @Override
    protected void load() {
        if (testCase == MockAddonTest.THROW_EXCEPTION) {
            throw new RuntimeException();
        }
        else if (testCase == MockAddonTest.CALL_SUPER) {
            super.onLoad();
        }
    }

    @Override
    protected void enable() {
        if (testCase == MockAddonTest.THROW_EXCEPTION) {
            throw new RuntimeException();
        }
        else if (testCase == MockAddonTest.CALL_SUPER) {
            super.onEnable();
        }
    }

    @Override
    protected void disable() {
        if (testCase == MockAddonTest.THROW_EXCEPTION) {
            throw new RuntimeException();
        }
        else if (testCase == MockAddonTest.CALL_SUPER) {
            super.onDisable();
        }
    }

}
