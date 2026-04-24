package io.github.sefiraat.networks.slimefun.network;

import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.utils.Theme;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.protection.Interaction;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class NetworkPowerDisplay extends NetworkObject {

    private static final int[] BACKGROUND_SLOTS = new int[] {
            0, 1, 2, 3, 5, 6, 7, 8
    };
    private static final int DISPLAY_SLOT = 4;

    private static final CustomItemStack EMPTY = new CustomItemStack(
            Material.RED_STAINED_GLASS_PANE,
            Theme.CLICK_INFO + "Status",
            Theme.PASSIVE + "Disconnected");

    public NetworkPowerDisplay(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.POWER_DISPLAY);
        addItemHandler(
                new BlockTicker() {
                    @Override
                    public boolean isSynchronized() {
                        return runSync();
                    }

                    @Override
                    public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                        BlockMenu blockMenu = BlockStorage.getInventory(block);
                        if (blockMenu != null) {
                            addToRegistry(block);
                            setDisplay(blockMenu);
                        }
                    }
                });
    }

    private void setDisplay(BlockMenu blockMenu) {
        if (blockMenu.hasViewer()) {
            final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

            if (definition.getNode() == null) {
                blockMenu.replaceExistingItem(DISPLAY_SLOT, EMPTY);
                return;
            }

            final NetworkRoot root = definition.getNode().getRoot();
            final long networkCharge = root.getRootPower();
            blockMenu.replaceExistingItem(DISPLAY_SLOT, getChargeStack(networkCharge));
        }
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                drawBackground(BACKGROUND_SLOTS);
                addItem(DISPLAY_SLOT, getChargeStack(0), (player, i, itemStack, clickAction) -> false);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return NetworkSlimefunItems.NETWORK_POWER_DISPLAY.canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(),
                                Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
    }

    private static CustomItemStack getChargeStack(long charge) {
        return new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                Theme.CLICK_INFO + "Status",
                Theme.PASSIVE + "Current Network Charge: " + charge + "j");
    }
}
