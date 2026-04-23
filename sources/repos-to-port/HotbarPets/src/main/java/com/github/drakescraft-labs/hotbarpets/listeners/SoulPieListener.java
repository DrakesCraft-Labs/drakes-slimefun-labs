package com.github.drakescraft-labs.hotbarpets.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.hotbarpets.HotbarPet;
import com.github.drakescraft-labs.hotbarpets.HotbarPets;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.items.CustomItemStack;

public class SoulPieListener implements Listener {

    private final HotbarPet eyamaz;

    public SoulPieListener(HotbarPets plugin) {
        eyamaz = (HotbarPet) SlimefunItem.getById("HOTBAR_PET_EYAMAZ");

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSoulHarvest(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player p = e.getEntity().getKiller();

            for (int i = 0; i < 9; ++i) {
                ItemStack item = p.getInventory().getItem(i);

                if (eyamaz != null && SlimefunUtils.isItemSimilar(item, eyamaz.getItem(), true)) {
                    e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), new CustomItemStack(new ItemStack(Material.PUMPKIN_PIE), "&bSoul Pie"));
                }
            }
        }

    }

}
