package me.mmmjjkx.betterChests.items.chests.ie;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.DistinctiveItem;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.collections.Pair;
import dev.drake.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import me.mmmjjkx.betterChests.BCGroups;
import me.mmmjjkx.betterChests.BetterChests;
import me.mmmjjkx.betterChests.utils.ItemStackBuilder;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * A block that stored large amounts of one item
 *
 * @author Mooy1, mmmjjkx
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public final class IEStorageUnit extends SlimefunItem implements InventoryBlock, DistinctiveItem {

    static final NamespacedKey EMPTY_KEY = new NamespacedKey(BetterChests.INSTANCE, "empty"); // key for empty item
    static final NamespacedKey DISPLAY_KEY = new NamespacedKey(BetterChests.INSTANCE, "display"); // key for display item
    private static final NamespacedKey ITEM_KEY = new NamespacedKey(BetterChests.INSTANCE, "item"); // item key for item pdc
    private static final NamespacedKey AMOUNT_KEY = new NamespacedKey(BetterChests.INSTANCE, "stored"); // amount key for item pdc

    static final int INPUT_SLOT = 10;
    static final int DISPLAY_SLOT = 13;
    static final int STATUS_SLOT = 4;
    static final int OUTPUT_SLOT = 16;
    static final int INTERACT_SLOT = 22;

    private static final ItemStack INTERACTION_ITEM = new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE,
            "&aQuick Actions",
            "&bLeft Click: &7Withdraw 1 item",
            "&bRight Click: &7Withdraw 1 stack",
            "&bShift Left Click: &7Deposit inventory",
            "&bShift Right Click: &7Withdraw inventory"
    ).getItemStack();
    private static final ItemStack LOADING_ITEM = new ItemStackBuilder(Material.CYAN_STAINED_GLASS_PANE,
            "&bStatus",
            "&7Loading..."
    ).getItemStack();

    final int max;
    private final Map<Location, IEStorageCache> caches = new HashMap<>();

    public static final PersistentDataType<byte[], ItemStack> ITEM_STACK = new PersistentDataType<>() {
        @Override
        public @NotNull Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public @NotNull Class<ItemStack> getComplexType() {
            return ItemStack.class;
        }

        @Override
        public byte @NotNull [] toPrimitive(@NotNull ItemStack itemStack, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (BukkitObjectOutputStream output = new BukkitObjectOutputStream(bytes)) {
                output.writeObject(itemStack);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bytes.toByteArray();
        }

        @Override
        public @NotNull ItemStack fromPrimitive(byte @NotNull [] arr, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            ByteArrayInputStream bytes = new ByteArrayInputStream(arr);
            try (BukkitObjectInputStream input = new BukkitObjectInputStream(bytes)) {
                return (ItemStack) input.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                return new ItemStackBuilder(Material.STONE, "&cERROR").getItemStack();
            }
        }
    };

    public IEStorageUnit(SlimefunItemStack item, int max, ItemStack[] recipe) {
        super(BCGroups.STORAGES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.max = max;

        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                IEStorageCache cache = caches.get(b.getLocation());
                if (cache != null) {
                    cache.tick(b);
                }
            }

        }, new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                BlockMenu menu = BlockStorage.getInventory(e.getBlock());
                IEStorageCache cache = caches.remove(menu.getLocation());
                if (cache != null && !cache.isEmpty()) {
                    cache.destroy(e, drops);
                } else {
                    drops.add(getItem().clone());
                }
                menu.dropItems(menu.getLocation(), INPUT_SLOT, OUTPUT_SLOT);
            }

        }, new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                onPlace(e, e.getBlockPlaced());
            }

        });

        new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                IEStorageUnit.this.setup(this);
            }

            @Override
            public boolean canOpen(@NotNull Block block, @NotNull Player player) {
                return Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return itemTransportFlow == ItemTransportFlow.WITHDRAW ? getInputSlots() : getOutputSlots();
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                return flow == ItemTransportFlow.WITHDRAW ? getInputSlots(menu, item) : getOutputSlots();
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Location l) {
                caches.put(l, new IEStorageCache(IEStorageUnit.this, menu));
            }
        };
    }

    static void transferToStack(@Nonnull ItemStack source, @Nonnull ItemStack target) {
        Pair<ItemStack, Integer> data = loadFromStack(source);
        if (data != null) {
            target.setItemMeta(saveToStack(target.getItemMeta(), data.getFirstValue(),
                    ItemUtils.getItemName(data.getFirstValue()), data.getSecondValue()));
        }
    }

    static ItemMeta saveToStack(ItemMeta meta, ItemStack displayItem, String displayName, int amount) {
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            lore.add(ChatColor.GOLD + "Stored: " + displayName + ChatColor.YELLOW + " x " + amount);
            meta.setLore(lore);
        }
        meta.getPersistentDataContainer().set(ITEM_KEY, ITEM_STACK, displayItem);
        meta.getPersistentDataContainer().set(AMOUNT_KEY, PersistentDataType.INTEGER, amount);
        return meta;
    }

    public ItemStack getDisplayingItem(Block b) {
        List<MetadataValue> metadata = b.getMetadata("ie_item");
        if (metadata.isEmpty()) {
            return BlockStorage.getInventory(b).getItemInSlot(DISPLAY_SLOT);
        } else {
            return (ItemStack) metadata.get(0).value();
        }
    }

    public long getCapacity() {
        return max;
    }

    @Nullable
    private static Pair<ItemStack, Integer> loadFromStack(ItemStack source) {
        if (source.hasItemMeta()) {
            PersistentDataContainer con = source.getItemMeta().getPersistentDataContainer();
            Integer amount = con.get(AMOUNT_KEY, PersistentDataType.INTEGER);
            if (amount != null) {
                ItemStack item = con.get(ITEM_KEY, ITEM_STACK);
                if (item != null) {
                    return new Pair<>(item, amount);
                }
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public Collection<ItemStack> getDrops() {
        return Collections.emptyList();
    }

    private void onPlace(@Nonnull BlockPlaceEvent e, @Nonnull Block b) {
        Pair<ItemStack, Integer> data = loadFromStack(e.getItemInHand());
        if (data != null) {
            IEStorageCache cache = this.caches.get(b.getLocation());
            cache.load(data.getFirstValue(), data.getFirstValue().getItemMeta());
            cache.amount(data.getSecondValue());
            b.setMetadata("ie_item", new FixedMetadataValue(BetterChests.INSTANCE, data.getFirstValue()));
        }
    }

    private void setup(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(ChestMenuUtils.getInputSlotTexture(), new int[]{
                0, 1, 2, 9, 11, 18, 19, 20
        });
        blockMenuPreset.drawBackground(ChestMenuUtils.getBackground(), new int[]{
                3, 5, 12, 14, 21, 23
        });
        blockMenuPreset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), new int[]{
                6, 7, 8, 15, 17, 24, 25, 26
        });
        blockMenuPreset.addMenuClickHandler(DISPLAY_SLOT, ChestMenuUtils.getEmptyClickHandler());
        blockMenuPreset.addItem(INTERACT_SLOT, INTERACTION_ITEM);
        blockMenuPreset.addItem(STATUS_SLOT, LOADING_ITEM);
    }

    @Nonnull
    private int[] getInputSlots(DirtyChestMenu dirtyChestMenu, ItemStack itemStack) {
        IEStorageCache cache = this.caches.get(((BlockMenu) dirtyChestMenu).getLocation());
        if (cache != null && (cache.isEmpty() || cache.matches(itemStack))) {
            cache.input();
            return new int[]{INPUT_SLOT};
        } else {
            return new int[0];
        }
    }

    @Override
    public int[] getInputSlots() {
        return new int[]{INPUT_SLOT};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{OUTPUT_SLOT};
    }

    public void reloadCache(Block b) {
        this.caches.get(b.getLocation()).reloadData();
    }

    @Nullable
    public IEStorageCache getCache(Location location) {
        return this.caches.get(location);
    }

    @Override
    public boolean canStack(@Nonnull ItemMeta sfItemMeta, @Nonnull ItemMeta itemMeta) {
        return sfItemMeta.getPersistentDataContainer().equals(itemMeta.getPersistentDataContainer());
    }
}