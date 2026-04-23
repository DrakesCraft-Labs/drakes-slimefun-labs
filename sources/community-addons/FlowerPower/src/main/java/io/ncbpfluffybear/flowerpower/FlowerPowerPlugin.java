package io.ncbpfluffybear.flowerpower;

import dev.drake.dough.config.Config;

import io.ncbpfluffybear.flowerpower.setup.FlowerPowerItemSetup;
import io.ncbpfluffybear.flowerpower.setup.ResearchSetup;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft-labs.slimefun4.api.SlimefunAddon;
import listeners.Events;
import utils.Utils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

/**
 * The main class of the FlowerPower addon
 *
 * @author NCBPFluffyBear
 */
public class FlowerPowerPlugin extends JavaPlugin implements SlimefunAddon {

    private static FlowerPowerPlugin instance;

    @Override
    public void onEnable() {

        instance = this;



        // Read something from your config.yml
        Config cfg = new Config(this);



        // Register events
        Utils.registerEvents(new Events());

        // Register all items
        FlowerPowerItemSetup.setup(getInstance());

        // Register all researches
        ResearchSetup.setup();
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/NCBPFluffyBear/FlowerPower/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    public static FlowerPowerPlugin getInstance() {
        return instance;
    }

}
