package com.github.drakescraft_labs.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.items.chicken.PocketChicken;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.Keys;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;
import com.github.drakescraft_labs.gcereborn.utils.SimpleProfiler;

public class GrowthChamber extends AbstractMachine {

    public GrowthChamber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WHEAT_SEEDS);
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        long __start = System.nanoTime();
        try {
            var config = GeneticChickengineering.getConfigService();
            ItemStack chicken = null;
            ItemStack seed = null;
            for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }

                if (ChickenUtils.isPocketChicken(item) && !ChickenUtils.isAdult(item)) {
                    chicken = item;
                } else if (ChickenUtils.isFood(item) && item.getAmount() == item.getMaxStackSize()) {
                    seed = item;
                }
            }

            if (chicken == null || seed == null) {
                return null;
            }

        ItemStack output = chicken.clone();
        output.setAmount(1);
        // Use PocketChickenData to avoid repeated PDC reads
        var data = PocketChickenData.fromItem(chicken);
        JsonObject adapter = data != null ? data.getAdapter() : PersistentDataAPI.get(output.getItemMeta(), Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
        var dnaState = data != null ? data.getState() : PersistentDataAPI.getIntArray(output.getItemMeta(), Keys.POCKET_CHICKEN_DNA);
        adapter.addProperty("baby", false);
        adapter.addProperty("_age", 6000);
        adapter.addProperty("_breedable", false);
        ChickenUtils.setPocketChicken(output, adapter, new DNA(dnaState));

            MachineRecipe recipe = new MachineRecipe(
                config.isTest() ? 1 : config.getGrowthChamberTime(),
                new ItemStack[] {seed.clone(), chicken.clone()},
                new ItemStack[] {output}
            );
            if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                return null;
            }
            ItemUtils.consumeItem(chicken, false);
            ItemUtils.consumeItem(seed, seed.getMaxStackSize(), false);
            return recipe;
        } finally {
            SimpleProfiler.record("GrowthChamber.findNextRecipe", System.nanoTime() - start);
        }
            ItemUtils.consumeItem(chicken, false);
            ItemUtils.consumeItem(seed, seed.getMaxStackSize(), false);
            return recipe;
        } finally {
            SimpleProfiler.record("GrowthChamber.findNextRecipe", System.nanoTime() - __start);
        }
    }
}
