package me.poma123.globalwarming.eventos;

import java.lang.reflect.Method;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public final class ProteccionHelper {

    private ProteccionHelper() {}

    public static class Zona {
        public final int minX;
        public final int maxX;
        public final int minZ;
        public final int maxZ;
        public final long area;
        public final String tipo; // "isla", "claim", "radio"

        public Zona(int minX, int maxX, int minZ, int maxZ, String tipo) {
            this.minX = minX;
            this.maxX = maxX;
            this.minZ = minZ;
            this.maxZ = maxZ;
            this.area = (long)(maxX - minX + 1) * (long)(maxZ - minZ + 1);
            this.tipo = tipo;
        }

        public boolean contiene(int x, int z) {
            return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        }
    }

    @Nullable
    public static Zona obtenerZona(@Nonnull Location loc, int radioDefecto) {
        // 1. Intentar BentoBox (SkyBlock / OneBlock)
        Zona zonaBentoBox = obtenerZonaBentoBox(loc);
        if (zonaBentoBox != null) {
            return zonaBentoBox;
        }

        // 2. Intentar WorldGuard / ProtectionStones (Survival / Clásico)
        Zona zonaWorldGuard = obtenerZonaWorldGuard(loc);
        if (zonaWorldGuard != null) {
            return zonaWorldGuard;
        }

        // 3. Fallback: zona por radio estándar
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        return new Zona(x - radioDefecto, x + radioDefecto, z - radioDefecto, z + radioDefecto, "radio");
    }

    @Nullable
    private static Zona obtenerZonaBentoBox(@Nonnull Location loc) {
        if (!Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
            return null;
        }
        try {
            Class<?> bentoBoxClass = Class.forName("world.bentobox.bentobox.BentoBox");
            Object inst = bentoBoxClass.getMethod("getInstance").invoke(null);
            Object islandsMgr = inst.getClass().getMethod("getIslands").invoke(inst);
            Method getIslandAt = islandsMgr.getClass().getMethod("getIslandAt", Location.class);
            Optional<?> opt = (Optional<?>) getIslandAt.invoke(islandsMgr, loc);
            if (opt != null && opt.isPresent()) {
                Object island = opt.get();
                int range = (Integer) island.getClass().getMethod("getProtectionRange").invoke(island);
                Location center = (Location) island.getClass().getMethod("getCenter").invoke(island);
                int cx = center.getBlockX();
                int cz = center.getBlockZ();
                return new Zona(cx - range, cx + range, cz - range, cz + range, "isla");
            }
        } catch (Throwable ignored) {}
        return null;
    }

    @Nullable
    private static Zona obtenerZonaWorldGuard(@Nonnull Location loc) {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            return null;
        }
        try {
            World world = loc.getWorld();
            if (world == null) return null;
            Class<?> wgClass = Class.forName("com.sk89q.worldguard.WorldGuard");
            Object wgInst = wgClass.getMethod("getInstance").invoke(null);
            Object platform = wgInst.getClass().getMethod("getPlatform").invoke(wgInst);
            Object container = platform.getClass().getMethod("getRegionContainer").invoke(platform);

            Class<?> adapterClass = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
            Object adaptedWorld = adapterClass.getMethod("adapt", World.class).invoke(null, world);
            Object regionMgr = container.getClass().getMethod("get", Class.forName("com.sk89q.worldedit.world.World")).invoke(container, adaptedWorld);
            if (regionMgr == null) return null;

            Class<?> bv3Class = Class.forName("com.sk89q.worldedit.math.BlockVector3");
            Object point = bv3Class.getMethod("at", double.class, double.class, double.class).invoke(null, loc.getX(), loc.getY(), loc.getZ());
            Object appRegions = regionMgr.getClass().getMethod("getApplicableRegions", bv3Class).invoke(regionMgr, point);

            Iterable<?> regions = (Iterable<?>) appRegions.getClass().getMethod("getRegions").invoke(appRegions);
            for (Object reg : regions) {
                String id = (String) reg.getClass().getMethod("getId").invoke(reg);
                if (id.equalsIgnoreCase("__global__")) continue;

                Object min = reg.getClass().getMethod("getMinimumPoint").invoke(reg);
                Object max = reg.getClass().getMethod("getMaximumPoint").invoke(reg);
                int minX = (Integer) min.getClass().getMethod("getX").invoke(min);
                int maxX = (Integer) max.getClass().getMethod("getX").invoke(max);
                int minZ = (Integer) min.getClass().getMethod("getZ").invoke(min);
                int maxZ = (Integer) max.getClass().getMethod("getZ").invoke(max);
                return new Zona(minX, maxX, minZ, maxZ, "claim");
            }
        } catch (Throwable ignored) {}
        return null;
    }
}
