package com.github.jackstar.coronalis.managers;

import com.github.jackstar.coronalis.Coronalis;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.logging.Level;

/**
 * Gestor de eventos cósmicos aleatorios del servidor Coronalis.
 *
 * <p>Cada 5 minutos tiene un 33% de probabilidad de disparar un evento global.
 * Los eventos tienen duración de 60–180 segundos y se anuncian en el chat.</p>
 *
 * <h3>Efectos en el sistema de observación:</h3>
 * <ul>
 *   <li>{@link CosmicEvent#SOLAR_FLARE}       — bloquea toda correlación de señal</li>
 *   <li>{@link CosmicEvent#GRAVITATIONAL_WAVE} — duplica el XP de descubrimiento</li>
 *   <li>{@link CosmicEvent#PULSAR_BURST}       — acelera ×1.5 la correlación automática</li>
 *   <li>{@link CosmicEvent#MAGNETIC_STORM}     — aumenta probabilidad de falla mecánica</li>
 * </ul>
 */
public class CosmicEventManager {

    // ── Enum de eventos ──────────────────────────────────────────────────────

    public enum CosmicEvent {
        CLEAR_SKY(
            "Cielo Despejado",
            null,
            1.0, 1.0, false, false
        ),
        SOLAR_FLARE(
            "Llamarada Solar",
            "§c§l¡LLAMARADA SOLAR DETECTADA! §r§cTodas las transmisiones están interferidas. Correlación §4§lBLOQUEADA§c.",
            1.0, 1.0, true, false
        ),
        GRAVITATIONAL_WAVE(
            "Onda Gravitacional",
            "§5§l¡EVENTO RARO — ONDA GRAVITACIONAL! §r§5Perturbación espacio-temporal detectada. §a§l+100% XP§r§5 por observación.",
            2.0, 1.0, false, false
        ),
        PULSAR_BURST(
            "Erupción de Púlsar",
            "§b§l¡BURST DE PÚLSAR! §r§bPSR B1919+21 emite una erupción masiva. Correlación §e§l×1.5§r§b más rápida.",
            1.0, 1.5, false, false
        ),
        MAGNETIC_STORM(
            "Tormenta Magnética",
            "§6§l¡TORMENTA MAGNÉTICA! §r§6Campo magnético terrestre distorsionado. Fallas mecánicas §c§lMÁS PROBABLES§6.",
            1.0, 1.0, false, true
        );

        /** Nombre visible del evento. */
        public final String displayName;
        /** Mensaje de broadcast (null para CLEAR_SKY). */
        public final String announcement;
        /** Multiplicador de XP de descubrimiento. */
        public final double xpMultiplier;
        /** Multiplicador de velocidad de correlación. */
        public final double correlationSpeedMult;
        /** Si true, la correlación de señal está completamente bloqueada. */
        public final boolean blocksSignal;
        /** Si true, la probabilidad de falla mecánica está aumentada. */
        public final boolean boostsFaultChance;

        CosmicEvent(@Nonnull String displayName, String announcement,
                    double xpMultiplier, double correlationSpeedMult,
                    boolean blocksSignal, boolean boostsFaultChance) {
            this.displayName        = displayName;
            this.announcement       = announcement;
            this.xpMultiplier       = xpMultiplier;
            this.correlationSpeedMult = correlationSpeedMult;
            this.blocksSignal       = blocksSignal;
            this.boostsFaultChance  = boostsFaultChance;
        }
    }

    // ── Estado ───────────────────────────────────────────────────────────────

    private volatile CosmicEvent activeEvent = CosmicEvent.CLEAR_SKY;
    private volatile long        eventEndMs  = 0L;
    private BukkitTask           task;

    private static final CosmicEvent[] RANDOM_EVENTS = {
        CosmicEvent.SOLAR_FLARE,
        CosmicEvent.GRAVITATIONAL_WAVE,
        CosmicEvent.PULSAR_BURST,
        CosmicEvent.MAGNETIC_STORM
    };

    private final Random random = new Random();

    // ── Ciclo de vida ────────────────────────────────────────────────────────

    /** Inicia el scheduler. Llamar desde {@code Coronalis#onEnable}. */
    public void start() {
        // Comprobar cada 5 minutos (6000 ticks)
        task = Bukkit.getScheduler().runTaskTimer(Coronalis.instance(), this::tryTrigger, 6000L, 6000L);
        Coronalis.log("§a[Coronalis] Sistema de eventos cósmicos iniciado. Ciclo: 5 min.");
    }

    /** Detiene el scheduler. Llamar desde {@code Coronalis#onDisable}. */
    public void stop() {
        if (task != null && !task.isCancelled()) task.cancel();
        activeEvent = CosmicEvent.CLEAR_SKY;
        eventEndMs  = 0L;
    }

    // ── Lógica ───────────────────────────────────────────────────────────────

    private void tryTrigger() {
        if (isEventActive()) return; // no apilar eventos
        try {
            if (random.nextInt(3) == 0) {
                CosmicEvent ev = RANDOM_EVENTS[random.nextInt(RANDOM_EVENTS.length)];
                int secs = 60 + random.nextInt(120); // 60–179 segundos
                triggerEvent(ev, secs);
            }
        } catch (Exception e) {
            Coronalis.instance().getLogger().log(Level.WARNING, "[Coronalis/CosmicEvents] Error al disparar evento", e);
        }
    }

    /**
     * Activa un evento cósmico con la duración indicada y lo anuncia al servidor.
     *
     * @param event   Evento a activar (CLEAR_SKY limpia el evento actual).
     * @param seconds Duración en segundos.
     */
    public void triggerEvent(@Nonnull CosmicEvent event, int seconds) {
        if (event == CosmicEvent.CLEAR_SKY) {
            clearEvent();
            return;
        }
        this.activeEvent = event;
        this.eventEndMs  = System.currentTimeMillis() + (seconds * 1000L);

        String prefix = "§5§l[✦ Coronalis] §r";
        Bukkit.broadcastMessage(prefix + event.announcement);
        Bukkit.broadcastMessage("§8  └─ §7Duración del evento: §e" + seconds + " segundos.");

        // Expiración automática
        Bukkit.getScheduler().runTaskLater(Coronalis.instance(), () -> {
            if (this.activeEvent == event) {
                clearEvent();
                Bukkit.broadcastMessage(prefix + "§7Evento §e" + event.displayName
                    + " §7finalizado. Condiciones de observación restauradas.");
            }
        }, (long) seconds * 20L);
    }

    /** Limpia el evento activo sin anuncio. */
    public void clearEvent() {
        activeEvent = CosmicEvent.CLEAR_SKY;
        eventEndMs  = 0L;
    }

    // ── Consultas (thread-safe) ───────────────────────────────────────────────

    /** @return true si hay un evento activo y no ha expirado. */
    public boolean isEventActive() {
        return activeEvent != CosmicEvent.CLEAR_SKY && System.currentTimeMillis() < eventEndMs;
    }

    /** @return Evento actual o CLEAR_SKY si no hay ninguno. */
    @Nonnull
    public CosmicEvent getActiveEvent() {
        return isEventActive() ? activeEvent : CosmicEvent.CLEAR_SKY;
    }

    public double getXpMultiplier()             { return getActiveEvent().xpMultiplier;         }
    public double getCorrelationSpeedMult()     { return getActiveEvent().correlationSpeedMult;  }
    public boolean isSignalBlocked()            { return getActiveEvent().blocksSignal;           }
    public boolean isFaultChanceBoosted()       { return getActiveEvent().boostsFaultChance;      }

    /** @return Segundos restantes del evento, o 0 si no hay ninguno. */
    public int getRemainingSeconds() {
        if (!isEventActive()) return 0;
        return (int) Math.max(0L, (eventEndMs - System.currentTimeMillis()) / 1000L);
    }
}
