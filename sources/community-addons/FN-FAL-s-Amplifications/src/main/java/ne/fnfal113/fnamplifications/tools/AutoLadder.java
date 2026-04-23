package ne.fnfal113.fnamplifications.tools;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

import ne.fnfal113.fnamplifications.tools.implementation.LadderTask;

import org.bukkit.inventory.ItemStack;

public class AutoLadder extends SlimefunItem {

    private final LadderTask ladderTask = new LadderTask();

    public AutoLadder(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public LadderTask getLadderTask() {
        return ladderTask;
    }

}