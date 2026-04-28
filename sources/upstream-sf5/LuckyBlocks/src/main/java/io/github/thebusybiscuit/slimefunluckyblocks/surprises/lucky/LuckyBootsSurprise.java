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
public final class LuckyBootsSurprise implements Surprise {

    private final ItemStack boots;

    public LuckyBootsSurprise() {
        boots = CustomItemStack.create(Material.DIAMOND_BOOTS, "&e&lLucky Boots");
        boots.addUnsafeEnchantment(Enchantment.PROTECTION, 10);
        boots.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10);
        boots.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 5);
        boots.addUnsafeEnchantment(Enchantment.THORNS, 10);
        boots.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Lucky Boots";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        l.getWorld().dropItemNaturally(l, boots.clone());
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
