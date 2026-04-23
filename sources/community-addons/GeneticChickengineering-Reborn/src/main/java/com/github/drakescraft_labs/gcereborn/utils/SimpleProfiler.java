package com.github.drakescraft_labs.gcereborn.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Very small profiler to record nanos per key and periodically report averages.
 */
public final class SimpleProfiler {

    private static final ConcurrentHashMap<String, AtomicLong> totals = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicLong> counts = new ConcurrentHashMap<>();

    private SimpleProfiler() {}

    public static void record(String key, long nanos) {
        totals.computeIfAbsent(key, k -> new AtomicLong()).addAndGet(nanos);
        counts.computeIfAbsent(key, k -> new AtomicLong()).incrementAndGet();
    }

    public static void startReporter(Plugin plugin) {
        // Report every 60 seconds asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    reportAndReset(plugin);
                } catch (Throwable t) {
                    plugin.getLogger().severe("Profiler reporter failed: " + t.getMessage());
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20L * 60L, 20L * 60L);
    }

    private static void reportAndReset(Plugin plugin) {
        if (totals.isEmpty()) {
            return;
        }

        List<Map.Entry<String, AtomicLong>> entries = new ArrayList<>(totals.entrySet());
        entries.sort(Comparator.comparingLong(e -> -e.getValue().get()));

        StringBuilder sb = new StringBuilder();
        sb.append("[SimpleProfiler] Report:\n");

        for (Map.Entry<String, AtomicLong> e : entries) {
            String key = e.getKey();
            long total = e.getValue().getAndSet(0L);
            AtomicLong cnt = counts.get(key);
            long c = cnt == null ? 0L : cnt.getAndSet(0L);
            if (c == 0) continue;
            double avgMicros = (total / (double) c) / 1000.0;
            sb.append(String.format(" - %s: count=%d avg=%.3fµs total=%,dns\n", key, c, avgMicros, total));
        }

        plugin.getLogger().info(sb.toString());
    }
}
