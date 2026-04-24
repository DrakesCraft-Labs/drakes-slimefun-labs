package com.github.drakescraft_labs.electricspawners;

import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.researches.Research;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import dev.drake.dough.config.Config;
import dev.drake.dough.updater.GitHubBuildsUpdater;

public class ElectricSpawners extends JavaPlugin implements Listener, SlimefunAddon {

    @Override
    public void onEnable() {
        Config cfg = new Config(this);

        // Setting up bStats


        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/ElectricSpawners/master").start();
        }

        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(this, "electric_spawners"), new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("db6bd9727abb55d5415265789d4f2984781a343c68dcaf57f554a5e9aa1cd")), "&9Electric Spawners"));
        Research research = new Research(new NamespacedKey(this, "electric_spawners"), 4820, "Powered Spawners", 30);

        for (String mob : cfg.getStringList("mobs")) {
            try {
                EntityType type = EntityType.valueOf(mob);
                new ElectricSpawner(itemGroup, mob, type, research).register(this);
            } catch (IllegalArgumentException x) {
                getLogger().log(Level.WARNING, "An Error has occured while adding an Electric Spawner for the (posibly outdated or invalid) EntityType \"{0}\"", mob);
            } catch (Exception x) {
                getLogger().log(Level.SEVERE, x, () -> "An Error has occured while adding an Electric Spawner for the EntityType \"" + mob + "\"");
            }
        }

        research.register();
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/ElectricSpawners/issues";
    }
}