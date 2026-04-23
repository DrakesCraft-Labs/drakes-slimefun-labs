package me.voper.slimeframe.implementation.items.machines;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.commons.lang.Validate;
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import me.voper.slimeframe.implementation.items.abstracts.AbstractMachine;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

public class BasaltGenerator extends AbstractMachine {

    private static final int TIME = Utils.secondsToSfTicks(1);
    private static final Map<BlockPosition, Integer> PROGRESS_MAP = new HashMap<>();

    private int production = -1;

    public BasaltGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        final BlockPosition blockPosition = new BlockPosition(b);
        int progress = PROGRESS_MAP.getOrDefault(blockPosition, 0);
        if (progress >= TIME) {
            ItemStack basalt = new ItemStack(Material.BASALT, production);

            if (menu.fits(basalt, getOutputSlots())) {
                menu.pushItem(basalt, getOutputSlots());
                PROGRESS_MAP.put(blockPosition, 0);
                MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.FINISHED);
                return true;
            }

            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
            return false;
        }

        PROGRESS_MAP.put(blockPosition, ++progress);
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.RUNNING);
        return true;
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new int[]{0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 24, 25, 26, 27, 28, 29, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44});
        preset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), getOutputBorder());
        preset.addItem(getStatusSlot(), MachineUtils.STATUS, ChestMenuUtils.getEmptyClickHandler());
    }

    public BasaltGenerator setProduction(int production) {
        Validate.isTrue(production > 0, "Production must be positive");
        this.production = production;
        return this;
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[]{22};
    }

    protected int[] getOutputBorder() {
        return new int[]{12, 13, 14, 21, 23, 30, 31, 32};
    }

    @Override
    public int getStatusSlot() {
        return 4;
    }

    @Override
    protected void onBreak(BlockBreakEvent e, BlockMenu menu, Location l) {
        menu.dropItems(l, getOutputSlots());
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        if (production == -1) production = 1;
        super.register(addon);
    }
}
