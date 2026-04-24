package com.github.drakescraft_labs.slimechem.implementation.machines;

import com.github.drakescraft_labs.slimechem.implementation.atomic.Molecule;
import com.github.drakescraft_labs.slimechem.implementation.atomic.MoleculeIngredient;
import com.github.drakescraft_labs.slimechem.lists.Categories;
import com.github.drakescraft_labs.slimechem.lists.Items;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Combines element and molecules into molecules and substances
 * 
 * @author Mooy1
 *
 */
public class ChemicalCombiner extends SlimefunItem { // TODO: change to some sort of AContainer with 6 slots for recipes

    public static final int ENERGY = 15;
    
    private static final int[] inputSlots = {10, 11, 12, 19, 20, 21};
    private static final int[] outputSlots = {25};
    
    public ChemicalCombiner() {
        super(Categories.MACHINES, Items.CHEMICAL_COMBINER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });

        for (Molecule m : Molecule.values()) {
            // register a container recipe
        }

        addRecipe(new SlimefunItemStack(SlimefunItems.COPPER_INGOT, 2), null, MoleculeIngredient.DIHYDROGEN);
        
    }

    public void addRecipe(@Nonnull ItemStack item, @Nonnull MoleculeIngredient... is) {
       // register a container recipe
    }

}

