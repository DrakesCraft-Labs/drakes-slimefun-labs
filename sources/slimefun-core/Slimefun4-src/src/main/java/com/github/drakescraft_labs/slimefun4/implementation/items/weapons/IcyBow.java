package com.github.drakescraft_labs.slimefun4.implementation.items.weapons;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.drakescraft_labs.slimefun4.api.MinecraftVersion;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.handlers.BowShootHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.utils.compatibility.VersionedPotionEffectType;

/**
 * The {@link IcyBow} is a special kind of bow which slows down any
 * {@link LivingEntity} it hits.
 *
 * @author TheBusyBiscuit
 * @author martinbrom
 *
 */
public class IcyBow extends SlimefunBow {

    @ParametersAreNonnullByDefault
    public IcyBow(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, recipe);
    }

    @Nonnull
    @Override
    public BowShootHandler onShoot() {
        return (e, n) -> {
            if (n instanceof Player player) {
                // Fixes #3060 - Don't apply effects if the arrow was successfully blocked.
                if (player.isBlocking() && e.getFinalDamage() <= 0) {
                    return;
                }

                if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
                    player.setFreezeTicks(60);
                }
            }
            n.getWorld().playEffect(n.getLocation(), Effect.STEP_SOUND, Material.ICE);
            n.getWorld().playEffect(n.getEyeLocation(), Effect.STEP_SOUND, Material.ICE);
            n.addPotionEffect(new PotionEffect(VersionedPotionEffectType.SLOWNESS, 20 * 2, 10));
            n.addPotionEffect(new PotionEffect(VersionedPotionEffectType.JUMP_BOOST, 20 * 2, -10));
        };
    }

}
