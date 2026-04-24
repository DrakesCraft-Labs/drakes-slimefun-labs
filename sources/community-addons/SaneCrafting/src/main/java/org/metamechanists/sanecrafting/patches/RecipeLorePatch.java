package org.metamechanists.sanecrafting.patches;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.sanecrafting.SaneCrafting;

import java.lang.reflect.Field;


// Once, I saw the mirror in my dreams. It spoke to me.
// 'Idra, go forth, and take my insights. It is time to apply the Holy principles of Reflection for the betterment of Humanity.'
// I woke up, and wrote this code. My life has been 10x better ever since. Follow the mirror.
@UtilityClass
public class RecipeLorePatch {
    private final ItemStack ITEMSTACK = new CustomItemStack(Material.CRAFTING_TABLE, "&bShaped Crafting Recipe");
    private final NamespacedKey KEY = new NamespacedKey("minecraft", "shaped");

    public void apply() {
        try {
            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                if (!item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
                    continue;
                }

                Field recipeTypeItemField = RecipeType.class.getDeclaredField("item");
                recipeTypeItemField.setAccessible(true);
                recipeTypeItemField.set(RecipeType.ENHANCED_CRAFTING_TABLE, ITEMSTACK);

                Field recipeTypeKeyField = RecipeType.class.getDeclaredField("key");
                recipeTypeKeyField.setAccessible(true);
                recipeTypeKeyField.set(RecipeType.ENHANCED_CRAFTING_TABLE, KEY);
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            SaneCrafting.getInstance().getLogger().info("Failed to apply ChangeRecipeTypePatch");
            e.printStackTrace();
            return;
        }

        SaneCrafting.getInstance().getLogger().info("Applied RecipeLore patch");
    }
}
