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
public final class DyeSurprise implements Surprise {
	
	@Nonnull
	@Override
	public String getName() {
		return "Dyes";
	}

	@Override
	public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.CYAN_DYE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.LIGHT_BLUE_DYE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.LIME_DYE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.MAGENTA_DYE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.ORANGE_DYE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.PINK_DYE));
		l.getWorld().dropItemNaturally(l, new ItemStack(Material.PURPLE_DYE));
		p.sendTitle(p.getName(), ChatColor.translateAlternateColorCodes('&', " has almost &b&odyed"), 10, 20, 10);
	}

	@Nonnull
	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.NEUTRAL;
	}

}
