package io.ncbpfluffybear.flowerpower.objects;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A SlimefunItem that can not be disenchanted or placed
 *
 * @author NCBPFluffyBear
 */
public class NonplaceableBlock extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    public NonplaceableBlock(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return PlayerRightClickEvent::cancel;
    }

    @Override
    public boolean isDisenchantable() {
        return false;
    }
}
