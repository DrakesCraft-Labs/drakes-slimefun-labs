package me.kaiyan.missilewarfare;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.kaiyan.missilewarfare.items.CustomItems;
import me.kaiyan.missilewarfare.listeners.ExplosionEventListener;
import me.kaiyan.missilewarfare.missiles.MissileConfig;
import me.kaiyan.missilewarfare.missiles.MissileController;
import me.kaiyan.missilewarfare.integrations.TownyLoader;
import me.kaiyan.missilewarfare.integrations.WorldGuardLoader;
import me.kaiyan.missilewarfare.util.PlayerID;
import me.kaiyan.missilewarfare.util.Translations;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Main plugin class for MissileWarfare, a Slimefun addon that adds
 * surface-to-surface, surface-to-air, and anti-elytra missile systems.
 *
 * @author MissileWarfare contributors
 */
public class MissileWarfare extends JavaPlugin implements SlimefunAddon {
    public static MissileWarfare plugin;
    public static List<MissileController> activemissiles;
    public static boolean worldGuardEnabled = false;
    public static boolean townyEnabled = false;
    public static Metrics metrics;
    public static int firedMissiles = 0;
    public static int blocksExploded = 0;

    @Override
    public void onEnable() {
        int pluginId = 14904;
        metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new SingleLineChart("missiles_fired", () -> {
            int missiles = firedMissiles;
            firedMissiles = 0;
            return missiles;
        }));
        metrics.addCustomChart(new SingleLineChart("missile_destroy", () -> {
            int blocks = blocksExploded;
            blocksExploded = 0;
            return blocks;
        }));

        getLogger().info("Missile Warfare Starting Up!");

        activemissiles = new ArrayList<>();
        plugin = this;
        Config cfg = new Config(this);
        Config saveFile;
        if (!new File(this.getDataFolder() + "/saveID.yml").exists()) {
            saveFile = new Config(new File(this.getDataFolder() + "/saveID.yml"));
            saveFile.createFile();
        } else {
            saveFile = new Config(new File(this.getDataFolder() + "/saveID.yml"));
        }
        File lang = new File(getDataFolder() + "/lang");
        if (!lang.exists()) {
            generateLangPacks(lang);
        }
        try {
            Translations.setup(new Config(getDataFolder() + "/lang/" + cfg.getString("translation-pack") + ".yml"));
            PlayerID.loadPlayers(saveFile);
            MissileConfig.setup(cfg);
            CustomItems.setup();
        } catch (Exception e) {
            getLogger().warning(e.toString());
            getLogger().warning("=== !LANG PACK INVALID, REVERTING TO EN LANG PACK! ===");
            getLogger().warning("/brokenLang/ created, the invalid langpack is in there");
            lang.renameTo(new File(getDataFolder() + "/brokenLang/"));
            generateLangPacks(lang);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerID.targets = new ArrayList<>();
            }
        }.runTaskTimer(this, 20, 200);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (activemissiles.isEmpty()) {
                    for (World world : getServer().getWorlds()) {
                        for (Entity entity : world.getEntities()) {
                            if (entity.getCustomName() != null) {
                                if (entity.getCustomName().equals("MissileHolder")) {
                                    entity.remove();
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, cfg.getInt("other.cleanup-wait-time"));

        getLogger().info("Checking For Worldguard");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getServer().getPluginManager().getPlugin("WorldGuard") != null && getServer().getPluginManager().getPlugin("WorldEdit") != null) {
                    WorldGuardLoader.load();
                }
                if (getServer().getPluginManager().getPlugin("Towny") != null) {
                    TownyLoader.setup();
                }
            }
        }.runTaskLater(this, 0);

        getServer().getPluginManager().registerEvents(new ExplosionEventListener(), this);
    }

    /**
     * Returns the singleton plugin instance.
     *
     * @return the MissileWarfare instance
     */
    @Nonnull
    public static MissileWarfare getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        for (MissileController missile : activemissiles) {
            try {
                missile.armourStand.remove();
                missile.update.cancel();
            } catch (NullPointerException e) {
                try {
                    missile.update.cancel();
                } catch (NullPointerException ignored) {
                    // already cancelled
                }
            }
        }
        PlayerID.savePlayers(new Config(new File(this.getDataFolder() + "/saveID.yml")));
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    /**
     * Generates default language pack files from bundled resources
     * and moves them into the lang directory.
     *
     * @param lang the target language directory
     */
    public void generateLangPacks(@Nonnull File lang) {
        String[] loadedpacks = this.getConfig().getStringList("saved-packs").toArray(new String[0]);
        for (String pack : loadedpacks) {
            saveResource(pack + ".yml", false);
        }

        lang.mkdir();

        File datafolder = getDataFolder();
        for (File file : datafolder.listFiles()) {
            if (file.getName().startsWith("pack-")) {
                try {
                    Files.move(file.toPath(), new File(lang.getPath(), file.getName()).toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
