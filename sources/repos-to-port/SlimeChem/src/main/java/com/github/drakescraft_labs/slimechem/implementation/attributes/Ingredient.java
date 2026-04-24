package com.github.drakescraft_labs.slimechem.implementation.attributes;

import com.github.drakescraft_labs.slimechem.implementation.atomic.MoleculeIngredient;
import com.github.drakescraft_labs.slimechem.implementation.atomic.Molecule;

import javax.annotation.Nonnull;

/**
 * This interface should be implemented by any {@link Object}
 * that could be an {@link MoleculeIngredient} of a {@link Molecule}
 *
 * @author Mooy1
 *
 */
public interface Ingredient extends Itemable {

    @Nonnull
    String getFormula(int i);

    @Nonnull
    MoleculeIngredient asIngredient(int amount);

    @Nonnull
    MoleculeIngredient asIngredient();
}
