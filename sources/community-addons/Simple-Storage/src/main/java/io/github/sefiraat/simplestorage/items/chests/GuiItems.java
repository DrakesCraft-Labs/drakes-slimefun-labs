package io.github.sefiraat.simplestorage.items.chests;

import io.github.sefiraat.simplestorage.items.Skulls;
import io.github.sefiraat.simplestorage.items.chests.network.NetworkElement;
import io.github.sefiraat.simplestorage.items.chests.network.NetworkElement.NetworkElementType;
import io.github.sefiraat.simplestorage.utils.Theme;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GuiItems {

    private GuiItems() {
        throw new IllegalStateException("Utility class");
    }

    public static CustomItemStack menuBackground() {
        return new CustomItemStack(
                Material.GRAY_STAINED_GLASS_PANE,
                Theme.PASSIVE + "Simple!",
                ""
        );
    }

    public static CustomItemStack menuInfo() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.GUI_NO1)),
                Theme.GUI_HEAD + "Info",
                "",
                "Page number : 1"
        );
    }

    public static CustomItemStack menuChestPageBack() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.GUI_BACK)),
                Theme.GUI_HEAD + "Back",
                "",
                Theme.CLICK_INFO + "Click to navigate back a page."
        );
    }

    public static CustomItemStack menuChestPageForward() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.GUI_FORWARD)),
                Theme.GUI_HEAD + "Forward",
                "",
                Theme.CLICK_INFO + "Click to navigate forward a page."
        );
    }

    public static CustomItemStack menuChestDummy() {
        return new CustomItemStack(
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Theme.PASSIVE + "No item here"
        );
    }

    public static CustomItemStack menuMasterDummy() {
        return new CustomItemStack(
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Theme.PASSIVE + "No inventory here"
        );
    }

    public static CustomItemStack menuClose() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.GUI_CLOSE)),
                Theme.GUI_HEAD + "Close"
        );
    }

    public static CustomItemStack menuRenameCell() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.GUI_RENAME_CELL)),
                Theme.GUI_HEAD + "Rename Cell"
        );
    }

    public static CustomItemStack menuSetMaterial() {
        return new CustomItemStack(
                PlayerHead.getItemStack(PlayerSkin.fromBase64(Skulls.GUI_SET_ICON)),
                Theme.GUI_HEAD + "Set Icon",
                "",
                Theme.ITEM_TYPEDESC + "Click this with an item on your",
                Theme.ITEM_TYPEDESC + "mouse to set the icon for this inventory"
        );
    }

    public static CustomItemStack menuCell(NetworkElement ne) {

        ItemStack i = NetworkElement.getItemStack(ne);
        String name;
        String storedName = ne.getDisplayName();
        if (storedName == null) {
            name = ChatColor.WHITE + "Scanned Inventory";
        } else {
            name = storedName;
        }

        if (ne.getType() == NetworkElement.NetworkElementType.INVENTORY_CELL) {
            return menuCellNormal(i, name);
        } else if (
                ne.getType() == NetworkElementType.INFINITY_BARREL ||
                ne.getType() == NetworkElementType.FLUFFY_BARREL
        ) {
            return menuCellBarrel(i, name, ne);
        } else {
            return menuCellError();
        }
    }

    public static CustomItemStack menuCellNormal(ItemStack i, String name) {
        return new CustomItemStack(
                i,
                Theme.GUI_HEAD + name,
                "",
                Theme.ITEM_TYPEDESC + "Inventory Type: " + NetworkElement.networkElementTypeName(NetworkElement.NetworkElementType.INVENTORY_CELL),
                "",
                Theme.CLICK_INFO + "Left click: " + ChatColor.WHITE + "Open inventory",
                Theme.CLICK_INFO + "Right click: " + ChatColor.WHITE + "Highlight inventory"
        );
    }

    public static CustomItemStack menuCellBarrel(ItemStack i, String name, NetworkElement networkElement) {
        String barrelAmount = Theme.ITEM_TYPEDESC + "Content: " + ChatColor.WHITE + networkElement.getBarrelAmount();
        return new CustomItemStack(
                i,
                Theme.GUI_HEAD + name,
                "",
                Theme.ITEM_TYPEDESC + "Inventory Type: " + NetworkElement.networkElementTypeName(networkElement.getType()),
                "",
                barrelAmount,
                "",
                Theme.CLICK_INFO + "Left click: " + ChatColor.WHITE + "Open inventory",
                Theme.CLICK_INFO + "Right click: " + ChatColor.WHITE + "Highlight inventory"
        );
    }

    public static CustomItemStack menuCellError() {
        return new CustomItemStack(
                Material.BARRIER,
                Theme.GUI_HEAD + "Scanned Inventory (Error)",
                "",
                Theme.ITEM_TYPEDESC + "Inventory Type: Unknown/Error",
                "",
                Theme.CLICK_INFO + "Left click: " + ChatColor.WHITE + "Open inventory",
                Theme.CLICK_INFO + "Right click: " + ChatColor.WHITE + "Highlight inventory"
        );
    }


}
