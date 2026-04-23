package me.voper.slimeframe.implementation.items.machines;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.libraries.dough.blocks.BlockPosition;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.ItemUtils;
import com.github.drakescraft-labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft-labs.slimefun4.utils.SlimefunUtils;

import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.items.abstracts.AbstractMachine;
import me.voper.slimeframe.utils.Colors;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@ParametersAreNonnullByDefault
public class ThermiaExtractor extends AbstractMachine {

    public static final RecipeType RECIPE_TYPE = new RecipeType(Keys.createKey("thermia_extractor_recipe"), SFrameStacks.THERMIA_EXTRACTOR);

    private static final String BLOCK_KEY = "thermia_progress";
    private static final int TIME = Utils.secondsToSfTicks(1);
    private static final Map<BlockPosition, Integer> COUNTER_MAP = new HashMap<>();

    private boolean completed;
    private boolean isInNether = true;
    private int coolantCanisters;

    // This variable is just a hacky way to keep track of changes in the coolantCanisters value,
    // so we can improve the performance in the method process, updating the blockInfo only when necessary
    private int lastValue = -1;

    public ThermiaExtractor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        final BlockPosition blockPosition = new BlockPosition(b);
        int progress = COUNTER_MAP.getOrDefault(blockPosition, 0);

        if (coolantCanisters != lastValue)
            BlockStorage.addBlockInfo(b.getLocation(), BLOCK_KEY, String.valueOf(coolantCanisters));
        lastValue = coolantCanisters;

        // The machine will only perform an action every 20 minecraft ticks (1 second)
        if (progress % TIME == 0) {
            if (coolantCanisters < 5 && !completed) {
                for (int slot : getInputSlots()) {
                    ItemStack itemInSlot = menu.getItemInSlot(slot);
                    if (!SlimefunUtils.isItemSimilar(itemInSlot, SFrameStacks.COOLANT_CANISTER, true)) continue;

                    ItemUtils.consumeItem(itemInSlot, false);
                    menu.replaceExistingItem(getProgressSlots()[coolantCanisters++], new CustomItemStack(Material.RED_STAINED_GLASS_PANE, " "));
                    completed = (coolantCanisters == 5);
                    COUNTER_MAP.put(blockPosition, ++progress);
                    break;
                }

                menu.replaceExistingItem(getStatusSlot(), new CustomItemStack(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.WHITE + "Coolant Canisters: " + ChatColor.RED + coolantCanisters + "/5"));
                return coolantCanisters >= 1;
            } else {
                // Output slots are full
                if (!menu.fits(SFrameStacks.DILUTED_THERMIA, getOutputSlots())) {
                    MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(),
                            new CustomItemStack(Material.BARRIER, ChatColor.DARK_RED + "No space!"));
                    return true;
                }

                if (--coolantCanisters < 0) {
                    menu.pushItem(SFrameStacks.DILUTED_THERMIA.clone(), getOutputSlots());
                    coolantCanisters = 0;
                    completed = false;
                } else {
                    menu.replaceExistingItem(getProgressSlots()[coolantCanisters], new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
                    menu.replaceExistingItem(getStatusSlot(), new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, Colors.ORANGE + "Diluting Thermia..."));
                }

                COUNTER_MAP.put(blockPosition, ++progress);
                return true;

            }
        }

        if (completed) {
            COUNTER_MAP.put(blockPosition, ++progress);
            return true;
        }

        menu.replaceExistingItem(getStatusSlot(), new CustomItemStack(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.WHITE + "Coolant Canisters: " + ChatColor.RED + coolantCanisters + "/5"));
        COUNTER_MAP.put(blockPosition, ++progress);
        return true;
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return menu.getBlock().getRelative(BlockFace.DOWN).getType() == Material.MAGMA_BLOCK && isInNether;
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(),
                new CustomItemStack(Material.BARRIER, ChatColor.DARK_RED + "Error",
                        ChatColor.RED + "This machine must be in the nether",
                        ChatColor.RED + "and above a Magma Block")
        );
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        isInNether = b.getWorld().getEnvironment() == World.Environment.NETHER;

        if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), BLOCK_KEY) == null) {
            BlockStorage.addBlockInfo(b.getLocation(), BLOCK_KEY, "0");
            this.coolantCanisters = 0;
        } else {
            String locationInfo = BlockStorage.getLocationInfo(b.getLocation(), BLOCK_KEY);
            int progress = Integer.parseInt(locationInfo);
            if (progress == 0) return;
            for (int i = 0; i < progress; i++) {
                menu.replaceExistingItem(getProgressSlots()[i], new CustomItemStack(Material.RED_STAINED_GLASS_PANE, " "));
            }
            menu.replaceExistingItem(getStatusSlot(), new CustomItemStack(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.WHITE + "Progress: " + Colors.ORANGE + locationInfo + "/5"));
            this.coolantCanisters = progress;
        }

    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(getProgressSlots());

        // Progress slots border
        preset.drawBackground(new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "),
                new int[]{0, 9, 18, 27, 36, 45, 46, 2, 11, 20, 29, 38, 47});

        preset.drawBackground(ChestMenuUtils.getInputSlotTexture(),
                new int[]{3, 4, 5, 6, 7, 8, 12, 17, 21, 22, 23, 24, 25, 26});

        preset.drawBackground(ChestMenuUtils.getOutputSlotTexture(),
                new int[]{30, 31, 32, 33, 34, 35, 39, 44, 48, 49, 50, 51, 52, 53});

        preset.addItem(getStatusSlot(), MachineUtils.STATUS, ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[]{13, 14, 15, 16};
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[]{40, 41, 42, 43};
    }

    @Override
    public int getStatusSlot() {
        return 46;
    }

    protected int[] getProgressSlots() {
        return new int[]{37, 28, 19, 10, 1};
    }

}
