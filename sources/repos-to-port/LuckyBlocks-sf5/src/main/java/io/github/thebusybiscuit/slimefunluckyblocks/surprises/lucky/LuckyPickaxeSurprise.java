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
public final class LuckyPickaxeSurprise implements Surprise {

    private final ItemStack pickaxe;

    public LuckyPickaxeSurprise() {
        pickaxe = CustomItemStack.create(Material.GOLDEN_PICKAXE, "&e&lLucky Pickaxe");
        pickaxe.addUnsafeEnchantment(Enchantment.EFFICIENCY, 10);
        pickaxe.addUnsafeEnchantment(Enchantment.FORTUNE, 10);
        pickaxe.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Lucky Pickaxe";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        l.getWorld().dropItemNaturally(l, pickaxe.clone());
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
