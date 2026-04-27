package org.metamechanists.kinematiccore.api.entity;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.kinematiccore.api.Exceptions;
import org.metamechanists.kinematiccore.api.addon.KinematicAddon;
import org.metamechanists.kinematiccore.api.state.StateReader;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Accessors(fluent = true)
@SuppressWarnings("unused")
public class KinematicEntitySchema {
    private static final Map<String, KinematicEntitySchema> schemas = new ConcurrentHashMap<>();

    @Getter
    private final String idWithoutNamespace;
    private KinematicAddon addon;
    @Getter
    private final EntityType entityType;
    @Getter
    private final Class<? extends KinematicEntity<?, ?>> kinematicClass;
    private final Constructor<? extends KinematicEntity<?, ?>> constructor;

    public KinematicEntitySchema(@NotNull String id, @NotNull EntityType entityType, @NotNull Class<? extends KinematicEntity<?, ?>> kinematicClass) {
        this.idWithoutNamespace = id;
        this.entityType = entityType;
        this.kinematicClass = kinematicClass;

        try {
            constructor = kinematicClass.getConstructor(StateReader.class, entityType.getEntityClass());
        } catch (NoSuchMethodException e) {
            throw new Exceptions.EntityMissingConstructorException(id);
        }

        constructor.setAccessible(true);
    }

    public @NotNull String id() {
        if (addon == null) {
            throw new Exceptions.NotRegisteredException(idWithoutNamespace);
        }
        return addon.name() + ":" + idWithoutNamespace;
    }

    @ApiStatus.Internal
    public @NotNull Constructor<? extends KinematicEntity<?, ?>> constructor() {
        return constructor;
    }

    public void register(@NotNull KinematicAddon addon) {
        String newId = addon.name() + ":" + idWithoutNamespace;
        if (schemas.containsKey(newId)) {
            throw new Exceptions.IdConflictException(newId);
        }

        this.addon = addon;
        schemas.put(newId, this);
    }

    public void unregister() {
        schemas.remove(id());
    }

    public boolean isRegistered(@NotNull KinematicEntitySchema schema) {
        return isRegistered(id());
    }

    public static @Nullable KinematicEntitySchema get(@NotNull String id) {
        return schemas.get(id);
    }

    public static boolean isRegistered(@NotNull String id) {
        return get(id) != null;
    }

    public static @NotNull Set<String> registeredSchemas() {
        return schemas.keySet();
    }

    public static @NotNull Set<String> registeredSchemasByAddon(@NotNull KinematicAddon addon) {
        //noinspection ObjectEquality
        return schemas.entrySet()
                .stream()
                .filter(schema -> schema.getValue().addon == addon)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
