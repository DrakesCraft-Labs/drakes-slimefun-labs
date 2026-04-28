package io.github.thebusybiscuit.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class LuckyChestplateSurprise implements Surprise {

    private final ItemStack chestplate;

    public LuckyChestplateSurprise() {
        chestplate = CustomItemStack.create(Material.DIAMOND_CHESTPLATE, "&e&lLucky Chestplate");
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION, 10);
        chestplate.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10);
        chestplate.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 5);
        chestplate.addUnsafeEnchantment(Enchantment.THORNS, 10);
        chestplate.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Lucky Chestplate";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        l.getWorld().dropItemNaturally(l, chestplate.clone());
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
