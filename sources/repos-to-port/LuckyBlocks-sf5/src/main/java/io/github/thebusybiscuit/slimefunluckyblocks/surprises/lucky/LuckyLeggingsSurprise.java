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
public final class LuckyLeggingsSurprise implements Surprise {

    private final ItemStack leggings;

    public LuckyLeggingsSurprise() {
        leggings = CustomItemStack.create(Material.DIAMOND_LEGGINGS, "&e&lLucky Leggings");
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION, 10);
        leggings.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10);
        leggings.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 5);
        leggings.addUnsafeEnchantment(Enchantment.THORNS, 10);
        leggings.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Lucky Leggings";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        l.getWorld().dropItemNaturally(l, leggings.clone());
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
