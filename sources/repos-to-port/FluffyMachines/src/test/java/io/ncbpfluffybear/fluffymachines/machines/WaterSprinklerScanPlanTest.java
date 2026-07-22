package io.ncbpfluffybear.fluffymachines.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class WaterSprinklerScanPlanTest {

    @Test
    void coversCompleteAreaWithoutOversizedBatches() {
        int area = 25;
        int budget = 4;
        Set<Integer> visited = new HashSet<>();

        for (int cycle = 0; cycle < 7; cycle++) {
            int[] indexes = WaterSprinklerScanPlan.indexes(
                (long) cycle * 20,
                12,
                64,
                -20,
                area,
                budget
            );

            assertEquals(budget, indexes.length);
            for (int index : indexes) {
                assertTrue(index >= 0 && index < area);
                visited.add(index);
            }
        }

        assertEquals(area, visited.size());
    }

    @Test
    void clampsInvalidBudget() {
        assertEquals(1, WaterSprinklerScanPlan.indexes(0, 0, 0, 0, 25, 0).length);
        assertEquals(25, WaterSprinklerScanPlan.indexes(0, 0, 0, 0, 25, 100).length);
    }
}
