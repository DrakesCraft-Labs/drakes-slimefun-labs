package me.schntgaispock.wildernether.slimefun.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.blocks.UnplaceableBlock;

/**
 * Might add more functionality in the future
 * 
 * @author SchnTgaiSpock
 */
public class WildernetherPlant extends UnplaceableBlock {
    
    @ParametersAreNonnullByDefault
    public WildernetherPlant(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc) {
        super(ig, is, rt, rc);
    }

}
