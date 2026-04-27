package dev.walshy.sfmobdrops;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import dev.drake.dough.updater.BlobBuildUpdater;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.core.debug.Debug;

import org.bstats.bukkit.Metrics;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import dev.walshy.sfmobdrops.drops.MobDrop;
import dev.walshy.sfmobdrops.drops.Drop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SfMobDrops extends JavaPlugin implements Listener {

    private static final String DEBUG = "sfmobdrops_debug";

    private static SfMobDrops instance;

    private final Set<MobDrop> mobDrops = new HashSet<>();

    private Config config;
    private boolean unitTest;

    public SfMobDrops() {}

    protected SfMobDrops(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        unitTest = true;
    }

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "SFMobDrops-drake");

        setInstance(this);
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        if (getConfig().getBoolean("settings.autoUpdate", true)
            && getDescription().getVersion().toLowerCase().startsWith("dev - ")
        ) {
            new BlobBuildUpdater(this, getFile(), "SFMobDrops").start();
        }

        if (!unitTest) {

        }
        
        config = new Config(this);
        loadDrops();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Guis(), this);

        getCommand("mobdrops").setExecutor(new MobDropsCommand());
    }

    @Override
    public void onDisable() {
        setInstance(null);
    }
    
    protected void loadDrops() {
        Set<MobDrop> newSet = config.loadConfig();

        this.mobDrops.clear();
        this.mobDrops.addAll(newSet);
        getLogger().info("Loaded in " + this.mobDrops.size() + " drops!");
    }

    @EventHandler
    public void onMobDeath(@Nonnull EntityDeathEvent e) {
        final Set<Drop> drops = findDropsFromEntity(e.getEntity());
        if (drops == null) return;

        Debug.log(DEBUG, "Found mob drop, has {} drops", drops.size());

        for (Drop drop : drops) {
            double chance = ThreadLocalRandom.current().nextDouble(100);

            Debug.log(DEBUG, "Evaluating {} - {} <= {}", drop.getSlimefunItem(), chance, drop.getChance());

            if (chance <= drop.getChance()) {
                final SlimefunItem item = SlimefunItem.getById(drop.getSlimefunItem());

                if (item != null && !item.isDisabledIn(e.getEntity().getWorld())) {
                    final ItemStack dropping = item.getItem().clone();
                    dropping.setAmount(drop.getAmount());

                    Debug.log(DEBUG, "Dropping {}x {}", drop.getAmount(), drop.getSlimefunItem());
                    e.getDrops().add(dropping);
                }
            }
        }
    }

    @Nullable
    private Set<Drop> findDropsFromEntity(@Nonnull LivingEntity entity) {
        for (MobDrop mobDrop : this.getMobDrops()) {
            if (mobDrop.isAllMobs() || entity.getType() == mobDrop.getDropsFrom()) {
                if (mobDrop.getEntityName() != null && entity.getCustomName() != null
                    && !mobDrop.getEntityName().equals(entity.getCustomName())
                ) {
                    continue;
                }

                if (mobDrop.getEntityNbtTag() != null && entity.getPersistentDataContainer().getKeys().stream()
                    .noneMatch(key -> key.equals(mobDrop.getEntityNbtTag()))
                ) {
                    continue;
                }

                return mobDrop.getDrops();
            }
        }
        return null;
    }

    @Nonnull
    public Set<MobDrop> getMobDrops() {
        return mobDrops;
    }

    public boolean isUnitTest() {
        return unitTest;
    }

    @Nonnull
    public static SfMobDrops getInstance() {
        return instance;
    }

    private static void setInstance(SfMobDrops ins) {
        instance = ins;
    }
}
