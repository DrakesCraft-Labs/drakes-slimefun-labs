package io.github.thebusybiscuit.slimefunluckyblocks.surprises.lucky;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class TamedCatsSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Tamed Cats";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		List<Cat.Type> catTypes = Registry.CAT_VARIANT.stream().toList();
		for (int i = 0; i < 8; i++) {
			Cat cat = (Cat) l.getWorld().spawnEntity(l.add(random.nextInt(4) - (double) random.nextInt(8), 1, random.nextInt(4) - (double) random.nextInt(8)), EntityType.CAT);
			cat.setAdult();
			cat.setOwner(p);
			cat.setCatType(catTypes.get(random.nextInt(catTypes.size())));
		}
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
