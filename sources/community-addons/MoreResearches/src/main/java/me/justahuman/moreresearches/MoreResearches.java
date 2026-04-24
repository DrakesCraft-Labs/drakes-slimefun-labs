package me.justahuman.moreresearches;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.updater.BlobBuildUpdater;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class MoreResearches extends JavaPlugin implements SlimefunAddon {
    private static MoreResearches instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new RegistryListener(), this);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        CommandCompletions<BukkitCommandCompletionContext> completions = commandManager.getCommandCompletions();
        completions.registerAsyncCompletion("slimefun_items", c -> Slimefun.getRegistry().getEnabledSlimefunItems()
                .stream().map(SlimefunItem::getId).collect(Collectors.toSet()));
        completions.registerStaticCompletion("languages", List.of("en_us", "zh_cn", "nl_nl"));

        commandManager.registerCommand(new ResearchCommands());

        if (getConfig().getBoolean("auto-update", true) && getDescription().getVersion().startsWith("Dev - ")) {
            BlobBuildUpdater updater = new BlobBuildUpdater(this, this.getFile(), "MoreResearches", "Dev");
            updater.start();
        }



    }

    @Override
    public void onDisable() {

    }

    public static MoreResearches getInstance() {
        return instance;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/JustAHuman-xD/MoreResearches/issues";
    }
}
