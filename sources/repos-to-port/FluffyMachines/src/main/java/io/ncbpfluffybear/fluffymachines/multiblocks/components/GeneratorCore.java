package io.ncbpfluffybear.fluffymachines.multiblocks.components;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetProvider;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class GeneratorCore extends SlimefunItem implements EnergyNetProvider {

    public GeneratorCore(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.GENERATOR;
    }

    // Making this block an EnergyNetProvider instead of an EnergyNetComponent causes it to tick. However, this block will not actively
    // push out energy unless made a generator.
    @Override
    public int getGeneratedOutput(@Nonnull Location l, @Nonnull Config data) {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 64;
    }
}
