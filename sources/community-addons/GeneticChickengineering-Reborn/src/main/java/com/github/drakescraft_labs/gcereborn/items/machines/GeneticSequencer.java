package com.github.drakescraft_labs.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.inventory.InvUtils;
import dev.drake.dough.items.ItemUtils;

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;
import com.github.drakescraft_labs.gcereborn.utils.SimpleProfiler;

public class GeneticSequencer extends AbstractMachine {

    public GeneticSequencer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
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
        var config = GeneticChickengineering.getConfigService();
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            var data = PocketChickenData.fromItem(item);
            if (data == null || data.isKnown()) {
                continue;
            }
            ItemStack chicken = item.clone();
            // Just in case these got stacked somehow
            chicken.setAmount(1);

                ItemStack learnedChicken = ChickenUtils.learnDNA(chicken);
                if (config.isPainEnabled()) {
                    if (!ChickenUtils.survivesPain(learnedChicken) && !config.isPainDeathEnabled()) {
                        continue;
                    }
                    ChickenUtils.possiblyHarm(learnedChicken);
                }
                MachineRecipe recipe = new MachineRecipe(
                    config.isTest() ? 1 : 30,
                    new ItemStack[] {chicken},
                    new ItemStack[] {learnedChicken}
                );
                if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    continue;
                }
                ChickenUtils.possiblyHarm(learnedChicken);
            }
            MachineRecipe recipe = new MachineRecipe(
                config.isTest() ? 1 : 30,
                new ItemStack[] {chicken},
                new ItemStack[] {learnedChicken}
            );
            if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                continue;
            }
            if (config.isPainEnabled() && ChickenUtils.getHealth(learnedChicken) <= 0d) {
                ItemUtils.consumeItem(chicken, false);
                if (config.isSoundsEnabled()) {
                    menu.getBlock().getWorld().playSound(menu.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1f, 1f);
                }
                continue;
            }
            menu.consumeItem(slot, 1);
            SimpleProfiler.record("GeneticSequencer.findNextRecipe", System.nanoTime() - __start);
            return recipe;
        }

        SimpleProfiler.record("GeneticSequencer.findNextRecipe", System.nanoTime() - __start);
        return null;
    }

}
