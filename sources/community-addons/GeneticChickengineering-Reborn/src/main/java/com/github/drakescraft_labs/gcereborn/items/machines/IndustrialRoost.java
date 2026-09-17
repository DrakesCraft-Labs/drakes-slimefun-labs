package com.github.drakescraft_labs.gcereborn.items.machines;

import java.util.ArrayList;
import java.util.List;
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
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.GuiItems;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;

public class IndustrialRoost extends AbstractMachine {

    private static final int[] INPUT_SLOTS = new int[] {10, 11, 12, 13};
    private static final int CATALYST_SLOT = 19;
    private static final int FEED_SLOT = 20;
    private static final int[] OUTPUT_SLOTS = new int[] {
        23, 24, 25, 26,
        32, 33, 34, 35,
        41, 42, 43, 44,
        50, 51, 52, 53
    };

    public IndustrialRoost(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WHEAT_SEEDS);
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
        preset.addItem(INFO_SLOT, GuiItems.BLACK_PANE, ChestMenuUtils.getEmptyClickHandler());

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, (p, slot, cursor, action) -> cursor != null && !cursor.getType().isAir());
        }
    }

    @Override
    protected void tick(@Nonnull Block b) {
        super.tick(b);
        if (GeneticChickengineering.getConfigService().isParticlesEnabled() && Math.random() < 0.15) {
            b.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, b.getLocation().add(0.5, 0.9, 0.5), 3, 0.3, 0.1, 0.3);
        }
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        List<ItemStack> adultChickens = new ArrayList<>();
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null && ChickenUtils.isPocketChicken(item) && ChickenUtils.isAdult(item)) {
                adultChickens.add(item);
            }
        }

        if (adultChickens.isEmpty()) {
            return null;
        }

        boolean hasCatalyst = false;
        ItemStack catItem = menu.getItemInSlot(CATALYST_SLOT);
        if (catItem != null && catItem.isSimilar(GCEItems.EXCITATION_CATALYST)) {
            hasCatalyst = true;
        }

        boolean hasVitaFeed = false;
        ItemStack feedItem = menu.getItemInSlot(FEED_SLOT);
        if (feedItem != null && feedItem.isSimilar(GCEItems.FORTIFIED_VITA_FEED)) {
            hasVitaFeed = true;
        }

        List<ItemStack> products = new ArrayList<>();
        for (ItemStack chick : adultChickens) {
            PocketChickenData data = PocketChickenData.fromItem(chick);
            if (data == null) continue;

            ItemStack prod = data.getResource().clone();
            if (hasCatalyst && ThreadLocalRandom.current().nextInt(100) < 35) {
                prod.setAmount(prod.getAmount() * 2);
            }
            products.add(prod);

            // Handle pain / harm if not protected by Vita-Feed
            if (GeneticChickengineering.getConfigService().isPainEnabled() && !hasVitaFeed) {
                ChickenUtils.possiblyHarm(chick);
            }
        }

        if (products.isEmpty()) {
            return null;
        }

        ItemStack[] outArray = products.toArray(new ItemStack[0]);
        MachineRecipe recipe = new MachineRecipe(
            GeneticChickengineering.getConfigService().isTest() ? 1 : Math.max(6, 16 - adultChickens.size() * 2),
            adultChickens.toArray(new ItemStack[0]),
            outArray
        );

        if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
            return null;
        }

        return recipe;
    }
}
