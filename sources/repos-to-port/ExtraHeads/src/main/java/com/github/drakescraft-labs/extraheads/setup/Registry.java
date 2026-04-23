package com.github.drakescraft-labs.extraheads.setup;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.EntityType;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.config.Config;

import lombok.Getter;

@Getter
public class Registry {

    private final Config config;
    private final Map<EntityType, SlimefunItem> heads = new EnumMap<>(EntityType.class);
    private final Map<String, Map<EntityType, String>> entityNames = new HashMap<>();

    public Registry(@Nonnull Config config) {
        this.config = config;
    }
}
