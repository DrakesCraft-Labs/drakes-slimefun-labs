package io.github.sefiraat.simplestorage.items.chests;

import dev.drake.infinitylib.machines.MenuBlock;
import io.github.sefiraat.simplestorage.utils.Utils;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class NetworkChest extends MenuBlock {

    static final int SLOT_INFO = 0;
    static final int SLOT_BACK = 1;
    static final int SLOT_FORWARD = 2;
    static final int[] BACKGROUND_SLOTS = {3, 4, 5};
    static final int[] AUGMENT_SLOTS = {6, 7, 8};

    private final Map<Location, NetworkInventoryCache> inventoryCaches = new HashMap<>();

    public NetworkChest(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem item, Config data) {
                NetworkInventoryCache networkInventoryCache = NetworkChest.this.inventoryCaches.get(block.getLocation());
                if (networkInventoryCache != null) {
                    networkInventoryCache.process();
                }
            }
        });
    }

    @Override
    protected void setup(@Nonnull BlockMenuPreset blockMenuPreset) {
        Utils.setUpChestMenu(blockMenuPreset, BACKGROUND_SLOTS, SLOT_BACK, SLOT_FORWARD, SLOT_INFO);

    }

    @Override
    protected int[] getInputSlots() {
        return new int[0];
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent event, @Nonnull BlockMenu blockMenu) {
        super.onBreak(event, blockMenu);
        Location location = blockMenu.getLocation();
        NetworkInventoryCache networkInventoryCache = inventoryCaches.remove(location);
        if (networkInventoryCache != null) {
            networkInventoryCache.kill(location);
        }
        blockMenu.dropItems(location, AUGMENT_SLOTS);
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        super.onNewInstance(menu, b);
        NetworkInventoryCache cache = new NetworkInventoryCache(menu);
        inventoryCaches.put(b.getLocation(), cache);
    }

}
