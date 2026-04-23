package com.github.drakescraft-labs.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.inventory.InvUtils;
import dev.drake.dough.items.ItemUtils;

import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft-labs.gcereborn.items.GCEItems;
import com.github.drakescraft-labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft-labs.gcereborn.utils.PocketChickenData;
import com.github.drakescraft-labs.gcereborn.utils.SimpleProfiler;

public class RestorationChamber extends AbstractMachine {

    public RestorationChamber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return GCEItems.POCKET_CHICKEN.clone();
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

                if (ChickenUtils.isPocketChicken(item)) {
                    chicken = item;
                } else if (ChickenUtils.isFood(item)) {
                    seed = item;
                }
            }

            if (chicken == null || seed == null) {
                return null;
            }

        PocketChickenData data = PocketChickenData.fromItem(chicken);
        double health = data != null ? data.getHealth() : ChickenUtils.getHealth(chicken);
        int seedAmount = seed.getAmount();
        int toConsume = 0;
        while (seedAmount > 0 && health < 4d) {
            seedAmount--;
            toConsume++;
            health = health + 0.25;
        }
            if (toConsume == 0) {
                return null;
            }
        ItemStack recipeSeeds = seed.clone();
        recipeSeeds.setAmount(toConsume);
        ItemStack recipeChick = chicken.clone();
        ChickenUtils.heal(recipeChick, toConsume * 0.25);
        MachineRecipe recipe = new MachineRecipe(
            config.isTest() ? 1 : config.getHealRate() * toConsume,
            new ItemStack[] {recipeSeeds, chicken.clone()},
            new ItemStack[] {recipeChick}
        );
        if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
            return null;
        }
            ItemUtils.consumeItem(chicken, false);
            ItemUtils.consumeItem(seed, toConsume, false);
            return recipe;
        } finally {
            SimpleProfiler.record("RestorationChamber.findNextRecipe", System.nanoTime() - __start);
        }
    }
}
