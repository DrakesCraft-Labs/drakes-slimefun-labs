package com.github.drakescraft_labs.gcereborn.items.machines;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.machines.MachineProcessor;
import com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation;
import dev.drake.dough.inventory.InvUtils;
import dev.drake.dough.items.ItemUtils;

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.utils.GuiItems;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;
import com.github.drakescraft_labs.gcereborn.utils.SimpleProfiler;

public class PrivateCoop extends AbstractMachine {

    public PrivateCoop(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return GCEItems.POCKET_CHICKEN.clone();
    }

    @Override
    protected void tick(@Nonnull Block b) {
        long __start = System.nanoTime();
        try {
            super.tick(b);

            MachineProcessor<CraftingOperation> processor = getMachineProcessor();
            if (processor.getOperation(b) != null) {
                if (ThreadLocalRandom.current().nextDouble() < 0.25) {
                    Location l = b.getLocation().toCenterLocation();
                    if (GeneticChickengineering.getConfigService().isParticlesEnabled()) {
                        l.getWorld().spawnParticle(Particle.HEART, l.add(0, 0.5, 0), 2, 0.2, 0, 0.2);
                    }
                }
                BlockMenu inv = BlockStorage.getInventory(b);
                // Check if parent chickens have been removed
                if (this.getParents(inv).size() != 2) {
                    processor.endOperation(b);
                    inv.replaceExistingItem(INFO_SLOT, GuiItems.BLACK_PANE);
                }
            }
        } finally {
            SimpleProfiler.record("PrivateCoop.tick", System.nanoTime() - __start);
        }
    }

    @Nonnull
    private List<ItemStack> getParents(@Nonnull BlockMenu inv) {
        List<ItemStack> parents = new LinkedList<>();
        for (int slot : getInputSlots()) {
            ItemStack parent = inv.getItemInSlot(slot);
            if (parent == null) {
                // since this machine only works with two parents
                // and this method is used to check for two chickens,
                // we just return the list here since it won't have
                // a length of two anyway, saving some time
                return parents;
            }
            var data = PocketChickenData.fromItem(parent);
            if (data != null && data.isAdult()) {
                parents.add(parent);
            }
        }
        return parents;
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        var config = GeneticChickengineering.getConfigService();
        List<ItemStack> parents = getParents(menu);
        if (parents.size() != 2) {
            return null;
        }

        ItemStack baby = ChickenUtils.breed(parents.get(0), parents.get(1));
        if (baby == null) {
            // Shouldn't ever be here, just in case
            return null;
        }
        MachineRecipe recipe = new MachineRecipe(
            config.isTest() ? 1 : 60,
            new ItemStack[] {parents.get(0), parents.get(1)},
            new ItemStack[] {baby}
        );
        Inventory inv = menu.toInventory();

        inv.setMaxStackSize(1);
        if (!InvUtils.fitAll(inv, recipe.getOutput(), getOutputSlots())) {
            return null;
        }

        if (GeneticChickengineering.getConfigService().isPainEnabled()) {
            for (ItemStack parent : parents) {
                if (!ChickenUtils.survivesPain(parent) && !GeneticChickengineering.getConfigService().isPainDeathEnabled()) {
                    return null;
                }
                ChickenUtils.possiblyHarm(parent);
                if (ChickenUtils.getHealth(parent) <= 0d) {
                    ItemUtils.consumeItem(parent, false);
                    if (GeneticChickengineering.getConfigService().isSoundsEnabled()) {
                        menu.getBlock().getWorld().playSound(menu.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1f, 1f);
                    }
                    return null;
                }
            }
        }

        return recipe;
    }
}
