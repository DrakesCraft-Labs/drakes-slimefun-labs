package com.github.drakescraft_labs.galactifun.base.items.rockets;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.api.items.Rocket;
import com.github.drakescraft_labs.galactifun.api.universe.attributes.atmosphere.Gas;
import com.github.drakescraft_labs.galactifun.base.items.AssemblyTable;
import com.github.drakescraft_labs.galactifun.core.CoreItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

public final class IonRocket extends Rocket {

    public IonRocket(SlimefunItemStack item, ItemStack[] recipe, int fuelCapacity, int storageCapacity) {
        super(CoreItemGroup.ITEMS, item, AssemblyTable.TYPE, recipe, fuelCapacity, storageCapacity);
    }

    @Override
    protected Map<ItemStack, Double> getAllowedFuels() {
        return Map.of(Gas.ARGON.item(), 18.0);
    }

    @Nonnull
    @Override
    protected Particle getLaunchParticles() {
        return Particle.SOUL_FIRE_FLAME;
    }

}
