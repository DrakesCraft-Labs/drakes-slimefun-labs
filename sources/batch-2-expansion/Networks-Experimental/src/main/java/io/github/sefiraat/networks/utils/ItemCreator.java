package io.github.sefiraat.networks.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemCreator {
    public static ItemStack create(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(name));
        meta.setLore(Utils.colorLore(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(ItemStack item, String name, String... lore) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(name));
        meta.setLore(Utils.colorLore(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(ItemStack item, String name) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.color(name));
        item.setItemMeta(meta);
        return item;
    }
}
