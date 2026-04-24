package de.jeff_media.morepersistentdatatypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public final class DataType {

    public static final PersistentDataType<Integer, Integer> INTEGER = PersistentDataType.INTEGER;
    public static final PersistentDataType<Long, Long> LONG = PersistentDataType.LONG;
    public static final PersistentDataType<String, String> STRING = PersistentDataType.STRING;
    public static final PersistentDataType<int[], int[]> INTEGER_ARRAY = PersistentDataType.INTEGER_ARRAY;
    public static final PersistentDataType<byte[], ItemStack[]> ITEM_STACK_ARRAY = new ItemStackArrayType();

    private DataType() {}

    private static final class ItemStackArrayType implements PersistentDataType<byte[], ItemStack[]> {
        @Override
        public Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public Class<ItemStack[]> getComplexType() {
            return ItemStack[].class;
        }

        @Override
        public byte[] toPrimitive(ItemStack[] complex, PersistentDataAdapterContext context) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 BukkitObjectOutputStream out = new BukkitObjectOutputStream(bos)) {
                out.writeInt(complex.length);
                for (ItemStack item : complex) {
                    out.writeObject(item);
                }
                return bos.toByteArray();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to serialize ItemStack[]", e);
            }
        }

        @Override
        public ItemStack[] fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(primitive);
                 BukkitObjectInputStream in = new BukkitObjectInputStream(bis)) {
                int length = in.readInt();
                ItemStack[] items = new ItemStack[length];
                for (int i = 0; i < length; i++) {
                    Object object = in.readObject();
                    items[i] = object instanceof ItemStack ? (ItemStack) object : null;
                }
                return items;
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException("Failed to deserialize ItemStack[]", e);
            }
        }
    }
}
