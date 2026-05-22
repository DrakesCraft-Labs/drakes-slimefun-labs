package com.github.jackstar.coronalis.managers;

import com.github.jackstar.coronalis.Coronalis;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor de sonidos atmosféricos de Coronalis.
 *
 * <p>Simula la experiencia de operar un observatorio de radio real:
 * estática de fondo, pitidos de señal, alertas de falla y fanfarrias de descubrimiento.</p>
 *
 * <h3>Categorías de sonido:</h3>
 * <ul>
 *   <li>AMBIENT — fondo de sala de control (cada 15s cerca de la consola)</li>
 *   <li>TRACKING — pitido de seguimiento activo</li>
 *   <li>CORRELATING — ruido de procesamiento de señal</li>
 *   <li>FAULT — alerta de falla mecánica</li>
 *   <li>DISCOVERY — fanfarria de correlación completada</li>
 *   <li>CALIBRATE — clic de calibración</li>
 *   <li>CABLE_CONNECT — click de cable colocado</li>
 *   <li>ENERGY_LOW — advertencia de SU baja</li>
 *   <li>RIVAL_DETECTED — alerta de red rival detectada</li>
 * </ul>
 */
public class SoundManager {

    // ── Enum de sonidos ──────────────────────────────────────────────────────

    public enum CoronalisSound {
        AMBIENT(Sound.BLOCK_BEACON_AMBIENT,         0.3f, 0.5f),
        TRACKING(Sound.BLOCK_NOTE_BLOCK_HAT,        0.6f, 2.0f),
        CORRELATING(Sound.BLOCK_BEACON_AMBIENT,     0.8f, 1.8f),
        FAULT(Sound.BLOCK_NOTE_BLOCK_BASS,          1.0f, 0.4f),
        DISCOVERY(Sound.UI_TOAST_CHALLENGE_COMPLETE,1.0f, 1.0f),
        CALIBRATE(Sound.BLOCK_COMPARATOR_CLICK,     0.7f, 1.5f),
        CABLE_CONNECT(Sound.BLOCK_CHAIN_PLACE,      0.9f, 1.2f),
        ENERGY_LOW(Sound.BLOCK_NOTE_BLOCK_BASS,     0.8f, 0.8f),
        RIVAL_DETECTED(Sound.ENTITY_ENDERMAN_STARE, 0.5f, 0.3f),
        ENERGY_FULL(Sound.BLOCK_BEACON_ACTIVATE,    0.6f, 1.4f),
        STATIC_1(Sound.BLOCK_CONDUIT_AMBIENT,       0.4f, 0.6f),
        STATIC_2(Sound.BLOCK_CONDUIT_AMBIENT_SHORT, 0.3f, 0.4f),
        SLEW_MOTOR(Sound.BLOCK_IRON_TRAPDOOR_OPEN,  0.7f, 0.9f),
        SLEW_DONE(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.3f),
        CORRELATION_TICK(Sound.BLOCK_DISPENSER_DISPENSE, 0.4f, 1.8f);

        public final Sound sound;
        public final float volume;
        public final float pitch;

        CoronalisSound(@Nonnull Sound sound, float volume, float pitch) {
            this.sound  = sound;
            this.volume = volume;
            this.pitch  = pitch;
        }
    }

    // ── Secuencias de sonido de radio simulada ────────────────────────────────

    /** Secuencia de sonidos que simula la detección de una señal de radio. */
    private static final CoronalisSound[] SIGNAL_SEQUENCE = {
        CoronalisSound.STATIC_1, CoronalisSound.TRACKING,
        CoronalisSound.STATIC_2, CoronalisSound.TRACKING,
        CoronalisSound.CORRELATING
    };

    /** Secuencia de alerta de falla: triple pitido bajo. */
    private static final CoronalisSound[] FAULT_SEQUENCE = {
        CoronalisSound.FAULT, CoronalisSound.FAULT, CoronalisSound.FAULT
    };

    // ── Estado interno ────────────────────────────────────────────────────────

    /** Jugadores con sonido ambiental activo y su tarea Bukkit. */
    private final Map<UUID, BukkitTask> ambientTasks = new ConcurrentHashMap<>();

    private final Random random = new Random();

    // ── API pública ───────────────────────────────────────────────────────────

    /**
     * Reproduce un sonido en la ubicación del jugador con los parámetros del enum.
     */
    public void play(@Nonnull Player player, @Nonnull CoronalisSound cs) {
        player.playSound(player.getLocation(), cs.sound, cs.volume, cs.pitch);
    }

    /**
     * Reproduce un sonido en una ubicación del mundo (visible para todos los cercanos).
     */
    public void playAt(@Nonnull Location loc, @Nonnull CoronalisSound cs) {
        if (loc.getWorld() != null) {
            loc.getWorld().playSound(loc, cs.sound, cs.volume, cs.pitch);
        }
    }

    /**
     * Reproduce la secuencia de detección de señal de radio con delays de 8 ticks (0.4s) entre sonidos.
     */
    public void playSignalSequence(@Nonnull Player player) {
        playSequenceDelayed(player, SIGNAL_SEQUENCE, 8L);
    }

    /**
     * Reproduce la secuencia de alerta de falla mecánica.
     */
    public void playFaultAlert(@Nonnull Player player) {
        playSequenceDelayed(player, FAULT_SEQUENCE, 10L);
    }

    /**
     * Reproduce la secuencia de descubrimiento con partícula sonora especial.
     */
    public void playDiscovery(@Nonnull Player player, @Nonnull Location loc) {
        play(player, CoronalisSound.DISCOVERY);
        // Después de 10 ticks (0.5s): repetición del ding
        Coronalis.instance().getServer().getScheduler().runTaskLater(
            Coronalis.instance(),
            () -> play(player, CoronalisSound.DISCOVERY),
            10L
        );
    }

    /**
     * Inicia el sonido ambiental de sala de control para un jugador.
     * Se reproduce estática suave cada 15 segundos cerca de la consola.
     */
    public void startAmbient(@Nonnull Player player) {
        if (ambientTasks.containsKey(player.getUniqueId())) return;
        BukkitTask task = Coronalis.instance().getServer().getScheduler()
            .runTaskTimer(Coronalis.instance(), () -> {
                if (!player.isOnline()) {
                    stopAmbient(player);
                    return;
                }
                // Alternar entre dos sonidos de estática aleatoriamente
                CoronalisSound cs = random.nextBoolean()
                    ? CoronalisSound.STATIC_1 : CoronalisSound.STATIC_2;
                // Variación de pitch para simular variación de frecuencia
                float pitchVariation = 0.4f + random.nextFloat() * 0.4f;
                player.playSound(player.getLocation(), cs.sound, cs.volume, pitchVariation);
            }, 0L, 300L); // cada 15 segundos (300 ticks)
        ambientTasks.put(player.getUniqueId(), task);
    }

    /**
     * Detiene el sonido ambiental de un jugador.
     */
    public void stopAmbient(@Nonnull Player player) {
        BukkitTask task = ambientTasks.remove(player.getUniqueId());
        if (task != null && !task.isCancelled()) task.cancel();
    }

    /**
     * Reproduce el sonido de motor durante una alineación PID.
     * La secuencia varía el pitch para simular aceleración/desaceleración.
     */
    public void playSlewSequence(@Nonnull Location loc, double errorDegrees) {
        // Mayor error → pitch más alto (más urgencia)
        float pitch = (float) Math.max(0.5, Math.min(2.0, 0.8 + (errorDegrees / 50.0)));
        if (loc.getWorld() != null) {
            loc.getWorld().playSound(loc, CoronalisSound.SLEW_MOTOR.sound,
                CoronalisSound.SLEW_MOTOR.volume, pitch);
        }
    }

    /**
     * Reproduce el sonido de calibración de un parámetro (clic de ajuste fino).
     */
    public void playCalibStep(@Nonnull Player player, int paramValue) {
        // pitch aumenta conforme avanza la calibración (0.5 → 2.0)
        float pitch = 0.5f + (paramValue / 100.0f) * 1.5f;
        player.playSound(player.getLocation(), CoronalisSound.CALIBRATE.sound,
            CoronalisSound.CALIBRATE.volume, pitch);
    }

    /**
     * Alerta de energía baja (< 20% de SU).
     */
    public void playEnergyLow(@Nonnull Location loc) {
        if (loc.getWorld() != null) {
            loc.getWorld().playSound(loc, CoronalisSound.ENERGY_LOW.sound, 0.9f, 0.5f);
            // Segundo pitido con delay
            Coronalis.instance().getServer().getScheduler().runTaskLater(Coronalis.instance(), () ->
                loc.getWorld().playSound(loc, CoronalisSound.ENERGY_LOW.sound, 0.7f, 0.6f), 8L);
        }
    }

    /**
     * Alerta de red rival detectada — sonido inquietante para todos los jugadores de la red.
     */
    public void playRivalDetected(@Nonnull Collection<? extends Player> players, @Nonnull Location loc) {
        for (Player p : players) {
            play(p, CoronalisSound.RIVAL_DETECTED);
        }
    }

    /**
     * Sonido de correlación en progreso — tick cada vez que avanza el progreso.
     */
    public void playCorrelationTick(@Nonnull Location loc, int progressPct) {
        if (loc.getWorld() == null) return;
        float pitch = 0.8f + (progressPct / 100.0f) * 1.2f; // pitch sube con el progreso
        loc.getWorld().playSound(loc, CoronalisSound.CORRELATION_TICK.sound, 0.5f, pitch);
    }

    /**
     * Para todos los sonidos ambientales activos (onDisable).
     */
    public void stopAll() {
        for (BukkitTask task : ambientTasks.values()) {
            if (!task.isCancelled()) task.cancel();
        }
        ambientTasks.clear();
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private void playSequenceDelayed(@Nonnull Player player,
                                     @Nonnull CoronalisSound[] sequence,
                                     long delayTicks) {
        for (int i = 0; i < sequence.length; i++) {
            final CoronalisSound cs = sequence[i];
            if (i == 0) {
                play(player, cs);
            } else {
                long delay = delayTicks * i;
                Coronalis.instance().getServer().getScheduler().runTaskLater(
                    Coronalis.instance(),
                    () -> { if (player.isOnline()) play(player, cs); },
                    delay
                );
            }
        }
    }
}
