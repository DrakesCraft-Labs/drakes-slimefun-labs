package me.sfiguz7.transcendence.implementation.utils;

import java.util.Collection;
import java.util.Locale;

public final class DaxiWorldPolicy {

    private DaxiWorldPolicy() {
    }

    /** Matches exact world names and modality prefixes without depending on Bukkit. */
    public static boolean isAllowed(
        String worldName,
        Collection<String> blockedWorlds,
        Collection<String> blockedPrefixes
    ) {
        String normalized = worldName.toLowerCase(Locale.ROOT);
        boolean exactMatch = blockedWorlds.stream()
            .map(name -> name.toLowerCase(Locale.ROOT))
            .anyMatch(normalized::equals);
        if (exactMatch) {
            return false;
        }

        return blockedPrefixes.stream()
            .map(prefix -> prefix.toLowerCase(Locale.ROOT))
            .noneMatch(normalized::startsWith);
    }
}
