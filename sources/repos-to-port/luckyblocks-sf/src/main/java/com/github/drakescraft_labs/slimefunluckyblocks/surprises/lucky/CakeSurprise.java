package com.github.drakescraft_labs.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.drakescraft_labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft_labs.slimefunluckyblocks.surprises.Surprise;

public final class CakeSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Cake";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		l.getBlock().setType(Material.CAKE);
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
