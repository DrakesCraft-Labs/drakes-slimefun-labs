package com.github.drakescraft_labs.mobcapturer;

import javax.annotation.Nonnull;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.mobcapturer.listeners.MobCaptureListener;
import com.github.drakescraft_labs.mobcapturer.listeners.PelletListener;
import com.github.drakescraft_labs.mobcapturer.setup.Registry;
import com.github.drakescraft_labs.mobcapturer.setup.Setup;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.libraries.dough.config.Config;
import com.github.drakescraft_labs.slimefun4.libraries.dough.updater.BlobBuildUpdater;

/**
 * MobCapturer Slimefun addon
 *
 * @author TheBusyBiscuit
 * @author ybw0014
 */
public class MobCapturer extends JavaPlugin implements SlimefunAddon {

    private static MobCapturer instance;

    private Registry registry;

    @Nonnull
    public static MobCapturer getInstance() {
        return instance;
    }

    private static void setInstance(@Nonnull MobCapturer plugin) {
        instance = plugin;
    }

    @Nonnull
    public static Registry getRegistry() {
        return getInstance().registry;
    }

    @Override
    public void onEnable() {
        setInstance(this);

        Config cfg = new Config(this);


        if (cfg.getBoolean("options.auto-update") && getPluginVersion().startsWith("Dev")) {
            new BlobBuildUpdater(this, getFile(), "MobCapturer").start();
        }

        registry = new Registry(cfg);

        Setup.setup();

        // listeners
        new PelletListener(this);
        new MobCaptureListener(this);
    }

    @Override
    @Nonnull
    public String getBugTrackerURL() {
        return "https://github.com/Slimefun-Addon-Community/MobCapturer/issues";
    }

    @Override
    @Nonnull
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
