package io.github.sefiraat.networks.slimefun.network;

import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.utils.Theme;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
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

public class NetworkWirelessReceiver extends NetworkObject {

    public static final int RECEIVED_SLOT = 13;

    private static final int[] BACKGROUND_SLOTS = new int[] {
            0, 1, 2, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26
    };

    private static final int[] RECEIVED_SLOTS_TEMPLATE = new int[] {
            3, 4, 5, 12, 14, 21, 22, 23
    };

    private static final CustomItemStack RECEIVED_BACKGROUND_STACK = new CustomItemStack(
            Material.GREEN_STAINED_GLASS_PANE,
            Theme.SUCCESS + "Received items");

    public NetworkWirelessReceiver(ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.WIRELESS_RECEIVER);
        this.getSlotsToDrop().add(RECEIVED_SLOT);

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
                            onTick(blockMenu);
                        }
                    }
                });
    }

    private void onTick(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final ItemStack itemStack = blockMenu.getItemInSlot(RECEIVED_SLOT);

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        definition.getNode().getRoot().addItemStack(itemStack);

    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                drawBackground(BACKGROUND_SLOTS);
                drawBackground(RECEIVED_BACKGROUND_STACK, RECEIVED_SLOTS_TEMPLATE);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return NetworkSlimefunItems.NETWORK_WIRELESS_RECEIVER.canUse(player, false)
                        && Slimefun.getProtectionManager()
                                .hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }

        };
    }

}
