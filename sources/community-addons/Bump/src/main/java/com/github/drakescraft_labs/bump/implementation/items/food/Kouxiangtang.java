package com.github.drakescraft_labs.bump.implementation.items.food;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.bump.utils.FoodLevelUtils;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

/**
 * Chewing gum.
 *
 * @author ybw0014
 */
public class Kouxiangtang extends ItemFood {

    public Kouxiangtang() {
        super(BumpItems.KOUXIANGTANG, RecipeType.COMPRESSOR, new ItemStack[]{
            SlimefunItems.MAGIC_SUGAR
        });
    }

    @Override
    public void applyFoodEffects(Player p) {
        FoodLevelUtils.add(p, 6);
        p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1000, 5));
    }
}
