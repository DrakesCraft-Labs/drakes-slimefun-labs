package org.metamechanists.kinematiccore.api.entity;


import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.kinematiccore.internal.entity.EntityStorage;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;


@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class KinematicEntity<T extends Entity, S extends KinematicEntitySchema> {
    private final String id;
    private final UUID uuid;
    private final transient T entity;

    protected KinematicEntity(@NotNull S schema, @NotNull Supplier<T> spawnEntity) {
        this.id = schema.id();
        this.entity = spawnEntity.get();
        assert entity.getType() == schema.entityType(); // sanity check, should be checked at compile time
        this.uuid = entity.getUniqueId();

        //noinspection ThisEscapedInObjectConstruction
        EntityStorage.instance().create(this);
    }

    protected KinematicEntity(@NotNull StateReader reader, @NotNull T entity) {
        this.id = reader.id();
        this.uuid = reader.uuid();
        this.entity = entity;
    }

    public void write(@NotNull StateWriter writer) {}

    public final @NotNull T entity() {
        return entity;
    }

    public final @NotNull UUID uuid() {
        return uuid;
    }

    public final void tick(long tick) {
        tick(entity, tick);
    }

    protected void tick(@NotNull T entity, long tick) {}

    public void onRightClick(@NotNull Player player) {}

    public void onRemove() {}

    /*
     * Not marked as nullable because in almost all cases we can assume this to be not null
     */
    public S schema() {
        KinematicEntitySchema schema = KinematicEntitySchema.get(id);
        assert schema != null;
        //noinspection unchecked
        return (S) schema;
    }

    public static @Nullable KinematicEntity<?, ?> get(UUID uuid) {
        return EntityStorage.instance().get(uuid);
    }

    public static @NotNull Map<String, Set<UUID>> loaded() {
        return EntityStorage.instance().loaded();
    }

    public static @Nullable Set<UUID> loadedById(@NotNull String id) {
        return EntityStorage.instance().loadedById(id);
    }

    public static @Nullable Set<UUID> loadedById(@NotNull KinematicEntitySchema schema) {
        return loadedById(schema.id());
    }
}
