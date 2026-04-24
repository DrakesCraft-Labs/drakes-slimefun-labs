package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class Thorium extends UnplaceableBlock implements Radioactive {

    private static final ItemStack thorium = new CustomItemStack(Material.PAPER, "&fHint!",
        "&a&oMake sure to first GEO-Scan the chunk in which you are", "&a&omining to discover Thorium!");

    public Thorium() {
        super(Items.LITEXPANSION, Items.THORIUM, RecipeType.GEO_MINER, new ItemStack[] {
                null, null, null,
                null, thorium, null,
                null, null, null
            }
        );
    }

    @Nonnull
    @Override
    public Radioactivity getRadioactivity() {
        return Radioactivity.HIGH;
    }
}
