package org.metamechanists.aircraft.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;

@UtilityClass
public class Keys {
    public @NotNull NamespacedKey newKey(String key) {
        return new NamespacedKey(Aircraft.getInstance(), key);
    }

    public final NamespacedKey AIRCRAFT = newKey("AIRCRAFT");
}
