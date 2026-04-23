package com.github.drakescraft_labs.hotbarpets.pets.special;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft_labs.hotbarpets.SimpleBasePet;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

public class PatriotPet extends SimpleBasePet {

    public PatriotPet(ItemGroup itemGroup, SlimefunItemStack item, ItemStack food, ItemStack[] recipe) {
        super(itemGroup, item, food, recipe);
    }

    @Override
    public void onUseItem(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 0));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100, 0));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, 1.0F, 2.0F);
    }

}
