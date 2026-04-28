package io.github.thebusybiscuit.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class VoidHoleSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Void Hole";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		for (int x = p.getLocation().getBlockX() - 1; x <= p.getLocation().getBlockX() + 1; x++) {
			for (int z = p.getLocation().getBlockZ() - 1; z <= p.getLocation().getBlockZ() + 1; z++) {
				for (int y = p.getLocation().getBlockY() + 1; y >= 0; y--) {
					l.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.UNLUCKY;
	}

}
