package com.github.drakescraft_labs.gcereborn.core.adapters;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.bukkit.entity.Animals;
import org.junit.jupiter.api.Test;

class AnimalsAdapterTest {

    /**
     * Los ítems de prueba no serializan un animal, por lo que su adaptador es nulo.
     */
    @Test
    void applyLeavesDefaultEntityUntouchedWhenDataIsMissing() {
        AnimalsAdapter<Animals> adapter = new AnimalsAdapter<>(Animals.class);

        assertDoesNotThrow(() -> adapter.apply(null, null));
    }

}
