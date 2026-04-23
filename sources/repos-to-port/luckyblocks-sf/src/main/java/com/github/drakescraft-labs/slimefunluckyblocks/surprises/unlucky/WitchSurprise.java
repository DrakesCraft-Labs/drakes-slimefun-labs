package com.github.drakescraft-labs.slimefunluckyblocks.surprises.unlucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class WitchSurprise implements Surprise {
	
	@Override
	public String getName() {
		return "Witch and Bats";
	}

	@Override
	public void activate(Random random, Player p, Location l) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1));
		l.getWorld().spawnEntity(l, EntityType.WITCH);
		
		for (int i = 0; i < 16; i++) {
			l.getWorld().spawnEntity(l, EntityType.BAT);
		}
	}

	@Override
	public LuckLevel getLuckLevel() {
		return LuckLevel.UNLUCKY;
	}

}
