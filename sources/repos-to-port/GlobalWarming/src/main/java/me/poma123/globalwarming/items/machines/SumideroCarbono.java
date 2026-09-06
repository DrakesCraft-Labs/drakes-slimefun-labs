/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
 *  com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
 *  com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem
 *  com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.poma123.globalwarming.items.machines;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.Items;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class SumideroCarbono
extends AContainer
implements RecipeDisplayItem {
    public static final RecipeType RECIPE_TYPE = new RecipeType(new NamespacedKey((Plugin)GlobalWarmingPlugin.getInstance(), "sumidero_carbono"), (ItemStack)Items.SUMIDERO_CARBONO);

    protected SumideroCarbono(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    protected void registerDefaultRecipes() {
        this.registerRecipe(12, new ItemStack[]{Items.CO2_CANISTER}, new ItemStack[]{Items.EMPTY_CANISTER});
    }

    public String getMachineIdentifier() {
        return "GW_SUMIDERO_CARBONO";
    }

    public ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_SHOVEL);
    }
}

