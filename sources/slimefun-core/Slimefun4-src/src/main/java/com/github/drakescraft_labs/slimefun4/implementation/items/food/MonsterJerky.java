package com.github.drakescraft_labs.slimefun4.implementation.items.food;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemConsumptionHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * {@link MonsterJerky} is basically just Rotten Flesh but without the Hunger Effect.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MeatJerky
 *
 */
public class MonsterJerky extends SimpleSlimefunItem<ItemConsumptionHandler> {

    @ParametersAreNonnullByDefault
    public MonsterJerky(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public ItemConsumptionHandler getItemHandler() {
        return (e, p, item) -> Slimefun.runSync(() -> {
            if (p.hasPotionEffect(PotionEffectType.HUNGER)) {
                p.removePotionEffect(PotionEffectType.HUNGER);
            }

            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5, 0));
        }, 1L);
    }

}
