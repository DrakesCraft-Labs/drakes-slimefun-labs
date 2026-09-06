/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.ParametersAreNonnullByDefault
 */
package me.poma123.globalwarming.api;

import javax.annotation.ParametersAreNonnullByDefault;

public enum WorldFilterType {
    WHITELIST("whitelist"),
    BLACKLIST("blacklist");

    private final String name;

    @ParametersAreNonnullByDefault
    private WorldFilterType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

