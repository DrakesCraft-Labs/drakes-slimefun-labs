package io.github.thebusybiscuit.chestterminal.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * The Export Bus pulls items from the CT Network Channel and places
 * them into the inventory it is attached to.
 *
 * @author TheBusyBiscuit
 */
public class ExportBus extends SlimefunItem {

    private static final int[] BORDER = {
        0, 1, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15,
        18, 22, 24, 27, 31, 33, 34, 35, 36, 40, 42, 43, 44,
        45, 46, 47, 48, 49, 50, 51, 52, 53, 32, 23, 41
    };

    @ParametersAreNonnullByDefault
    public ExportBus(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        new BlockMenuPreset(getId(), "\u00a73CT Export Bus") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                String owner = BlockStorage.getLocationInfo(b.getLocation(), "owner");
                return (owner != null && owner.equals(p.getUniqueId().toString())) || p.hasPermission("slimefun.cargo.bypass");
            }

            @Nonnull
            @Override
            public int[] getSlotsAccessedByItemTransport(@Nonnull ItemTransportFlow flow) {
                return new int[0];
            }
        };

        addItemHandler(new CTBlockBreakHandler(getInputSlots()));

        addItemHandler(new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Block b = e.getBlock();
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());
                BlockStorage.addBlockInfo(b, "index", "0");
                BlockStorage.addBlockInfo(b, "filter-type", "whitelist");
                BlockStorage.addBlockInfo(b, "filter-lore", "true");
                BlockStorage.addBlockInfo(b, "filter-durability", "true");
            }
        });
    }

    /**
     * Constructs the menu layout for the Export Bus.
     *
     * @param preset the {@link BlockMenuPreset} to populate
     */
    protected void constructMenu(@Nonnull BlockMenuPreset preset) {
        MenuClickHandler click = (p, slot, item, action) -> false;

        for (int i : BORDER) {
            preset.addItem(i, CustomItemStack.create(Material.CYAN_STAINED_GLASS_PANE, " "), click);
        }

        preset.addItem(7, CustomItemStack.create(Material.BLUE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(8, CustomItemStack.create(Material.BLUE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(16, CustomItemStack.create(Material.BLUE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(25, CustomItemStack.create(Material.BLUE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(26, CustomItemStack.create(Material.BLUE_STAINED_GLASS_PANE, " "), click);

        preset.addItem(2, CustomItemStack.create(Material.PAPER, "\u00a73Items", "", "\u00a7bPut in all Items you want to", "\u00a7bwhitelist"), click);
    }

    /**
     * Returns the inventory slots used for item input.
     *
     * @return an array of slot indices
     */
    @Nonnull
    public int[] getInputSlots() {
        return new int[] { 19, 20, 21, 28, 29, 30, 37, 38, 39 };
    }
}
