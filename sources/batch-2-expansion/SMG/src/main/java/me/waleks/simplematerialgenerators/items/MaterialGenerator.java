package me.waleks.simplematerialgenerators.items;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.ItemSetting;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.items.settings.IntRangeSetting;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.blocks.BlockPosition;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class MaterialGenerator extends SlimefunItem {

    private static final Map<BlockPosition, Integer> generatorProgress = new HashMap<>();

    private final ItemSetting<Integer> rate;
    private ItemStack item;

    @ParametersAreNonnullByDefault
    public MaterialGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int defaultRate) {
        super(itemGroup, item, recipeType, recipe);
        this.rate = new IntRangeSetting(this, "rate", 2, defaultRate, 1000);
        addItemSetting(this.rate);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            @ParametersAreNonnullByDefault
            public void tick(Block b, SlimefunItem sf, Config data) {
                MaterialGenerator.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    public void tick(@Nonnull Block b) {
        Block targetBlock = b.getRelative(BlockFace.UP);
        if (targetBlock.getType() == Material.CHEST) {
            BlockState state = targetBlock.getState(false);
            if (state instanceof InventoryHolder) {
                Inventory inv = ((InventoryHolder) state).getInventory();
                if (inv.firstEmpty() != -1) {
                    final BlockPosition pos = new BlockPosition(b);
                    int progress = generatorProgress.getOrDefault(pos, 0);

                    if (progress >= this.rate.getValue()) {
                        progress = 0;
                        inv.addItem(this.item);
                    } else {
                        progress++;
                    }
                    generatorProgress.put(pos, progress);
                }
            }
        }
    }

    public final MaterialGenerator setItem(@Nonnull Material material) {
        this.item = new ItemStack(material);
        return this;
    }
}

