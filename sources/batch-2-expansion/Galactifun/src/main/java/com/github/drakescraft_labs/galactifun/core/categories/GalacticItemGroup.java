package com.github.drakescraft_labs.galactifun.core.categories;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.core.WorldSelector;
import com.github.drakescraft_labs.slimefun4.api.items.groups.FlexItemGroup;
import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

/**
 * Category for exploring the universe
 *
 * @author Mooy1
 */
public final class GalacticItemGroup extends FlexItemGroup {

    public GalacticItemGroup(NamespacedKey key, ItemStack item) {
        super(key, item);
    }

    @Override
    public boolean isVisible(@Nonnull Player player, @Nonnull PlayerProfile playerProfile, @Nonnull SlimefunGuideMode slimefunGuideMode) {
        return true;
    }

    @Override
    public void open(@Nonnull Player player, @Nonnull PlayerProfile playerProfile, @Nonnull SlimefunGuideMode slimefunGuideMode) {
        new WorldSelector((player1, i, itemStack, clickAction) -> {
            playerProfile.getGuideHistory().goBack(Slimefun.getRegistry().getSlimefunGuide(slimefunGuideMode));
            return false;
        }).open(player);
        playerProfile.getGuideHistory().add(this, 1);
    }

}
