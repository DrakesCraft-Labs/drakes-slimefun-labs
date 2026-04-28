package io.github.thebusybiscuit.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class ZombiePigmenSurprise implements Surprise {

    @Nonnull
    @Override
    public String getName() {
        return "Angry Zombie Pigmen";
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        for (int i = 0; i < 4; i++) {
            PigZombie pigman = (PigZombie) l.getWorld().spawnEntity(l.add(random.nextInt(4) - (double) random.nextInt(8), 1, random.nextInt(4) - (double) random.nextInt(8)), EntityType.ZOMBIFIED_PIGLIN);
            pigman.setAngry(true);
            pigman.setTarget(p);
        }
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.UNLUCKY;
    }

}
