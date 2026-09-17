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
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
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
            boolean isRapidFeed = false;
            int seedSlot = -1;

            for (int slot : getInputSlots()) {
                ItemStack item = menu.getItemInSlot(slot);

                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }

                if (ChickenUtils.isPocketChicken(item) && !ChickenUtils.isAdult(item) && chicken == null) {
                    chicken = item;
                } else if (item.isSimilar(GCEItems.RAPID_GROWTH_FEED) && seed == null) {
                    seed = item;
                    isRapidFeed = true;
                    seedSlot = slot;
                } else if (ChickenUtils.isFood(item) && item.getAmount() == item.getMaxStackSize() && seed == null) {
                    seed = item;
                    isRapidFeed = false;
                    seedSlot = slot;
                }
            }

            if (chicken == null || seed == null) {
                return null;
            }

            ItemStack output = chicken.clone();
            output.setAmount(1);
            var data = PocketChickenData.fromItem(chicken);
            ItemMeta outputMeta = output.getItemMeta();
            JsonObject adapter = data != null && data.getAdapter() != null ? data.getAdapter().deepCopy() : new JsonObject();
            var dnaState = data != null ? data.getState() : PersistentDataAPI.getIntArray(outputMeta, Keys.POCKET_CHICKEN_DNA);
            adapter.addProperty("baby", false);
            adapter.addProperty("_age", 6000);
            adapter.addProperty("_breedable", false);
            
            if (data != null && data.getExpandedSpecies() != null) {
                ChickenUtils.setExpandedPocketChicken(output, adapter, new DNA(dnaState), data.getExpandedSpecies());
            } else {
                ChickenUtils.setPocketChicken(output, adapter, new DNA(dnaState));
            }

            int processTime = isRapidFeed ? 10 : config.getGrowthChamberTime();
            if (config.isTest()) {
                processTime = 1;
            }

            MachineRecipe recipe = new MachineRecipe(
                processTime,
                new ItemStack[] {seed.clone(), chicken.clone()},
                new ItemStack[] {output}
            );
            if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                return null;
            }

            ItemUtils.consumeItem(chicken, 1, false);
            if (isRapidFeed) {
                menu.consumeItem(seedSlot, 1);
            } else {
                ItemUtils.consumeItem(seed, seed.getMaxStackSize(), false);
            }
            return recipe;
        } finally {
            SimpleProfiler.record("GrowthChamber.findNextRecipe", System.nanoTime() - __start);
        }
    }
}
