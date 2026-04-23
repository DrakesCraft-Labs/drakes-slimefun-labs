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

public final class Enderator extends AContainer implements RecipeDisplayItem {

    public static final RecipeType TYPE = new RecipeType(new NamespacedKey(SimpleStorage.inst(), "enderator"), SimpleStorageItemStacks.ENDERATOR, "");
    public static final int SPEED = 1;
    public static final int CAPACITY = 131072;
    public static final int CONSUMPTION = 32768;

    public Enderator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        setCapacity(CAPACITY);
        setEnergyConsumption(CONSUMPTION);
        setProcessingSpeed(SPEED);
        register(SimpleStorage.inst());
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(300, SimpleStorageItemStacks.CHEST_DIAMOND, SimpleStorageItemStacks.INVENTORY_CELL);
        registerRecipe(5, new SlimefunItemStack(SimpleStorageItemStacks.CHUNK_CHROMITE, 2), SimpleStorageItemStacks.CHUNK_BASTNAESITE);
        registerRecipe(5, new SlimefunItemStack(SimpleStorageItemStacks.CHUNK_BASTNAESITE, 2), SimpleStorageItemStacks.CHUNK_ANTIMONY);
        registerRecipe(5, new SlimefunItemStack(SimpleStorageItemStacks.CHUNK_ANTIMONY, 2), SimpleStorageItemStacks.RHENIUM_CHUNK);
        registerRecipe(5, new SlimefunItemStack(SimpleStorageItemStacks.RHENIUM_CHUNK, 2), SimpleStorageItemStacks.CHUNK_CHROMITE);
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.ENDER_EYE);

    }

    @Override
    @Nonnull
    public String getMachineIdentifier() {
        return "ENDERATOR";
    }

}
