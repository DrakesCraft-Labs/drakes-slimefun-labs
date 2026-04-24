package dev.drake.dough.items.nms;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import dev.drake.dough.versions.MinecraftVersion;

/**
 * Adaptador unificado para nombres de items, optimizado para Paper 1.21.1.
 * Se ha purgado toda la lógica de versiones legacy y NMS.
 */
public interface ItemNameAdapter {

    @ParametersAreNonnullByDefault
    @Nonnull
    String getName(ItemStack item) throws IllegalAccessException, InvocationTargetException;

    public static @Nullable ItemNameAdapter get() {
        if (MinecraftVersion.isMocked()) {
            return new ItemNameAdapterMockBukkit();
        }

        // En DrakesLab, solo soportamos Paper 1.21.1+ nativo.
        return new ItemNameAdapterPaper();
    }
}
