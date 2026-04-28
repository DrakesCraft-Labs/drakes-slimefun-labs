package io.github.mooy1.infinityexpansion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class TestInfinityExpansion {

    @Test
    void classLoads() {
        assertDoesNotThrow(() -> Class.forName("io.github.mooy1.infinityexpansion.InfinityExpansion"));
    }

}
