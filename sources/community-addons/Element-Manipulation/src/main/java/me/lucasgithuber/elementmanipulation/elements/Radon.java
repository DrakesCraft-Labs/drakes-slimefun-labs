package me.lucasgithuber.elementmanipulation.elements;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Radon extends SimpleSlimefunItem<ItemUseHandler> {
    public Radon(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @NotNull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (p.getGameMode() != GameMode.CREATIVE) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
            e.cancel();
            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 20 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 20, 1));
            p.setFireTicks(20 * 20);
        };
    }
}

