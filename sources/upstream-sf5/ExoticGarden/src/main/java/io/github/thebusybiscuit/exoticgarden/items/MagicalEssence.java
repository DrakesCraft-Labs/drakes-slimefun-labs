package io.github.thebusybiscuit.exoticgarden.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

/**
 * Magical Essence is the output item from magical ore plants.
 *
 * @author TheBusyBiscuit
 */
public class MagicalEssence extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public MagicalEssence(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { item.item(), item.item(), item.item(), item.item(), null, item.item(), item.item(), item.item(), item.item() });
    }

    @Override
    public boolean useVanillaBlockBreaking() {
        return true;
    }

}
