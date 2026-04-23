package me.schntgaispock.wildernether.util;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * Various item generators
 * 
 * @author SchnTgaiSpock
 */
public class ItemUtil {

    static Pattern spacePattern = Pattern.compile(" ");
    
    public static ItemStack getWaterBottle() {
        ItemStack potion = new CustomItemStack(Material.POTION);
        PotionMeta meta = ((PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION));
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(meta);
        return potion;
    }

    public static SlimefunItemStack modifyWaterBottle(SlimefunItemStack potion, Color color) {
        SlimefunItemStack water = modifyPotionStack(potion, color);
        PotionMeta meta = ((PotionMeta) water.getItemMeta());
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        water.setItemMeta(meta);
        return water;
    }

    public static SlimefunItemStack modifyPotionStack(SlimefunItemStack potion, Color color, PotionEffect... type) {
        PotionMeta meta = (PotionMeta) potion.getItemMeta().clone();
        for (PotionEffect t : type) {
            meta.addCustomEffect(t, true);
        }
        meta.setColor(color);
        potion.setItemMeta(meta);
        return potion;
    }

    public static SlimefunItemStack formattedSlimefunItemStack(Theme theme, String id, Material mat, String name, String... lore) {
        String fName = theme.getColor() + name;
        if (lore.length == 0) {
            return new SlimefunItemStack(id, mat, fName);
        }

        int l = lore.length - 1;
        boolean hasPostLore = false;
        String[] fLore = new String[l + 1];

        fLore[0] = theme.getNoticeColor() + lore[0];
        fLore[l] = l > 1 && (hasPostLore = lore[l-1].equals(""))? theme.getNoticeColor() + "&o" + lore[l] : "";
        for (int i = 1; i <= (hasPostLore ? l - 1 : l); i++) {
            fLore[i] = theme.getLoreColor() + lore[i];
        }

        return new SlimefunItemStack(id, mat, fName, fLore);
    }

}
