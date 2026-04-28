package io.github.thebusybiscuit.slimefunluckyblocks.surprises;

import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a surprise event that is triggered when a Lucky Block is broken.
 *
 * @author TheBusyBiscuit
 */
public interface Surprise {

    /**
     * Returns the display name of this surprise.
     *
     * @return the name of this surprise
     */
    @Nonnull
    String getName();

    /**
     * Returns the luck level category for this surprise.
     *
     * @return the {@link LuckLevel} of this surprise
     */
    @Nonnull
    LuckLevel getLuckLevel();

    /**
     * Activates this surprise at the given location for the given player.
     *
     * @param random the {@link Random} instance to use
     * @param p      the {@link Player} who triggered the surprise
     * @param l      the {@link Location} where the surprise occurs
     */
    void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l);

}
