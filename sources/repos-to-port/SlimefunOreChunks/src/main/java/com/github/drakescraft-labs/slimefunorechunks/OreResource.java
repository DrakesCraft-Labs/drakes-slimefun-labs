package com.github.drakescraft-labs.slimefunorechunks;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.geo.GEOResource;

public class OreResource implements GEOResource {
	
	private final NamespacedKey key;
	private final OreChunk oreChunk;
	
	public OreResource(NamespacedKey key, OreChunk item) {
		this.key = key;
		this.oreChunk = item;
	}

	@Override
	public String getName() {
		return oreChunk.getName();
	}
	
	@Override
	public int getDefaultSupply(Environment environment, Biome biome) {
		if (environment == Environment.NORMAL) {
			int amplifier = oreChunk.getAmplifier();
			return ThreadLocalRandom.current().nextInt(amplifier * 2, 18 + amplifier * 4);
		}
		
		return 0;
	}
	
	@Override
	public int getMaxDeviation() {
		return 6;
	}
	
	@Override
	public NamespacedKey getKey() {
		return key;
	}
	
	@Override
	public ItemStack getItem() {
		return oreChunk.getItem();
	}
	
	@Override
	public boolean isObtainableFromGEOMiner() {
		return true;
	}

}
