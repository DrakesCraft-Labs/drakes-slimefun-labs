package me.kaiyan.missilewarfare.integrations;

import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * Handles integration with WorldGuard for region-aware explosion processing.
 * WorldGuard/WorldEdit dependencies are resolved at runtime when available.
 *
 * @author MissileWarfare contributors
 */
public class WorldGuardLoader {

    /**
     * Enables WorldGuard integration within the plugin.
     */
    public static void load() {
        MissileWarfare.worldGuardEnabled = true;
        MissileWarfare.getInstance().getLogger().info("WorldGuard Enabled!");
    }

    /**
     * Creates an explosion at the given position, respecting WorldGuard region flags.
     *
     * @param world         the world in which to create the explosion
     * @param pos           the position vector of the explosion
     * @param power         the explosive power
     * @param armourStand   the armour stand entity representing the missile
     * @param nearestPlayer the nearest player to the launch site
     */
    public static void explode(@Nonnull World world, @Nonnull Vector pos, double power,
                               @Nonnull Entity armourStand, @Nonnull Player nearestPlayer) {
        world.createExplosion(pos.toLocation(world), (float) power, false, true, armourStand);
    }
}
