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
public final class ValuablesSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Valuables";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
	    FireworkUtils.launchRandom(p, 3);
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.EMERALD, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.DIAMOND, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.GOLD_INGOT, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.IRON_INGOT, 4));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.LAPIS_LAZULI, 8));
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
