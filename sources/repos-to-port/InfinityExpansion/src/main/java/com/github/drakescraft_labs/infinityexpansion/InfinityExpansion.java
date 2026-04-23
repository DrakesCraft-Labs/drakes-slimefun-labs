package com.github.drakescraft_labs.infinityexpansion;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.github.drakescraft_labs.infinityexpansion.categories.Groups;
import com.github.drakescraft_labs.infinityexpansion.commands.GiveRecipe;
import com.github.drakescraft_labs.infinityexpansion.commands.PrintItem;
import com.github.drakescraft_labs.infinityexpansion.commands.SetData;
import com.github.drakescraft_labs.infinityexpansion.items.Researches;
import com.github.drakescraft_labs.infinityexpansion.items.SlimefunExtension;
import com.github.drakescraft_labs.infinityexpansion.items.blocks.Blocks;
import com.github.drakescraft_labs.infinityexpansion.items.gear.Gear;
import com.github.drakescraft_labs.infinityexpansion.items.generators.Generators;
import com.github.drakescraft_labs.infinityexpansion.items.machines.Machines;
import com.github.drakescraft_labs.infinityexpansion.items.materials.Materials;
import com.github.drakescraft_labs.infinityexpansion.items.mobdata.MobData;
import com.github.drakescraft_labs.infinityexpansion.items.quarries.Quarries;
import com.github.drakescraft_labs.infinityexpansion.items.storage.Storage;
import com.github.drakescraft_labs.infinityexpansion.items.storage.StorageSaveFix;
import com.github.drakescraft_labs.infinitylib.common.Scheduler;
import com.github.drakescraft_labs.infinitylib.core.AbstractAddon;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

public class InfinityExpansion extends AbstractAddon {

    public InfinityExpansion(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "Mooy1", "InfinityExpansion", "master", "auto-update");
    }

    public InfinityExpansion() {
        super("Mooy1", "InfinityExpansion", "master", "auto-update");
        StorageSaveFix.fixStuff(getLogger());
    }

    @Override
    protected void enable() {
        Metrics metrics = new Metrics(this, 8991);
        String autoUpdates = String.valueOf(autoUpdatesEnabled());
        metrics.addCustomChart(new SimplePie("auto_updates", () -> autoUpdates));

        Plugin lx = getServer().getPluginManager().getPlugin("LiteXpansion");
        if (lx != null && lx.getConfig().getBoolean("options.nerf-other-addons")) {
            Scheduler.run(() -> log(Level.WARNING,
                    "########################################################",
                    "LiteXpansion nerfs energy generation in this addon.",
                    "You can disable these nerfs in the LiteXpansion config.",
                    "Under 'options:' add 'nerf-other-addons: false'",
                    "########################################################"
            ));
        }

        getAddonCommand()
                .addSub(new GiveRecipe())
                .addSub(new SetData())
                .addSub(new PrintItem());

        Groups.setup(this);
        MobData.setup(this);
        Materials.setup(this);
        Machines.setup(this);
        Quarries.setup(this);
        Gear.setup(this);
        Blocks.setup(this);
        Storage.setup(this);
        Generators.setup(this);
        SlimefunExtension.setup(this);

        if (getConfig().getBoolean("balance-options.enable-researches")) {
            Researches.setup();
        }
    }

    @Override
    public void disable() {

    }

}
