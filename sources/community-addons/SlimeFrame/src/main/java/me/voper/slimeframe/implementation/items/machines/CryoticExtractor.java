package me.voper.slimeframe.implementation.items.machines;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.items.abstracts.AbstractMachine;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import lombok.Setter;
import lombok.experimental.Accessors;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@Accessors(chain = true)
@ParametersAreNonnullByDefault
public class CryoticExtractor extends AbstractMachine {

    public static final RecipeType RECIPE_TYPE = new RecipeType(Keys.createKey("cryotic_recipe"), SFrameStacks.CRYOTIC_EXTRACTOR);

    private static final int TIME = Utils.secondsToSfTicks(5 * 60);
    private static final Map<BlockPosition, Integer> PROGRESS_MAP = new HashMap<>();
    public static final Map<Biome, Integer> VALID_BIOMES = new EnumMap<>(Biome.class);

    static {
        VALID_BIOMES.put(Biome.SNOWY_TAIGA, 2);
        VALID_BIOMES.put(Biome.SNOWY_PLAINS, 1);
        VALID_BIOMES.put(Biome.SNOWY_SLOPES, 1);
        VALID_BIOMES.put(Biome.SNOWY_BEACH, 1);
        VALID_BIOMES.put(Biome.ICE_SPIKES, 3);
        VALID_BIOMES.put(Biome.FROZEN_OCEAN, 2);
        VALID_BIOMES.put(Biome.FROZEN_PEAKS, 2);
        VALID_BIOMES.put(Biome.FROZEN_RIVER, 1);
        VALID_BIOMES.put(Biome.DEEP_FROZEN_OCEAN, 3);
    }

    @Setter
    private int processingSpeed = 1;

    public CryoticExtractor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        final BlockPosition blockPosition = new BlockPosition(b);
        int progress = PROGRESS_MAP.getOrDefault(blockPosition, 0);
        if (progress >= TIME) {
            int cryoticAmount = VALID_BIOMES.get(b.getBiome());
            ItemStack cryotic = new SlimefunItemStack(SFrameStacks.CRYOTIC, cryoticAmount);

            if (menu.fits(cryotic, getOutputSlots())) {
                menu.pushItem(cryotic, getOutputSlots());
                PROGRESS_MAP.put(blockPosition, 0);
                updateProgress(menu, 0);
                return true;
            }

            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
            return false;
        }

        progress += processingSpeed;
        PROGRESS_MAP.put(blockPosition, progress);
        updateProgress(menu, progress);
        return true;
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return VALID_BIOMES.containsKey(menu.getBlock().getBiome());
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(),
                new CustomItemStack(Material.BARRIER, ChatColor.RED + "There isn't any cryotic in this Biome!",
                        ChatColor.WHITE + "Valid Biomes:",
                        ChatColor.BLUE + "Snowy Taiga",
                        ChatColor.BLUE + "Snowy Plains",
                        ChatColor.BLUE + "Snowy Slopes",
                        ChatColor.BLUE + "Snowy Beach",
                        ChatColor.BLUE + "Ice Spikes"
                )
        );
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

    private void updateProgress(BlockMenu menu, int progress) {
        ChestMenuUtils.updateProgressbar(menu, getStatusSlot(), TIME - progress, TIME, new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "Extracting..."));
    }

}
