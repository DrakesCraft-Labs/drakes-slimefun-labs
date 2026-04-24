package com.github.drakescraft_labs.bump.implementation.groups;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.dough.items.CustomItemStack;
import com.github.drakescraft_labs.bump.api.appraise.AppraiseType;
import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.menus.AppraiseTypesMenu;
import com.github.drakescraft_labs.bump.utils.AppraiseUtils;

final class AppraiseInfoMenu extends AppraiseTypesMenu {
    @ParametersAreNonnullByDefault
    public AppraiseInfoMenu(String name, Consumer<AppraiseType> successCallback, Runnable backCallback) {
        super(name, successCallback, backCallback);
    }

    @Nonnull
    @Override
    public ItemStack getDisplayItem(@Nonnull AppraiseType type) {
        List<String> lore = AppraiseUtils.getDescriptionLore(type);
        lore.add("");
        lore.add(Bump.getLocalization().getString("appraise_info.click"));

        return new CustomItemStack(
            Material.PAPER,
            Bump.getLocalization().getString("appraise_info.name", type.getName()),
            lore
        );
    }
}
