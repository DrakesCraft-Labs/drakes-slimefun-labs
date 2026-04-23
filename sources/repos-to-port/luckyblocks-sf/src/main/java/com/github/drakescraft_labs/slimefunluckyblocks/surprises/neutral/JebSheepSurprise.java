package com.github.drakescraft_labs.slimefunluckyblocks.surprises.neutral;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import com.github.drakescraft_labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.Surprise;

public final class JebSheepSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "jeb Sheep";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		Sheep sheep = (Sheep) l.getWorld().spawnEntity(l, EntityType.SHEEP);
		sheep.setCustomName("jeb_");
		sheep.setCustomNameVisible(true);
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.NEUTRAL;
	}

}
