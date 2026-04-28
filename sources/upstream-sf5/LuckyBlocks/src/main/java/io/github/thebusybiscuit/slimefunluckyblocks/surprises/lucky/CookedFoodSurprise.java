package io.github.thebusybiscuit.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.LuckLevel;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * A {@link Surprise} implementation.
 *
 * @author TheBusyBiscuit
 */
public final class CookedFoodSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Cooked Food";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		FireworkUtils.launchRandom(p, 3);
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_BEEF, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_CHICKEN, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_PORKCHOP, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_COD, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_MUTTON, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_RABBIT, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKED_SALMON, 4));
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
