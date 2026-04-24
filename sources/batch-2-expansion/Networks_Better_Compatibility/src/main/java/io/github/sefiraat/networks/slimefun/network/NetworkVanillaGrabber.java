package io.github.sefiraat.networks.slimefun.network;


import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class NetworkVanillaGrabber extends NetworkDirectional {

    private static final int[] BACKGROUND_SLOTS = new int[]{
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18, 20, 22, 23, 24, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int INPUT_SLOT = 25;
    private static final int NORTH_SLOT = 11;
    private static final int SOUTH_SLOT = 29;
    private static final int EAST_SLOT = 21;
    private static final int WEST_SLOT = 19;
    private static final int UP_SLOT = 25;
    private static final int DOWN_SLOT = 32;

    public NetworkVanillaGrabber(ItemGroup itemGroup,
                                 SlimefunItemStack item,
                                 RecipeType recipeType,
                                 ItemStack[] recipe
    ) {
        super(itemGroup, item, recipeType, recipe, NodeType.GRABBER);
    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        if (blockMenu != null) {
            tryGrabItem(blockMenu);
        }
    }

    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final BlockFace direction = getCurrentDirection(blockMenu);
        final Block block = blockMenu.getBlock();
        final Block targetBlock = blockMenu.getBlock().getRelative(direction);
        final UUID uuid = UUID.fromString(BlockStorage.getLocationInfo(block.getLocation(), OWNER_KEY));
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if (!Slimefun.getProtectionManager().hasPermission(offlinePlayer, targetBlock, Interaction.INTERACT_BLOCK)) {
            return;
        }

        final BlockState blockState = targetBlock.getState();

        if (!(blockState instanceof InventoryHolder holder)) {
            return;
        }

        final Inventory inventory = holder.getInventory();
        final ItemStack stack = blockMenu.getItemInSlot(INPUT_SLOT);

        if (stack != null && stack.getType() != Material.AIR) {
            return;
        }

        boolean wildChests = Networks.getSupportedPluginManager().isWildChests();
        boolean isChest = wildChests && isWildChest(targetBlock);

        sendDebugMessage(block.getLocation(), "WildChests detected: " + wildChests);
        sendDebugMessage(block.getLocation(), "Block detected as chest: " + isChest);

        if (inventory instanceof FurnaceInventory furnace) {
            final ItemStack result = furnace.getResult();
            if (result != null && result.getType() != Material.AIR) {
                blockMenu.replaceExistingItem(INPUT_SLOT, result);
                furnace.setResult(null);
            }
        } else if (inventory instanceof BrewerInventory brewer) {
            for (int i = 0; i < 3; i++) {
                final ItemStack fuel = brewer.getContents()[i];
                if (fuel != null && fuel.getType() != Material.AIR) {
                    blockMenu.replaceExistingItem(INPUT_SLOT, fuel);
                    final ItemStack[] contents = brewer.getContents();
                    contents[i] = null;
                    brewer.setContents(contents);
                    return;
                }
            }
        } else if (wildChests && isChest) {
            sendDebugMessage(block.getLocation(), "WildChest test failed, escaping");
        } else if (InvUtils.getFirstItem(inventory) != null) {
            final ItemStack item = InvUtils.getFirstItem(inventory);
            if (item != null) {
                blockMenu.replaceExistingItem(INPUT_SLOT, item.clone());
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    private boolean isWildChest(Block block) {
        try {
            Class<?> apiClass = Class.forName("com.bgsoftware.wildchests.api.WildChestsAPI");
            java.lang.reflect.Method getChestMethod = apiClass.getMethod("getChest", org.bukkit.Location.class);
            return getChestMethod.invoke(null, block.getLocation()) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @Override
    public int getNorthSlot() {
        return NORTH_SLOT;
    }

    @Override
    public int getSouthSlot() {
        return SOUTH_SLOT;
    }

    @Override
    public int getEastSlot() {
        return EAST_SLOT;
    }

    @Override
    public int getWestSlot() {
        return WEST_SLOT;
    }

    @Override
    public int getUpSlot() {
        return UP_SLOT;
    }

    @Override
    public int getDownSlot() {
        return DOWN_SLOT;
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{INPUT_SLOT};
    }

    @Override
    public boolean runSync() {
        return true;
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.GRAY, 1);
    }
}
