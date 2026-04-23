package me.EzCoins.MiniBlocks;

import com.github.drakescraft_labs.infinitylib.core.AbstractAddon;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import dev.drake.dough.updater.GitHubBuildsUpdater;
import me.EzCoins.MiniBlocks.core.Groups;
import me.EzCoins.MiniBlocks.itemsetup.ItemSetup;
import me.EzCoins.MiniBlocks.utils.CustomHead;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public final class MiniBlocks extends AbstractAddon implements SlimefunAddon {

    public static MiniBlocks plugin;

    public static MiniBlocks getInstance() {
        return plugin;
    }

    public MiniBlocks(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "TheRealEzCoins", "MiniBlocks", "master", "auto-update");
    }


    public MiniBlocks() {
        super("TheRealEzCoins", "MiniBlocks", "master", "auto-update");
    }







    @Override
    protected void enable() {
        plugin = this;
        getLogger().info("------------------------");
        getLogger().info("|         MiniBlocks ~ EzCoins          |");
        getLogger().info("------------------------");

        CustomHead.setupHead();


        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        if (getConfig().getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheRealEzCoins/MiniBlocks/master").start();
        }



        int pluginId = 15867;
        Metrics metrics = new Metrics(this, pluginId);


        Groups.setup(this);
        ItemSetup.setup(this);


    }


    @Override
    public void disable () {

        getLogger().info("Now Shutting down...");
    }

}


