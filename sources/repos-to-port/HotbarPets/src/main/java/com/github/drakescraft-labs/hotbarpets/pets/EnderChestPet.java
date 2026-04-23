package com.github.drakescraft-labs.hotbarpets.pets;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.hotbarpets.SimpleBasePet;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;

public class EnderChestPet extends SimpleBasePet {

    public EnderChestPet(ItemGroup itemGroup, SlimefunItemStack item, ItemStack food, ItemStack[] recipe) {
        super(itemGroup, item, food, recipe);
    }

    @Override
    public void onUseItem(Player p) {
        p.openInventory(p.getEnderChest());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 2.0F);
    }

}
