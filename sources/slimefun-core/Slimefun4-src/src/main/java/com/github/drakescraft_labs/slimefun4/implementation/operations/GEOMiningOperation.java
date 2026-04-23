package com.github.drakescraft_labs.slimefun4.implementation.operations;

import java.util.OptionalInt;

import javax.annotation.Nonnull;

import dev.drake.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.api.geo.GEOResource;
import com.github.drakescraft_labs.slimefun4.api.geo.ResourceManager;
import com.github.drakescraft_labs.slimefun4.core.machines.MachineOperation;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.geo.GEOMiner;

/**
 * This {@link MachineOperation} represents a {@link GEOMiner}
 * mining a {@link GEOResource}.
 *
 * @author iTwins
 *
 * @see GEOMiner
 */
public class GEOMiningOperation extends MiningOperation {

    private final GEOResource resource;

    public GEOMiningOperation(@Nonnull GEOResource resource, int totalTicks) {
        super(resource.getItem().clone(), totalTicks);
        this.resource = resource;
    }

    /**
     * This returns the {@link GEOResource} back to the chunk
     * when the {@link GEOMiningOperation} gets cancelled
     */
    @Override
    public void onCancel(@Nonnull BlockPosition position) {
        ResourceManager resourceManager = Slimefun.getGPSNetwork().getResourceManager();
        OptionalInt supplies = resourceManager.getSupplies(resource, position.getWorld(), position.getChunkX(), position.getChunkZ());
        supplies.ifPresent(s -> resourceManager.setSupplies(resource, position.getWorld(), position.getChunkX(), position.getChunkZ(), s + 1));
    }

}
