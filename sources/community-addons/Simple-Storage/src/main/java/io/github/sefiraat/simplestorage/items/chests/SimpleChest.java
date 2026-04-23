package io.github.sefiraat.simplestorage.items.chests;

import com.github.drakescraft_labs.infinitylib.machines.MenuBlock;
import io.github.sefiraat.simplestorage.configuration.ManagerConfiguration;
import io.github.sefiraat.simplestorage.utils.Utils;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public final class SimpleChest extends MenuBlock {

    static final int SLOT_INFO = 0;
    static final int SLOT_BACK = 1;
    static final int SLOT_FORWARD = 2;
    static final int SLOT_INPUT = 4;
    static final int[] BACKGROUND_SLOTS = {3, 5, 6, 7, 8};

    private final Map<Location, SimpleInventoryCache> inventoryCaches = new HashMap<>();

    private final int pages;

    public SimpleChest(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int pages) {
        super(category, item, recipeType, recipe);

        this.pages = pages;

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem item, Config data) {
                SimpleInventoryCache simpleInventoryCache = SimpleChest.this.inventoryCaches.get(block.getLocation());
                if (simpleInventoryCache != null) {
                    simpleInventoryCache.process();
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
        return new int[] {SLOT_INPUT};
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent event, @Nonnull BlockMenu blockMenu) {
        super.onBreak(event, blockMenu);
        Location location = blockMenu.getLocation();
        SimpleInventoryCache simpleInventoryCache = inventoryCaches.remove(location);
        if (simpleInventoryCache != null) {
            simpleInventoryCache.kill(location);
        }
        blockMenu.dropItems(location, SLOT_INPUT);
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        super.onNewInstance(menu, b);
        String foundID = BlockStorage.getLocationInfo(b.getLocation(),"simple-chest-id");
        if (foundID != null) {
            inventoryCaches.put(b.getLocation(), new SimpleInventoryCache(menu, pages, Integer.parseInt(foundID)));
        } else {
            int chestID = ManagerConfiguration.getNextChestID();
            BlockStorage.addBlockInfo(b, "simple-chest-id", String.valueOf(chestID));
            ManagerConfiguration.setupChest(chestID);
            inventoryCaches.put(b.getLocation(), new SimpleInventoryCache(menu, pages, chestID));
        }
    }
}
