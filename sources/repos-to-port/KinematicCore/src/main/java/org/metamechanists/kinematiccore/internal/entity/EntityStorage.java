package org.metamechanists.kinematiccore.internal.entity;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapdb.Serializer;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.addon.KinematicAddon;
import org.metamechanists.kinematiccore.api.entity.KinematicEntity;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.api.state.StateReader;
import org.metamechanists.kinematiccore.api.state.StateWriter;
import org.metamechanists.kinematiccore.internal.storage.PersistentStorage;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Accessors(fluent = true)
public final class EntityStorage extends PersistentStorage<UUID, KinematicEntity<?, ?>> {
    @Getter
    private static EntityStorage instance;
    private static final Map<UUID, Entity> entityCache = new ConcurrentHashMap<>();

    private EntityStorage() {
        super("entity", Serializer.UUID);
    }

    public static void init() {
        instance = new EntityStorage();
    }

    @Override
    protected @NotNull UUID key(@NotNull KinematicEntity<?, ?> kinematicEntity) {
        return kinematicEntity.uuid();
    }

    @Override
    protected @Nullable String id(@NotNull KinematicEntity<?, ?> kinematicEntity) {
        if (kinematicEntity.schema() == null) {
            return null;
        }
        return kinematicEntity.schema().id();
    }

    /*
     * Overriden because we must also store a reference to the Entity, to be used
     * when we deserialize. We can't use Bukkit.getEntities when deserializing
     * because that would be an asynchronous call.
     */
    @Override
    public void load(UUID uuid) {
        super.load(uuid);
        entityCache.put(uuid, Bukkit.getEntity(uuid));
    }

    @Override
    protected @Nullable Map.Entry<String, KinematicEntity<?, ?>> deserialize(UUID uuid, byte @NotNull [] bytes) {
        StateReader reader = new StateReader(uuid, bytes);

        KinematicEntitySchema schema = KinematicEntitySchema.get(reader.id());
        if (schema == null) {
            KinematicCore.instance().getLogger().warning("Failed to load entity with ID " + reader.id() + " (schema not found)");
            KinematicCore.instance().getLogger().warning(KinematicEntitySchema.registeredSchemas().toString());
            return null;
        }

        try {
            Entity entity = entityCache.remove(uuid);
            assert entity != null;
            assert entity.getType() == schema.entityType();
            return new AbstractMap.SimpleEntry<>(schema.id(), schema.constructor().newInstance(reader, entity));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 IllegalArgumentException e) {
            KinematicCore.instance().getLogger().severe("Error while loading entity with ID " + reader.id());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected byte @NotNull [] serialize(@NotNull KinematicEntity<?, ?> kinematicEntity) {
        StateWriter writer = new StateWriter(kinematicEntity.schema().id());
        kinematicEntity.write(writer);
        return writer.toBytes();
    }

    @Override
    protected void onDelete(@NotNull KinematicEntity<?, ?> kinematicEntity) {
        kinematicEntity.onRemove();
    }

    public void cleanup(KinematicAddon addon) {
        Set<String> schemas = KinematicEntitySchema.registeredSchemasByAddon(addon);
        cleanup(schemas);
    }
}
