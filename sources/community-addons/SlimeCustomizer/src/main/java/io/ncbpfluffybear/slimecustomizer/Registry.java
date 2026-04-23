package io.ncbpfluffybear.slimecustomizer;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.collections.Pair;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores stuff.
 *
 * @author ybw0014
 */
public final class Registry {
    public static final Map<ItemStack[], Pair<RecipeType, String>> existingRecipes = new HashMap<>();
    public static final Map<String, ItemGroup> allItemGroups = new HashMap<>();
}
