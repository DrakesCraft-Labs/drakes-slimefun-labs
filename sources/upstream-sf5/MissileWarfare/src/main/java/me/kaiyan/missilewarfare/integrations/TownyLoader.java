package me.kaiyan.missilewarfare.integrations;

import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Handles integration with the Towny plugin for explosion permission checks.
 * Currently stubbed out pending compatible dependency versions.
 *
 * @author MissileWarfare contributors
 */
public class TownyLoader {

    /**
     * Enables Towny integration within the plugin.
     */
    public static void setup() {
        MissileWarfare.townyEnabled = true;
    }

    /**
     * Checks whether an explosion is permitted at the given location
     * based on Towny town membership rules.
     *
     * @param nearestPlayer the player nearest to the missile launch site
     * @param loc           the location of the explosion
     * @return {@code true} if the explosion should proceed, {@code false} otherwise
     */
    public static boolean exploded(@Nonnull Player nearestPlayer, @Nonnull Location loc) {
        return false;
    }
}
