package com.github.drakescraft_labs.gcereborn;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import io.papermc.lib.PaperLib;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import dev.drake.dough.updater.BlobBuildUpdater;

import com.github.drakescraft_labs.gcereborn.core.commands.GCECommand;
import com.github.drakescraft_labs.gcereborn.core.services.ConfigurationService;
import com.github.drakescraft_labs.gcereborn.core.services.IntegrationService;
import com.github.drakescraft_labs.gcereborn.core.services.LocalizationService;
import com.github.drakescraft_labs.gcereborn.setup.Items;
import com.github.drakescraft_labs.gcereborn.setup.Researches;
import com.github.drakescraft_labs.gcereborn.utils.SimpleProfiler;
import com.github.drakescraft_labs.guizhanlib.slimefun.addon.AbstractAddon;
import com.github.drakescraft_labs.guizhanlib.updater.GuizhanBuildsUpdater;

import org.bstats.bukkit.Metrics;

public class GeneticChickengineering extends AbstractAddon {

    private static final String DEFAULT_LANG = "en-US";

    private ConfigurationService configService;
    private LocalizationService localization;
    private IntegrationService integrationService;
    private boolean debugEnabled = false;

    public GeneticChickengineering() {
        super("ybw0014", "GeneticChickengineering-Reborn", "master", "options.auto-update");
    }

    @Nonnull
    public static ConfigurationService getConfigService() {
        return inst().configService;
    }

    @Nonnull
    public static LocalizationService getLocalization() {
        return inst().localization;
    }

    @Nonnull
    public static IntegrationService getIntegrationService() {
        return inst().integrationService;
    }

    public static void debug(@Nonnull String message, @Nonnull Object... args) {
        Preconditions.checkNotNull(message, "message cannot be null");

        if (inst().debugEnabled) {
            inst().getLogger().log(Level.INFO, "[DEBUG] " + message, args);
        }
    }

    @Nonnull
    private static GeneticChickengineering inst() {
        return getInstance();
    }

    @Override
    public void enable() {
        File datadir = this.getDataFolder();
        if (!datadir.exists()) {
            datadir.mkdirs();
        }

        // config
        configService = new ConfigurationService(this);

        // debug
        debugEnabled = configService.isDebug();

        // localization
        log(Level.INFO, "Loading language...");
        String lang = configService.getLang();
        localization = new LocalizationService(this);
        localization.addLanguage(lang);
        if (!lang.equals(DEFAULT_LANG)) {
            localization.addLanguage(DEFAULT_LANG);
        }
        localization.setIdPrefix("GCE_");
        log(Level.INFO, localization.getString("console.load.language"), lang);

        // paper check
        if (!PaperLib.isPaper()) {
            log(Level.SEVERE, localization.getString("console.paper-only"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // items
        log(Level.INFO, localization.getString("console.load.items"));
        Items.setup(this);

        // researches
        log(Level.INFO, localization.getString("console.load.researches"));
        Researches.setup();

        // listeners

        // commands
        if (configService.isCommandsEnabled()) {
            PluginCommand command = getCommand("geneticchickengineering");
            if (command == null) {
                log(Level.SEVERE, localization.getString("console.load.commands-fail"));
            } else {
                new GCECommand(command).register();
            }
        }

        // integrations
        log(Level.INFO, localization.getString("console.load.integrations"));
        integrationService = new IntegrationService(this);

        if (configService.isProfilerEnabled()) {
            SimpleProfiler.startReporter(this);
        }

        // metrics
        setupMetrics();
    }

    @Override
    public void disable() {
        // do nothing
    }

    private void setupMetrics() {
        new Metrics(this, 20243);
    }

    @Override
    protected void autoUpdate() {
        if (getPluginVersion().startsWith("Dev")) {
            new BlobBuildUpdater(this, getFile(), getGithubRepo()).start();
        } else if (getPluginVersion().startsWith("Build")) {
            try {
                // use updater in lib plugin
                Class<?> clazz = Class.forName("com.github.drakescraft_labs.guizhanlibplugin.updater.GuizhanUpdater");
                Method updaterStart = clazz.getDeclaredMethod("start", Plugin.class, File.class, String.class, String.class, String.class);
                updaterStart.invoke(null, this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch());
            } catch (Exception ignored) {
                // use updater in lib
                new GuizhanBuildsUpdater(this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch()).start();
            }
        }
    }
}
