package com.github.drakescraft_labs.galactifun.api.universe.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Interface for identifying enums in this package
 *
 * @author Mooy1
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UniversalType {

    @Getter
    private final String id;

    @Getter
    private final String description;

}
