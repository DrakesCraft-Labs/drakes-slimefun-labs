package me.jasperchasetoq.compressioncraft;

import dev.drake.infinitylib.core.AbstractAddon;

import dev.drake.dough.config.Config;
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

