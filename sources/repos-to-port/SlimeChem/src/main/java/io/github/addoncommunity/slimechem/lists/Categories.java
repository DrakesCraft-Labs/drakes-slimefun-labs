package io.github.addoncommunity.slimechem.lists;

import dev.drake.dough.items.CustomItemStack;


import io.github.addoncommunity.slimechem.SlimeChem;
import com.github.drakescraft-labs.slimefun4.api.items.Category;
import dev.drake.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class Categories {
    
    private static final SlimeChem instance = SlimeChem.getInstance();
    
    public static final Category MACHINES = new Category(new NamespacedKey(instance, "machines"), new CustomItemStack(Material.DIAMOND_BLOCK, "Chemical Machines"), 3);
    public static final Category MOLECULES = new Category(new NamespacedKey(instance, "molecules"), new CustomItemStack(Material.DIAMOND, "Molecules"), 3);
    public static final Category ELEMENTS = new Category(new NamespacedKey(instance, "elements"), new CustomItemStack(Material.DIAMOND, "Elements"), 3);
    public static final Category ISOTOPES = new Category(new NamespacedKey(instance, "Is�topos"), new CustomItemStack(Material.DIAMOND, "Is�topos"), 3);
    public static final Category SUBATOMIC = new Category(new NamespacedKey(instance, "subatomic"), new CustomItemStack(Material.DIAMOND, "Part�culas Subat�micas"), 3);

}
