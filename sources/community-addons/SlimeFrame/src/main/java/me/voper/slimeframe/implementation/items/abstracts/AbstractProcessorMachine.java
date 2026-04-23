package me.voper.slimeframe.implementation.items.abstracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineProcessHolder;
import com.github.drakescraft_labs.slimefun4.core.machines.MachineProcessor;
import com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation;
import com.github.drakescraft_labs.slimefun4.libraries.commons.lang.Validate;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;
import com.github.drakescraft_labs.slimefun4.utils.itemstack.ItemStackWrapper;

import me.voper.slimeframe.implementation.items.machines.MachineDesign;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;

@ParametersAreNonnullByDefault
public abstract class AbstractProcessorMachine extends AbstractMachine implements MachineProcessHolder<CraftingOperation> {

    private final MachineProcessor<CraftingOperation> processor = new MachineProcessor<>(this);
    @Getter
    private int processingSpeed = -1;
    protected List<MachineRecipe> recipes = new ArrayList<>();

    public AbstractProcessorMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.processor.setProgressBar(getProgressBar());
    }

    public AbstractProcessorMachine setProcessingSpeed(int speed) {
        Validate.isTrue(speed > 0, "Speed must be positive");
        this.processingSpeed = speed;
        return this;
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        if (processingSpeed == -1) processingSpeed = 1;
        super.register(addon);
    }

    public void registerRecipe(MachineRecipe recipe) {
        // Here we must divide the recipe ticks by 2 because the MachineRecipe constructor multiplies it by 2
        // Basically the constructor assumes that the tick rate is always the same: 1 second = 2 sf ticks = 20 minecraft ticks (sf ticks every 10 minecraft ticks)
        // So it multiplies the amount of seconds by 2 to get the total of sf ticks
        recipe.setTicks(Utils.secondsToSfTicks(recipe.getTicks() / 2) / getProcessingSpeed());
        recipes.add(recipe);
    }

    public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
        registerRecipe(new MachineRecipe(seconds, input, output));
    }

    public void registerRecipe(int seconds, ItemStack input, ItemStack output) {
        registerRecipe(seconds, new ItemStack[]{input}, new ItemStack[]{output});
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        CraftingOperation operation = processor.getOperation(b);

        if (operation != null) {
            if (!operation.isFinished()) {
                updateProgress(menu, b);
                operation.addProgress(processingSpeed);
            } else {
                MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.FINISHED);
                for (ItemStack output : operation.getResults()) {
                    menu.pushItem(output.clone(), getOutputSlots());
                }
                processor.endOperation(b);
            }
            return true;
        } else {
            MachineRecipe nextRecipe = findNextRecipe(menu);
            if (nextRecipe != null) {
                operation = new CraftingOperation(nextRecipe);
                processor.startOperation(b, operation);
                updateProgress(menu, b);
                return true;
            }
            MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.WAITING);
            return false;
        }
    }

    protected abstract ItemStack getProgressBar();

    protected void updateProgress(BlockMenu menu, Block b) {
        Validate.notNull(processor.getOperation(b), "Operation must not be null");
        processor.updateProgressBar(menu, getStatusSlot(), processor.getOperation(b));
    }

    protected MachineRecipe findNextRecipe(BlockMenu menu) {
        // Creates a slot-ItemStack map according to the input slots
        Map<Integer, ItemStack> inv = new HashMap<>();

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item != null) {
                inv.put(slot, ItemStackWrapper.wrap(item));
            }
        }

        // If the output slots are full, return null
        int maxedSlots = 0;
        for (int slot : getOutputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item != null && item.getAmount() == item.getMaxStackSize()) {
                maxedSlots += 1;
            }
        }
        if (maxedSlots == getOutputSlots().length) {
            return null;
        }

        // For each recipe, we must check if the input actually matches the recipe input
        Map<Integer, Integer> found = new HashMap<>();
        for (MachineRecipe recipe : recipes) {
            for (ItemStack input : recipe.getInput()) {
                for (int slot : getInputSlots()) {
                    // It also checks the amount of both items
                    if (SlimefunUtils.isItemSimilar(inv.get(slot), input, true)) {
                        found.put(slot, input.getAmount());
                        break;
                    }
                }
            }

            if (found.size() == recipe.getInput().length) {
                if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
                    return null;
                }

                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    menu.consumeItem(entry.getKey(), entry.getValue());
                }

                return recipe;
            } else {
                found.clear();
            }
        }

        return null;
    }

    @Nonnull
    @Override
    public MachineProcessor<CraftingOperation> getMachineProcessor() {
        return processor;
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(MachineDesign.CRAFTING_MACHINE.background());
        preset.drawBackground(ChestMenuUtils.getInputSlotTexture(), MachineDesign.CRAFTING_MACHINE.inputBorder());
        preset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), MachineDesign.CRAFTING_MACHINE.outputBorder());
        preset.addItem(getStatusSlot(), MachineUtils.STATUS, ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return MachineDesign.CRAFTING_MACHINE.inputSlots();
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return MachineDesign.CRAFTING_MACHINE.outputSlots();
    }

    @Override
    public int getStatusSlot() {
        return MachineDesign.CRAFTING_MACHINE.statusSlot();
    }
}
