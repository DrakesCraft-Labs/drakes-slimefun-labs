package io.github.thebusybiscuit.slimefunluckyblocks.surprises.neutral;

import java.util.Random;

import org.bukkit.ChatColor;
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
public final class CookieSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Cookies";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.COOKIE));
		p.sendTitle("", ChatColor.translateAlternateColorCodes('&',"&bCOOOOOKKIIIIIEESSS!!!!"), 10, 20, 10);
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.NEUTRAL;
	}

}
