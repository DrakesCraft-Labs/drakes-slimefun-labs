package org.metamechanists.kinematiccore.api.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.kinematiccore.api.Exceptions;
import org.metamechanists.kinematiccore.internal.state.KryoStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SuppressWarnings("unused")
public class StateReader {
    private final UUID uuid; // Not stored - auxiliary information
    private String id;
    private int version;
    private final Map<String, Object> map = new HashMap<>();

    public StateReader(UUID uuid, byte[] bytes) {
        this.uuid = uuid;
        KryoStorage.read(bytes, (kryo, input) -> {
            id = input.readString();
            version = input.readInt();

            while (input.position() < input.limit()) {
                String key = input.readString();
                Object value = kryo.readClassAndObject(input);
                map.put(key, value);
            }
        });
    }

    public UUID uuid() {
        return uuid;
    }

    public String id() {
        return id;
    }

    public int version() {
        return version;
    }

    public @Nullable <T> T get(@NotNull String key, @NotNull Class<T> clazz) {
        if (!map.containsKey(key)) {
            throw new Exceptions.ValueNotFoundException(key, map.keySet());
        }

        Object object = map.get(key);
        if (object == null) {
            return null;
        }
        if (!clazz.isInstance(object)) {
            throw new Exceptions.ValueWrongTypeException(key, clazz.getSimpleName(), object.getClass().getSimpleName());
        }
        return clazz.cast(object);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T get(@NotNull String key, @NotNull T instance) {
        if (!map.containsKey(key)) {
            throw new Exceptions.ValueNotFoundException(key, map.keySet());
        }

        Object object = map.get(key);
        if (object == null) {
            return null;
        }
        if (!instance.getClass().isInstance(object)) {
            throw new Exceptions.ValueWrongTypeException(key, instance.getClass().getSimpleName(), object.getClass().getSimpleName());
        }
        return (T) object;
    }
}
