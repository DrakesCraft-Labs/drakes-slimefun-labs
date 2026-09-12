package dev.j3fftw.litexpansion.machine.api;

import dev.j3fftw.litexpansion.LiteXpansion;

public interface PoweredMachine {

    int getDefaultEnergyConsumption();

    default int getFinalEnergyConsumption() {
        return LiteXpansion.getInstance().getConfig().getBoolean("options.nerf-other-addons", false)
            ? getDefaultEnergyConsumption() * 2
            : getDefaultEnergyConsumption();
    }
}
