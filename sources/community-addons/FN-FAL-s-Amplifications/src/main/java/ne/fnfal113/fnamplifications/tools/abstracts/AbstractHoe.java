package ne.fnfal113.fnamplifications.tools.abstracts;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractHoe extends SlimefunItem implements NotPlaceable {

    public AbstractHoe(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    /**
     *
     * @param player the player involved in the interaction
     * @param clickedBlock the block that the player right-clicked
     */
    public abstract void onRightClick(Player player, Block clickedBlock);

    /**
     *
     * @param player the player involved in the interaction
     * @param clickedBlock the block that the player left-clicked
     * @param itemStack the tool used in to harvest
     */
    public abstract void onLeftClick(Player player, Block clickedBlock, ItemStack itemStack);

}