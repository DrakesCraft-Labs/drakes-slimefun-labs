package me.jasperchasetoq.compressioncraft;

import com.github.drakescraft-labs.infinitylib.core.AbstractAddon;
import com.github.drakescraft-labs.infinitylib.metrics.bukkit.Metrics;
import com.github.drakescraft-labs.slimefun4.libraries.dough.config.Config;
import me.jasperchasetoq.compressioncraft.setup.CompressionCraftItemSetup;

import java.io.File;

public class CompressionCraft extends AbstractAddon {

    private static CompressionCraft instance;

    public CompressionCraft() {
        super("JasperChaseTOQ", "CompressionCraft", "master", "options.auto-update");
    }
    @Override
    public void enable() {

        instance = this;

        Metrics metrics = new Metrics(this, 15648);

        Config cfg = new Config(this);

        CompressionCraftItemSetup.setup(this);
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
    }

    @Override
    public void disable() {
        instance = null;
    }
    public static CompressionCraft getInstance() {
            return instance;
        }

}

