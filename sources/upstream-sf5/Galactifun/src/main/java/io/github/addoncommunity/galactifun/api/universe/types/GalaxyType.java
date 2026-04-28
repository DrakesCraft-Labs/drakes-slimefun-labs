package io.github.addoncommunity.galactifun.api.universe.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;


import com.google.common.collect.ImmutableSet;

/**
 * Types of galaxies
 *
 * @author Mooy1
 */
public final class GalaxyType extends UniversalType {

    public static final Map<String, GalaxyType> allTypes = new HashMap<>();

    public static final GalaxyType ELLIPTICAL = new GalaxyType("Elliptical", "ELLIPTICAL");
    public static final GalaxyType SPIRAL = new GalaxyType("Spiral", "SPIRAL");
    public static final GalaxyType IRREGULAR = new GalaxyType("Irregular", "IRREGULAR");

    public GalaxyType(String name, String id) {
        super(name, id);
        allTypes.put(id, this);
    }

    public static GalaxyType getById(@Nonnull String id) {
        return allTypes.get(id);
    }

    @Nonnull
    public static Set<GalaxyType> allTypes() {
        return ImmutableSet.copyOf(allTypes.values());
    }

}
