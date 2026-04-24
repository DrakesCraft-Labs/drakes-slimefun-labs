package com.github.drakescraft_labs.bump.implementation.items.weapons;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.bump.core.services.sounds.BumpSound;
import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.bump.implementation.tasks.WeaponProjectileTask;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

/**
 * {@link DevilSword Demon Slayer Sword} will launch {@link SmallFireball} when using.
 *
 * @author ybw0014
 */
public class DevilSword extends BumpSword {

    public DevilSword() {
        super(5, BumpItems.DEVIL_SWORD, RecipeType.ARMOR_FORGE, new ItemStack[]{
            SlimefunItems.MAGIC_LUMP_2, SlimefunItems.ENDER_RUNE, SlimefunItems.MAGIC_LUMP_2,
            SlimefunItems.FIRE_RUNE, SlimefunItems.FIRE_RUNE, SlimefunItems.MAGIC_LUMP_2,
            SlimefunItems.ENDER_RUNE, SlimefunItems.MAGIC_LUMP_2, null
        });
    }

    @Override
    public void onItemUse(Player p, ItemStack itemStack) {
        Bump.getLocalization().sendActionbarMessage(p, "weapon.devil_sword");

        BumpSound.DEVIL_SWORD_USE.playFor(p);

        for (int i = 0; i < 20; i++) {
            Projectile projectile = p.launchProjectile(SmallFireball.class);
            WeaponProjectileTask.track(projectile);
            p.spawnParticle(Particle.ENCHANT, p.getLocation(), 1);
        }
    }
}
