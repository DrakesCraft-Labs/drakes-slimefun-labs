package ne.fnfal113.fnamplifications.quivers;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

import ne.fnfal113.fnamplifications.quivers.abstracts.AbstractQuiver;
import ne.fnfal113.fnamplifications.utils.Keys;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpectralQuiver extends AbstractQuiver {

    public SpectralQuiver(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int quiverSize) {
        super(itemGroup, item, recipeType, recipe, Keys.SPECTRAL_ARROWS_KEY, Keys.SPECTRAL_ARROWS_ID_KEY, Keys.SPECTRAL_STATE_KEY, quiverSize, new ItemStack(Material.SPECTRAL_ARROW, 1));
    }

}