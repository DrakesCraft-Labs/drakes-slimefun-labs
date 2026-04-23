package com.github.drakescraft-labs.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class EmeraldBlockSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Emerald Block";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		l.getWorld().spawnFallingBlock(l.add(0.5, 5, 0.5), Material.EMERALD_BLOCK.createBlockData());
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.LUCKY;
	}

}
