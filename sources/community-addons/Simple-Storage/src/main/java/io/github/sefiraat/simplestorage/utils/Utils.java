package io.github.sefiraat.simplestorage.utils;

import io.github.sefiraat.simplestorage.items.chests.GuiItems;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static int randInt(Random r, int min, int max) {
        return r.nextInt((max - min) + 1) + min;
    }

    public static void setUpChestMenu(BlockMenuPreset blockMenuPreset, int[] background, int slotBack, int slotForward, int slotInfo) {
        blockMenuPreset.setSize(54);
        blockMenuPreset.drawBackground(GuiItems.menuBackground(), background);
        blockMenuPreset.addItem(slotBack, GuiItems.menuChestPageBack());
        blockMenuPreset.addItem(slotForward, GuiItems.menuChestPageForward());
        blockMenuPreset.addItem(slotInfo, GuiItems.menuInfo());
    }

    public static SlimefunItemStack simpleStorageItemStack(String id, String skull, SimpleItemType type, String name, String... loreLines) {
        SlimefunItemStack itemStack = new SlimefunItemStack(
                id,
                skull,
                itemTypeColor(type) + name,
                ""
        );
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = im.getLore();
        lore.addAll(Arrays.asList(loreLines));
        lore.add("");
        lore.add(itemTypeDescriptor(type));
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public static ChatColor itemTypeColor(SimpleItemType type) {
        switch (type) {
            case DROP: return Theme.ITEM_RARE_DROP;
            case CHEST: return Theme.ITEM_CHEST;
            case MACHINE: return Theme.ITEM_MACHINE;
            case CRAFTING: return Theme.ITEM_CRAFTING;
            default: return Theme.ITEM_BASE;
        }
    }

    public static String itemTypeDescriptor(SimpleItemType type) {
        switch (type) {
            case DROP: return Theme.LORE_TYPE_DROP;
            case CHEST: return Theme.LORE_TYPE_CHEST;
            case MACHINE: return Theme.LORE_TYPE_MACHINE;
            case CRAFTING: return Theme.LORE_TYPE_CRAFT;
            default: return Theme.LORE_TYPE_BASE;
        }
    }

    public enum SimpleItemType {
        CRAFTING,
        MACHINE,
        CHEST,
        DROP,
        BASE
    }




}
