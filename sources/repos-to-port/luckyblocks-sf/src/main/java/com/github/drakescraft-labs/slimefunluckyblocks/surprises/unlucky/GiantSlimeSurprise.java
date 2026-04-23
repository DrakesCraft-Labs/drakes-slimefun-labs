package com.github.drakescraft-labs.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class GiantSlimeSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Cobwebs";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		p.getLocation().getBlock().setType(Material.COBWEB);
		p.getEyeLocation().getBlock().setType(Material.COBWEB);
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.UNLUCKY;
	}

}
