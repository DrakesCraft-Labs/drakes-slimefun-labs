package com.github.drakescraft_labs.extraheads.setup;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import com.github.drakescraft_labs.extraheads.ExtraHeads;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.items.CustomItemStack;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RecipeTypes {

    public static final RecipeType DECAPITATION = new RecipeType(
        new NamespacedKey(ExtraHeads.getInstance(), "decapitation"),
        new CustomItemStack(Material.IRON_SWORD, "&6Kill the specified Mob")
    );
}
