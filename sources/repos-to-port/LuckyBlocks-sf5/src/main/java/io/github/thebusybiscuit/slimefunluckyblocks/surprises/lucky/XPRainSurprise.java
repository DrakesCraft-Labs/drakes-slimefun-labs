package io.github.thebusybiscuit.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class XPRainSurprise implements Surprise {

    @Nonnull
    @Override
    public String getName() {
        return "XP Rain";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        FireworkUtils.launchRandom(p, 3);
        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(0, 0.5, 0.15));
        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(0.15, 0.5, 0.15));
        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(0.15, 0.5, 0));

        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(0, 0.5, -0.15));
        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(-0.15, 0.5, -0.15));
        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(-0.15, 0.5, 0));

        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(-0.15, 0.5, 0.15));
        l.getWorld().spawnEntity(l.add(0, 2, 0), EntityType.EXPERIENCE_BOTTLE).setVelocity(new Vector(0.15, 0.5, -0.15));
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
