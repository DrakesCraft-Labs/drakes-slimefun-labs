package org.metamechanists.kinematiccore.internal.state;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.BiConsumer;


@SuppressWarnings("WeakerAccess")
public final class KryoStorage {
    private static final Kryo writeKryo = new Kryo();
    private static final Kryo readKryo = new Kryo();

    private KryoStorage() {}

    private static class WorldSerializer extends Serializer<World> {
        @Override
        public void write(Kryo kryo, Output output, World world) {
            kryo.writeObject(output, world.getUID());
        }

        @SuppressWarnings("DataFlowIssue")
        @Override
        public World read(Kryo kryo, Input input, Class<? extends World> aClass) {
            return Bukkit.getWorld(kryo.readObject(input, UUID.class));
        }
    }

    private static class LocationSerializer extends Serializer<Location> {
        @Override
        public void write(Kryo kryo, Output output, Location location) {
            kryo.writeObject(output, location.getWorld());
            output.writeDouble(location.x());
            output.writeDouble(location.y());
            output.writeDouble(location.z());
            output.writeFloat(location.getYaw());
            output.writeFloat(location.getPitch());
        }

        @Override
        public Location read(Kryo kryo, Input input, Class<? extends Location> clazz) {
            return new Location(
                    kryo.readObject(input, World.class),
                    input.readDouble(),
                    input.readDouble(),
                    input.readDouble(),
                    input.readFloat(),
                    input.readFloat());
        }
    }

    static {
        registerDefaultSerializer(UUID.class, new DefaultSerializers.UUIDSerializer());
        registerDefaultSerializer(World.class, new WorldSerializer());
        registerDefaultSerializer(Location.class, new LocationSerializer());
        readKryo.setRegistrationRequired(false);
        writeKryo.setRegistrationRequired(false);
    }

    public static void registerDefaultSerializer(Class<?> clazz, Serializer<?> serializer) {
        readKryo.addDefaultSerializer(clazz, serializer);
        writeKryo.addDefaultSerializer(clazz, serializer);
    }

    public static synchronized byte[] write(@NotNull BiConsumer<Kryo, Output> consumer) {
        Output output = new Output(1024, -1);
        consumer.accept(writeKryo, output);
        return output.toBytes();
    }

    public static synchronized void read(byte[] bytes,@NotNull BiConsumer<Kryo, Input> consumer) {
        Input input = new Input(bytes);
        consumer.accept(writeKryo, input);
    }
}
