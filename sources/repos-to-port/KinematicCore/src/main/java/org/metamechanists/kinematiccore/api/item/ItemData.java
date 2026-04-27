package org.metamechanists.kinematiccore.api.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.kinematiccore.KinematicCore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


@SuppressWarnings({"ClassWithOnlyPrivateConstructors", "NonFinalUtilityClass", "unused", "WeakerAccess"})
public class ItemData {
    private static final Map<String, NamespacedKey> keys = new ConcurrentHashMap<>();

    private ItemData() {}

    private static @NotNull NamespacedKey key(@NotNull String key) {
        return keys.computeIfAbsent(key, k -> new NamespacedKey(KinematicCore.instance(), key));
    }

    private static void editMeta(@NotNull ItemStack stack, @NotNull String key, @NotNull Consumer<PersistentDataContainer> editor) {
        stack.editMeta(meta -> editor.accept(meta.getPersistentDataContainer()));
    }

    public static void unset(@NotNull ItemStack stack, @NotNull String key) {
        editMeta(stack, key, pdc -> pdc.remove(key(key)));
    }

    public static void set(@NotNull ItemStack stack, @NotNull String key, Byte value) {
        editMeta(stack, key, pdc -> pdc.set(key(key), PersistentDataType.BYTE, value));
    }

    public static boolean has(@NotNull ItemStack stack, @NotNull String key) {
        return stack.getItemMeta().getPersistentDataContainer().has(key(key));
    }

    public static @Nullable Byte getByte(@NotNull ItemStack stack, @NotNull String key) {
        return stack.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.BYTE);
    }
}