package com.github.drakescraft_labs.ecopower.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * This is the multiblock variant of the {@link SteamTurbine}, as shown in the {@link SlimefunGuide}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SteamTurbineMultiblock extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public SteamTurbineMultiblock(ItemGroup itemGroup, SlimefunItemStack item, SteamTurbine turbine) {
        super(itemGroup, item, RecipeType.MULTIBLOCK, new ItemStack[] {
                null, turbine.getItem(), null,
                null, new CustomItemStack(Material.WATER_BUCKET, "&fWater (Bubble Column)"), null,
                null, new ItemStack(Material.MAGMA_BLOCK), null
        });
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();
            e.getPlayer().sendMessage("Psst, this Item is just a dummy. You need to place the actual structure down.");
        };
    }

}
