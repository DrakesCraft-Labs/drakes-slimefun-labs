package me.kaiyan.missilewarfare.util;

import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Provides translation and localisation support by reading strings from
 * language pack configuration files.
 *
 * @author MissileWarfare contributors
 */
public class Translations {
    public static Config pack;
    public static String missileType = "";

    /**
     * Returns the translated string for the given config key.
     *
     * @param key the translation key
     * @return the translated string
     */
    @Nonnull
    public static String get(@Nonnull String key) {
        return pack.getString(key);
    }

    /**
     * Returns the translated string array for the given config key.
     *
     * @param key the translation key
     * @return an array of translated strings
     */
    @Nonnull
    public static String[] getarr(@Nonnull String key) {
        return pack.getStringList(key).toArray(new String[0]);
    }

    /**
     * Returns a formatted page of text built from the string list at the given key.
     *
     * @param key the translation key for the page content
     * @return the formatted page string
     */
    @Nonnull
    public static String getPage(@Nonnull String key) {
        StringBuilder output = new StringBuilder();
        String[] arr = getarr(key + ".content");

        for (String str : arr) {
            str += "\n";
            output.append(ChatColor.translateAlternateColorCodes('&', str));
        }

        return output.toString();
    }

    /**
     * Returns the colour-formatted material display name.
     *
     * @param material the material translation key
     * @return the formatted material name
     */
    @Nonnull
    public static String getMaterialName(@Nonnull String material) {
        return ChatColor.translateAlternateColorCodes('&', "&e" + get("materials." + material + ".name"));
    }

    /**
     * Returns the colour-formatted material lore lines.
     *
     * @param material the material translation key
     * @return an array of lore strings
     */
    @Nonnull
    public static String[] getMaterialLore(@Nonnull String material) {
        List<String> lore = pack.getStringList("materials." + material + ".lore");
        for (String str : lore) {
            ChatColor.translateAlternateColorCodes('&', "&7" + str);
        }
        return lore.toArray(new String[0]);
    }

    /**
     * Returns the colour-formatted type lore for the current missile type.
     *
     * @return the type lore string
     */
    @Nonnull
    public static String getTypeLore() {
        return ChatColor.translateAlternateColorCodes('&', "&c" + get("missiles." + missileType + ".lore"));
    }

    /**
     * Sets the current missile type context for subsequent translation calls.
     *
     * @param type the missile type key
     */
    public static void setType(@Nonnull String type) {
        missileType = type;
    }

    /**
     * Returns the colour-formatted missile display name for the given variant.
     *
     * @param type the missile variant key
     * @return the formatted missile name
     */
    @Nonnull
    public static String getMissileName(@Nonnull String type) {
        return ChatColor.translateAlternateColorCodes('&', "&e" + get("missiles." + missileType + ".missiles." + type + ".name"));
    }

    /**
     * Returns the colour-formatted missile variant string.
     *
     * @param type the missile variant key
     * @return the formatted variant string
     */
    @Nonnull
    public static String getMissileVariant(@Nonnull String type) {
        return ChatColor.translateAlternateColorCodes('&', "&a" + get("missiles." + missileType + ".missiles." + type + ".variant"));
    }

    /**
     * Returns the colour-formatted missile lore for the given variant.
     *
     * @param type the missile variant key
     * @return the formatted lore string
     */
    @Nonnull
    public static String getMissileLore(@Nonnull String type) {
        return ChatColor.translateAlternateColorCodes('&', "&7" + get("missiles." + missileType + ".missiles." + type + ".lore"));
    }

    /**
     * Returns the colour-formatted display name for a special missile type.
     *
     * @param type the special missile key
     * @return the formatted name
     */
    @Nonnull
    public static String getSpecialName(@Nonnull String type) {
        return ChatColor.translateAlternateColorCodes('&', "&c" + get("specialmissiles." + type + ".name"));
    }

    /**
     * Returns the colour-formatted lore array for a special missile type.
     *
     * @param type the special missile key
     * @return an array of lore strings
     */
    @Nonnull
    public static String[] getSpecialLore(@Nonnull String type) {
        List<String> lore = pack.getStringList("specialmissiles." + type + ".lore");
        for (String str : lore) {
            ChatColor.translateAlternateColorCodes('&', "&7" + str);
        }
        return lore.toArray(new String[0]);
    }

    /**
     * Returns the colour-formatted after-lore for a special missile type.
     *
     * @param type the special missile key
     * @return the formatted after-lore string
     */
    @Nonnull
    public static String getSpecialALore(@Nonnull String type) {
        return ChatColor.translateAlternateColorCodes('&', "&7" + get("specialmissiles." + type + ".afterlore"));
    }

    /**
     * Initialises the translation system with the given language pack config.
     *
     * @param cfg the language pack configuration
     */
    public static void setup(@Nonnull Config cfg) {
        pack = cfg;
    }
}
