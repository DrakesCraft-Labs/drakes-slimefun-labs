package io.github.thebusybiscuit.chestterminal.items;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.events.BlockPlacerPlaceEvent;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.cargo.CargoNet;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;

/**
 * The Access Terminal allows players to browse and interact with items
 * stored in the Cargo network on the ChestTerminal channel.
 *
 * @author TheBusyBiscuit
 */
public class AccessTerminal extends SimpleSlimefunItem<BlockTicker> {

    private final int[] terminalSlots = {
        0, 1, 2, 3, 4, 5, 6,
        9, 10, 11, 12, 13, 14, 15,
        18, 19, 20, 21, 22, 23, 24,
        27, 28, 29, 30, 31, 32, 33,
        36, 37, 38, 39, 40, 41, 42
    };

    @ParametersAreNonnullByDefault
    public AccessTerminal(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        new BlockMenuPreset(getId(), "\u00a73CT Access Terminal") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                menu.replaceExistingItem(46, new CustomItemStack(SlimefunUtils.getCustomHead("f2599bd986659b8ce2c4988525c94e19ddd39fad08a38284a197f1b70675acc"), "\u00a77\u21E6 Previous Page", "", "\u00a7c(This may take up to a Second to update)"));
                menu.addMenuClickHandler(46, (p, slot, item, action) -> {
                    int page = getPage(b) - 1;
                    if (page > 0) {
                        BlockStorage.addBlockInfo(b, "page", String.valueOf(page));
                        newInstance(menu, b);
                    }
                    return false;
                });

                menu.replaceExistingItem(50, new CustomItemStack(SlimefunUtils.getCustomHead("c2f910c47da042e4aa28af6cc81cf48ac6caf37dab35f88db993accb9dfe516"), "\u00a77Next Page \u21E8", "", "\u00a7c(This may take up to a Second to update)"));
                menu.addMenuClickHandler(50, (p, slot, item, action) -> {
                    int page = getPage(b) + 1;
                    BlockStorage.addBlockInfo(b, "page", String.valueOf(page));
                    newInstance(menu, b);
                    return false;
                });
            }

            private int getPage(@Nonnull Block b) {
                String page = BlockStorage.getLocationInfo(b.getLocation(), "page");
                return page == null ? 1 : Integer.parseInt(page);
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Nonnull
            @Override
            public int[] getSlotsAccessedByItemTransport(@Nonnull ItemTransportFlow flow) {
                return new int[0];
            }
        };

        addItemHandler(new CTBlockBreakHandler(new int[] { 17, 44 }));

        addItemHandler(new BlockPlaceHandler(true) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "page", "1");
            }

            @Override
            public void onBlockPlacerPlace(@Nonnull BlockPlacerPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlock(), "page", "1");
            }
        });
    }

    /**
     * Constructs the menu layout for the Access Terminal.
     *
     * @param preset the {@link BlockMenuPreset} to populate
     */
    protected void constructMenu(@Nonnull BlockMenuPreset preset) {
        MenuClickHandler click = (p, slot, item, action) -> false;

        preset.addItem(45, CustomItemStack.create(Material.BLACK_STAINED_GLASS_PANE, " "), click);
        preset.addItem(46, CustomItemStack.create(Material.RED_STAINED_GLASS_PANE, "This will update shortly"));
        preset.addItem(47, CustomItemStack.create(Material.BLACK_STAINED_GLASS_PANE, " "), click);
        preset.addItem(48, CustomItemStack.create(Material.BLACK_STAINED_GLASS_PANE, " "), click);
        preset.addItem(49, CustomItemStack.create(Material.BLACK_STAINED_GLASS_PANE, " "), click);
        preset.addItem(50, CustomItemStack.create(Material.RED_STAINED_GLASS_PANE, "This will update shortly"));
        preset.addItem(51, CustomItemStack.create(Material.BLACK_STAINED_GLASS_PANE, " "), click);

        preset.addItem(7, CustomItemStack.create(Material.CYAN_STAINED_GLASS_PANE, " "), click);
        preset.addItem(8, CustomItemStack.create(Material.CYAN_STAINED_GLASS_PANE, " "), click);
        preset.addItem(16, CustomItemStack.create(Material.CYAN_STAINED_GLASS_PANE, " "), click);
        preset.addItem(25, CustomItemStack.create(Material.CYAN_STAINED_GLASS_PANE, " "), click);
        preset.addItem(26, CustomItemStack.create(Material.CYAN_STAINED_GLASS_PANE, " "), click);

        preset.addItem(34, CustomItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(35, CustomItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(43, CustomItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(52, CustomItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, " "), click);
        preset.addItem(53, CustomItemStack.create(Material.ORANGE_STAINED_GLASS_PANE, " "), click);
    }

    @Nonnull
    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            private final ItemStack item = CustomItemStack.create(Material.BARRIER, "\u00a74No Cargo Net connected!");
            private final MenuClickHandler click = (p, slot, stack, action) -> false;

            @Override
            public void tick(@Nonnull Block b, @Nonnull SlimefunItem sf, @Nonnull Config data) {
                if (CargoNet.getNetworkFromLocation(b.getLocation()) == null) {
                    BlockMenu menu = BlockStorage.getInventory(b);

                    for (int slot : terminalSlots) {
                        menu.replaceExistingItem(slot, item);
                        menu.addMenuClickHandler(slot, click);
                    }
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }
}
