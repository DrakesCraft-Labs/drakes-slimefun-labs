package com.xmooncorp.magic8ball.implementation;

import com.xmooncorp.magic8ball.Magic8Ball;
import com.xmooncorp.magic8ball.core.ConfigBasedLocalization;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public final class Items {

    private Items() {}

    private static final ConfigBasedLocalization localization = Magic8Ball.instance().localization();

    private static final String[] m8ballFragmentLore = localization.getStringList("items.M8BALL_FRAGMENT.lore").toArray(new String[0]);
    private static final String[] m8ballItemLore = localization.getStringList("items.M8BALL_ITEM.lore").toArray(new String[0]);

    /* Materials */
    public static final SlimefunItemStack MAGIC_8_BALL_FRAGMENT = new SlimefunItemStack(
            "MAGIC_8_BALL_FRAGMENT",
            Material.ECHO_SHARD,
            localization.getString("items.M8BALL_FRAGMENT.name"),
            m8ballFragmentLore
    );

    /* Tools */
    public static final SlimefunItemStack MAGIC_8_BALL = new SlimefunItemStack(
            "MAGIC_8_BALL",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYyNDEyZDYzZGQyZTBlMjMyMjMwYWJkMDRhOGY3MjZlNTFjOTNhNzI2ODZjN2RhZTE3MjJjNDY3N2Y5ZjU0OCJ9fX0=",
            localization.getString("items.M8BALL_ITEM.name"),
            m8ballItemLore
    );

    static {
        addGlow(MAGIC_8_BALL_FRAGMENT);
        makeUnbreakable(MAGIC_8_BALL_FRAGMENT);

        addGlow(MAGIC_8_BALL);
        makeUnbreakable(MAGIC_8_BALL);
    }

    private static void addGlow(@Nonnull SlimefunItemStack item) {
        item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    private static void makeUnbreakable(@Nonnull SlimefunItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }
}
