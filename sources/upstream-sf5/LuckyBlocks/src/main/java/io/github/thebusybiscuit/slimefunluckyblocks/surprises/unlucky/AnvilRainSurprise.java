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
public final class AnvilRainSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Anvil Rain";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		for (int x = p.getLocation().getBlockX() - 1; x <= p.getLocation().getBlockX() + 1; x++) {
			for (int z = p.getLocation().getBlockZ() - 1; z <= p.getLocation().getBlockZ() + 1; z++) {
				for (int y = p.getLocation().getBlockY() - 1; y <= p.getLocation().getBlockY() + 2; y++) {
					if (y == p.getLocation().getBlockY() - 1) p.getWorld().getBlockAt(x, y, z).setType(Material.OBSIDIAN);
					else if (!(p.getLocation().getBlockX() == x && p.getLocation().getBlockZ() == z)) p.getWorld().getBlockAt(x, y, z).setType(Material.IRON_BARS);
				}
			}
		}
		p.getWorld().spawnFallingBlock(p.getLocation().add(0.5, 16, 0.5), Material.ANVIL.createBlockData());
		p.getWorld().spawnFallingBlock(p.getLocation().add(0.5, 24, 0.5), Material.ANVIL.createBlockData());
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.UNLUCKY;
	}

}
