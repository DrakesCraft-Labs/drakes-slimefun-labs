package com.github.drakescraft-labs.gcereborn.utils;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.NamespacedKey;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;

import lombok.experimental.UtilityClass;

@SuppressWarnings("ConstantConditions")
@UtilityClass
public final class Keys {

    public static final NamespacedKey CHICKEN_DNA = get("chicken_dna");
    public static final NamespacedKey POCKET_CHICKEN_DNA = get("gce_pocket_chicken_dna");
    public static final NamespacedKey POCKET_CHICKEN_ADAPTER = get("gce_pocket_chicken_adapter");

    public static final String METADATA = "gce_pocket_chicken_dna";

    @Nonnull
    public static NamespacedKey get(@Nonnull String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");

        return new NamespacedKey(GeneticChickengineering.getInstance(), name);
    }
}
