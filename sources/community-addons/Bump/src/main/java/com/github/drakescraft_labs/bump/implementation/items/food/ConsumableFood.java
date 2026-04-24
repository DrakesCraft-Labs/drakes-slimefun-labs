package com.github.drakescraft_labs.bump.implementation.items.food;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.groups.BumpItemGroups;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemConsumptionHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;

/**
 * A {@link ConsumableFood} is a {@link SlimefunItem} that is based on vanilla food or potion.
 * Player can get effects after consuming.
 *
 * @author ybw0014
 */
public abstract class ConsumableFood extends SimpleSlimefunItem<ItemConsumptionHandler> {

    protected ConsumableFood(SlimefunItemStack itemStack, RecipeType recipeType, ItemStack[] recipe) {
        super(BumpItemGroups.FOOD, itemStack, recipeType, recipe);
    }

    @Nonnull
    @Override
    public ItemConsumptionHandler getItemHandler() {
        return (e, p, item) -> {
            String messageKey = this.getId().toLowerCase(Locale.ROOT);
            Bump.getLocalization().sendActionbarMessage(p, "food." + messageKey);

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(e.getItem(), false);
            }

            this.applyFoodEffects(p);
        };
    }

    public abstract void applyFoodEffects(Player p);
}
