package me.voper.slimeframe.utils;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import com.github.drakescraft_labs.slimefun4.utils.compatibility.VersionedEnchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

import lombok.experimental.UtilityClass;

@UtilityClass
@ParametersAreNonnullByDefault
public final class Utils {

    public static final ItemStack[] NULL_ITEMS_ARRAY = new ItemStack[]{null, null, null, null, null, null, null, null, null};

    public static final int SF_TICK_RATE = Slimefun.getTickerTask().getTickRate();
    public static final double SF_TICKS_PER_SECOND = (double) 20 / SF_TICK_RATE;

    public static int sfTicksToSeconds(int ticks) {
        return (int) Math.round(ticks * SF_TICKS_PER_SECOND);
    }

    public static int secondsToSfTicks(int seconds) {
        return (int) Math.round((double) (seconds * 20) / SF_TICK_RATE);
    }

    public static int energyPerTickToSeconds(int energy) {
        return (int) Math.round((double) energy * SF_TICKS_PER_SECOND);
    }

    public static void enchant(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.addEnchant(VersionedEnchantment.LUCK_OF_THE_SEA, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemMeta);
    }

    public static void disenchant(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.removeEnchant(VersionedEnchantment.LUCK_OF_THE_SEA);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemMeta);
    }

    public static String formatMaterialString(Material m) {
        String[] name = m.name().toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();

        for (String word : name) {
            if (!word.isBlank()) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
                sb.append(capitalizedWord).append(" ");
            }
        }

        return sb.toString().trim();
    }

}
