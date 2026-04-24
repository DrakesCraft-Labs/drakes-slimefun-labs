package me.apeiros.alchimiavitae;

import dev.drake.infinitylib.core.AbstractAddon;
import dev.drake.infinitylib.metrics.bukkit.Metrics;

import me.apeiros.alchimiavitae.setup.Setup;

/**
 * Main class
 */
public class AlchimiaVitae extends AbstractAddon {

    private static AlchimiaVitae instance;

    public AlchimiaVitae() {
        super("Apeiros-46B", "AlchimiaVitae", "master", "options.auto-update");
    }

    @Override
    public void enable() {
        // Set instance
        instance = this;

        // Set up items and listeners
        Setup.setup(this);

        // bStats
        
    }

    @Override
    public void disable() {
        // Set instance to null
        instance = null;
    }

    public static AlchimiaVitae i() {
        return instance;
    }

}
