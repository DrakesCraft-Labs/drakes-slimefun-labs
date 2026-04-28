package io.github.thebusybiscuit.slimefunluckyblocks.surprises.pandora;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
public final class ReapersSurprise implements Surprise {

    private final ItemStack hoe;

    public ReapersSurprise() {
        hoe = CustomItemStack.create(Material.GOLDEN_HOE, "&e&lLucky Hoe");
        hoe.addUnsafeEnchantment(Enchantment.SHARPNESS, 10);
        hoe.addUnsafeEnchantment(Enchantment.LOOTING, 10);
        hoe.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 5);
        hoe.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Reapers";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        for (int i = 0; i < 4; i++) {
            Zombie zombie = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
            zombie.getAttribute(VersionedAttribute.MAX_HEALTH).setBaseValue(120D);
            zombie.setHealth(120D);

            zombie.getEquipment().setHelmet(VersionedPlayerHead.getItemStack("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTkzN2FmMjYzMzI2ZTJiNDA5MDQyNzFiODMxYzNiMTc2ZWEyMWYwMTg2YmZhZjRlMTZlZWUxZTI4OWRkYWQ4In19fQ=="));
            zombie.getEquipment().setHelmetDropChance(0F);

            zombie.getEquipment().setItemInMainHand(hoe.clone());
            zombie.getEquipment().setItemInMainHandDropChance(0F);
            zombie.setCanPickupItems(false);

            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 255));
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 999999999, 1));
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1));
        }
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.PANDORA;
    }

}
