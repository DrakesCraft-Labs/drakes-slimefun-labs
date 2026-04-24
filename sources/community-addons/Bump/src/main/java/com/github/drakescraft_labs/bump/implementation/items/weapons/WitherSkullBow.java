package com.github.drakescraft_labs.bump.implementation.items.weapons;

import javax.annotation.Nonnull;

import org.bukkit.Sound;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.bump.core.handlers.BowUseHandler;
import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.bump.implementation.tasks.WeaponProjectileTask;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

/**
 * {@link WitherSkullBow Withered bow} will launch {@link WitherSkull} when using.
 *
 * @author ybw0014
 */
public class WitherSkullBow extends BumpBow {

    public WitherSkullBow() {
        super(5, BumpItems.WITHERSKULL_BOW, RecipeType.ARMOR_FORGE, new ItemStack[]{
            SlimefunItems.NECROTIC_SKULL, BumpItems.PEACH_WOOD, SlimefunItems.NECROTIC_SKULL,
            SlimefunItems.POWER_CRYSTAL, BumpItems.PEACH_WOOD, SlimefunItems.NECROTIC_SKULL,
            BumpItems.PEACH_WOOD, null, null
        });
    }

    @Nonnull
    @Override
    public BowUseHandler getItemHandler() {
        return (e, p, item) -> {
            e.setCancelled(true);
            if (costHunger(p)) {
                damageItem(p, item);

                Bump.getLocalization().sendActionbarMessage(p, "weapon.wither_skull_bow");

                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.0F);

                Projectile projectile = p.launchProjectile(WitherSkull.class);
                WeaponProjectileTask.track(projectile);
            } else {
                Bump.getLocalization().sendActionbarMessage(p, "weapon.low-food-level");
            }
        };
    }
}
