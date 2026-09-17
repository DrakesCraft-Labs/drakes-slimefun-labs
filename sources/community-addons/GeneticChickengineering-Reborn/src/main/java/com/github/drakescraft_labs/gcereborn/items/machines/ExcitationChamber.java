package com.github.drakescraft_labs.gcereborn.items.machines;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.machines.MachineProcessor;
import com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.items.chicken.ExpandedChickenSpecies;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;
import com.github.drakescraft_labs.gcereborn.utils.GuiItems;
import com.github.drakescraft_labs.gcereborn.utils.SimpleProfiler;

public class ExcitationChamber extends AbstractMachine {

    private static final int[] BACKGROUND = new int[] {
        0, 1, 2, 6, 7, 8,
        9, 10, 11, 15, 16, 17,
        18, 19, 20, 21, 23, 24,
        25, 26
    };
    private static final int[] INPUT_BORDER = new int[] {3, 5, 12, 13, 14};
    private static final int[] OUTPUT_BORDER = new int[] {27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 44};
    private static final int[] INPUT_SLOTS = new int[] {4};
    private static final int[] OUTPUT_SLOTS = new int[] {37, 38, 39, 40, 41, 42, 43};

    public ExcitationChamber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return GCEItems.POCKET_CHICKEN.clone();
    }

    @Override
    public int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    @Override
    public int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

    @Override
    protected void constructMenu(@Nonnull BlockMenuPreset preset) {
        for (int i : BACKGROUND) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : INPUT_BORDER) {
            preset.addItem(i, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : OUTPUT_BORDER) {
            preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(INFO_SLOT, GuiItems.BLACK_PANE, ChestMenuUtils.getEmptyClickHandler());

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, (p, slot, cursor, action) -> cursor != null && !cursor.getType().isAir());
        }
    }

    @Override
    protected void tick(@Nonnull Block b) {
        long __start = System.nanoTime();
        try {
            super.tick(b);
            BlockMenu inv = BlockStorage.getInventory(b);
            MachineProcessor<CraftingOperation> processor = getMachineProcessor();
            if (processor.getOperation(b) != null) {
                ItemStack chicken = inv.getItemInSlot(getInputSlots()[0]);
                var data = PocketChickenData.fromItem(chicken);
                if (data == null || !data.isAdult()) {
                    processor.endOperation(b);
                    inv.replaceExistingItem(INFO_SLOT, GuiItems.BLACK_PANE);
                } else if (data.getExpandedSpecies() != null && data.getExpandedSpecies().isRadioactive()) {
                    if (GeneticChickengineering.getConfigService().isParticlesEnabled() && Math.random() < 0.3) {
                        b.getWorld().spawnParticle(Particle.WARPED_SPORE, b.getLocation().add(0.5, 0.8, 0.5), 3, 0.2, 0.2, 0.2, 0.02);
                    }
                }
            }
        } finally {
            SimpleProfiler.record("ExcitationChamber.tick", System.nanoTime() - __start);
        }
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        long __start = System.nanoTime();
        try {
            var config = GeneticChickengineering.getConfigService();
            for (int slot : getInputSlots()) {
                ItemStack chicken = menu.getItemInSlot(slot);
                PocketChickenData data = PocketChickenData.fromItem(chicken);
                if (data == null || !data.isAdult()) {
                    continue;
                }

                ItemStack resourceIcon = data.getResource();

                ItemStack chickResource;
                if (ThreadLocalRandom.current().nextInt(100) < config.getResourceFailRate()) {
                    chickResource = new ItemStack(Material.EGG);
                } else {
                    chickResource = resourceIcon.clone();
                }

                int speed = Math.max(1, (config.getResourceBaseTime() + data.getResourceTier() - 2 * data.getDNAStrength()) / getSpeed());
                MachineRecipe recipe = new MachineRecipe(
                    config.isTest() ? 1 : speed,
                    new ItemStack[] {chicken},
                    new ItemStack[] {chickResource}
                );
                if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    continue;
                }

                if (config.isPainEnabled()) {
                    if (!(data.getHealth() > 0.25) && !config.isPainDeathEnabled()) {
                        continue;
                    }
                    ChickenUtils.possiblyHarm(chicken);
                    if (ChickenUtils.getHealth(chicken) <= 0d) {
                        ItemUtils.consumeItem(chicken, false);
                        if (config.isSoundsEnabled()) {
                            GeneticChickengineering.getScheduler().run(() ->
                                menu.getLocation().getWorld().playSound(menu.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1f, 1f)
                            );
                        }
                        continue;
                    }
                }

                return recipe;
            }
        } finally {
            SimpleProfiler.record("ExcitationChamber.findNextRecipe", System.nanoTime() - __start);
        }
        return null;
    }

}
