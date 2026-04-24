package dev.drake.sefilib.persistence;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * This class stores the usable PersistentDataTypes.
 */
public class PersistenceTypes {

    private PersistenceTypes() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The PersistentDataType for an {@link ItemStack}
     */
    public static final PersistentDataType<byte[], ItemStack> ITEM_STACK = new BukkitObjectDataType<>(ItemStack.class);
    /**
     * The PersistentDataType for an {@link ItemMeta}
     */
    public static final PersistentDataType<byte[], ItemMeta> ITEM_META = new BukkitObjectDataType<>(ItemMeta.class);
    /**
     * The PersistentDataType for an {@link OfflinePlayer}
     */
    public static final PersistentDataType<byte[], OfflinePlayer> OFFLINE_PLAYER = new BukkitObjectDataType<>(
        OfflinePlayer.class
    );
    /**
     * The PersistentDataType for a {@link org.bukkit.Location}
     */
    public static final PersistentDataType<byte[], org.bukkit.Location> LOCATION = new BukkitObjectDataType<>(org.bukkit.Location.class);

    /**
     * The PersistentDataType for an {@link ItemStack} array
     */
    public static final PersistentDataType<byte[], ItemStack[]> ITEM_STACK_ARRAY = new BukkitObjectDataType<>(ItemStack[].class);

    /**
     * The PersistentDataType for a double array
     */
    public static final PersistentDataType<byte[], double[]> DOUBLE_ARRAY = new DoubleArrayDataType();
}
