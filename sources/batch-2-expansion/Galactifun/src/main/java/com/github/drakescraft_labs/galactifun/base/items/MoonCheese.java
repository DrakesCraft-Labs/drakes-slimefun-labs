package com.github.drakescraft_labs.galactifun.base.items;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;

public final class MoonCheese extends SimpleSlimefunItem<BlockPlaceHandler> {

    public MoonCheese(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public BlockPlaceHandler getItemHandler() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                p.setFoodLevel(Math.min(p.getFoodLevel() + 2, 20));
                p.setSaturation(p.getSaturation() + 2);
                ItemUtils.consumeItem(e.getItemInHand(), false);
            }
        };
    }

}
