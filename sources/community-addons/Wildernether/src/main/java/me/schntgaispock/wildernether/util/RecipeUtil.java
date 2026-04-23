package me.schntgaispock.wildernether.util;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;

/**
 * Utility class for recipes
 * 
 * @author SchnTgaiSpock
 */
public class RecipeUtil {

    public static int recipeHash(@Nonnull ItemStack[] recipe) {
        String recipeString = "";
        for (ItemStack item : recipe) {
            if (item != null) {
                recipeString += item.getType().name();

                SlimefunItem sfitem = SlimefunItem.getByItem(item);
                if (sfitem != null) {
                    recipeString += "|" + sfitem.getId();
                }
            }
            recipeString += "\n";
        }

        return recipeString.hashCode();
    }

    public static ItemStack[] reduceRecipe(ItemStack[] recipe) {
        ItemStack[] reduced = new ItemStack[recipe.length];
        for (int i = 0; i < reduced.length; i++) {
            if (recipe[i] != null) {
                reduced[i] = recipe[i].clone();
                reduced[i].setAmount(1);
            } else {
                reduced[i] = null;
            }
        }
        return reduced;
    }
}
