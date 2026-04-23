package dev.j3fftw.worldeditslimefun.tasks;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class VoidOutputsTask extends AbstractTask {
    private final int[] slots;

    public VoidOutputsTask(SlimefunItem sfItem) {
        super(sfItem);

        BlockMenuPreset preset = Slimefun.getRegistry().getMenuPresets().get(sfItem.getId());
        this.slots = preset == null ? new int[0] : preset.getSlotsAccessedByItemTransport(ItemTransportFlow.WITHDRAW);
    }

    @Override
    public void runTask(Block block) {
        BlockMenu menu = BlockStorage.getInventory(block);
        if (menu == null) {
            return;
        }

        for (int slot : this.slots) {
            menu.replaceExistingItem(slot, new ItemStack(Material.AIR));
        }
    }
}
