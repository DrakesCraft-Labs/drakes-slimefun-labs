package com.github.drakescraft_labs.bump.implementation.items.weapons;

import org.bukkit.Sound;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.bump.implementation.tasks.WeaponProjectileTask;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

/**
 * {@link SkyDevilSword Heaven-demon Crumble Sword} will fire 3 {@link DragonFireball dragon fireballs}
 * and get some potion effects when using
 *
 * @author ybw0014
 */
public class SkyDevilSword extends BumpSword {

    public SkyDevilSword() {
        super(5, BumpItems.SKY_DEVIL_SWORD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
            null, null, null,
            BumpItems.SKY_SWORD, BumpItems.UPDATE_POWER, BumpItems.DEVIL_SWORD,
            null, null, null
        });
    }

    @Override
    public void onItemUse(Player p, ItemStack itemStack) {
        Bump.getLocalization().sendActionbarMessage(p, "weapon.sky_devil_sword.activated");

        p.setGlowing(true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 3));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 3));

        new BukkitRunnable() {
            int count = 3;

            @Override
            public void run() {
                if (count > 0) {
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
                    Projectile projectile = p.launchProjectile(DragonFireball.class);
                    WeaponProjectileTask.track(projectile);
                    count--;
                } else {
                    cancel();
                    p.setGlowing(false);
                    Bump.getLocalization().sendActionbarMessage(p, "weapon.sky_devil_sword.deactivated");
                }
            }
        }.runTaskTimer(Bump.getInstance(), 1L, 100L);
    }
}
