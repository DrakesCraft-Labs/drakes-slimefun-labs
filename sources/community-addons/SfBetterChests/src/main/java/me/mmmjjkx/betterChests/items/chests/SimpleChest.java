package me.mmmjjkx.betterChests.items.chests;

import com.github.drakescraft_labs.slimefun4.api.events.AndroidMineEvent;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import me.mmmjjkx.betterChests.BCGroups;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class SimpleChest extends SlimefunItem implements InventoryBlock {
    private final int size;

    public SimpleChest(int size, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(BCGroups.STORAGES, item, recipeType, recipe);

        this.size = size;

        createPreset(this, bmp -> bmp.setSize(size));

        addItemHandler(new BlockBreakHandler(true, true) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent e, @NotNull ItemStack i, @NotNull List<ItemStack> drops) {
                drops.clear();
                drop(e.getBlock());
            }

            @Override
            public void onExplode(@NotNull Block b, @NotNull List<ItemStack> drops) {
                drops.clear();
                drop(b);
            }

            @Override
            public void onAndroidBreak(@NotNull AndroidMineEvent e) {
                drop(e.getBlock());
            }

            private void drop(Block b) {
                BlockMenu menu = BlockStorage.getInventory(b);
                menu.dropItems(b.getLocation(), getSlots());

                World world = b.getWorld();
                ItemStack item = getItem().clone();
                item.setType(b.getType());
                world.dropItemNaturally(b.getLocation(), item);
            }
        });
    }

    @Override
    public @NotNull Collection<ItemStack> getDrops() {
        return List.of(); // handled by BlockBreakHandler
    }

    @Override
    public int[] getInputSlots() {
        return getSlots();
    }

    @Override
    public int[] getOutputSlots() {
        return getInputSlots();
    }

    protected int[] getSlots() {
        return IntStream.range(0, size).toArray();
    }
}
