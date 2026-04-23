package dev.j3fftw.worldeditslimefun.tasks;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RefillInputsTask extends AbstractTask {

    protected final int[] slots;
    protected List<ItemStack> inputs = new ArrayList<>();

    public RefillInputsTask(SlimefunItem sfItem) {
        super(sfItem);

        BlockMenuPreset preset = Slimefun.getRegistry().getMenuPresets().get(sfItem.getId());
        this.slots = preset == null ? new int[0] : preset.getSlotsAccessedByItemTransport(ItemTransportFlow.INSERT);
    }

    @Override
    public void runTask(Block block) {
        BlockMenu menu = BlockStorage.getInventory(block);
        if (menu == null) {
            return;
        }

        for (ItemStack itemStack : this.inputs) {
            menu.pushItem(new ItemStack(itemStack), this.slots);
        }
    }


    public void setInputs(List<ItemStack> inputs) {
        this.inputs = inputs;
    }
}
