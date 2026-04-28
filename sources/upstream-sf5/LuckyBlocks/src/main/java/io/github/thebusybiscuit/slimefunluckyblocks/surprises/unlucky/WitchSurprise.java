package io.github.thebusybiscuit.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class WitchSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Witch and Bats";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1));
		l.getWorld().spawnEntity(l, EntityType.WITCH);
		
		for (int i = 0; i < 16; i++) {
			l.getWorld().spawnEntity(l, EntityType.BAT);
		}
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.UNLUCKY;
	}

}
