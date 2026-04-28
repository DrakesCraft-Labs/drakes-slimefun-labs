package io.github.thebusybiscuit.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class TamedDogsSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Tamed Dogs";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		for (int i = 0; i < 8; i++) {
			Wolf dog = (Wolf) l.getWorld().spawnEntity(l.add(random.nextInt(4) - (double) random.nextInt(8), 1, random.nextInt(4) - (double) random.nextInt(8)), EntityType.WOLF);
			dog.setAdult();
			dog.setCollarColor(DyeColor.values()[random.nextInt(DyeColor.values().length)]);
			dog.setOwner(p);
		}
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
