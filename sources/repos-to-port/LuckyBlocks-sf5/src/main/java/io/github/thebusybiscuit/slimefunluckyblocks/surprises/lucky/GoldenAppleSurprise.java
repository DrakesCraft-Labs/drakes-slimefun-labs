package io.github.thebusybiscuit.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class GoldenAppleSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Golden Apples";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.GOLDEN_APPLE, 3));
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
