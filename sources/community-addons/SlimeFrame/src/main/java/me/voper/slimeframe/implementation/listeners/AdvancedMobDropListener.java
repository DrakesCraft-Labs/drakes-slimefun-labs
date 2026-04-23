package me.voper.slimeframe.implementation.listeners;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.attributes.AdvancedMobDrop;

public class AdvancedMobDropListener implements Listener {

    private final SlimeFrame plugin;

    public AdvancedMobDropListener(SlimeFrame plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player p = e.getEntity().getKiller();

            Set<ItemStack> customDrops = Slimefun.getRegistry().getMobDrops().get(e.getEntityType());

            if (customDrops != null && !customDrops.isEmpty()) {
                for (ItemStack drop : customDrops) {
                    if (canDrop(p, drop, e.getEntityType())) {
                        e.getDrops().add(drop.clone());
                    }
                }
            }
        }
    }


    private boolean canDrop(Player p, ItemStack item, EntityType entity) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem == null) return false;
        if (!sfItem.canUse(p, true)) return false;

        if (sfItem instanceof AdvancedMobDrop advancedMobDrop) {

            // Will be handled by the Slimefun RandomMobDrop listener
            if (advancedMobDrop.getMobDropChance() != 0) return false;

            int random = ThreadLocalRandom.current().nextInt(100);

            return advancedMobDrop.getMobDropChance(entity) > random;
        }

        return false;
    }

}
