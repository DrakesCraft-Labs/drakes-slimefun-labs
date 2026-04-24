package me.voper.slimeframe.implementation.items.machines;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.blocks.BlockPosition;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractMachine;
import me.voper.slimeframe.implementation.items.multiblocks.Foundry;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

public class TelluriumFragmentsSynthesizer extends AbstractMachine {

    public static final RecipeType RECIPE_TYPE = new RecipeType(Keys.createKey("tellurium_frags_recipe"), SFrameStacks.TELLURIUM_FRAGMENTS_SYNTHESIZER);

    private static final int TIME = Utils.secondsToSfTicks(3 * 60);
    private static final Map<BlockPosition, Integer> PROGRESS_MAP = new HashMap<>();

    public TelluriumFragmentsSynthesizer(SlimefunItemStack item, ItemStack[] recipe) {
        super(Groups.MACHINES, item, Foundry.RECIPE_TYPE, recipe);
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        final BlockPosition blockPosition = new BlockPosition(b);
        int progress = PROGRESS_MAP.getOrDefault(blockPosition, 0);
        if (progress >= TIME) {
            ItemStack fragment = SFrameStacks.TELLURIUM_FRAGMENT.clone();

            if (menu.fits(fragment, getOutputSlots())) {
                menu.pushItem(fragment, getOutputSlots());
                PROGRESS_MAP.put(blockPosition, 0);
                menu.replaceExistingItem(getStatusSlot(), MachineUtils.FINISHED);
                return true;
            }

            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
            return false;
        }

        PROGRESS_MAP.put(blockPosition, ++progress);
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.CONDUIT, ChatColor.RED + "Synthesizing..."));
        return true;
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new int[]{0, 1, 2, 3, 5, 6, 7, 8});
        preset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53});
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
