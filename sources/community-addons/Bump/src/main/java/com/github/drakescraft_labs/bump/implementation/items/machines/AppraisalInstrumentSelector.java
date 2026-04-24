package com.github.drakescraft_labs.bump.implementation.items.machines;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.dough.items.CustomItemStack;
import com.github.drakescraft_labs.bump.api.appraise.AppraiseType;
import com.github.drakescraft_labs.bump.implementation.Bump;
import com.github.drakescraft_labs.bump.implementation.menus.AppraiseTypesMenu;
import com.github.drakescraft_labs.bump.utils.AppraiseUtils;

import lombok.NonNull;

/**
 * A selector menu that can be opened from {@link AppraisalInstrument}.
 *
 * @author ybw0014
 */
final class AppraisalInstrumentSelector extends AppraiseTypesMenu {

    public AppraisalInstrumentSelector(@NonNull Consumer<AppraiseType> successCallback, @NonNull Runnable backCallback) {
        super(Bump.getLocalization().getString("gui.appraise_type_selector_menu.title"), successCallback, backCallback);
    }

    @Nonnull
    @Override
    public ItemStack getDisplayItem(@Nonnull AppraiseType type) {
        List<String> lore = AppraiseUtils.getDescriptionLore(type);
        lore.addAll(Bump.getLocalization().getStringList("gui.appraise_type_selector_menu.lore"));

        return new CustomItemStack(
            Material.PAPER,
            Bump.getLocalization().getString("appraise_info.name", type.getName()),
            lore
        );
    }
}
