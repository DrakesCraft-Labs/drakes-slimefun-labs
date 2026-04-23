package me.voper.slimeframe.implementation.listeners;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.tasks.CoolantRaknoidsTask;
import me.voper.slimeframe.utils.Keys;

public class CoolantRaknoidsListener implements Listener {

    private static final List<PotionEffect> RAKNOIDS_POTIONS = List.of(
            new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 3),
            new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1),
            new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2),
            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5),
            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 5)
    );

    private static final String NATURAL = "natural";
    private static final String NOT_NATURAL = "not-natural";

    public CoolantRaknoidsListener(@Nonnull SlimeFrame plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCaveSpiderSpawn(@Nonnull CreatureSpawnEvent e) {
        if (!(e.getEntity() instanceof CaveSpider caveSpider)) return;

        final String data = e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL ? NATURAL : NOT_NATURAL;

        int random = ThreadLocalRandom.current().nextInt(100) + 1;
        if (data.equals(NATURAL)) {
            if (random < 45) addRaknoid(caveSpider, data);
        } else {
            if (random < 15) addRaknoid(caveSpider, data);
        }

    }

    @EventHandler
    public void onRaknoidsLoad(@Nonnull EntitiesLoadEvent e) {
        for (Entity entity : e.getEntities()) {
            if (!PersistentDataAPI.hasString(entity, Keys.RAKNOID)) continue;
            CoolantRaknoidsTask.RAKNOIDS_UUIDS.add(entity.getUniqueId());
        }
    }

    @EventHandler
    public void onRaknoidsUnload(@Nonnull EntitiesUnloadEvent e) {
        for (Entity entity : e.getEntities()) {
            CoolantRaknoidsTask.RAKNOIDS_UUIDS.remove(entity.getUniqueId());
        }
    }

    @EventHandler
    public void onChunkLoad(@Nonnull ChunkLoadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            if (!PersistentDataAPI.hasString(entity, Keys.RAKNOID)) continue;
            CoolantRaknoidsTask.RAKNOIDS_UUIDS.add(entity.getUniqueId());
        }
    }

    @EventHandler
    public void onChunkUnload(@Nonnull ChunkUnloadEvent e) {
        for (Entity entity : e.getChunk().getEntities()) {
            CoolantRaknoidsTask.RAKNOIDS_UUIDS.remove(entity.getUniqueId());
        }
    }

    @EventHandler
    public void onRaknoidDeath(@Nonnull EntityDeathEvent e) {
        if (!(e.getEntity() instanceof CaveSpider caveSpider)) return;
        if (!PersistentDataAPI.hasString(caveSpider, Keys.RAKNOID)) return;
        final int amount = PersistentDataAPI.getString(caveSpider, Keys.RAKNOID).equals(NATURAL) ?
                ThreadLocalRandom.current().nextInt(7) :
                ThreadLocalRandom.current().nextInt(3);
        e.getDrops().add(new SlimefunItemStack(SFrameStacks.COOLANT_CANISTER, amount));
    }

    private void addRaknoid(CaveSpider caveSpider, String data) {
        PersistentDataAPI.setString(caveSpider, Keys.RAKNOID, data);
        CoolantRaknoidsTask.RAKNOIDS_UUIDS.add(caveSpider.getUniqueId());
        caveSpider.addPotionEffects(RAKNOIDS_POTIONS);
    }

}
