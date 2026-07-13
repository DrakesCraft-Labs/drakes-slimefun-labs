package com.github.drakescraft_labs.slimefun4.core.services;

import java.util.function.IntSupplier;

/**
 * Repeats a persistence operation until its pending queue is empty or the
 * safety limit is reached.
 */
public final class PersistenceDrain {

    private PersistenceDrain() {
    }

    /**
     * Drains pending data without hiding a failed or incomplete final pass.
     *
     * @param maxPasses maximum number of writes to attempt
     * @param pendingChanges returns the current number of pending changes
     * @param save writes the currently pending changes
     * @return the number of writes attempted
     */
    public static int drain(int maxPasses, IntSupplier pendingChanges, Runnable save) {
        int passes = 0;
        int limit = Math.max(1, maxPasses);

        while (passes < limit && pendingChanges.getAsInt() > 0) {
            save.run();
            passes++;
        }

        return passes;
    }
}
