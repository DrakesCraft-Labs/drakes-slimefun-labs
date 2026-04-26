package com.github.drakescraft_labs.slimefuntranslation.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.Nullable;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;

import org.bukkit.inventory.ItemStack;

/**
 * Lectura de {@link ItemStack} en paquetes cliente donde 1.20.5+ usa {@code HashedStack}
 * (p. ej. WINDOW_CLICK): ProtocolLib &lt;5.4 deja {@link PacketContainer#getItemModifier()} vacío.
 */
public final class PacketItemStackReaders {

    private PacketItemStackReaders() {}

    /**
     * Primer campo mapeado como ItemStack Bukkit (SET_CREATIVE_SLOT y paquetes antiguos).
     */
    @Nullable
    public static ItemStack readSingletonItemModifierOrNull(PacketContainer packet) {
        try {
            return packet.getItemModifier().read(0);
        } catch (FieldAccessException | IndexOutOfBoundsException | IllegalStateException ignored) {
            return null;
        }
    }

    /**
     * Ítem bajo el cursor en WINDOW_CLICK: ItemStack directo o vía componente de record (HashedStack → NMS ItemStack).
     */
    @Nullable
    public static ItemStack readWindowClickCarriedItem(PacketContainer packet) {
        ItemStack direct = readSingletonItemModifierOrNull(packet);
        if (direct != null) {
            return direct;
        }
        return readRecordCarriedAsBukkit(packet.getHandle());
    }

    @Nullable
    private static ItemStack readRecordCarriedAsBukkit(@Nullable Object handle) {
        if (handle == null || !handle.getClass().isRecord()) {
            return null;
        }
        try {
            Object carried = null;
            for (var c : handle.getClass().getRecordComponents()) {
                String n = c.getName();
                if ("carriedItem".equals(n) || "cursor".equals(n) || n.toLowerCase().contains("carried")) {
                    carried = c.getAccessor().invoke(handle);
                    break;
                }
            }
            if (carried == null) {
                var comps = handle.getClass().getRecordComponents();
                if (comps.length > 0) {
                    carried = comps[comps.length - 1].getAccessor().invoke(handle);
                }
            }
            return nmsLikeToBukkit(carried);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Nullable
    private static ItemStack nmsLikeToBukkit(@Nullable Object carried) {
        if (carried == null) {
            return null;
        }
        try {
            Class<?> nmsItemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
            if (nmsItemStackClass.isInstance(carried)) {
                return asBukkitCopy(carried);
            }
            for (Method m : carried.getClass().getMethods()) {
                if (m.getParameterCount() != 0 || !Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                if (!nmsItemStackClass.isAssignableFrom(m.getReturnType())) {
                    continue;
                }
                Object nms;
                try {
                    nms = m.invoke(carried);
                } catch (ReflectiveOperationException e) {
                    continue;
                }
                if (nms == null) {
                    continue;
                }
                ItemStack out = asBukkitCopy(nms);
                if (out != null) {
                    return out;
                }
            }
            for (Method m : carried.getClass().getDeclaredMethods()) {
                if (m.getParameterCount() != 0 || !nmsItemStackClass.isAssignableFrom(m.getReturnType())) {
                    continue;
                }
                m.setAccessible(true);
                Object nms;
                try {
                    nms = m.invoke(carried);
                } catch (ReflectiveOperationException e) {
                    continue;
                }
                if (nms == null) {
                    continue;
                }
                ItemStack out = asBukkitCopy(nms);
                if (out != null) {
                    return out;
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Nullable
    private static ItemStack asBukkitCopy(Object nmsItemStack) throws ReflectiveOperationException {
        Class<?> nms = Class.forName("net.minecraft.world.item.ItemStack");
        Class<?> craft = Class.forName("org.bukkit.craftbukkit.inventory.CraftItemStack");
        Object bukkit = craft.getMethod("asBukkitCopy", nms).invoke(null, nmsItemStack);
        return (ItemStack) bukkit;
    }
}
