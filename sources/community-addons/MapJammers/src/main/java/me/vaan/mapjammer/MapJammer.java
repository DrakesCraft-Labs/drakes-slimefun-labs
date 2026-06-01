package me.vaan.mapjammer;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import me.vaan.mapjammer.implementation.Setup;
import me.vaan.mapjammer.runnables.CheckPlayers;
import me.vaan.mapjammer.util.ConfigStorage;
import me.vaan.mapjammer.util.ShowHideInterface;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.jpenilla.squaremap.api.PlayerManager;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import java.io.File;
import java.lang.reflect.Method;

public final class MapJammer extends JavaPlugin implements SlimefunAddon {

    private volatile boolean mapTaskStarted;

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "MapJammer-drake");

        configure();

        ItemGroup group = new ItemGroup(new NamespacedKey(this, "map_jammer"), new CustomItemStack(Material.COMPASS, "&eMap Jamming"));
        new Setup(this, group);

        tryStartMapTask();
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPluginEnable(PluginEnableEvent e) {
                String n = e.getPlugin().getName();
                if ("squaremap".equalsIgnoreCase(n) || "dynmap".equalsIgnoreCase(n)) {
                    tryStartMapTask();
                }
            }
        }, this);

        new BukkitRunnable() {
            int ticks;

            @Override
            public void run() {
                if (mapTaskStarted) {
                    cancel();
                    return;
                }
                tryStartMapTask();
                ticks += 100;
                if (ticks > 20 * 120) {
                    cancel();
                    getLogger().warning("No se detectó squaremap ni dynmap tras 120s. Los jammers no ocultarán en el mapa hasta que esos plugins estén cargados.");
                }
            }
        }.runTaskTimer(this, 20L, 100L);
    }

    private synchronized void tryStartMapTask() {
        if (mapTaskStarted) {
            return;
        }
        PluginManager pm = getServer().getPluginManager();
        long period = 20L * ConfigStorage.TIME_FRAME;

        if (pm.isPluginEnabled("squaremap")) {
            try {
                Squaremap sqmp = SquaremapProvider.get();
                PlayerManager mng = sqmp.playerManager();
                ShowHideInterface iface = new ShowHideInterface() {
                    @Override
                    public void show(Player p) {
                        mng.show(p.getUniqueId());
                    }

                    @Override
                    public void hide(Player p) {
                        mng.hide(p.getUniqueId());
                    }
                };
                new CheckPlayers(iface).runTaskTimer(this, 0L, period);
                mapTaskStarted = true;
                getLogger().info("MapJammer enlazado a squaremap.");
            } catch (Throwable t) {
                getLogger().warning("squaremap está habilitado pero la API no respondió: " + t.getMessage());
            }
            return;
        }

        Plugin dynmap = pm.getPlugin("dynmap");
        if (dynmap != null && dynmap.isEnabled()) {
            try {
                Method visibilityMethod = findDynmapVisibilityMethod(dynmap);
                ShowHideInterface iface = new ShowHideInterface() {
                    @Override
                    public void show(Player p) {
                        setDynmapVisibility(dynmap, visibilityMethod, p, true);
                    }

                    @Override
                    public void hide(Player p) {
                        setDynmapVisibility(dynmap, visibilityMethod, p, false);
                    }
                };
                new CheckPlayers(iface).runTaskTimer(this, 0L, period);
                mapTaskStarted = true;
                getLogger().info("MapJammer enlazado a dynmap.");
            } catch (Throwable t) {
                getLogger().warning("dynmap está habilitado pero la API falló: " + t.getMessage());
            }
        }
    }

    // Dynmap mantiene el nombre antiguo con typo en varias versiones, pero algunos forks corrigen la firma.
    private Method findDynmapVisibilityMethod(Plugin dynmap) throws NoSuchMethodException {
        try {
            return dynmap.getClass().getMethod("setPlayerVisiblity", String.class, boolean.class);
        } catch (NoSuchMethodException ignored) {
            return dynmap.getClass().getMethod("setPlayerVisibility", String.class, boolean.class);
        }
    }

    // La API se invoca por reflexión para no depender de un repositorio Maven inestable durante CI.
    private void setDynmapVisibility(Plugin dynmap, Method visibilityMethod, Player player, boolean visible) {
        try {
            visibilityMethod.invoke(dynmap, player.getName(), visible);
        } catch (ReflectiveOperationException | RuntimeException e) {
            getLogger().warning("No se pudo actualizar visibilidad dynmap para " + player.getName() + ": " + e.getMessage());
        }
    }

    private void configure() {
        FileConfiguration config = this.getConfig();
        getDataFolder().mkdirs();
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
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
