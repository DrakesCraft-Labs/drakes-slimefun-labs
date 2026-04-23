package com.github.drakescraft_labs.hotbarpets;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.hotbarpets.groups.BossMobs;
import com.github.drakescraft_labs.hotbarpets.groups.FarmAnimals;
import com.github.drakescraft_labs.hotbarpets.groups.HostileMobs;
import com.github.drakescraft_labs.hotbarpets.groups.PassiveMobs;
import com.github.drakescraft_labs.hotbarpets.groups.PeacefulAnimals;
import com.github.drakescraft_labs.hotbarpets.groups.SpecialPets;
import com.github.drakescraft_labs.hotbarpets.groups.UtilityPets;
import com.github.drakescraft_labs.hotbarpets.listeners.DamageListener;
import com.github.drakescraft_labs.hotbarpets.listeners.FoodListener;
import com.github.drakescraft_labs.hotbarpets.listeners.GeneralListener;
import com.github.drakescraft_labs.hotbarpets.listeners.PhantomListener;
import com.github.drakescraft_labs.hotbarpets.listeners.ProjectileListener;
import com.github.drakescraft_labs.hotbarpets.listeners.SoulPieListener;
import com.github.drakescraft_labs.hotbarpets.listeners.TNTListener;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import dev.drake.dough.config.Config;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.updater.GitHubBuildsUpdater;

public class HotbarPets extends JavaPlugin implements Listener, SlimefunAddon {

    private ItemGroup itemGroup;

    @Override
    public void onEnable() {
        Config cfg = new Config(this);

        // Setting up bStats
        new Metrics(this, 4859);

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/HotbarPets/master").start();
        }

        itemGroup = new ItemGroup(new NamespacedKey(this, "pets"), new CustomItemStack(PetTexture.CATEGORY.getAsItem(), "&dHotbar Pets", "", "&a> Click to open"));

        // Add all the Pets via their Group class
        new FarmAnimals(this);
        new PeacefulAnimals(this);
        new PassiveMobs(this);
        new HostileMobs(this);
        new BossMobs(this);
        new UtilityPets(this);
        new SpecialPets(this);

        // Registering the Listeners
        new DamageListener(this);
        new FoodListener(this);
        new GeneralListener(this);
        new PhantomListener(this);
        new ProjectileListener(this);
        new SoulPieListener(this);
        new TNTListener(this);

        // Registering our task
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HotbarPetsRunnable(), 0L, 2000L);
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/HotbarPets/issues";
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
