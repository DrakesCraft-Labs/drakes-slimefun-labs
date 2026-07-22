package io.ncbpfluffybear.fluffymachines.machines;

final class WaterSprinklerScanPlan {

    private static final int SCAN_WINDOW_TICKS = 20;

    private WaterSprinklerScanPlan() {
    }

    /**
     * Rotates contiguous work batches across the complete sprinkler area. The
     * location phase prevents nearby sprinklers from scanning the same cells in
     * the same server tick.
     */
    static int[] indexes(long worldTime, int x, int y, int z, int area, int budget) {
        int normalizedArea = Math.max(1, area);
        int normalizedBudget = Math.max(1, Math.min(normalizedArea, budget));
        long cycle = Math.floorDiv(worldTime, SCAN_WINDOW_TICKS);
        long locationPhase = 73428767L * x ^ 912931L * y ^ 19349663L * z;
        int start = Math.floorMod(cycle * normalizedBudget + locationPhase, normalizedArea);
        int[] indexes = new int[normalizedBudget];

        for (int i = 0; i < normalizedBudget; i++) {
            indexes[i] = (start + i) % normalizedArea;
        }

        return indexes;
    }
}
