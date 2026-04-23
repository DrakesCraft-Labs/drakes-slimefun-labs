package io.github.sefiraat.simplestorage.items.chests;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.configuration.ManagerConfiguration;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

import static io.github.sefiraat.simplestorage.items.chests.SimpleChest.SLOT_INPUT;

public final class SimpleInventoryCache extends AbstractCache {

    private final int chestID;
    private final Map<Integer, ItemStack> items = new HashMap<>();

    public SimpleInventoryCache(BlockMenu blockMenu, int pages, int chestID) {
        super(blockMenu);

        this.pages = pages;
        this.chestID = chestID;
    }

    public void updateItems() {
        items.clear();
        ConfigurationSection section = SimpleStorage.inst().getManagerConfiguration().getChestConfig().getConfigurationSection(ManagerConfiguration.CHESTS + "." + chestID);
        for (int i = 1; i <= (pages * 45); i++) {
            ItemStack itemStack = section.getItemStack(ManagerConfiguration.SLOT + i);
            items.put(i, itemStack);
        }
    }

    public void input() {
        ItemStack input = blockMenu.getItemInSlot(SLOT_INPUT);
        if (input == null) {
            return;
        }
        // Check matching first
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            if (entry.getValue() != null && inputOverSaved(entry, input)) {
                return;
            }
        }
        // Then slot into next free slot, if available
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            if (entry.getValue() == null) {
                ManagerConfiguration.setChestSlotItem(chestID, entry.getKey(), input.clone());
                input.setAmount(0);
                return;
            }
        }
        // Item doesn't fit
    }

    public boolean inputOverSaved(Map.Entry<Integer, ItemStack> entry, ItemStack input) {
        ItemStack saveStack = entry.getValue();
        int saveSlot = entry.getKey();
        if (entry.getValue().isSimilar(input) && entry.getValue().getAmount() < entry.getValue().getMaxStackSize()) {
            int amountFit = entry.getValue().getMaxStackSize() - entry.getValue().getAmount();
            if (amountFit > input.getAmount()) {
                saveStack.setAmount(saveStack.getAmount() + input.clone().getAmount());
                input.setAmount(0);
            } else {
                saveStack.setAmount(saveStack.getMaxStackSize());
                input.setAmount(input.getAmount() - amountFit);
            }
            ManagerConfiguration.setChestSlotItem(chestID, saveSlot, saveStack);
            return true;
        }
        return false;
    }

    public void process() {
        updateItems();
        input();
        updateView();
    }

    public void kill(Location location) {
        dropItems(location);
        ManagerConfiguration.removeChestID(chestID);
        BlockStorage.clearBlockInfo(location);
    }

    public void dropItems(Location location) {
        ConfigurationSection section = SimpleStorage.inst().getManagerConfiguration().getChestConfig().getConfigurationSection(ManagerConfiguration.CHESTS + "." + chestID);
        for (String s : section.getKeys(false)) {
            if (!s.equals(ManagerConfiguration.PLACED)) {
                ItemStack itemStack = section.getItemStack(s);
                if (itemStack != null) {
                    location.getWorld().dropItemNaturally(location,itemStack);
                }
            }
        }
        if (blockMenu.getItemInSlot(SLOT_INPUT) != null) {
            location.getWorld().dropItemNaturally(location, blockMenu.getItemInSlot(SLOT_INPUT).clone());
        }
    }

    public void updateView() {
        if (blockMenu.hasViewer()) {
            for (int i = 0; i < 45 ; i++) {
                int slotNo = i + 9;
                int listSlotNo = ((page - 1) * 45) + (i + 1);
                ItemStack itemStack = items.get(listSlotNo);
                if (itemStack == null) {
                    blockMenu.replaceExistingItem(slotNo, GuiItems.menuChestDummy());
                    blockMenu.addMenuClickHandler(slotNo, (player, i1, itemStack1, clickAction) -> false);
                } else {
                    ItemStack guiVersion = setGuiItem(itemStack.clone());
                    blockMenu.replaceExistingItem(slotNo, guiVersion);
                    blockMenu.addMenuClickHandler(slotNo, (player, i1, itemStack1, clickAction) -> guiClickItem(player, slotNo, listSlotNo, itemStack, clickAction));
                }
            }
        }
    }

    private boolean guiClickItem(Player player, int slotNo, int listSlotNo, ItemStack itemStack, ClickAction clickAction) {
        if (player.getItemOnCursor().getType() == Material.AIR) {
            if (clickAction.isRightClicked()) {
                guiRightClick(player, slotNo, listSlotNo, itemStack);
            } else if (clickAction.isShiftClicked()) {
                guiShiftClick(player, slotNo, listSlotNo, itemStack);
            } else {
                guiLeftClick(player, slotNo, listSlotNo, itemStack);
            }
        }
        return false;
    }

    private void guiRightClick(Player player, int slotNo, int listSlotNo, ItemStack itemStack) {
        ItemStack cursorStack = itemStack.clone();
        cursorStack.setAmount((int) Math.ceil(cursorStack.getAmount() / 2.0));
        itemStack.setAmount(itemStack.getAmount() - cursorStack.getAmount());
        player.setItemOnCursor(cursorStack);
        if (itemStack.getAmount() > 0) {
            blockMenu.replaceExistingItem(slotNo, setGuiItem(itemStack.clone()));
            ManagerConfiguration.setChestSlotItem(chestID, listSlotNo, itemStack);
        } else {
            blockMenu.replaceExistingItem(slotNo, GuiItems.menuChestDummy());
            blockMenu.addMenuClickHandler(slotNo, (player2, i2, itemStack2, clickAction2) -> false);
            ManagerConfiguration.setChestSlotItem(chestID, listSlotNo, null);
        }
    }

    private void guiShiftClick(Player player, int slotNo, int listSlotNo, ItemStack itemStack) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(itemStack.clone());
            blockMenu.replaceExistingItem(slotNo, GuiItems.menuChestDummy());
            blockMenu.addMenuClickHandler(slotNo, (player2, i2, itemStack2, clickAction2) -> false);
            ManagerConfiguration.setChestSlotItem(chestID, listSlotNo, null);
        }
    }

    private void guiLeftClick(Player player, int slotNo, int listSlotNo, ItemStack itemStack) {
        player.setItemOnCursor(itemStack.clone());
        blockMenu.replaceExistingItem(slotNo, GuiItems.menuChestDummy());
        blockMenu.addMenuClickHandler(slotNo, (player2, i2, itemStack2, clickAction2) -> false);
        ManagerConfiguration.setChestSlotItem(chestID, listSlotNo, null);
    }

    public static ItemStack setGuiItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer c = itemMeta.getPersistentDataContainer();
        c.set(new NamespacedKey(SimpleStorage.inst(), "gui"), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
