package ne.fnfal113.relicsofcthonia;

import dev.drake.dough.updater.BlobBuildUpdater;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import ne.fnfal113.relicsofcthonia.config.ConfigManager;
import ne.fnfal113.relicsofcthonia.core.Items;
import ne.fnfal113.relicsofcthonia.listeners.MiningListener;
import ne.fnfal113.relicsofcthonia.listeners.MobListener;
import ne.fnfal113.relicsofcthonia.listeners.OffHandClickListener;
import ne.fnfal113.relicsofcthonia.listeners.PiglinMainListener;
import ne.fnfal113.relicsofcthonia.listeners.RegistryFinalizedListener;
import ne.fnfal113.relicsofcthonia.listeners.RelicVoiderListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public final class RelicsOfCthonia extends JavaPlugin implements SlimefunAddon {

    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    @Getter private static RelicsOfCthonia instance;

    @Override
    public void onEnable() {
        instance = this;


        getLogger().info("************************************************************");
        getLogger().info("*         Relics of Cthonia - Created by FN_FAL113         *");
        getLogger().info("*               Slimefun Addon Jam 2022 Entry              *");
        getLogger().info("*                     The Nether Theme                     *");
        getLogger().info("************************************************************");

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Items.register();

        registerEvents();

        if (getConfig().getBoolean("auto-update", true) && getDescription().getVersion().startsWith("Dev - ")) {
            new BlobBuildUpdater(this, getFile(), "RelicsOfCthonia").start();
        }
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MobListener(), this);
        getServer().getPluginManager().registerEvents(new PiglinMainListener(), this);
        getServer().getPluginManager().registerEvents(new OffHandClickListener(), this);
        getServer().getPluginManager().registerEvents(new RelicVoiderListener(), this);
        getServer().getPluginManager().registerEvents(new RegistryFinalizedListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(instance);
        getLogger().log(Level.INFO, "Successfully disabled any running task that exist");
    }

    @Override
    public @Nonnull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public @Nonnull String getBugTrackerURL() {
        return "https://github.com/FN-FAL113/RelicsOfCthonia/issues";
    }
}
