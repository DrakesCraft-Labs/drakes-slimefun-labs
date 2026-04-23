package io.github.sefiraat.simplestorage.items.machines;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.items.SimpleStorageItemStacks;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class MetalPressBasic extends AContainer implements RecipeDisplayItem {

    public static final RecipeType TYPE = new RecipeType(new NamespacedKey(SimpleStorage.inst(), "metal-press"), SimpleStorageItemStacks.METAL_PRESS_BASIC, "");
    public static final int SPEED = 1;
    public static final int CAPACITY = 32768;
    public static final int CONSUMPTION = 8192;

    public MetalPressBasic(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        setCapacity(CAPACITY);
        setEnergyConsumption(CONSUMPTION);
        setProcessingSpeed(SPEED);
        register(SimpleStorage.inst());
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(20, new ItemStack[]{SimpleStorageItemStacks.DIE_BASIC, new SlimefunItemStack(SimpleStorageItemStacks.ELECTRUM_INGOT, 4)}, new ItemStack[]{SimpleStorageItemStacks.ELECTRUM_PLATE});
        registerRecipe(40, new ItemStack[]{SimpleStorageItemStacks.DIE_REINFORCED, new SlimefunItemStack(SimpleStorageItemStacks.CUPRONICKEL_REINFORCED, 4)}, new ItemStack[]{SimpleStorageItemStacks.CUPRONICKEL_PLATE});
        registerRecipe(60, new ItemStack[]{SimpleStorageItemStacks.DIE_ADVANCED, new SlimefunItemStack(SimpleStorageItemStacks.PEWTER_REINFORCED, 4)}, new ItemStack[]{SimpleStorageItemStacks.PEWTER_PLATE});
        registerRecipe(120, new ItemStack[]{SimpleStorageItemStacks.DIE_ADVANCED, new SlimefunItemStack(SimpleStorageItemStacks.NICKEL_SUPER_INGOT, 4)}, new ItemStack[]{SimpleStorageItemStacks.NICKEL_SUPERALLOY_PLATE});
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.PISTON);

    }

    @Override
    public @Nonnull String getMachineIdentifier() {
        return "METAL_PRESS";
    }

}
