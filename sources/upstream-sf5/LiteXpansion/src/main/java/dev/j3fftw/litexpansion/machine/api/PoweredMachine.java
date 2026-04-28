package dev.j3fftw.litexpansion.machine.api;

import dev.j3fftw.litexpansion.LiteXpansion;

/**
 * Represents the {@link PoweredMachine} class.
 *
 * @author j3fftw
 */
public interface PoweredMachine {

    int getDefaultEnergyConsumption();

    default int getFinalEnergyConsumption() {
        return LiteXpansion.getInstance().getConfig().getBoolean("options.nerf-other-addons", true)
            ? getDefaultEnergyConsumption() * 2
            : getDefaultEnergyConsumption();
    }
}
