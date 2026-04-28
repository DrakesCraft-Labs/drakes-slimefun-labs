package io.github.addoncommunity.galactifun.api.universe.types;


/**
 * Interface for identifying enums in this package
 *
 * @author Mooy1
 */
public class UniversalType {

    private final String id;

    private final String description;


    UniversalType(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String id() { return id; }

    public String description() { return description; }
}
