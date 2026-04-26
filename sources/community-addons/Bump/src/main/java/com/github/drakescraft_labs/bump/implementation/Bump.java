package com.github.drakescraft_labs.bump.implementation;

import java.io.File;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.bump.core.BumpRegistry;
import com.github.drakescraft_labs.bump.core.services.ConfigUpdateService;
import com.github.drakescraft_labs.bump.core.services.LocalizationService;
import com.github.drakescraft_labs.bump.core.services.sounds.SoundService;
import com.github.drakescraft_labs.bump.implementation.setup.AppraiseSetup;
import com.github.drakescraft_labs.bump.implementation.setup.ItemGroupsSetup;
import com.github.drakescraft_labs.bump.implementation.setup.ItemsSetup;
import com.github.drakescraft_labs.bump.implementation.setup.ListenerSetup;
import com.github.drakescraft_labs.bump.implementation.setup.ResearchSetup;
import com.github.drakescraft_labs.bump.implementation.tasks.WeaponProjectileTask;
import com.github.drakescraft_labs.bump.utils.WikiUtils;
import com.github.drakescraft_labs.bump.utils.tags.BumpTag;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import com.github.drakescraft_labs.bump.core.config.BumpPluginYaml;
import net.guizhanss.guizhanlib.slimefun.addon.Scheduler;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;

import com.google.common.base.Preconditions;

/**
 * Main class for Bump (Slimefun Drake / Paper 1.21.1).
 * <p>
 * Replaces Guizhan {@code AbstractAddon} (BusyBiscuit {@code SlimefunAddon}) with {@link JavaPlugin}
 * + Drake {@link SlimefunAddon} while keeping Guizhan utilities that only depend on {@link JavaPlugin}.
 *
 * @author ybw0014
 */
public final class Bump extends JavaPlugin implements SlimefunAddon {

    private static final int SLIMEFUN_TICK_MOD = 1_000_000_007;
    private static final String DEFAULT_LANG = "en-US";
    private static final String AUTO_UPDATE_KEY = "options.auto-update";

    private static Bump instance;

    private BumpPluginYaml addonConfig;
    private Scheduler scheduler;
    private int slimefunTickCount;
    private boolean autoUpdateEnabled;

    private LocalizationService localization;
    private BumpRegistry registry;
    private SoundService soundService;

    @Nonnull
    public static Bump getInstance() {
        return Objects.requireNonNull(instance, "Bump is not enabled!");
    }

    @Nonnull
    public static BumpPluginYaml getAddonConfig() {
        return Objects.requireNonNull(getInstance().addonConfig, "Bump config is not loaded!");
    }

    @Nonnull
    public static Scheduler getScheduler() {
        return Objects.requireNonNull(getInstance().scheduler, "Bump scheduler is not initialized!");
    }

    public static int getSlimefunTickCount() {
        return getInstance().slimefunTickCount;
    }

    @Nonnull
    public static PluginCommand getPluginCommand(@Nonnull String command) {
        Preconditions.checkArgument(command != null, "command should not be null");
        return Objects.requireNonNull(getInstance().getCommand(command));
    }

    @Nonnull
    public static NamespacedKey createKey(@Nonnull String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static void log(@Nonnull Level level, @Nonnull String message, @Nullable Object... args) {
        Preconditions.checkArgument(level != null, "Log level cannot be null");
        Preconditions.checkArgument(message != null, "Log message cannot be null");
        getInstance().getLogger().log(level, ChatUtil.color(MessageFormat.format(message, args)));
    }

    public static void log(@Nonnull Level level, @Nonnull Throwable ex, @Nonnull String message, @Nullable Object... args) {
        Preconditions.checkArgument(level != null, "Log level cannot be null");
        Preconditions.checkArgument(message != null, "Log message cannot be null");
        getInstance().getLogger().log(level, ex, () -> ChatUtil.color(MessageFormat.format(message, args)));
    }

    public static void sendConsole(@Nonnull String message, @Nullable Object... args) {
        Preconditions.checkArgument(message != null, "Log message cannot be null");
        Bukkit.getConsoleSender().sendMessage("[" + getInstance().getName() + "] " + ChatUtil.color(MessageFormat.format(message, args)));
    }

    @Nonnull
    public static LocalizationService getLocalization() {
        return getInstance().localization;
    }

    @Nonnull
    public static BumpRegistry getRegistry() {
        return getInstance().registry;
    }

    @Nonnull
    public static SoundService getSoundService() {
        return getInstance().soundService;
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/SlimefunGuguProject/Bump/issues";
    }

    @Nonnull
    public String getGithubUser() {
        return "SlimefunGuguProject";
    }

    @Nonnull
    public String getGithubRepo() {
        return "Bump";
    }

    @Nonnull
    public String getGithubBranch() {
        return "main";
    }

    @Override
    public void onEnable() {
        instance = this;

        boolean brokenConfig = false;
        try {
            addonConfig = new BumpPluginYaml(this, "config.yml");
        } catch (RuntimeException e) {
            brokenConfig = true;
            e.printStackTrace();
        }

        if (addonConfig != null && addonConfig.contains(AUTO_UPDATE_KEY) && addonConfig.getBoolean(AUTO_UPDATE_KEY)) {
            autoUpdateEnabled = true;
            if (!brokenConfig) {
                autoUpdate();
            }
        }

        scheduler = new Scheduler(this);
        if (Bukkit.getPluginManager().isPluginEnabled("Slimefun")) {
            scheduler.repeat((int) Slimefun.getTickerTask().getTickRate(), () ->
                slimefunTickCount = (slimefunTickCount + 1) % SLIMEFUN_TICK_MOD);
        }

        runEnable();
    }

    private void runEnable() {
        sendConsole("&6&l  _____");
        sendConsole("&6&l /\\   _`\\             ");
        sendConsole("&6&l \\ \\ \\L\\ \\  __  __    ___ ___   _____   ");
        sendConsole("&6&l  \\ \\  _ <'/\\ \\/\\ \\ /' __` __`\\/\\ '__`\\ ");
        sendConsole("&6&l   \\ \\ \\L\\ \\ \\ \\_\\ \\/\\ \\/\\ \\/\\ \\ \\ \\L\\ \\");
        sendConsole("&6&l    \\ \\____/\\ \\____/\\ \\_\\ \\_\\ \\_\\ \\ ,__/");
        sendConsole("&6&l     \\/___/  \\/___/  \\/_/\\/_/\\/_/\\ \\ \\/ ");
        sendConsole("&6&l                                  \\ \\_\\ ");
        sendConsole("&6&l                                   \\/_/ ");
        sendConsole("&a&l  Bump 2 for Slimefun4 RC-30+");
        sendConsole("&a&l  Powered By bxx2004, SlimefunGuguProject");
        sendConsole("&a&l  GitHub: https://github.com/SlimefunGuguProject/Bump");
        sendConsole("&a&l  Issues: https://github.com/SlimefunGuguProject/Bump/issues");

        BumpPluginYaml config = getAddonConfig();
        new ConfigUpdateService(config);

        registry = new BumpRegistry(this, config);

        log(Level.INFO, "Loading language...");
        String lang = config.getString("options.lang", DEFAULT_LANG);
        localization = new LocalizationService(this);
        localization.addLanguage(lang);
        getRegistry().setLanguage(lang);
        if (!lang.equals(DEFAULT_LANG)) {
            localization.addLanguage(DEFAULT_LANG);
        }
        log(Level.INFO, "Loaded language {0}", lang);

        Slimefun slimefun = Slimefun.instance();
        if (slimefun != null && isSCSlimefun(slimefun.getPluginVersion())
            && lang.equalsIgnoreCase(DEFAULT_LANG) && !lang.startsWith("zh-")
        ) {
            log(Level.WARNING, "你似乎正在使用汉化版粘液科技，但未设置Bump的语言。");
            log(Level.WARNING, "Bump是一个支持多语言的粘液附属，默认语言为英文。");
            log(Level.WARNING, "你需要在 /plugins/Bump/config.yml 中，");
            log(Level.WARNING, "设置 options.lang 为 zh-CN 来将Bump的语言改为简体中文。");
        }

        BumpTag.reloadAll();

        soundService = new SoundService(new BumpPluginYaml(this, "sounds.yml"));
        soundService.load(true);

        AppraiseSetup.setupTypes();
        AppraiseSetup.setupStars();

        ItemGroupsSetup.setup(this);

        ItemsSetup.setup(this);

        boolean enableResearch = config.getBoolean("options.enable-research", true);
        if (enableResearch) {
            ResearchSetup.setup();
        }

        WikiUtils.setupJson();

        ListenerSetup.setup(this);

        WeaponProjectileTask.start();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        slimefunTickCount = 0;
        instance = null;
        addonConfig = null;
        scheduler = null;
    }

    private void autoUpdate() {
        try {
            Class<?> clazz = Class.forName("net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater");
            Method updaterStart = clazz.getDeclaredMethod("start", Plugin.class, File.class, String.class, String.class, String.class);
            updaterStart.invoke(null, this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch());
        } catch (Exception ignored) {
            GuizhanBuildsUpdater.start(this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch());
        }
    }

    @Nonnull
    public String getWikiURL() {
        return "https://slimefun-addons-wiki.guizhanss.cn/bump/{0}";
    }

    private boolean isSCSlimefun(@Nonnull String sfVersion) {
        return sfVersion.endsWith("-canary") || sfVersion.endsWith("-release") || sfVersion.endsWith("-Beta") || sfVersion.endsWith("-Insider");
    }

    /**
     * Guizhan {@code AbstractAddon} exposed config through {@link #getConfig()}; Bump keeps the same contract.
     */
    @Nonnull
    @Override
    public FileConfiguration getConfig() {
        return addonConfig;
    }

    @Override
    public void saveDefaultConfig() {
        // BumpPluginYaml crea el YAML desde recursos al arrancar
    }
}
