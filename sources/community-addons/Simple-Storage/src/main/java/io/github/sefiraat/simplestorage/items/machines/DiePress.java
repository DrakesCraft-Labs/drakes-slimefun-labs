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

public final class DiePress extends AContainer implements RecipeDisplayItem {

    public static final RecipeType TYPE = new RecipeType(new NamespacedKey(SimpleStorage.inst(), "die-press"), SimpleStorageItemStacks.DIE_PRESS, "");
    public static final int SPEED = 1;
    public static final int CAPACITY = 16384;
    public static final int CONSUMPTION = 4096;

    public DiePress(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        setCapacity(CAPACITY);
        setEnergyConsumption(CONSUMPTION);
        setProcessingSpeed(SPEED);
        register(SimpleStorage.inst());
    }

    private void addRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(seconds, new ItemStack[] { input }, new ItemStack[] { output });
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(20, SimpleStorageItemStacks.ZAMAK_1, SimpleStorageItemStacks.DIE_BASIC);
        addRecipe(30, SimpleStorageItemStacks.ZAMAK_2, SimpleStorageItemStacks.DIE_REINFORCED);
        addRecipe(40, SimpleStorageItemStacks.ZAMAK_3, SimpleStorageItemStacks.DIE_ADVANCED);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.PISTON);

    }

    @Override
    public @Nonnull String getMachineIdentifier() {
        return "DIE_PRESS";
    }

}
