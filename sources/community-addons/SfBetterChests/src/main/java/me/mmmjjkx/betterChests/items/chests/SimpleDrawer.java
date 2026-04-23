package me.mmmjjkx.betterChests.items.chests;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotHopperable;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft-labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft-labs.slimefun4.libraries.dough.protection.Interaction;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import me.mmmjjkx.betterChests.BCGroups;
import me.mmmjjkx.betterChests.BetterChests;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class SimpleDrawer extends SlimefunItem implements NotHopperable {
    private final NamespacedKey ITEM_ENTITY = new NamespacedKey(BetterChests.INSTANCE, "drawer_item_entity");
    private final NamespacedKey NAME_ENTITY = new NamespacedKey(BetterChests.INSTANCE, "drawer_item_name_entity");
    private final NamespacedKey COUNT_ENTITY = new NamespacedKey(BetterChests.INSTANCE, "drawer_item_count_entity");
    private final NamespacedKey FACING = new NamespacedKey(BetterChests.INSTANCE, "drawer_facing");

    private static final ItemStack EMPTY = new ItemStack(Material.BARRIER);

    private final Map<Location, EntityContainer> entities = new HashMap<>();
    /**
     *  Get the capacity of the drawer.
     */
    @Getter
    private final long capacity;

    static {
        EMPTY.editMeta(m -> m.displayName(Component.text("Empty")));
    }

    public SimpleDrawer(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, long capacity) {
        super(BCGroups.STORAGES, item, recipeType, recipe);

        this.capacity = capacity;

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                Player p = e.getPlayer();

                BlockFace facing = p.getFacing();
                Block placed = e.getBlockPlaced();

                Location itemLoc = getLocation(placed.getLocation(), facing);

                entities.put(e.getBlock().getLocation(), spawnEntity(itemLoc, e.getBlock(), facing));
            }
        });

        addItemHandler((BlockUseHandler) e -> {
            e.cancel();

            Player p = e.getPlayer();
            boolean taking = p.getInventory().getItemInMainHand().getType() == Material.AIR;

            Optional<Block> clickedBlock = e.getClickedBlock();
            if (clickedBlock.isEmpty()) {
                return;
            }

            Block clicked = clickedBlock.get();
            EntityContainer container = tryGetContainer(clicked.getLocation());

            PersistentDataAPI.setString(container.item, ITEM_ENTITY, container.item.getUniqueId().toString());
            PersistentDataAPI.setString(container.itemName, NAME_ENTITY, container.itemName.getUniqueId().toString());
            PersistentDataAPI.setString(container.itemCount, COUNT_ENTITY, container.itemCount.getUniqueId().toString());

            TileState state = (TileState) clicked.getState();

            PersistentDataAPI.setString(state, ITEM_ENTITY, container.item.getUniqueId().toString());
            PersistentDataAPI.setString(state, NAME_ENTITY, container.itemName.getUniqueId().toString());
            PersistentDataAPI.setString(state, COUNT_ENTITY, container.itemCount.getUniqueId().toString());

            state.update();

            if (!Slimefun.getProtectionManager().hasPermission(p, clicked.getLocation(), Interaction.INTERACT_BLOCK)) {
                p.sendMessage("§cYou do not have permission to interact with this block.");
                return;
            }

            if (taking) {
                int count = Integer.parseInt(Objects.requireNonNull(container.itemCount.getText()));
                if (count == 0) {
                    p.sendMessage("§cThere is no item in this drawer.");
                    return;
                }

                PlayerInventory pinv = p.getInventory();

                ItemStack itemStack = Objects.requireNonNull(container.item.getItemStack());
                int maxStackSize = itemStack.getMaxStackSize();

                if (itemStack.isSimilar(pinv.getItemInMainHand())) {
                    ItemStack hand = pinv.getItemInMainHand();
                    int handCount = hand.getAmount();

                    int toTake = Math.min(count, maxStackSize - handCount);

                    container.itemName.text(getItemName(itemStack));
                    container.itemCount.text(Component.text(String.valueOf(count - toTake)));

                    if (count - toTake == 0) {
                        container.itemName.text(Component.text("Empty"));
                        container.item.setItemStack(new ItemStack(Material.BARRIER));
                    }

                    pinv.getItemInMainHand().setAmount(handCount + toTake);
                } else {
                    ItemStack beingGet = itemStack.clone();

                    int toTake = Math.min(count, maxStackSize);
                    beingGet.setAmount(toTake);

                    if (!InvUtils.fits(pinv, beingGet, IntStream.range(0, 36).toArray())) {
                        p.sendMessage("§cYour inventory is full.");
                        return;
                    }

                    container.itemName.text(getItemName(itemStack));
                    container.itemCount.text(Component.text(String.valueOf(count - toTake)));

                    if (count - toTake == 0) {
                        container.itemName.text(Component.text("Empty"));
                        container.item.setItemStack(EMPTY);
                    }

                    pinv.addItem(beingGet);
                }

                p.updateInventory();
                storeItem(clicked.getLocation(), container.item.getItemStack(), Integer.parseInt(container.itemCount.getText()));
            } else {
                ItemStack handItem = p.getInventory().getItemInMainHand();
                if (handItem.getType() == Material.AIR || handItem.getAmount() < 1) {
                    p.sendMessage("§cYou need to hold an item in your main hand to place it in the drawer.");
                    return;
                }

                long count = Long.parseLong(Objects.requireNonNullElse(container.itemCount.getText(), "0"));

                if (count == 0) {
                    container.itemName.text(getItemName(handItem));
                    container.item.setItemStack(handItem.clone());
                    container.itemCount.text(Component.text(handItem.getAmount()));
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    p.updateInventory();

                    storeItem(clicked.getLocation(), container.item.getItemStack(), Integer.parseInt(Objects.requireNonNullElse(container.itemCount.getText(), "0")));
                    return;
                }

                ItemStack itemStack = Objects.requireNonNull(container.item.getItemStack());
                if (itemStack.isSimilar(handItem)) {
                    int handCount = handItem.getAmount();
                    int toAdd = handCount;

                    if (count == capacity) {
                        p.sendMessage("§cThis drawer is full.");
                        return;
                    } else if (count + toAdd > capacity) {
                        toAdd = (int) (capacity - count);
                    }

                    handItem.setAmount(handCount - toAdd);

                    storeItem(clicked.getLocation(), container.item.getItemStack(), Integer.parseInt(container.itemCount.getText()));

                    p.updateInventory();

                    container.itemName.text(getItemName(itemStack));
                    container.itemCount.text(Component.text(count + toAdd));
                } else {
                    p.sendMessage("§cThe drawer already contains other items.");
                }
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent e, @NotNull ItemStack i, @NotNull List<ItemStack> drops) {
                Location itemLoc = e.getBlock().getLocation();
                Block block = e.getBlock();

                tryGetContainer(itemLoc);
                EntityContainer container = entities.remove(itemLoc);

                World w = block.getWorld();

                ItemStack stack = Objects.requireNonNullElseGet(container.item.getItemStack(), () -> {
                    List<MetadataValue> l1 = block.getMetadata("bc_drawer_item");
                    if (!l1.isEmpty()) {
                        return (ItemStack) l1.get(0).value();
                    } else {
                        return new ItemStack(Material.AIR);
                    }
                });

                int count = Integer.parseInt(Objects.requireNonNullElseGet(container.itemCount.getText(), () -> {
                    List<MetadataValue> l2 = block.getMetadata("bc_drawer_count");
                    if (!l2.isEmpty()) {
                        return l2.get(0).asString();
                    } else {
                        return "0";
                    }
                }));

                if (stack.getType() == Material.BARRIER && count < 1) {
                    container.item.remove();
                    container.itemName.remove();
                    container.itemCount.remove();
                    return;
                }

                stack.setAmount(count);
                w.dropItemNaturally(itemLoc, stack);

                block.removeMetadata("bc_drawer_item", BetterChests.INSTANCE);
                block.removeMetadata("bc_drawer_count", BetterChests.INSTANCE);

                container.item.remove();
                container.itemName.remove();
                container.itemCount.remove();
            }
        });
    }

    public static Component getItemName(ItemStack item) {
        if (item == null) {
            return Component.text("Empty");
        }

        Component name = item.displayName();
        if (name instanceof TranslatableComponent tc) {
            return tc.args().get(0);
        }
        return name;
    }

    private void storeItem(final Location barrelLoc, ItemStack item, long count) {
        Block block = barrelLoc.getBlock();
        block.setMetadata("bc_drawer_item", new FixedMetadataValue(BetterChests.INSTANCE, item.asOne()));
        block.setMetadata("bc_drawer_count", new FixedMetadataValue(BetterChests.INSTANCE, count));
    }

    private EntityContainer tryGetContainer(final Location barrelLoc) {
        Block block = barrelLoc.getBlock();
        if (entities.get(barrelLoc) != null) {
            EntityContainer container = entities.get(barrelLoc);
            if (!container.chunkLoaded()) {
                entities.remove(barrelLoc);
                long count = Long.parseLong(Objects.requireNonNullElse(container.itemCount.getText(), "0"));
                block.removeMetadata("bc_drawer_count", BetterChests.INSTANCE);
                block.setMetadata("bc_drawer_count", new FixedMetadataValue(BetterChests.INSTANCE, count)); // re-sync count
                return tryGetContainer(barrelLoc);
            }
            return container;
        } else {
            AtomicReference<ItemDisplay> itemA = new AtomicReference<>();
            AtomicReference<TextDisplay> itemNameA = new AtomicReference<>();
            AtomicReference<TextDisplay> itemCountA = new AtomicReference<>();

            if (!block.getChunk().isEntitiesLoaded()) {
                block.getChunk().load();
            }

            for (Entity e : block.getChunk().getEntities()) {
                if (!(block.getState() instanceof TileState st)) {
                    continue;
                }

                if (e instanceof ItemDisplay && PersistentDataAPI.hasString(e, ITEM_ENTITY)) {
                    UUID itemUUID = UUID.fromString(PersistentDataAPI.getString(e, ITEM_ENTITY, ""));
                    UUID itemUUIDBlock = UUID.fromString(PersistentDataAPI.getString(st, ITEM_ENTITY, ""));
                    if (itemUUID.equals(itemUUIDBlock)) {
                        itemA.set((ItemDisplay) e);
                    }
                } else if (e instanceof TextDisplay && PersistentDataAPI.hasString(e, NAME_ENTITY)) {
                    UUID itemNameUUID = UUID.fromString(PersistentDataAPI.getString(e, NAME_ENTITY, ""));
                    UUID itemNameUUIDBlock = UUID.fromString(PersistentDataAPI.getString(st, NAME_ENTITY, ""));
                    if (itemNameUUID.equals(itemNameUUIDBlock)) {
                        itemNameA.set((TextDisplay) e);
                    }
                } else if (e instanceof TextDisplay && PersistentDataAPI.hasString(e, COUNT_ENTITY)) {
                    UUID countEntityUUID = UUID.fromString(PersistentDataAPI.getString(e, COUNT_ENTITY, ""));
                    UUID countEntityUUIDBlock = UUID.fromString(PersistentDataAPI.getString(st, COUNT_ENTITY, ""));
                    if (countEntityUUID.equals(countEntityUUIDBlock)) {
                        itemCountA.set((TextDisplay) e);
                    }
                }
            }

            ItemDisplay item = itemA.get();
            TextDisplay itemName = itemNameA.get();
            TextDisplay itemCount = itemCountA.get();

            if (item == null || itemName == null || itemCount == null) {
                if (item != null) item.remove();
                if (itemName != null) itemName.remove();
                if (itemCount != null) itemCount.remove();

                int ordinal = block.getState() instanceof TileState ts ? PersistentDataAPI.getInt(ts, FACING, 0) : 0;
                BlockFace face = BlockFace.values()[ordinal];

                EntityContainer container = spawnEntity(getLocation(barrelLoc, face), block, face);
                entities.put(barrelLoc, container);
                return container;
            }

            retainItem(block, item, itemName, itemCount);

            EntityContainer container = new EntityContainer(item, itemName, itemCount);
            entities.put(barrelLoc, container);
            return container;
        }
    }

    private EntityContainer spawnEntity(Location itemLoc, Block block, BlockFace facing) {
        World w = block.getWorld();

        float rotationYaw = switch (facing) {
            case SOUTH -> -180.0F;
            case WEST -> -90.0F;
            case EAST -> 90.0F;
            default -> 0F;
        };

        ItemDisplay item = w.spawn(itemLoc, ItemDisplay.class, i -> {
            i.setItemStack(new ItemStack(Material.BARRIER));
            i.setTransformation(getTransformation());
            i.setPersistent(true);
            i.setInvulnerable(true);
        });

        TextDisplay itemName = w.spawn(itemLoc.clone().add(0, 0.2, 0.001), TextDisplay.class, t -> {
            t.text(Component.text("Empty"));
            t.setPersistent(true);
            t.setInvulnerable(true);
            t.setVisibleByDefault(true);
            t.setAlignment(TextDisplay.TextAlignment.CENTER);
        });

        TextDisplay itemCount = w.spawn(itemLoc.clone().subtract(0, 0.5, -0.001), TextDisplay.class, t -> {
            t.text(Component.text(0));
            t.setPersistent(true);
            t.setInvulnerable(true);
            t.setVisibleByDefault(true);
            t.setAlignment(TextDisplay.TextAlignment.CENTER);
        });

        PersistentDataAPI.setInt((TileState) block.getState(), FACING, facing.ordinal());

        retainItem(block, item, itemName, itemCount);

        item.setRotation(rotationYaw, 0);
        itemName.setRotation(rotationYaw, 0);
        itemCount.setRotation(rotationYaw, 0);

        return new EntityContainer(item, itemName, itemCount);
    }

    private void retainItem(Block block, ItemDisplay item, TextDisplay itemName, TextDisplay itemCount) {
        MetadataValue itemStack = getMetadata(block, "bc_drawer_item");
        if (itemStack != null) {
            ItemStack itemStackValue = (ItemStack) itemStack.value();
            if (itemStackValue != null && itemStackValue.getType() != Material.AIR) {
                item.setItemStack(itemStackValue);
                itemName.text(getItemName(itemStackValue));
            }
        }

        MetadataValue countValue = getMetadata(block, "bc_drawer_count");
        if (countValue != null) {
            itemCount.text(Component.text(String.valueOf(countValue.asLong())));
        }
    }

    private Transformation getTransformation() {
        final Vector3f scale = new Vector3f(0.39f, 0.39f, 0.39f);
        return new Transformation(new Vector3f(), new AxisAngle4f(), scale, new AxisAngle4f());
    }

    private Location getLocation(Location barrelLoc, BlockFace facing) {
        return switch (facing) {
            case SOUTH -> barrelLoc.clone().add(0.5, 0.5, -0.002);
            case WEST -> barrelLoc.clone().add(1.005, 0.5, 0.5);
            case EAST -> barrelLoc.clone().add(0, 0.5, 0.502);
            default -> barrelLoc.clone().add(0.5, 0.5, 1.002);
        };
    }

    private MetadataValue getMetadata(Metadatable m, String key) {
        List<MetadataValue> metadata = m.getMetadata(key);
        return metadata.isEmpty() ? null : metadata.get(0);
    }

    /**
     * Add items to the drawer.
     * (For hooks)
     *
     * @param barrelLoc the location of the drawer
     * @param item      the item to add
     * @return a pair of a boolean indicating <b>whether the item was added</b> and <b>an integer indicating the remaining number of items in the drawer</b>.
     */
    public Pair<Boolean, Integer> addItem(Location barrelLoc, ItemStack item) {
        if (item.getType() == Material.AIR) {
            return Pair.of(false, 0);
        }

        if (item.getAmount() < 1) {
            return Pair.of(false, 0);
        }

        MetadataValue theItem = getMetadata(barrelLoc.getBlock(), "bc_drawer_item");
        EntityContainer container = entities.get(barrelLoc);

        if (container == null) {
            return Pair.of(false, 0);
        }

        if (theItem == null) {
            container.item.setItemStack(item.asOne().clone());
            container.itemName.text(getItemName(item));
            container.itemCount.text(Component.text(item.getAmount()));

            int toAdd = (int) Math.min(item.getAmount(), capacity > Integer.MAX_VALUE ? Integer.MAX_VALUE : capacity);
            item.setAmount(item.getAmount() - toAdd);

            if (item.getAmount() < 1) {
                item.setType(Material.AIR);
            }

            storeItem(barrelLoc, item, item.getAmount());
            return Pair.of(true, item.getAmount());
        }

        ItemStack drawerItem = (ItemStack) theItem.value();

        if (drawerItem == null || !drawerItem.isSimilar(item)) {
            return Pair.of(false, 0);
        }

        long drawerCount = 0;

        MetadataValue countValue = getMetadata(barrelLoc.getBlock(), "bc_drawer_count");
        if (countValue != null) {
            drawerCount = countValue.asLong();
        }

        int toAdd = item.getAmount();

        if (drawerCount + toAdd > capacity) {
            toAdd = (int) (capacity - drawerCount);
        }

        item.setAmount(item.getAmount() - toAdd);

        if (item.getAmount() < 1) {
            item.setType(Material.AIR);
        }

        container.itemCount.text(Component.text(drawerCount + toAdd));

        storeItem(barrelLoc, drawerItem, drawerCount + toAdd);

        return Pair.of(true, item.getAmount());
    }

    /**
     * Get the item currently being stored in the drawer.
     * (For hooks)
     *
     * @param barrelLoc the location of the drawer
     * @return the item being stored (the amount is not included), or null if there is no item.
     */
    public ItemStack getStoringItem(Location barrelLoc) {
        MetadataValue theItem = getMetadata(barrelLoc.getBlock(), "bc_drawer_item");
        if (entities.get(barrelLoc) == null || theItem == null) {
            return null;
        }

        ItemStack item = (ItemStack) theItem.value();
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        return item.asOne();
    }

    /**
     * Get the count of the item currently being stored in the drawer.
     * Prefer using {@link #getStoringItemCountSafely(Location)} if you want to avoid flow exceptions.
     * (For hooks)
     *
     * @param barrelLoc the location of the drawer
     * @return the count of the item being stored, or 0 if there is no item.
     */
    public long getStoringItemCount(Location barrelLoc) {
        Block block = barrelLoc.getBlock();
        if (entities.get(barrelLoc) == null || getMetadata(block, "bc_drawer_item") == null) {
            return 0;
        }

        MetadataValue countValue = getMetadata(block, "bc_drawer_count");
        EntityContainer container = entities.get(barrelLoc);

        if (container == null) {
            return countValue != null ? countValue.asLong() : 0;
        }

        if (container.itemCount == null) {
            tryGetContainer(barrelLoc);
            container = entities.get(barrelLoc);
            String text = container.itemCount.getText();
            if (text == null) {
                return 0;
            }
            return Long.parseLong(text);
        }

        String text = container.itemCount.getText();
        if (text == null) {
            return 0;
        }

        return Long.parseLong(text);
    }

    /**
     * Get the count of the item currently being stored in the drawer.
     * You can use this method safely without worrying about flow exceptions (long -> int).
     * (For hooks)
     *
     * @param barrelLoc the location of the drawer
     * @return the count of the item being stored, or 0 if there is no item.
     */
    public int getStoringItemCountSafely(Location barrelLoc) {
        return (int) Math.min(getStoringItemCount(barrelLoc), Integer.MAX_VALUE - 1);
    }

    /**
     * Take items from the drawer.
     * (For hooks)
     *
     * @param barrelLoc the location of the drawer
     * @param count     the number of items to take
     * @return the item taken, or null if there is no item or less than the specified number of items in the drawer.
     */
    @CanIgnoreReturnValue
    public ItemStack takeItem(Location barrelLoc, int count) {
        EntityContainer container = entities.get(barrelLoc);
        if (container == null) {
            return null;
        }

        ItemStack item = container.item.getItemStack();
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        if (count < 1) {
            return null; //Will anyone take a negative amount or zero numbers of items?
        }

        int itemCount = 0;
        MetadataValue countValue = getMetadata(barrelLoc.getBlock(), "bc_drawer_count");
        if (countValue != null) {
            itemCount = countValue.asInt();
        }

        if (itemCount < 1) {
            return null;
        }

        if (count > itemCount) {
            return null;
        }

        ItemStack copy = item.clone();

        itemCount -= count;
        copy.setAmount(count);

        container.itemCount.text(Component.text(itemCount));

        if (itemCount == 0) {
            container.item.setItemStack(new ItemStack(Material.BARRIER));
            container.itemName.text(Component.text("Empty"));
        }

        storeItem(barrelLoc, item, itemCount);

        return copy;
    }

    private record EntityContainer(ItemDisplay item, TextDisplay itemName, TextDisplay itemCount) {
        boolean chunkLoaded() {
            return item.getChunk().isLoaded() && item.isValid();
        }
    }
}
