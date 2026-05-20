package io.github.sefiraat.networks.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public final class Utils {

    public static String color(String str) {
        if (str == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> colorLore(String... lore) {
        List<String> ll = new ArrayList<>();
        for (String l : lore) {
            ll.add(color(l));
        }

        return ll;
    }

    public static void giveOrDropItem(Player p, ItemStack toGive) {
        for (ItemStack leftover : p.getInventory().addItem(toGive).values()) {
            p.getWorld().dropItemNaturally(p.getLocation(), leftover);
        }
    }

    public static void send(CommandSender p, String message) {
        p.sendMessage(color("&7[&6Network&7] &r" + message));
    }
}
