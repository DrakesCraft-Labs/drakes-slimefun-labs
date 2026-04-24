package com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.Surprise;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

public final class LuckyLeggingsSurprise implements Surprise {

    private final ItemStack leggings;

    public LuckyLeggingsSurprise() {
        leggings = new CustomItemStack(Material.DIAMOND_LEGGINGS, "&e&lLucky Leggings");
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION, 10);
        leggings.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10);
        leggings.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 5);
        leggings.addUnsafeEnchantment(Enchantment.THORNS, 10);
        leggings.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Override
    public String getName() {
        return "Lucky Leggings";
    }

    @Override
    public void activate(Random random, Player p, Location l) {
        l.getWorld().dropItemNaturally(l, leggings.clone());
    }

    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
