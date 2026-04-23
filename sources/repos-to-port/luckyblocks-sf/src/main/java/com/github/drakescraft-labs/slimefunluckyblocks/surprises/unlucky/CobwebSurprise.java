package com.github.drakescraft-labs.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class CobwebSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Giant Slime";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		Slime slime = (Slime) l.getWorld().spawnEntity(l, EntityType.SLIME);
		slime.setSize(7);
		slime.setTarget(p);
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.UNLUCKY;
	}

}
