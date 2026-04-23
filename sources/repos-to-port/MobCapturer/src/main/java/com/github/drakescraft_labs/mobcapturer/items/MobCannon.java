package com.github.drakescraft_labs.mobcapturer.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.drakescraft_labs.mobcapturer.MobCapturer;
import com.github.drakescraft_labs.mobcapturer.setup.ItemStacks;
import com.github.drakescraft_labs.mobcapturer.setup.Keys;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import dev.drake.dough.items.ItemUtils;

/**
 * The Mob Cannon can shoot mob capturing pellets.
 *
 * @author TheBusyBiscuit
 * @author ybw0014
 */
public class MobCannon extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public MobCannon(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            if (consumeAmmo(e.getPlayer())) {
                Snowball projectile = e.getPlayer().launchProjectile(Snowball.class);
                projectile.setMetadata(Keys.MOB_CAPTURING_PELLET, new FixedMetadataValue(MobCapturer.getInstance(),
                    e.getPlayer().getUniqueId()));
            }
        };
    }

    @ParametersAreNonnullByDefault
    private boolean consumeAmmo(Player p) {
        if (p.getGameMode() == GameMode.CREATIVE) {
            return true;
        }

        for (ItemStack item : p.getInventory()) {
            if (ItemStacks.MOB_CAPTURING_PELLET.getItem().isItem(item)) {
                ItemUtils.consumeItem(item, false);
                return true;
            }
        }

        return false;
    }

}
