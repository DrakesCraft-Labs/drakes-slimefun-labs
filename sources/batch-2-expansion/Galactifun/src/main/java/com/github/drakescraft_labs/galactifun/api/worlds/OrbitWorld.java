package com.github.drakescraft_labs.galactifun.api.worlds;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.galactifun.api.universe.PlanetaryObject;

/**
 * An interface implemented by {@link AlienWorld}s to indicate that they are a space world orbiting
 * another {@link PlanetaryObject}
 */
public interface OrbitWorld {

    /**
     * Gets the {@link PlanetaryObject} that this world is orbiting
     *
     * @return the {@link PlanetaryObject} that this world is orbiting
     */
    @Nonnull
    PlanetaryObject getPlanet();

}
