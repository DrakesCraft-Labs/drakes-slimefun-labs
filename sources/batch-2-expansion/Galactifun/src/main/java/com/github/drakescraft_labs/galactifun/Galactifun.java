package com.github.drakescraft_labs.galactifun;

import java.io.File;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.github.drakescraft_labs.galactifun.api.worlds.AlienWorld;
import com.github.drakescraft_labs.galactifun.api.worlds.PlanetaryWorld;
import com.github.drakescraft_labs.galactifun.base.BaseAlien;
import com.github.drakescraft_labs.galactifun.base.BaseItems;
import com.github.drakescraft_labs.galactifun.base.BaseMats;
import com.github.drakescraft_labs.galactifun.base.BaseUniverse;
import com.github.drakescraft_labs.galactifun.core.CoreItemGroup;
import com.github.drakescraft_labs.galactifun.core.commands.AlienRemoveCommand;
import com.github.drakescraft_labs.galactifun.core.commands.AlienSpawnCommand;
import com.github.drakescraft_labs.galactifun.core.commands.EffectsCommand;
import com.github.drakescraft_labs.galactifun.core.commands.GalactiportCommand;
import com.github.drakescraft_labs.galactifun.core.commands.SealedCommand;
import com.github.drakescraft_labs.galactifun.core.commands.StructureCommand;
import com.github.drakescraft_labs.galactifun.core.managers.AlienManager;
import com.github.drakescraft_labs.galactifun.core.managers.ProtectionManager;
import com.github.drakescraft_labs.galactifun.core.managers.WorldManager;
import dev.drake.infinitylib.common.Scheduler;
import dev.drake.infinitylib.core.AbstractAddon;

import com.github.drakescraft_labs.slimefun4.api.MinecraftVersion;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.updater.BlobBuildUpdater;
import com.github.drakescraft_labs.slimefun4.libraries.paperlib.PaperLib;


public final class Galactifun extends AbstractAddon {

    @Getter
    private static Galactifun instance;

    private boolean isTest = false;

    private AlienManager alienManager;
    private WorldManager worldManager;
    private ProtectionManager protectionManager;

    private boolean shouldDisable = false;

    public Galactifun() {
        super("Slimefun-Addon-Community", "Galactifun", "master", "auto-update");
    }

    public Galactifun(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file, "Slimefun-Addon-Community", "Galactifun", "master", "auto-update");
        isTest = true;
    }

    public static AlienManager alienManager() {
        return instance.alienManager;
    }

    public static WorldManager worldManager() {
        return instance.worldManager;
    }

    public static ProtectionManager protectionManager() {
        return instance.protectionManager;
    }

    @Override
    protected void enable() {
        instance = this;

        if (!isTest) {
            if (!PaperLib.isPaper()) {
                log(Level.SEVERE, "Galactifun only supports Paper and its forks (i.e. Airplane and Purpur)");
                log(Level.SEVERE, "Please use Paper or a fork of Paper");
                shouldDisable = true;
            }
            if (Slimefun.getMinecraftVersion().isBefore(MinecraftVersion.MINECRAFT_1_17)) {
                log(Level.SEVERE, "Galactifun only supports Minecraft 1.17 and above");
                log(Level.SEVERE, "Please use Minecraft 1.17 or above");
                shouldDisable = true;
            }
            if (Bukkit.getPluginManager().isPluginEnabled("ClayTech")) {
                log(Level.SEVERE, "Galactifun will not work properly with ClayTech");
                log(Level.SEVERE, "Please disable ClayTech");
                shouldDisable = true;
            }

            if (Bukkit.getPluginManager().isPluginEnabled("ChatColor2")) {
                log(Level.SEVERE, "Galactifun will not work properly with ChatColor2");
                log(Level.SEVERE, "Please disable ChatColor2");
                shouldDisable = true;
            }

            if (shouldDisable) {
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }



        if (!isTest && this.getConfig().getBoolean("auto-update") && !getPluginVersion().contains("MODIFIED")) {
            new BlobBuildUpdater(this, this.getFile(), "Galactifun").start();
        }

        this.alienManager = new AlienManager(this);
        this.worldManager = new WorldManager(this);
        this.protectionManager = new ProtectionManager();

        BaseAlien.setup(this.alienManager);
        if (!isTest) {
            BaseUniverse.setup(this);
        }
        CoreItemGroup.setup(this);
        BaseMats.setup();
        BaseItems.setup(this);

        // log after startup
        Scheduler.run(() -> log(Level.INFO,
                "################# Galactifun " + getPluginVersion() + " #################",
                "",
                "Galactifun is open source, you can contribute or report bugs at: ",
                getBugTrackerURL(),
                "Join the Slimefun Addon Community Discord: discord.gg/SqD3gg5SAU",
                "",
                "###################################################"
        ));

        getAddonCommand()
                .addSub(new GalactiportCommand())
                .addSub(new AlienSpawnCommand())
                .addSub(new AlienRemoveCommand())
                .addSub(new StructureCommand(this))
                .addSub(new SealedCommand())
                .addSub(new EffectsCommand());
    }

    @Override
    protected void disable() {
        if (shouldDisable) return;

        this.alienManager.onDisable();

        // Do this last
        instance = null;
    }

    @Override
    public void load() {
        if (!isTest) {
            // Default to not logging world settings
            Bukkit.spigot().getConfig().set("world-settings.default.verbose", false);
        }
    }

    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@Nonnull String worldName, @Nullable String id) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        PlanetaryWorld planetaryWorld = this.worldManager.getWorld(world);
        if (planetaryWorld instanceof AlienWorld) {
            return planetaryWorld.world().getGenerator();
        }

        return null;
    }

}
