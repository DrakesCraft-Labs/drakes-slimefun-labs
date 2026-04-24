package com.github.drakescraft_labs.slimechem.implementation.attributes;

/**
 * This interface represents an atom
 *
 * @author Seggan
 */
public interface Atom extends Itemable {

    double getMass();

    int getNumber();

    int getRadiationLevel();

    boolean isRadioactive();
}
