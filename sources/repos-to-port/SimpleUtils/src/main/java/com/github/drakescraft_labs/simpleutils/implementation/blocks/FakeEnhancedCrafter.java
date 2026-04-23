package com.github.drakescraft_labs.simpleutils.implementation.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.infinitylib.common.Scheduler;
import com.github.drakescraft_labs.infinitylib.common.StackUtils;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import dev.drake.dough.items.ItemStackSnapshot;

class FakeEnhancedCrafter extends EnhancedCraftingTable {

    private final List<ItemStackSnapshot[]> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();

    FakeEnhancedCrafter(ItemGroup category, SlimefunItemStack item) {
        super(category, item);

        Scheduler.run(this::init);
    }

    private void init() {
        EnhancedCraftingTable real = SlimefunItems.ENHANCED_CRAFTING_TABLE.getItem(EnhancedCraftingTable.class);
        List<ItemStack[]> recipes = Objects.requireNonNull(real).getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            if (i % 2 == 0) {
                inputs.add(ItemStackSnapshot.wrapArray(recipes.get(i)));
            } else {
                outputs.add(recipes.get(i)[0]);
            }
        }
    }

    @Nullable
    ItemStack craft(ItemStackSnapshot[] input) {
        loop: for (int i = 0; i < inputs.size(); i++) {
            ItemStackSnapshot[] recipe = inputs.get(i);

            for (int j = 0; j < recipe.length; j++) {
                if (!StackUtils.isSimilar(input[j], recipe[j])) {
                    continue loop;
                }
            }

            return outputs.get(i);
        }

        return null;
    }

}
