package com.github.drakescraft_labs.bump.implementation.items.food;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.bump.utils.FoodLevelUtils;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

/**
 * Zongzi. Rice dumpling
 *
 * @author haiman233
 */
public class Zongzi extends ItemFood {

    public Zongzi() {
        super(BumpItems.ZONGZI, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
            new ItemStack(Material.LILY_PAD), new ItemStack(Material.ACACIA_LEAVES), new ItemStack(Material.LILY_PAD),
            new ItemStack(Material.ACACIA_LEAVES), new ItemStack(Material.WHEAT), new ItemStack(Material.ACACIA_LEAVES),
            new ItemStack(Material.LILY_PAD), new ItemStack(Material.ACACIA_LEAVES), new ItemStack(Material.LILY_PAD)
        });
    }

    @Override
    public void applyFoodEffects(Player p) {
        FoodLevelUtils.add(p, 8);
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, 100, 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1000, 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 150, 2));
        p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 500, 2));
    }
}
