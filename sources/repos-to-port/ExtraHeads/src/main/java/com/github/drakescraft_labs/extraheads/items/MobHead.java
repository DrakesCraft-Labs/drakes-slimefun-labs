package com.github.drakescraft_labs.extraheads.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.extraheads.ExtraHeads;
import com.github.drakescraft_labs.extraheads.setup.ItemGroups;
import com.github.drakescraft_labs.extraheads.setup.RecipeTypes;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

import lombok.Getter;

public class MobHead extends SlimefunItem {

    @Getter
    private final EntityType entityType;

    @ParametersAreNonnullByDefault
    public MobHead(EntityType type, SlimefunItemStack item, ItemStack recipe) {
        super(ItemGroups.MAIN, item, RecipeTypes.DECAPITATION, new ItemStack[] {
            null, null, null, null, recipe, null, null, null, null
        });

        this.entityType = type;
    }

    @Override
    public void postRegister() {
        super.postRegister();

        if (!isDisabled()) {
            ExtraHeads.getRegistry().getHeads().put(entityType, this);
        }
    }
}
