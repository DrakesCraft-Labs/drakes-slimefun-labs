package com.github.drakescraft-labs.slimefunluckyblocks.surprises.neutral;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class WanderingTraderSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Wandering Trader";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		l.getWorld().spawnEntity(l, EntityType.WANDERING_TRADER);
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.NEUTRAL;
	}

}
