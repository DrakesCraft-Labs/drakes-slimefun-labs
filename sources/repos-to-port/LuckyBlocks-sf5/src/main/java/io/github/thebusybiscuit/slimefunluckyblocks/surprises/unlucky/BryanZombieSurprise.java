package io.github.thebusybiscuit.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedAttribute;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class BryanZombieSurprise implements Surprise {

    private final ItemStack axe;

    public BryanZombieSurprise() {
        axe = CustomItemStack.create(Material.GOLDEN_AXE, "&e&lLucky Axe");
        axe.addUnsafeEnchantment(Enchantment.SHARPNESS, 10);
        axe.addUnsafeEnchantment(Enchantment.EFFICIENCY, 10);
        axe.addUnsafeEnchantment(Enchantment.FORTUNE, 10);
        axe.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Bryan";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        Zombie zombie = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        zombie.getAttribute(VersionedAttribute.MAX_HEALTH).setBaseValue(60D);
        zombie.setHealth(60D);
        zombie.getEquipment().setItemInMainHand(axe.clone());
        zombie.getEquipment().setItemInMainHandDropChance(0F);
        zombie.setCanPickupItems(false);
        zombie.setCustomName(ChatColor.translateAlternateColorCodes('&', "&eBryan"));
        zombie.setCustomNameVisible(true);
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.UNLUCKY;
    }

}
