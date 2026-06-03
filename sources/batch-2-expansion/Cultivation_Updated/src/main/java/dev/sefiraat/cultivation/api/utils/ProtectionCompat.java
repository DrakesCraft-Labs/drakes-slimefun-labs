package dev.sefiraat.cultivation.api.utils;

import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public final class ProtectionCompat {

    private ProtectionCompat() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean hasPermission(Player player, Location location, String interactionName) {
        Object manager = Slimefun.getProtectionManager();
        for (Method method : manager.getClass().getMethods()) {
            if (!method.getName().equals("hasPermission") || method.getParameterCount() != 3) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (!parameterTypes[0].isAssignableFrom(player.getClass()) || !parameterTypes[2].isEnum()) {
                continue;
            }
            Object target = parameterTypes[1].isAssignableFrom(location.getClass()) ? location : location.getBlock();
            try {
                Object interaction = Enum.valueOf((Class<Enum>) parameterTypes[2], interactionName);
                return (boolean) method.invoke(manager, player, target, interaction);
            } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                // Try another compatible overload.
            }
        }
        return true;
    }
}
