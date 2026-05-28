package dev.j3fftw.worldeditslimefun.utils;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final List<String> SLIMEFUN_BLOCKS = new ArrayList<>();
    public static final List<String> SLIMEFUN_ITEMS = new ArrayList<>();
    public static final Map<String, Material> MATERIALS = new HashMap<>();

    public static void init() {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (!(item instanceof UnplaceableBlock) && item.getItem().getType().isBlock()) {
                SLIMEFUN_BLOCKS.add(item.getId());
            }
            SLIMEFUN_ITEMS.add(item.getId());
        }

        for (Material material : Material.values()) {
            if (!material.isEmpty() && !material.isLegacy()) {
                MATERIALS.put(material.name(), material);
            }
        }

        SLIMEFUN_BLOCKS.sort(Comparator.naturalOrder());
        SLIMEFUN_ITEMS.sort(Comparator.naturalOrder());
    }

    @Nonnull
    public static String beautifyBlockPosition(@Nonnull BlockPosition position) {
        return "%s, %s, %s (%s)".formatted(position.getX(), position.getY(), position.getZ(), position.getWorld().getName());
    }
}
