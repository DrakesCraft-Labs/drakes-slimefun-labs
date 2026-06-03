package com.github.drakescraft_labs.slimechem.lists;

import com.github.drakescraft_labs.slimechem.SlimeChem;
import com.github.drakescraft_labs.slimefun4.utils.HeadTexture;
import lombok.experimental.UtilityClass;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

@UtilityClass
public final class RecipeTypes {
    
    private static final SlimeChem instance = SlimeChem.getInstance();

    public static final RecipeType RTG = new RecipeType(new NamespacedKey(instance, "rtg"), new CustomItemStack(
        HeadTexture.NUCLEAR_REACTOR.getAsItemStack(),
        "&4RTG",
        "&7You can obtain this isotope by putting the listed",
        "&7isotopes into the Radioisotope Thermoelectric Generator"
    ));
    public static final RecipeType FUSION = new RecipeType(new NamespacedKey(instance, "fusion"), new CustomItemStack(Material.EMERALD_BLOCK, "test"));
    public static final RecipeType DISSOLVER = new RecipeType(new NamespacedKey(instance, "dissolver"), Items.CHEMICAL_DISSOLVER);
    public static final RecipeType COMBINER = new RecipeType(new NamespacedKey(instance, "combiner"), Items.CHEMICAL_COMBINER);
    
}
