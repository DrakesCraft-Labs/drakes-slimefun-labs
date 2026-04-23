package me.gallowsdove.foxymachines.implementation.materials;

import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.items.groups.SubItemGroup;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import me.gallowsdove.foxymachines.Items;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SimpleMaterial extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    @ParametersAreNonnullByDefault
    public SimpleMaterial(SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int amount) {
        this(Items.MATERIALS_ITEM_GROUP, item, type, recipe, amount);
    }

    @ParametersAreNonnullByDefault
    public SimpleMaterial(SubItemGroup subItemGroup, SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int amount) {
        super(subItemGroup, item, type, recipe, new SlimefunItemStack(item, amount));
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return PlayerRightClickEvent::cancel;
    }
}
