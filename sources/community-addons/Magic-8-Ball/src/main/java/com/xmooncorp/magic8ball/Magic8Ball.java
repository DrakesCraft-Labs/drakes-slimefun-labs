package com.xmooncorp.magic8ball;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import com.xmooncorp.magic8ball.core.ConfigBasedLocalization;
import com.xmooncorp.magic8ball.implementation.Items;
import com.xmooncorp.magic8ball.implementation.resources.Magic8BallFragmentResource;
import com.xmooncorp.magic8ball.implementation.setup.ItemSetup;
import com.xmooncorp.magic8ball.implementation.setup.ResearchSetup;
import dev.drake.dough.config.Config;

import org.bukkit.plugin.java.JavaPlugin;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class Magic8Ball extends JavaPlugin implements SlimefunAddon {

    private static Magic8Ball instance;
    private ConfigBasedLocalization localization;
    private Config config;

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "Magic8Ball-drake");


        instance = this;
        config = new Config(this);


        loadLanguage();

        log(localization().getString("console.registering-geo"));
        registerGeoResources();

        log(localization().getString("console.loading-items"));
        loadItems();

        log(localization().getString("console.loading-researches"));
        loadResearches();

        log(localization().getString("console.addon-enabled"));

    }

    @Override
    public void onDisable() {
        log(localization().getString("console.addon-disabled"));
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return "https://github.com/xMoonGames/Magic-8-Ball/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        /*
         * You will need to return a reference to your Plugin here.
         * If you are using your main class for this, simply return "this".
         */
        return this;
    }

    @Nonnull
    public static Magic8Ball instance(){
        return instance;
    }



    public static void log(@Nonnull String message) {
        instance().getLogger().info(message);
    }

    private void loadItems() {
        try {
            ItemSetup.setup(this);
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "Error loading Magic8Ball Items");
        }
    }

    private void loadResearches() {
        try {
            ResearchSetup.setupResearches();
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "Error loading Magic8Ball researches");
        }
    }

    private void registerGeoResources() {
        new Magic8BallFragmentResource(this, Items.MAGIC_8_BALL_FRAGMENT, "magic8ball_fragment_geo_resource").register();
    }

    private void loadLanguage() {
        instance().localization = new ConfigBasedLocalization("en-US", config());
        log(localization().getString("console.loading-language"));
        log(localization().getString("console.loaded-language") + " " + localization.getName());
    }

    public ConfigBasedLocalization localization () {
        return instance().localization;
    }

    public Config config () {
        return instance().config;
    }
}
