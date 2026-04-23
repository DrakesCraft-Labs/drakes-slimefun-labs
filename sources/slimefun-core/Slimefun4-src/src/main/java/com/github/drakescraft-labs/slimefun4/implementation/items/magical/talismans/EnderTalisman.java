package com.github.drakescraft-labs.slimefun4.implementation.items.magical.talismans;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.block.EnderChest;
import org.bukkit.inventory.ItemStack;

import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.items.groups.LockedItemGroup;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;

/**
 * An {@link EnderTalisman} is a special version of {@link Talisman}
 * that works while it is in your {@link EnderChest}.
 * 
 * @author TheBusyBiscuit
 *
 */
class EnderTalisman extends Talisman {

    private static final LockedItemGroup ENDER_TALISMANS_ITEMGROUP = new LockedItemGroup(new NamespacedKey(Slimefun.instance(), "ender_talismans"), new CustomItemStack(SlimefunItems.ENDER_TALISMAN, "&7Talismans - &aTier II"), 3, Talisman.TALISMANS_ITEMGROUP.getKey());

    @ParametersAreNonnullByDefault
    public EnderTalisman(Talisman parent, SlimefunItemStack item) {
        super(ENDER_TALISMANS_ITEMGROUP, item, new ItemStack[] { SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3, null, parent.getItem(), null, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.ENDER_LUMP_3 }, parent.isConsumable(), parent.isEventCancelled(), parent.getMessageSuffix(), parent.getChance(), parent.getEffects());
    }

    @Override
    void loadEnderTalisman() {
        /*
         * Let's override that, otherwise we would be creating Ender Talismans
         * for every Ender Talisman too.
         */
    }

    @Override
    public void postRegister() {
        /*
         * Let's override that, otherwise we would be creating Ender Talismans
         * for every Ender Talisman too.
         */
    }
}
