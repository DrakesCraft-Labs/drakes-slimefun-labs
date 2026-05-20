package com.github.drakescraft_labs.bump.implementation.items.machines;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;

import dev.drake.dough.protection.Interaction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;

/**
 * Single-input / single-output menu machine on Slimefun Drake (replaces Guizhan {@code MenuBlock}).
 *
 * @author ybw0014
 */
public abstract class SimpleMenuBlock extends SlimefunItem implements InventoryBlock, EnergyNetComponent {

    private static final int[] BACKGROUND = {
        0, 4, 8, 9, 17, 18, 22, 26
    };
    private static final int[] INPUT_BACKGROUND = {
        1, 2, 3, 10, 12, 19, 20, 21
    };
    private static final int[] OUTPUT_BACKGROUND = {
        5, 6, 7, 14, 16, 23, 24, 25
    };
    private static final int INPUT_SLOT = 11;
    private static final int OPERATION_SLOT = 13;
    private static final int OUTPUT_SLOT = 15;

    @ParametersAreNonnullByDefault
    protected SimpleMenuBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(
            new BlockBreakHandler(false, false) {
                @Override
                public void onPlayerBreak(BlockBreakEvent e, ItemStack tool, List<ItemStack> drops) {
                    BlockMenu menu = BlockStorage.getInventory(e.getBlock());
                    if (menu != null) {
                        SimpleMenuBlock.this.onBreak(e, menu);
                    }
                }
            },
            new BlockPlaceHandler(false) {
                @Override
                public void onPlayerPlace(BlockPlaceEvent e) {
                    SimpleMenuBlock.this.onPlace(e, e.getBlockPlaced());
                }
            }
        );
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    protected final int[] getTransportSlots(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        return switch (flow) {
            case INSERT -> getInputSlots(menu, item);
            case WITHDRAW -> getOutputSlots();
        };
    }

    @ParametersAreNonnullByDefault
    protected int[] getInputSlots(DirtyChestMenu menu, ItemStack item) {
        return getInputSlots();
    }

    @ParametersAreNonnullByDefault
    protected void onNewInstance(BlockMenu blockMenu, Block b) {
        blockMenu.addMenuClickHandler(OPERATION_SLOT, (player, slot, itemStack, clickAction) -> {
            onOperate(blockMenu, b, player, clickAction);
            return false;
        });
    }

    @ParametersAreNonnullByDefault
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        var l = menu.getLocation();
        menu.dropItems(l, getInputSlots());
        menu.dropItems(l, getOutputSlots());
    }

    @ParametersAreNonnullByDefault
    protected void onPlace(BlockPlaceEvent e, Block b) {
        // default: no-op (Guizhan MenuBlock parity)
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                SimpleMenuBlock.this.setup(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                SimpleMenuBlock.this.onNewInstance(menu, b);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                return SimpleMenuBlock.this.getTransportSlots(menu, flow, item);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                if (p.hasPermission("slimefun.inventory.bypass")) {
                    return true;
                }
                return SimpleMenuBlock.this.canUse(p, false)
                    && (Slimefun.instance().isUnitTest()
                        || Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
            }
        };
    }

    @Nonnull
    protected abstract ItemStack getOperationSlotItem();

    @ParametersAreNonnullByDefault
    protected abstract void onOperate(BlockMenu menu, Block b, Player p, ClickAction action);

    @Override
    public abstract int getCapacity();

    @Nonnull
    @Override
    public final EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    protected final int getInputSlot() {
        return INPUT_SLOT;
    }

    protected final int getOutputSlot() {
        return OUTPUT_SLOT;
    }

    @ParametersAreNonnullByDefault
    private void setup(BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(ChestMenuUtils.getBackground(), BACKGROUND);
        blockMenuPreset.drawBackground(ChestMenuUtils.getInputSlotTexture(), INPUT_BACKGROUND);
        blockMenuPreset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), OUTPUT_BACKGROUND);

        blockMenuPreset.addItem(OPERATION_SLOT, getOperationSlotItem());
        blockMenuPreset.addMenuClickHandler(OPERATION_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { INPUT_SLOT };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] { OUTPUT_SLOT };
    }
}
