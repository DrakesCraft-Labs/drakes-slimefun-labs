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
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPlayerHead;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class WalshrusSurprise implements Surprise {

    private final ItemStack sword;

    public WalshrusSurprise() {
        sword = CustomItemStack.create(Material.GOLDEN_SWORD, "&e&lLucky Sword");
        sword.addUnsafeEnchantment(Enchantment.SHARPNESS, 10);
        sword.addUnsafeEnchantment(Enchantment.LOOTING, 10);
        sword.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Walshrus";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        Zombie zombie = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        zombie.getAttribute(VersionedAttribute.MAX_HEALTH).setBaseValue(40D);
        zombie.setHealth(40D);

        zombie.getEquipment().setHelmet(VersionedPlayerHead.getItemStack("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk2NmYwZWJkNzdmMWJjZDY1NmZhMmRjM2VmMDMwM2UyNmE2YTNkZTQ5OGMzOTk5ZDM5ZmRjYWNjNWY1YWQifX19"));
        zombie.getEquipment().setHelmetDropChance(0F);

        zombie.getEquipment().setItemInMainHand(sword.clone());
        zombie.getEquipment().setItemInMainHandDropChance(0F);
        zombie.setCanPickupItems(false);
        zombie.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4Walshrus"));
        zombie.setCustomNameVisible(true);
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.UNLUCKY;
    }

}
