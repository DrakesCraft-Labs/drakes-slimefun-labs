/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.poma123.globalwarming.eventos;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.eventos.EventoClimatico;
import me.poma123.globalwarming.eventos.Nevada;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GestorEventos {
    private final Map<String, EventoActivo> activos = new HashMap<String, EventoActivo>();
    private final int intervaloSorteo;
    private final double probabilidad;
    private final int duracionMinima;
    private final int duracionMaxima;
    private final Map<EventoClimatico, Boolean> permitidos = new EnumMap<EventoClimatico, Boolean>(EventoClimatico.class);
    private final boolean anunciar;

    public GestorEventos(int intervaloSorteo, double probabilidad, int duracionMinima, int duracionMaxima, Map<EventoClimatico, Boolean> permitidos, boolean anunciar) {
        this.intervaloSorteo = Math.max(30, intervaloSorteo);
        this.probabilidad = Math.max(0.0, Math.min(1.0, probabilidad));
        this.duracionMinima = Math.max(30, duracionMinima);
        this.duracionMaxima = Math.max(this.duracionMinima, duracionMaxima);
        this.permitidos.putAll(permitidos);
        this.anunciar = anunciar;
    }

    public double getDesviacion(@Nonnull World mundo) {
        EventoActivo activo = this.activos.get(mundo.getName());
        return activo == null ? 0.0 : activo.evento.getDesviacionCelsius();
    }

    @Nullable
    public EventoClimatico getEventoActivo(@Nonnull World mundo) {
        EventoActivo activo = this.activos.get(mundo.getName());
        return activo == null ? null : activo.evento;
    }

    public void arrancar(int intervaloTicks) {
        new BukkitRunnable(){

            public void run() {
                GestorEventos.this.repasar();
            }
        }.runTaskTimer((Plugin)GlobalWarmingPlugin.getInstance(), 600L, 20L * (long)this.intervaloSorteo);
        new BukkitRunnable(){

            public void run() {
                GestorEventos.this.nevar();
            }
        }.runTaskTimer((Plugin)GlobalWarmingPlugin.getInstance(), 700L, 60L);
    }

    private void repasar() {
        long ahora = System.currentTimeMillis();
        for (World mundo : Bukkit.getWorlds()) {
            EventoClimatico elegido;
            if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(mundo.getName()) || mundo.getEnvironment() != World.Environment.NORMAL) continue;
            EventoActivo activo = this.activos.get(mundo.getName());
            if (activo != null) {
                if (ahora < activo.acabaEn) continue;
                this.terminar(mundo, activo.evento);
                continue;
            }
            if (mundo.getPlayers().isEmpty() || !(ThreadLocalRandom.current().nextDouble() < this.probabilidad) || (elegido = this.sortear()) == null) continue;
            this.empezar(mundo, elegido, ahora);
        }
    }

    @Nullable
    private EventoClimatico sortear() {
        double total = 0.0;
        for (EventoClimatico e : EventoClimatico.values()) {
            if (!this.permitidos.getOrDefault((Object)e, Boolean.TRUE).booleanValue()) continue;
            total += e.getPeso();
        }
        if (total <= 0.0) {
            return null;
        }
        double tirada = ThreadLocalRandom.current().nextDouble() * total;
        for (EventoClimatico e : EventoClimatico.values()) {
            if (!this.permitidos.getOrDefault((Object)e, Boolean.TRUE).booleanValue() || !((tirada -= e.getPeso()) <= 0.0)) continue;
            return e;
        }
        return null;
    }

    private void empezar(@Nonnull World mundo, @Nonnull EventoClimatico evento, long ahora) {
        int duracion = ThreadLocalRandom.current().nextInt(this.duracionMinima, this.duracionMaxima + 1);
        this.activos.put(mundo.getName(), new EventoActivo(evento, ahora + (long)duracion * 1000L));
        this.aplicarCielo(mundo, evento.getCielo(), duracion);
        if (this.anunciar) {
            this.avisar(mundo, String.valueOf(ChatColor.DARK_GRAY) + "\u2501\u2501\u2501 " + GestorEventos.color(evento.getTitulo()) + String.valueOf(ChatColor.DARK_GRAY) + " \u2501\u2501\u2501", GestorEventos.color(evento.getDescripcion()));
        }
    }

    private void terminar(@Nonnull World mundo, @Nonnull EventoClimatico evento) {
        this.activos.remove(mundo.getName());
        if (this.anunciar) {
            this.avisar(mundo, GestorEventos.color("&7El " + ChatColor.stripColor((String)GestorEventos.color(evento.getTitulo())).toLowerCase() + " ha pasado. El tiempo vuelve a la normalidad."), null);
        }
    }

    private void aplicarCielo(@Nonnull World mundo, @Nonnull EventoClimatico.Cielo cielo, int duracionSegundos) {
        int ticks = duracionSegundos * 20;
        switch (cielo) {
            case DESPEJADO: {
                mundo.setStorm(false);
                mundo.setThundering(false);
                mundo.setWeatherDuration(ticks);
                break;
            }
            case LLUVIA: {
                mundo.setStorm(true);
                mundo.setThundering(false);
                mundo.setWeatherDuration(ticks);
                break;
            }
            case TORMENTA: {
                mundo.setStorm(true);
                mundo.setThundering(true);
                mundo.setWeatherDuration(ticks);
                mundo.setThunderDuration(ticks);
                break;
            }
        }
    }

    private void nevar() {
        for (Map.Entry<String, EventoActivo> entrada : this.activos.entrySet()) {
            World mundo;
            if (!entrada.getValue().evento.nieva() || (mundo = Bukkit.getWorld((String)entrada.getKey())) == null) continue;
            List<Player> jugadores = mundo.getPlayers();
            for (Player p : jugadores) {
                Nevada.copos(p, 6);
            }
        }
    }

    private void avisar(@Nonnull World mundo, @Nonnull String linea1, @Nullable String linea2) {
        for (Player p : mundo.getPlayers()) {
            if (GlobalWarmingPlugin.getSilenciados() != null && GlobalWarmingPlugin.getSilenciados().estaSilenciado(p)) continue;
            p.sendMessage("");
            p.sendMessage(linea1);
            if (linea2 != null) {
                p.sendMessage(linea2);
            }
            p.sendMessage("");
        }
    }

    @Nonnull
    private static String color(@Nonnull String texto) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)texto);
    }

    private static final class EventoActivo {
        private final EventoClimatico evento;
        private final long acabaEn;

        private EventoActivo(EventoClimatico evento, long acabaEn) {
            this.evento = evento;
            this.acabaEn = acabaEn;
        }
    }
}

