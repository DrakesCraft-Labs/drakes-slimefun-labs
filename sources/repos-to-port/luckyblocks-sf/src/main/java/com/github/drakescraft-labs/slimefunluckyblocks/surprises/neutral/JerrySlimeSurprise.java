package com.github.drakescraft-labs.slimefunluckyblocks.surprises.neutral;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class JerrySlimeSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Jerry the Slime";
	}

	@Override
	public void activate(Random ranom, Player p, Location l) {
		Slime slime = (Slime) l.getWorld().spawnEntity(l, EntityType.SLIME);
		slime.setSize(1);
		slime.setCustomName(ChatColor.translateAlternateColorCodes('&', "&2Jerry"));
		slime.setCustomNameVisible(true);
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.NEUTRAL;
	}

}
