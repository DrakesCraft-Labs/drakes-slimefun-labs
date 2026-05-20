package io.github.sefiraat.networks.slimefun.network;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.DistinctiveItem;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.utils.ChatUtils;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import dev.drake.dough.data.persistent.PersistentDataAPI;
import dev.drake.dough.protection.Interaction;
import dev.drake.dough.protection.ProtectionManager;
import io.github.sefiraat.networks.network.stackcaches.ItemStackCache;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.utils.*;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import lombok.Getter;
import lombok.Setter;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkQuantumStorage extends SlimefunItem implements DistinctiveItem {

    public static final String BS_AMOUNT = "stored_amount";
    public static final String BS_VOID = "void_excess";
    public static final String BS_CUSTOM_MAX_AMOUNT = "custom_max_amount";

    public static final int INPUT_SLOT = 1;
    public static final int ITEM_SLOT = 4;
    public static final int ITEM_SET_SLOT = 13;
    public static final int OUTPUT_SLOT = 7;

    private static final int[] SIZES = new int[]{
            4096,
            32768,
            262144,
            2097152,
            16777216,
            134217728,
            1073741824,
            Integer.MAX_VALUE
    };
    private static final ItemStack BACK_INPUT = ItemCreator.create(
            Material.GREEN_STAINED_GLASS_PANE,
            Theme.PASSIVE + "Input"
    );

    private static final ItemStack BACK_ITEM = ItemCreator.create(
            Material.BLUE_STAINED_GLASS_PANE,
            Theme.PASSIVE + "Item Stored"
    );

    private static final ItemStack NO_ITEM = ItemCreator.create(
            Material.RED_STAINED_GLASS_PANE,
            Theme.ERROR + "No Registered Item",
            Theme.PASSIVE + "Click the icon below while",
            Theme.PASSIVE + "holding an item to register it."
    );

    private static final ItemStack SET_ITEM = ItemCreator.create(
            Material.LIME_STAINED_GLASS_PANE,
            Theme.SUCCESS + "Set Item",
            Theme.PASSIVE + "Drag an item on top of this pane to register it.",
            Theme.PASSIVE + "Shift Click to change voiding"
    );

    private static final ItemStack SET_ITEM_SUPPORTING_CUSTOM_MAX = ItemCreator.create(
            Material.LIME_STAINED_GLASS_PANE,
            Theme.SUCCESS + "Set up",
            Theme.PASSIVE + "Pick up the item and click here to set the item",
            Theme.CLICK_INFO + "Click here to set change capacity",
            Theme.CLICK_INFO + "Shift+left click" + Theme.PASSIVE + " Toggle Void input"
    );

    private static final ItemStack BACK_OUTPUT = ItemCreator.create(
            Material.ORANGE_STAINED_GLASS_PANE,
            Theme.PASSIVE + "Output"
    );

    private static final int[] INPUT_SLOTS = new int[]{0, 2};
    private static final int[] ITEM_SLOTS = new int[]{3, 5};
    private static final int[] OUTPUT_SLOTS = new int[]{6, 8};
    private static final int[] BACKGROUND_SLOTS = new int[]{9, 10, 11, 12, 14, 15, 16, 17};

    private static final Map<Location, QuantumCache> CACHES = new HashMap<>();

    static {
        final ItemMeta itemMeta = NO_ITEM.getItemMeta();
        PersistentDataAPI.setBoolean(itemMeta, Keys.newKey("display"), true);
        NO_ITEM.setItemMeta(itemMeta);
    }

    private final List<Integer> slotsToDrop = new ArrayList<>();
    @Getter
    private final int maxAmount;
    @Setter
    private boolean supportsCustomMaxAmount = false;

    public NetworkQuantumStorage(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int maxAmount) {
        super(itemGroup, item, recipeType, recipe);
        this.maxAmount = maxAmount;
        slotsToDrop.add(INPUT_SLOT);
        slotsToDrop.add(OUTPUT_SLOT);
    }

    @ParametersAreNonnullByDefault
    public static void tryInputItem(Location location, ItemStack[] input, QuantumCache cache) {
        if (cache.getItemStack() == null) {
            return;
        }

        for (ItemStack itemStack : input) {
            if (isBlacklisted(itemStack)) {
                continue;
            }

            if (StackUtils.itemsMatch(cache, itemStack, true)) {
                int leftover = cache.increaseAmount(itemStack.getAmount());
                itemStack.setAmount(leftover);
            }
        }

        syncBlock(location, cache);
    }

    private static boolean isBlacklisted(@Nonnull ItemStack itemStack) {
        return itemStack.getType() == Material.AIR
                || itemStack.getType().getMaxDurability() < 0
                || Tag.SHULKER_BOXES.isTagged(itemStack.getType())
                || SlimefunItem.getByItem(itemStack) instanceof NetworkQuantumStorage;
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public static ItemStack getItemStack(QuantumCache cache, BlockMenu blockMenu) {
        if (cache.getItemStack() == null || cache.getAmount() <= 0) {
            return null;
        }
        return getItemStack(cache, blockMenu, cache.getItemStack().getMaxStackSize());
    }

    @ParametersAreNonnullByDefault
    @Nullable
    public static ItemStack getItemStack(QuantumCache cache, BlockMenu blockMenu, int amount) {
        if (cache.getAmount() < amount) {
            // Storage has no content or not enough, mix and match!
            ItemStack output = blockMenu.getItemInSlot(OUTPUT_SLOT);
            ItemStack fetched = cache.withdrawItem(amount);

            if (output != null
                    && output.getType() != Material.AIR
                    && StackUtils.itemsMatch(cache, output, true)
            ) {
                // We have an output item we can use also
                if (fetched == null || fetched.getType() == Material.AIR) {
                    // Storage is totally empty - just use output slot
                    fetched = output.clone();
                    if (fetched.getAmount() > amount) {
                        fetched.setAmount(amount);
                    }
                    output.setAmount(output.getAmount() - fetched.getAmount());
                } else {
                    // Storage has content, lets add on top of it
                    int additional = Math.min(amount - fetched.getAmount(), output.getAmount());
                    output.setAmount(output.getAmount() - additional);
                    fetched.setAmount(fetched.getAmount() + additional);
                }
            }
            syncBlock(blockMenu, cache);
            return fetched;
        } else {
            // Storage has everything we need
            ItemStack fetched = cache.withdrawItem(amount);
            syncBlock(blockMenu, cache);
            return fetched;
        }
    }

    private static void updateDisplayItem(@Nonnull BlockMenu menu, @Nonnull QuantumCache cache) {
        if (cache.getItemStack() == null) {
            menu.replaceExistingItem(ITEM_SLOT, NO_ITEM);
        } else {
            final ItemStack itemStack = cache.getItemStack().clone();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            final List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
            lore.add("");
            lore.add(Theme.CLICK_INFO + "Voiding: " + Theme.PASSIVE + StringUtils.toTitleCase(String.valueOf(cache.isVoidExcess())));
            lore.add(Theme.CLICK_INFO + "Amount: " + Theme.PASSIVE + cache.getAmount());
            if (cache.supportsCustomMaxAmount()) {
                // Cache limit is set at the potentially custom max amount set
                // The player could set the custom maximum amount to be the actual maximum amount
                lore.add(Theme.CLICK_INFO + "Capacity: " + Theme.SUCCESS + cache.getLimit());
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(1);
            menu.replaceExistingItem(ITEM_SLOT, itemStack);
        }
    }

    private static void syncBlock(@Nonnull Location location, @Nonnull QuantumCache cache) {
        BlockStorage.addBlockInfo(location, BS_AMOUNT, String.valueOf(cache.getAmount()));
        BlockStorage.addBlockInfo(location, BS_VOID, String.valueOf(cache.isVoidExcess()));
        if (cache.supportsCustomMaxAmount()) {
            BlockStorage.addBlockInfo(location, BS_CUSTOM_MAX_AMOUNT, String.valueOf(cache.getLimit()));
        }
    }

    private static void syncBlock(@Nonnull BlockMenu blockMenu, @Nonnull QuantumCache cache) {
        syncBlock(blockMenu.getLocation(), cache);
        blockMenu.markDirty();
    }

    public static Map<Location, QuantumCache> getCaches() {
        return CACHES;
    }

    public static int[] getSizes() {
        return SIZES;
    }

    public static void setItem(@Nonnull BlockMenu blockMenu, @Nonnull ItemStack itemStack, @Nonnull int amount) {
        if (isBlacklisted(itemStack)) {
            return;
        }

        final QuantumCache cache = CACHES.get(blockMenu.getLocation());
        if (cache == null || cache.getAmount() > 0) {
            return;
        }
        itemStack.setAmount(1);
        cache.setItemStack(itemStack);
        cache.setAmount(amount);
        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu, cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    @Override
    public void preRegister() {
        addItemHandler(
                new BlockTicker() {
                    @Override
                    public boolean isSynchronized() {
                        return true;
                    }

                    @Override
                    public void tick(Block b, SlimefunItem item, Config data) {
                        onTick(b);
                    }
                },
                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent event, ItemStack item, List<ItemStack> drops) {
                        onBreak(event);
                    }
                },
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                        onPlace(event);
                    }
                }
        );
    }

    private void onTick(Block block) {
        final BlockMenu blockMenu = BlockStorage.getInventory(block);

        if (blockMenu == null) {
            CACHES.remove(block.getLocation());
            return;
        }

        final QuantumCache cache = CACHES.get(blockMenu.getLocation());

        if (cache == null) {
            return;
        }

        if (blockMenu.hasViewer()) {
            updateDisplayItem(blockMenu, cache);
        }

        // Move items from the input slot into the card
        final ItemStack input = blockMenu.getItemInSlot(INPUT_SLOT);
        if (input != null && input.getType() != Material.AIR) {
            tryInputItem(blockMenu.getLocation(), new ItemStack[]{input}, cache);
        }

        // Output items
        final ItemStack output = blockMenu.getItemInSlot(OUTPUT_SLOT);
        ItemStack fetched = null;
        if (output == null || output.getType() == Material.AIR) {
            // No item in output, try output
            fetched = cache.withdrawItem();
        } else if (StackUtils.itemsMatch(cache, output, true) && output.getAmount() < output.getMaxStackSize()) {
            // There is an item, but it's not filled so lets top it up if we can
            final int requestAmount = output.getMaxStackSize() - output.getAmount();
            fetched = cache.withdrawItem(requestAmount);
        }

        if (fetched != null && fetched.getType() != Material.AIR) {
            blockMenu.pushItem(fetched, OUTPUT_SLOT);
            syncBlock(blockMenu, cache);
            blockMenu.markDirty();
        }

        CACHES.put(blockMenu.getLocation().clone(), cache);
        blockMenu.markDirty();
    }

    private void toggleVoid(@Nonnull BlockMenu blockMenu) {
        final QuantumCache cache = CACHES.get(blockMenu.getLocation());
        cache.setVoidExcess(!cache.isVoidExcess());
        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu, cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    private void setItem(@Nonnull BlockMenu blockMenu, @Nonnull Player player) {
        final ItemStack itemStack = player.getItemOnCursor().clone();

        if (isBlacklisted(itemStack)) {
            return;
        }

        final QuantumCache cache = CACHES.get(blockMenu.getLocation());
        if (cache == null || cache.getAmount() > 0) {
            player.sendMessage(Theme.WARNING + "Quantum Storage must be empty before changing the set item.");
            return;
        }
        itemStack.setAmount(1);
        cache.setItemStack(itemStack);
        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu, cache);
        CACHES.put(blockMenu.getLocation(), cache);
    }

    private void setCustomMaxAmount(@Nonnull BlockMenu blockMenu, @Nonnull Player player, @Nonnull int newMaxAmount) {
        final QuantumCache cache = CACHES.get(blockMenu.getLocation());
        if (cache == null || !cache.supportsCustomMaxAmount()) {
            Utils.send(player, "Quantum storage does not exist and cannot be set. Please check whether quantum storage exists!");
            return;
        }

        cache.setLimit(newMaxAmount);
        updateDisplayItem(blockMenu, cache);
        syncBlock(blockMenu, cache);
        CACHES.put(blockMenu.getLocation(), cache);

        player.sendMessage(
                Theme.PASSIVE + "[" + Theme.GOLD + "Networks" + Theme.PASSIVE + "] " +
                        Theme.SUCCESS + "Capacity changed: " + newMaxAmount
        );
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                for (int i : INPUT_SLOTS) {
                    addItem(i, BACK_INPUT, (p, slot, item, action) -> false);
                }
                for (int i : ITEM_SLOTS) {
                    addItem(i, BACK_ITEM, (p, slot, item, action) -> false);
                }
                for (int i : OUTPUT_SLOTS) {
                    addItem(i, BACK_OUTPUT, (p, slot, item, action) -> false);
                }
                ItemStack setItemItemstack = supportsCustomMaxAmount ? SET_ITEM_SUPPORTING_CUSTOM_MAX : SET_ITEM;
                addItem(ITEM_SET_SLOT, setItemItemstack, (p, slot, item, action) -> false);
                addMenuClickHandler(ITEM_SLOT, ChestMenuUtils.getEmptyClickHandler());
                drawBackground(BACKGROUND_SLOTS);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return Slimefun.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(player.getUniqueId()), block, Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return new int[]{INPUT_SLOT};
                } else if (flow == ItemTransportFlow.WITHDRAW) {
                    return new int[]{OUTPUT_SLOT};
                }
                return new int[0];
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block block) {
                menu.addMenuClickHandler(ITEM_SET_SLOT, (p, slot, item, action) -> {
                    final QuantumCache cache = CACHES.get(menu.getLocation());
                    if (action.isShiftClicked()) {
                        toggleVoid(menu);
                    } else if (
                            cache != null &&
                                    cache.supportsCustomMaxAmount() &&
                                    p.getItemOnCursor().getType() == Material.AIR
                    ) {
                        p.closeInventory();
                        p.sendMessage(
                                Theme.PASSIVE + "[" + Theme.GOLD + "Networks" + Theme.PASSIVE + "] " +
                                        Theme.WARNING + "Please enter the capacity of the network's quantum storage. The maximum limit is: " + Integer.MAX_VALUE + " !");
                        ChatUtils.awaitInput(p, s -> {
                            // Catching the error is cleaner than directly validating the string
                            try {
                                if (s.isBlank()) {
                                    return;
                                }
                                int newMax = Math.max(1, Math.min(Integer.parseInt(s), maxAmount));
                                setCustomMaxAmount(menu, p, newMax);
                            } catch (NumberFormatException e) {
                                p.sendMessage(
                                        Theme.PASSIVE + "[" + Theme.GOLD + "Network" + Theme.PASSIVE + "] " +
                                                Theme.ERROR + "Network quantum storage must be: 1 to " + Integer.MAX_VALUE + " !");
                            }
                        });
                    } else {
                        setItem(menu, p);
                    }
                    return false;
                });


                // Insert all
                int INSERT_ALL_SLOT = 16;
                menu.replaceExistingItem(INSERT_ALL_SLOT,
                        ItemCreator.create(Material.PINK_STAINED_GLASS_PANE, "&bQuick deposit",
                                "&7> Click here to add all available items to your inventory", "&7Deposit into quantum storage"));
                menu.addMenuClickHandler(INSERT_ALL_SLOT, (pl, slot, item, action) -> {
                    insertAll(pl, menu, block);
                    return false;
                });

                // Extract all
                int EXTRACT_SLOT = 17;
                menu.replaceExistingItem(EXTRACT_SLOT,
                        ItemCreator.create(Material.RED_STAINED_GLASS_PANE, "&6Quick take out",
                                "&7> [Left click] Click to take out items and fill your inventory",
                                "&7> [Right click] Click to take out an item",
                                "&7> [Shift+right click] Click to take out 64 items"
                        ));
                menu.addMenuClickHandler(EXTRACT_SLOT, (pl, slot, item, action) -> {
                    extract(pl, menu, block, action);
                    return false;
                });


                // Cache may exist if placed with items held inside.
                QuantumCache cache = CACHES.get(block.getLocation());
                if (cache == null) {
                    cache = addCache(menu);
                }
                updateDisplayItem(menu, cache);
            }
        };
    }

    public void insertAll(Player p, BlockMenu menu, Block b) {
        PlayerInventory inv = p.getInventory();
        QuantumCache cache = CACHES.get(menu.getLocation());
        if (cache == null) return;

        ItemStack storedItem = cache.getItemStack();
        int capacity = cache.getLimit();

        ItemStack[] contents = inv.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null || item.getType() == Material.AIR) continue;

            ItemStackCache storedItemCache = new ItemStackCache(storedItem);

            if (StackUtils.itemsMatch(storedItemCache, item, true)) {
                int toAdd = Math.toIntExact(Math.min(item.getAmount(), capacity - cache.getAmount()));
                if (toAdd > 0) {

                    item.setAmount(item.getAmount() - toAdd);
                    cache.increaseAmount(toAdd);

                    if (item.getAmount() == 0) {
                        inv.setItem(i, null);
                    }
                }
            }
        }

        syncBlock(menu, cache);
        updateDisplayItem(menu, cache);
    }

    public void extract(Player p, BlockMenu menu, Block b, ClickAction action) {
        QuantumCache cache = CACHES.get(menu.getLocation());
        if (cache == null) return;

        ItemStack storedItem = cache.getItemStack();
        int stored = Math.toIntExact(cache.getAmount());
        if (action.isShiftClicked() && action.isRightClicked()) {
            ItemStack extractedItem = cache.withdrawItem(64);
            if (extractedItem != null) {
                Utils.giveOrDropItem(p, extractedItem);
            }
        } else if (action.isRightClicked()) {
            ItemStack extractedItem = cache.withdrawItem(1);
            if (extractedItem != null) {
                Utils.giveOrDropItem(p, extractedItem);
            }
        } else {
            PlayerInventory inv = p.getInventory();
            ItemStack[] contents = inv.getStorageContents();

            for (int i = 0; i < contents.length; i++) {
                if (contents[i] == null || contents[i].getType() == Material.AIR) {
                    if (stored == 0) break;

                    int amountToExtract = Math.min(stored, storedItem.getMaxStackSize());
                    ItemStack itemToInsert = storedItem.clone();
                    itemToInsert.setAmount(amountToExtract);
                    contents[i] = itemToInsert;

                    cache.reduceAmount(amountToExtract);
                    stored -= amountToExtract;

                    if (stored == 0) break;
                }
            }

            p.getInventory().setStorageContents(contents);
            updateDisplayItem(menu, cache);
        }

        syncBlock(menu, cache);
    }

    private QuantumCache addCache(@Nonnull BlockMenu blockMenu) {
        final Location location = blockMenu.getLocation();
        final String amountString = BlockStorage.getLocationInfo(location, BS_AMOUNT);
        final String voidString = BlockStorage.getLocationInfo(location, BS_VOID);
        final int amount = amountString == null ? 0 : Integer.parseInt(amountString);
        final boolean voidExcess = voidString == null || Boolean.parseBoolean(voidString);
        int maxAmount = this.maxAmount;
        if (this.supportsCustomMaxAmount) {
            final String customMaxAmountString = BlockStorage.getLocationInfo(blockMenu.getLocation(), BS_CUSTOM_MAX_AMOUNT);
            if (customMaxAmountString != null) {
                maxAmount = Integer.parseInt(customMaxAmountString);
            }
        }
        final ItemStack itemStack = blockMenu.getItemInSlot(ITEM_SLOT);

        QuantumCache cache = createCache(itemStack, blockMenu, amount, maxAmount, voidExcess, supportsCustomMaxAmount);


        CACHES.put(location, cache);
        return cache;
    }

    private QuantumCache createCache(@Nullable ItemStack itemStack, @Nonnull BlockMenu menu, int amount, int maxAmount, boolean voidExcess, boolean supportsCustomMaxAmount) {
        if (itemStack == null || itemStack.getType() == Material.AIR || isDisplayItem(itemStack)) {
            menu.addItem(ITEM_SLOT, NO_ITEM);
            menu.markDirty();
            return new QuantumCache(null, 0, maxAmount, true, supportsCustomMaxAmount);
        } else {
            final ItemStack clone = itemStack.clone();
            final ItemMeta itemMeta = clone.getItemMeta();
            final List<String> lore = itemMeta.getLore();
            for (int i = 0; i < 3; i++) {
                if (lore != null && lore.isEmpty()) {
                    break;
                }
                lore.remove(lore.size() - 1);
            }

            if (supportsCustomMaxAmount) {
                if (!lore.isEmpty()) {
                    lore.remove(lore.size() - 1);
                }
            }
            itemMeta.setLore(lore.isEmpty() ? null : lore);
            clone.setItemMeta(itemMeta);

            final QuantumCache cache = new QuantumCache(clone, amount, maxAmount, voidExcess, supportsCustomMaxAmount);

            updateDisplayItem(menu, cache);
            return cache;
        }
    }

    private boolean isDisplayItem(@Nonnull ItemStack itemStack) {
        return PersistentDataAPI.getBoolean(itemStack.getItemMeta(), Keys.newKey("display"));
    }

    protected void onBreak(@Nonnull BlockBreakEvent event) {
        final Location location = event.getBlock().getLocation();
        final BlockMenu blockMenu = BlockStorage.getInventory(event.getBlock());

        if (blockMenu != null) {
            final QuantumCache cache = CACHES.remove(blockMenu.getLocation());

            if (cache != null && cache.getAmount() > 0 && cache.getItemStack() != null) {
                final ItemStack itemToDrop = this.getItem().clone();
                final ItemMeta itemMeta = itemToDrop.getItemMeta();

                DataTypeMethods.setCustom(itemMeta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, cache);
                cache.addMetaLore(itemMeta);
                itemToDrop.setItemMeta(itemMeta);
                location.getWorld().dropItem(location.clone().add(0.5, 0.5, 0.5), itemToDrop);
                event.setDropItems(false);
            }

            for (int i : this.slotsToDrop) {
                blockMenu.dropItems(location, i);
            }
        }
    }

    protected void onPlace(@Nonnull BlockPlaceEvent event) {
        final ItemStack itemStack = event.getItemInHand();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final QuantumCache cache = DataTypeMethods.getCustom(itemMeta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE);

        if (cache == null) {
            return;
        }

        syncBlock(event.getBlock().getLocation(), cache);
        CACHES.put(event.getBlock().getLocation(), cache);
    }

    public boolean supportsCustomMaxAmount() {
        return this.supportsCustomMaxAmount;
    }

    @Override
    public boolean canStack(@Nonnull ItemMeta sfItemMeta, @Nonnull ItemMeta itemMeta) {
        return sfItemMeta.getPersistentDataContainer().equals(itemMeta.getPersistentDataContainer());
    }
}
