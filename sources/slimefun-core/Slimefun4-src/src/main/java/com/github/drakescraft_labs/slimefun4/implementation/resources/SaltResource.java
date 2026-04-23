package com.github.drakescraft_labs.slimefun4.implementation.resources;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

import com.github.drakescraft_labs.slimefun4.api.MinecraftVersion;
import com.github.drakescraft_labs.slimefun4.api.geo.GEOResource;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeMap;

/**
 * A {@link GEOResource} which consists of Salt.
 * 
 * @author TheBusyBiscuit
 *
 */
class SaltResource extends AbstractResource {

    private static final int DEFAULT_OVERWORLD_VALUE = 6;
    private static final int DEFAULT_NETHER_VALUE = 8;

    private final BiomeMap<Integer> biomes;

    SaltResource() {
        super("salt", "Salt", SlimefunItems.SALT, 18, true);
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_18)) {
            // 1.18+ renamed most biomes
            biomes = getBiomeMap(this, "/biome-maps/salt_v1.18.json");
        } else {
            biomes = getBiomeMap(this, "/biome-maps/salt_v1.16.json");
        }
    }

    @Override
    public int getDefaultSupply(Environment environment, Biome biome) {
        if (environment == Environment.NORMAL) {
            return biomes.getOrDefault(biome, DEFAULT_OVERWORLD_VALUE);
        } else if (environment == Environment.NETHER) {
            return biomes.getOrDefault(biome, DEFAULT_NETHER_VALUE);
        } else {
            return 0;
        }
    }

}
