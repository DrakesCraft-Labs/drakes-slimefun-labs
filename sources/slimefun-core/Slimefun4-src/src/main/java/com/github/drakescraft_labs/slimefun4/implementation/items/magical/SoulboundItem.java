package com.github.drakescraft_labs.slimefun4.implementation.items.magical;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.attributes.Soulbound;
import com.github.drakescraft_labs.slimefun4.implementation.items.magical.runes.SoulboundRune;
import com.github.drakescraft_labs.slimefun4.implementation.listeners.SoulboundListener;

/**
 * Represents an Item that will not drop upon death.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Soulbound
 * @see SoulboundRune
 * @see SoulboundListener
 *
 */
public class SoulboundItem extends SlimefunItem implements Soulbound, NotPlaceable {

    @ParametersAreNonnullByDefault
    public SoulboundItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(itemGroup, item, type, recipe);
    }

}
