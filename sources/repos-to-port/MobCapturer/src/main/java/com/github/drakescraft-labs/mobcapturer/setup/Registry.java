package com.github.drakescraft-labs.mobcapturer.setup;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.github.drakescraft-labs.mobcapturer.items.MobEgg;
import dev.drake.dough.config.Config;

import lombok.Getter;

/**
 * The registry houses adapters and configs of MobCapturer.
 *
 * @author ybw0014
 */
@Getter
public final class Registry {

    private final Map<EntityType, MobEgg<?>> adapters = new EnumMap<>(EntityType.class);
    private final Config config;

    public Registry(Config config) {
        this.config = config;
    }
}
