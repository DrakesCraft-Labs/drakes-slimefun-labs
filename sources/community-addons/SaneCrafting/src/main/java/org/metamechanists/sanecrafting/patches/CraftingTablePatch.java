package org.metamechanists.sanecrafting.patches;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.items.VanillaItem;
import com.github.drakescraft-labs.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.sanecrafting.SaneCrafting;
import org.metamechanists.sanecrafting.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static org.metamechanists.sanecrafting.Util.generateRecipeId;


@UtilityClass
public class CraftingTablePatch {
    @Nullable
    private List<ItemStack[]> getRecipes() {
        EnhancedCraftingTable enhancedCraftingTable = Util.findMultiblock(EnhancedCraftingTable.class);
        if (enhancedCraftingTable == null) {
            return null;
        }
        return enhancedCraftingTable.getRecipes();
    }

    private void convertRecipe(List<ItemStack> input, ItemStack output) {
        convertRecipe(generateRecipeId(output), input, output);
    }

    private void convertRecipe(String recipeId, List<ItemStack> input, ItemStack output) {
        // Remove recipe if already registered
        NamespacedKey key = new NamespacedKey(SaneCrafting.getInstance(), recipeId);
        if (Bukkit.getServer().getRecipe(key) != null) {
            Bukkit.getServer().removeRecipe(key);
        }

        // Convert to shape
        String itemCharacters = "abcdefghi";
        List<String> shape = new ArrayList<>(List.of("abc", "def", "ghi"));
        Map<Character, ItemStack> ingredients = new HashMap<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int i = y*3 + x;
                char character = itemCharacters.charAt(i);
                ItemStack itemStack = input.get(i);
                if (itemStack == null) {
                    shape.set(y, shape.get(y).replace(character, ' '));
                } else {
                    ingredients.put(character, itemStack);
                }
            }
        }

        // Trim vertical
        for (int y = shape.size() - 1; y >= 0; y--) {
            if (Objects.equals(shape.get(y), "   ")) {
                shape.remove(y);
            }
        }

        // Skip if no recipe (just in case)
        if (shape.isEmpty()) {
            return;
        }

        // Trim horizontal
        for (int x = shape.get(0).length() - 1; x >= 0; x--) {
            boolean allRowsEmptyAtX = true;
            for (int y = shape.size() - 1; y >= 0; y--) {
                if (shape.get(y).charAt(x) != ' ') {
                    allRowsEmptyAtX = false;
                    break;
                }
            }
            if (allRowsEmptyAtX) {
                for (int y = shape.size() - 1; y >= 0; y--) {
                    String newRow = new StringBuilder(shape.get(y))
                            .deleteCharAt(x)
                            .toString();
                    shape.set(y, newRow);
                }
            }
        }

        ShapedRecipe recipe = new ShapedRecipe(key, output);
        recipe.shape(shape.toArray(new String[]{}));
        for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
            recipe.setIngredient(entry.getKey(), entry.getValue());
        }
        Bukkit.getServer().addRecipe(recipe);
    }

    public void apply() {
        List<ItemStack[]> recipes = getRecipes();
        if (recipes == null) {
            return;
        }

        int changedRecipes = 0;
        for (int j = 0; j < recipes.size(); j += 2) {
            ItemStack[] input = recipes.get(j);
            ItemStack output = recipes.get(j + 1)[0];
            if (SlimefunItem.getByItem(output) == null) {
                // Skip vanilla slimefunitem recipes for now
                continue;
            }

            try {
                convertRecipe(Arrays.asList(input), output);
            } catch (RuntimeException e) {
                String name = PlainTextComponentSerializer.plainText().serialize(output.displayName());
                SaneCrafting.getInstance().getLogger().severe("Failed to convert Enhanced Crafting Table recipe for " + name);
                e.printStackTrace();
                continue;
            }

            changedRecipes++;
        }

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (item instanceof VanillaItem vanillaItem && item.getRecipeType() == RecipeType.ENHANCED_CRAFTING_TABLE) {
                try {
                    convertRecipe(generateRecipeId(vanillaItem), Arrays.asList(vanillaItem.getRecipe()), vanillaItem.getRecipeOutput());
                    changedRecipes++;
                } catch (RuntimeException e) {
                    String name = PlainTextComponentSerializer.plainText().serialize(vanillaItem.getItem().displayName());
                    SaneCrafting.getInstance().getLogger().severe("Failed to convert Enhanced Crafting Table recipe for " + name);
                    e.printStackTrace();
                }
            }
        }

        SaneCrafting.getInstance().getLogger().info("Applied CraftingTable patch and converted " + changedRecipes + " Enhanced Crafting Table recipes to regular Crafing Table recipes");
    }
}
