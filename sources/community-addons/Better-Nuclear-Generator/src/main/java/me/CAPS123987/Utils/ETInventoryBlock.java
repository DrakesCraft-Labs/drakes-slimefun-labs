package me.CAPS123987.Utils;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ETInventoryBlock {

    int[] getInputSlots();

    int[] getOutputSlots();

    default void createPreset(SlimefunItem item, Consumer<BlockMenuPreset> setup) {
        String title = item.getItemName();
        new BlockMenuPreset(item.getId(), title) {
            public void init() {
                setup.accept(this);
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow == ItemTransportFlow.INSERT ? getInputSlots() : getOutputSlots();
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK);
            }
        };
    }
    default void createPreset(SlimefunItem item, Consumer<BlockMenuPreset> setup, BiConsumer<BlockMenu,Block> instance) {
        String title = item.getItemName();
        new BlockMenuPreset(item.getId(), title) {
            public void init() {
                setup.accept(this);
            }
            @Override
            public void newInstance(BlockMenu menu, Block b) {
                instance.accept(menu, b);
            }

            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return flow == ItemTransportFlow.INSERT ? getInputSlots() : getOutputSlots();
            }

            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK);
            }
        };
    }
}
