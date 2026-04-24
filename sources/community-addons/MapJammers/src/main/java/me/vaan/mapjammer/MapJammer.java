package com.github.drakescraft_labs.mapjammer;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.mapjammer.implementation.Setup;
import com.github.drakescraft_labs.mapjammer.runnables.CheckPlayers;
import com.github.drakescraft_labs.mapjammer.util.ConfigStorage;
import com.github.drakescraft_labs.mapjammer.util.ShowHideInterface;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.jpenilla.squaremap.api.PlayerManager;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import java.io.File;

public final class MapJammer extends JavaPlugin implements SlimefunAddon {


    @Override
    public void onEnable() {
        configure();

        startMapTask();

        ItemGroup group = new ItemGroup(new NamespacedKey(this, "map_jammer"), new CustomItemStack(Material.COMPASS, "&eMap Jamming"));
        new Setup(this, group);
    }

    private void startMapTask() {
        PluginManager pm = getServer().getPluginManager();
        // seconds * 20L (20 ticks = 1 sec)
        long period = 20L * ConfigStorage.TIME_FRAME;

        if (pm.isPluginEnabled("squaremap")) {
            Squaremap sqmp = SquaremapProvider.get();
            PlayerManager mng = sqmp.playerManager();

            ShowHideInterface showHideInterface = new ShowHideInterface() {
                @Override
                public void show(Player p) {
                    mng.show(p.getUniqueId());
                }

                @Override
                public void hide(Player p) {
                    mng.hide(p.getUniqueId());
                }
            };

            new CheckPlayers(showHideInterface).runTaskTimer(this, 0L, period);
            return;
        }

        Plugin dynmap = pm.getPlugin("dynmap");
        if (dynmap != null && dynmap.isEnabled()) {
            DynmapCommonAPI dynmapAPI = (DynmapCommonAPI) dynmap;

            ShowHideInterface showHideInterface = new ShowHideInterface() {
                @Override
                public void show(Player p) {
                    dynmapAPI.setPlayerVisiblity(p.getName(), true);
                }

                @Override
                public void hide(Player p) {
                    dynmapAPI.setPlayerVisiblity(p.getName(), false);
                }
            };

            new CheckPlayers(showHideInterface).runTaskTimer(this, 0L, period);
            return;
        }

        this.setEnabled(false);
        getLogger().warning("No supported map plugin found, MapJammers won't work.");
    }

    private void configure() {
        FileConfiguration config = this.getConfig();

        getDataFolder().mkdirs();
        if(!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();

        new ConfigStorage(config);
    }

    @Override
    public void onDisable() {

    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }
}
