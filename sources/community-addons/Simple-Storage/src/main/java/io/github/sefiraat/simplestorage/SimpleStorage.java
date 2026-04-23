package io.github.sefiraat.simplestorage;

import com.github.drakescraft-labs.infinitylib.core.AbstractAddon;
import io.github.sefiraat.simplestorage.categories.Categories;
import io.github.sefiraat.simplestorage.configuration.ManagerConfiguration;
import io.github.sefiraat.simplestorage.items.Blocks;
import io.github.sefiraat.simplestorage.items.Items;
import io.github.sefiraat.simplestorage.items.Machines;
import io.github.sefiraat.simplestorage.items.Materials;
import io.github.sefiraat.simplestorage.listeners.ManagerListeners;
import io.github.sefiraat.simplestorage.runnables.ManagerRunnables;


import java.util.Random;

public class SimpleStorage extends AbstractAddon {

    private static SimpleStorage instance;

    public SimpleStorage() {
        super("Sefiraat", "SimpleStorage", "master", "auto-update");
    }

    public static SimpleStorage inst() {
        return instance;
    }

    private ManagerConfiguration managerConfiguration;
    private ManagerRunnables managerRunnables;
    private ManagerListeners managerListeners;

    private final Random random = new Random();

    public ManagerConfiguration getManagerConfiguration() {
        return managerConfiguration;
    }
    public ManagerRunnables getManagerRunnables() {
        return managerRunnables;
    }
    public ManagerListeners getManagerListeners() {
        return managerListeners;
    }

    public Random getRandom() {
        return random;
    }

    @Override
    protected void enable() {



        instance = this;

        getLogger().info("########################################");
        getLogger().info("  SimpleStorage - Created by Sefiraat   ");
        getLogger().info("########################################");

        managerConfiguration = new ManagerConfiguration(this);
        managerRunnables = new ManagerRunnables(this);
        managerListeners = new ManagerListeners();

        setupSlimefun();
    }

    @Override
    protected void disable() {
        saveConfig();
        managerConfiguration.saveAdditionalConfigs();
        instance = null;
    }

    private void setupSlimefun() {
        Categories.set(this);
        Materials.set(this);
        Machines.set();
        Blocks.set(this);
        Items.set(this);
    }


}
