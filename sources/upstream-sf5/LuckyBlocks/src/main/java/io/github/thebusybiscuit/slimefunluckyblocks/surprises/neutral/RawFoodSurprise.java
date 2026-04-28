package io.github.thebusybiscuit.slimefunluckyblocks.surprises.neutral;

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
public final class RawFoodSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Raw Food";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.BEEF, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.CHICKEN, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.PORKCHOP, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COD, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.SALMON, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.RABBIT, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.MUTTON, 4));
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.NEUTRAL;
	}

}
