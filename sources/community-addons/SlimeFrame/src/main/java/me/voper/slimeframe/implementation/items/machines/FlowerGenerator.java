package me.voper.slimeframe.implementation.items.machines;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import me.voper.slimeframe.implementation.items.abstracts.AbstractMachine;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import lombok.Setter;
import lombok.experimental.Accessors;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@Accessors(chain = true)
@ParametersAreNonnullByDefault
public class FlowerGenerator extends AbstractMachine {

    private static final int TIME = Utils.secondsToSfTicks(6);
    private static final Map<BlockPosition, Integer> PROGRESS_MAP = new HashMap<>();
    private static final Map<Material, ItemStack> POTTED_FLOWERS_MAP = new EnumMap<Material, ItemStack>(
            Tag.FLOWER_POTS.getValues().stream()
                    .filter(m -> m.name().startsWith("POTTED_"))
                    .collect(Collectors.toMap(Function.identity(), material -> {
                        Material flower = Material.getMaterial(material.name().substring(7));
                        return flower == null ? new ItemStack(material) : new ItemStack(flower);
                    }))
    );

    @Setter
    private int production = 1;
    private Material pottedFlower;

    public FlowerGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        final BlockPosition blockPosition = new BlockPosition(b);
        int progress = PROGRESS_MAP.getOrDefault(blockPosition, 0);

        if (progress >= TIME) {
            ItemStack output = POTTED_FLOWERS_MAP.get(pottedFlower).clone();
            output.setAmount(production);

            if (menu.fits(output, getOutputSlots())) {
                menu.pushItem(output, getOutputSlots());
                PROGRESS_MAP.put(blockPosition, 0);
                updateProgress(menu, 0);
                return true;
            }

            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
            return false;
        }

        PROGRESS_MAP.put(blockPosition, ++progress);
        updateProgress(menu, progress);
        return true;
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return recursiveCheck(menu.getBlock());
    }

    private boolean recursiveCheck(Block b) {
        Block relative = b.getRelative(BlockFace.UP);
        if (relative.getType() == this.getItem().getType()) {
            return recursiveCheck(relative);
        } else {
            pottedFlower = relative.getType();
            return POTTED_FLOWERS_MAP.containsKey(pottedFlower);
        }
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        menu.replaceExistingItem(getStatusSlot(), new CustomItemStack(Material.FLOWER_POT, ChatColor.RED + "There must be a potted flower above this machine."));
    }

    private void updateProgress(BlockMenu menu, int progress) {
        ChestMenuUtils.updateProgressbar(menu, getStatusSlot(), TIME - progress, TIME, new ItemStack(Material.SUNFLOWER));
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new int[]{0, 1, 2, 3, 5, 6, 7, 8});
        preset.drawBackground(new CustomItemStack(Material.PINK_STAINED_GLASS_PANE, " "), new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53});
        preset.addItem(getStatusSlot(), MachineUtils.STATUS, ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[]{19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    }

    @Override
    public int getStatusSlot() {
        return 4;
    }

}