package com.github.drakescraft-labs.slimefun4.utils.itemstack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.drake.dough.common.ChatColors;
import dev.drake.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft-labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft-labs.slimefun4.core.guide.SlimefunGuideImplementation;
import com.github.drakescraft-labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;

/**
 * This is just a helper {@link ItemStack} class for the {@link SlimefunGuide} {@link ItemStack}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuide
 * @see SlimefunGuideImplementation
 *
 */
public class SlimefunGuideItem extends ItemStack {

    public SlimefunGuideItem(@Nonnull SlimefunGuideImplementation implementation, @Nonnull String name) {
        super(Material.ENCHANTED_BOOK);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColors.color(name));

        List<String> lore = new ArrayList<>();
        SlimefunGuideMode type = implementation.getMode();
        lore.add(type == SlimefunGuideMode.CHEAT_MODE ? ChatColors.color("&4&lOnly openable by Admins") : "");
        lore.add(ChatColors.color("&eRight Click &8\u21E8 &7Browse Items"));
        lore.add(ChatColors.color("&eShift + Right Click &8\u21E8 &7Open Settings / Credits"));

        meta.setLore(lore);

        PersistentDataAPI.setString(meta, Slimefun.getRegistry().getGuideDataKey(), type.name());
        Slimefun.getItemTextureService().setTexture(meta, "SLIMEFUN_GUIDE");

        setItemMeta(meta);
    }

}
