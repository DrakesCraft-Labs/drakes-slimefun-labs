package com.github.drakescraft_labs.galactifun.base.universe.jupiter;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.api.universe.PlanetaryObject;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.DayCycle;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.Gravity;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.Orbit;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.atmosphere.Atmosphere;
import com.github.drakescraft_labs.galactifun.api.universe.types.PlanetaryType;
import com.github.drakescraft_labs.galactifun.api.worlds.FlatWorld;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;

public final class Europa extends FlatWorld {

    public Europa(String name, PlanetaryType type, Orbit orbit, PlanetaryObject orbiting, ItemStack baseItem,
                  DayCycle dayCycle, Atmosphere atmosphere, Gravity gravity) {
        super(name, type, orbit, orbiting, baseItem, dayCycle, atmosphere, gravity);
    }

    @Override
    protected boolean enabledByDefault() {
        return false;
    }

    @Nonnull
    @Override
    protected Int2ObjectSortedMap<Material> getLayers() {
        // double brace init go brr
        return new Int2ObjectLinkedOpenHashMap<>() {{
            put(30, Material.PACKED_ICE);
            put(60, Material.ICE);
        }};
    }

    @Nonnull
    @Override
    protected Biome getBiome() {
        return Biome.FROZEN_OCEAN;
    }

    @Override
    public void getPopulators(@Nonnull List<BlockPopulator> populators) {

    }

}
