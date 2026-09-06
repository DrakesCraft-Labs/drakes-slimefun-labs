/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 */
package me.poma123.globalwarming.api;

import javax.annotation.ParametersAreNonnullByDefault;

public enum TemperatureType {
    CELSIUS("Grados Celsius", "\u00b0C"),
    FAHRENHEIT("Grados Fahrenheit", "\u00b0F"),
    KELVIN("Kelvin", "K");

    private final String name;
    private final String suffix;

    @ParametersAreNonnullByDefault
    private TemperatureType(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }
}

