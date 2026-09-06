/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  org.bukkit.Location
 */
package me.poma123.globalwarming.eventos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Location;

public final class RegistroClimatizadores {
    private static final long CADUCIDAD_MS = 5000L;
    private static final Map<Location, Anuncio> ACTIVOS = new ConcurrentHashMap<Location, Anuncio>();

    private RegistroClimatizadores() {
    }

    public static void anunciar(@Nonnull Location l, double radio, double objetivo) {
        int r = (int) Math.round(radio);
        ProteccionHelper.Zona zona = ProteccionHelper.obtenerZona(l, r);
        anunciar(l, zona, objetivo);
    }

    public static void anunciar(@Nonnull Location l, @Nullable ProteccionHelper.Zona zona, double objetivo) {
        ACTIVOS.put(l, new Anuncio(l, zona, objetivo));
    }

    public static void olvidar(@Nonnull Location l) {
        ACTIVOS.remove(l);
    }

    @Nullable
    public static Double objetivoEn(@Nonnull Location l) {
        long ahora = System.currentTimeMillis();
        Double mejor = null;
        double mejorDistancia = Double.MAX_VALUE;
        for (Map.Entry<Location, Anuncio> entrada : ACTIVOS.entrySet()) {
            Location maquina = entrada.getKey();
            Anuncio a = entrada.getValue();
            if (a.caducado(ahora)) {
                ACTIVOS.remove(maquina);
                continue;
            }
            if (maquina.getWorld() == null || !maquina.getWorld().equals((Object)l.getWorld())) continue;
            if (!a.cubre(l, maquina)) continue;
            double distancia = maquina.distanceSquared(l);
            if (distancia < mejorDistancia) {
                mejorDistancia = distancia;
                mejor = a.objetivo;
            }
        }
        return mejor;
    }

    public static boolean estaCubierto(@Nonnull Location l) {
        return RegistroClimatizadores.objetivoEn(l) != null;
    }

    public static int activos() {
        long ahora = System.currentTimeMillis();
        ACTIVOS.entrySet().removeIf(e -> ((Anuncio)e.getValue()).caducado(ahora));
        return ACTIVOS.size();
    }

    private static final class Anuncio {
        private final ProteccionHelper.Zona zona;
        private final double radioAlCuadrado;
        private final double objetivo;
        private final long visto;

        private Anuncio(Location maquina, @Nullable ProteccionHelper.Zona zona, double objetivo) {
            this.zona = zona;
            if (zona != null && "radio".equals(zona.tipo)) {
                double r = (zona.maxX - zona.minX) / 2.0;
                this.radioAlCuadrado = r * r;
            } else {
                this.radioAlCuadrado = 0.0;
            }
            this.objetivo = objetivo;
            this.visto = System.currentTimeMillis();
        }

        private boolean cubre(Location l, Location maquina) {
            if (zona == null) {
                return false;
            }
            if ("radio".equals(zona.tipo)) {
                return maquina.distanceSquared(l) <= radioAlCuadrado;
            }
            return zona.contiene(l.getBlockX(), l.getBlockZ());
        }

        private boolean caducado(long ahora) {
            return ahora - this.visto > 5000L;
        }
    }
}

