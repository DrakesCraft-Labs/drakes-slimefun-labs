package com.github.drakescraft_labs.villagertrade.implementation.managers;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.villagertrade.VillagerTrade;
import com.github.drakescraft_labs.villagertrade.implementation.menu.TradeMenuTask;

public final class TaskManager {
    public TaskManager() {
        scheduleSync(20, new TradeMenuTask());
    }

    private void scheduleSync(int interval, @Nonnull Runnable runnable) {
        VillagerTrade.getScheduler().repeat(interval, runnable);
    }

    private void scheduleAsync(int interval, @Nonnull Runnable runnable) {
        VillagerTrade.getScheduler().repeatAsync(interval, runnable);
    }
}
