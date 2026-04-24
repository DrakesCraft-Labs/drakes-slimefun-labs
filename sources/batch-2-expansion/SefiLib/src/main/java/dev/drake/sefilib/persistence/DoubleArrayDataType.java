package dev.drake.sefilib.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;

public class DoubleArrayDataType implements PersistentDataType<byte[], double[]> {

    @Override
    @Nonnull
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    @Nonnull
    public Class<double[]> getComplexType() {
        return double[].class;
    }

    @Override
    @Nonnull
    public byte[] toPrimitive(@Nonnull double[] complex, @Nonnull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[complex.length * 8]);
        for (double d : complex) {
            bb.putDouble(d);
        }
        return bb.array();
    }

    @Override
    @Nonnull
    public double[] fromPrimitive(@Nonnull byte[] primitive, @Nonnull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        double[] doubleArray = new double[primitive.length / 8];
        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = bb.getDouble();
        }
        return doubleArray;
    }
}
