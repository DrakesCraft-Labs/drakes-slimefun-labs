package com.github.drakescraft_labs.galactifun.base.universe;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.api.universe.StarSystem;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.DayCycle;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.Gravity;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.Orbit;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.atmosphere.Atmosphere;
import com.github.drakescraft_labs.galactifun.api.universe.types.PlanetaryType;
import com.github.drakescraft_labs.galactifun.api.worlds.SimpleAlienWorld;
import com.github.drakescraft_labs.galactifun.api.worlds.populators.LakePopulator;
import com.github.drakescraft_labs.galactifun.api.worlds.populators.VolcanoPopulator;

/**
 * Class for Venus
 *
 * @author Seggan
 */
public final class Venus extends SimpleAlienWorld {

    public Venus(String name, PlanetaryType type, Orbit orbit, StarSystem orbiting, ItemStack baseItem,
                 DayCycle dayCycle, Atmosphere atmosphere, Gravity gravity) {
        super(name, type, orbit, orbiting, baseItem, dayCycle, atmosphere, gravity);
    }

    @Override
    public void getPopulators(@Nonnull List<BlockPopulator> populators) {
        populators.add(new VolcanoPopulator(117, Material.MAGMA_BLOCK, Material.LAVA));
        populators.add(new LakePopulator(75, Material.LAVA));
    }

    @Nonnull
    @Override
    protected Material generateMaterial(@Nonnull Random random, int x, int y, int z, int top) {
        if (y > 75) {
            return Material.BLACKSTONE;
        } else if (y > 9) {
            return Material.BASALT;
        } else if (y > 8) {
            return Material.YELLOW_TERRACOTTA;
        } else {
            return Material.BASALT;
        }
    }

    @Nonnull
    @Override
    protected Biome getBiome() {
        return Biome.BASALT_DELTAS;
    }

    @Override
    protected int getAverageHeight() {
        return 100;
    }

    @Override
    protected int getMaxDeviation() {
        return 50;
    }

    @Override
    protected double getScale() {
        return .02;
    }

    @Override
    protected double getFrequency() {
        return .3;
    }

}
