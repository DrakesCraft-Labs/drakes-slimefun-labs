package io.github.sefiraat.danktech2.core;

import io.github.sefiraat.danktech2.slimefun.packs.DankPack;
import io.github.sefiraat.danktech2.slimefun.packs.TrashPack;
import io.github.sefiraat.danktech2.theme.ThemeType;
import io.github.sefiraat.danktech2.utils.GeneralUtils;
import io.github.sefiraat.danktech2.utils.Keys;
import io.github.sefiraat.danktech2.utils.Skulls;
import io.github.sefiraat.danktech2.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.danktech2.utils.datatypes.PersistentTrashInstanceType;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;

public class TrashGUI extends ChestMenu {

    protected static final int[] DISPLAY_SLOTS = new int[]{
        0, 1, 2, 3, 4, 5, 6, 7, 8, 27, 28, 29, 30, 31, 32, 33, 34, 35
    };
    protected static final int[] INPUT_SLOTS = new int[]{
        9, 10, 11, 12, 13, 14, 15, 16, 17, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    protected static final int[] INTERACTION_SLOTS = new int[]{
        18, 19, 20, 21, 22, 23, 24, 25, 26, 45, 46, 47, 48, 49, 50, 51, 52, 53
    };

    protected static final ItemStack EMPTY_STACK = new CustomItemStack(
        Skulls.GUI_EMPTY.getPlayerHead(),
        MessageFormat.format("{0}Slot Empty", ThemeType.PASSIVE.getColor())
    );
    protected static final ItemStack LOCKED_STACK = new CustomItemStack(
        Material.RED_STAINED_GLASS_PANE,
        MessageFormat.format("{0}Slot Locked", ThemeType.PASSIVE.getColor())
    );
    protected static final ItemStack INPUT_STACK = new CustomItemStack(
        Material.LIGHT_GRAY_STAINED_GLASS_PANE,
        MessageFormat.format("{0}Input", ThemeType.PASSIVE.getColor())
    );
    protected static final ItemStack INTERACTION_STACK = new CustomItemStack(
        Skulls.GUI_WITHDRAW.getPlayerHead(), ThemeType.CLICK_INFO.getColor() + "Add/Withdraw Items",
        "",
        MessageFormat.format("{0}Left Click: {1}Clear Filter", ThemeType.CLICK_INFO.getColor(), ThemeType.PASSIVE.getColor())
    );


    private TrashPackInstance packInstance;
    private final ItemStack itemStack;
    private final TrashPack trashPack;

    public TrashGUI(TrashPackInstance packInstance, ItemStack itemStack) {
        super("Dank Pack - Tier " + packInstance.getTier());
        this.packInstance = packInstance;
        this.itemStack = itemStack;
        this.trashPack = (TrashPack) SlimefunItem.getByItem(itemStack);

        for (int i = 0; i < DISPLAY_SLOTS.length; i++) {
            // Fill with locked slots
            addItem(DISPLAY_SLOTS[i], LOCKED_STACK, ChestMenuUtils.getEmptyClickHandler());
            addItem(INPUT_SLOTS[i], LOCKED_STACK, ChestMenuUtils.getEmptyClickHandler());
            addItem(INTERACTION_SLOTS[i], LOCKED_STACK, ChestMenuUtils.getEmptyClickHandler());
        }

        // Don't let players shift click into the GUI to bypass the handler
        addPlayerInventoryClickHandler((p, slot, item, action) -> !action.isShiftClicked());
        setupAllItems();
    }

    private void setupAllItems() {
        for (int i = 0; i < trashPack.getSlots(); i++) {

            final int finalSlot = i;
            final ItemStack slotStack = packInstance.getItem(i);

            if (slotStack == null) {
                replaceExistingItem(DISPLAY_SLOTS[i], EMPTY_STACK);

                replaceExistingItem(INPUT_SLOTS[i], INPUT_STACK);
                addMenuClickHandler(INPUT_SLOTS[i], (p, slot, item, action) -> setNewItem(p, finalSlot));

                replaceExistingItem(INTERACTION_SLOTS[i], INTERACTION_STACK);
                addMenuClickHandler(INTERACTION_SLOTS[i], (p, slot, item, action) -> false);
            } else {
                replaceExistingItem(DISPLAY_SLOTS[i], getDisplayStack(slotStack));

                replaceExistingItem(INPUT_SLOTS[i], INPUT_STACK);
                addMenuClickHandler(INPUT_SLOTS[i], (p, slot, item, action) -> false);

                replaceExistingItem(INTERACTION_SLOTS[i], INTERACTION_STACK);
                addMenuClickHandler(INTERACTION_SLOTS[i], (player, i1, itemStack1, clickAction) -> interactWithItem(clickAction, finalSlot));
            }
        }
    }

    private boolean setNewItem(Player player, int instanceSlot) {
        loadInstance();
        ItemStack heldItem = player.getItemOnCursor();
        if (heldItem.getType() != Material.AIR
            && allowedInTrash(heldItem)
        ) {
            ItemStack clone = heldItem.clone();
            clone.setAmount(1);
            this.packInstance.setItem(instanceSlot, clone);
            saveInstance();
            setupAllItems();
        }
        return false;
    }

    private boolean interactWithItem(ClickAction clickAction, int instanceSlot) {
        loadInstance();
        if (!clickAction.isShiftClicked() && !clickAction.isRightClicked()) {
            clearSlot(instanceSlot);
        }
        return false;
    }

    private void clearSlot(int instanceSlot) {
        this.packInstance.clearItem(instanceSlot);
        saveInstance();
        setupAllItems();
    }

    private void loadInstance() {
        this.packInstance = DataTypeMethods.getCustom(
            this.itemStack.getItemMeta(),
            Keys.TRASH_INSTANCE,
            PersistentTrashInstanceType.TYPE
        );
    }

    private void saveInstance() {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        DataTypeMethods.setCustom(
            itemMeta,
            Keys.TRASH_INSTANCE,
            PersistentTrashInstanceType.TYPE,
            packInstance
        );
        this.itemStack.setItemMeta(itemMeta);
    }

    public boolean allowedInTrash(ItemStack itemStack) {
        SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);
        return !(slimefunItem instanceof DankPack) && !(slimefunItem instanceof TrashPack);
    }

    protected static ItemStack getDisplayStack(ItemStack itemStack) {
        return itemStack.clone();
    }
}
