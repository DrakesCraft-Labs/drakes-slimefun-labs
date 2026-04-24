package com.github.drakescraft_labs.slimechem.implementation.subatomic;

import lombok.Getter;

/**
 * Enum of quarks
 *
 * @author Mooy1
 * 
 * @see Nucleon
 *
 */
@Getter
public enum Quark implements Fermion {

    UP(),
    DOWN(),
    CHARM(),
    STRANGE(),
    TOP(),
    BOTTOM();
    
}
