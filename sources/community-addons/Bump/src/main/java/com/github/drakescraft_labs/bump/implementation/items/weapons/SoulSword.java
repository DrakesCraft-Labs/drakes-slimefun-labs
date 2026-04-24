package com.github.drakescraft_labs.bump.implementation.items.weapons;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.bump.utils.Attributes;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.bump.core.services.sounds.BumpSound;
import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.BumpItems;
import com.github.drakescraft_labs.bump.implementation.groups.BumpItemGroups;
import com.github.drakescraft_labs.bump.utils.FoodLevelUtils;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

/**
 * {@link SoulSword Soul sword} will convert hunger to health.
 *
 * @author ybw0014
 */
public class SoulSword extends SimpleSlimefunItem<ItemUseHandler> {

    public SoulSword() {
        super(BumpItemGroups.WEAPON, BumpItems.SOUL_SWORD, RecipeType.ARMOR_FORGE, new ItemStack[]{
            null, null, null,
            BumpItems.SOUL_PAPER, new ItemStack(Material.DIAMOND_SWORD), BumpItems.SOUL_PAPER,
            null, null, null
        });
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            double health = p.getHealth();
            double maxHealth = p.getAttribute(Attributes.get("max_health")).getBaseValue();
            int foodLevel = p.getFoodLevel();

            if (maxHealth <= health) {
                Bump.getLocalization().sendActionbarMessage(p, "weapon.unavailable");
                return;
            }

            if (foodLevel >= 2) {
                if (maxHealth - health <= foodLevel) {
                    // Food level can be partially converted to full health
                    FoodLevelUtils.set(p, (int) (foodLevel - (maxHealth - health)));
                    p.setHealth(maxHealth);
                    Bump.getLocalization().sendActionbarMessage(p, "weapon.soul_sword.converted-part");
                } else {
                    // Food level can be all converted to health
                    FoodLevelUtils.set(p, 0);
                    p.setHealth(health + foodLevel);
                    Bump.getLocalization().sendActionbarMessage(p, "weapon.soul_sword.converted-all");
                }

                BumpSound.SOUL_SWORD_USE.playFor(p);
            } else {
                Bump.getLocalization().sendActionbarMessage(p, "weapon.low-food-level");
            }
        };
    }
}
