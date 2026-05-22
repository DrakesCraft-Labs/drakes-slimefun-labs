package com.github.jackstar.coronalis.implementation.data;

import org.bukkit.Location;

import javax.annotation.Nonnull;

/**
 * Estado de calibración individual de un Radiotelescopio en la red Coronalis.
 *
 * <p>Un telescopio completamente calibrado tiene los 5 parámetros al 100 %,
 * lo que maximiza su contribución a la eficiencia del array.</p>
 *
 * <h3>Parámetros de calibración:</h3>
 * <ol>
 *   <li>Azimut (AZIMUTH) — alineación horizontal</li>
 *   <li>Elevación (ELEVATION) — alineación vertical</li>
 *   <li>Frecuencia (FREQUENCY) — sintonización de la banda de radio</li>
 *   <li>Fase (PHASE) — coherencia de fase con la red</li>
 *   <li>Ganancia (GAIN) — amplificación de la señal recibida</li>
 * </ol>
 *
 * <p>Cada parámetro es un valor de 0 (sin calibrar) a 100 (calibración perfecta).</p>
 */
public final class TelescopeState {

    /** Ubicación del bloque de telescopio. */
    private final Location loc;

    // ── Parámetros de calibración (0–100) ────────────────────────────────────

    private int azimuthCalib    = 0;
    private int elevationCalib  = 0;
    private int frequencyCalib  = 0;
    private int phaseCalib      = 0;
    private int gainCalib       = 0;

    /** Si true, el telescopio sufrió BEARING_FAILURE y necesita recalibración total. */
    private boolean needsFullRecal = false;

    /** Número de calibraciones totales realizadas (estadística). */
    private int calibCount = 0;

    public TelescopeState(@Nonnull Location loc) {
        this.loc = loc.clone();
    }

    // ── Parámetros ───────────────────────────────────────────────────────────

    public enum CalibParam {
        AZIMUTH    ("§dAzimut",     "§7Alineación horizontal del plato parabólico."),
        ELEVATION  ("§bElevación",  "§7Alineación vertical de la antena."),
        FREQUENCY  ("§eFrequencia", "§7Sintonización de banda de radio (1mm–21cm)."),
        PHASE      ("§5Fase",       "§7Coherencia de fase con la red interferométrica."),
        GAIN       ("§aGanancia",   "§7Factor de amplificación de la señal recibida.");

        public final String label;
        public final String description;

        CalibParam(@Nonnull String label, @Nonnull String description) {
            this.label       = label;
            this.description = description;
        }
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int get(CalibParam param) {
        return switch (param) {
            case AZIMUTH   -> azimuthCalib;
            case ELEVATION -> elevationCalib;
            case FREQUENCY -> frequencyCalib;
            case PHASE     -> phaseCalib;
            case GAIN      -> gainCalib;
        };
    }

    /**
     * Aumenta la calibración de un parámetro en {@code amount} puntos.
     * El valor se limita a [0, 100].
     */
    public void advance(CalibParam param, int amount) {
        switch (param) {
            case AZIMUTH   -> azimuthCalib    = clamp(azimuthCalib    + amount);
            case ELEVATION -> elevationCalib  = clamp(elevationCalib  + amount);
            case FREQUENCY -> frequencyCalib  = clamp(frequencyCalib  + amount);
            case PHASE     -> phaseCalib      = clamp(phaseCalib      + amount);
            case GAIN      -> gainCalib       = clamp(gainCalib       + amount);
        }
    }

    /** Reinicia la calibración de todos los parámetros a 0. */
    public void resetAll() {
        azimuthCalib = elevationCalib = frequencyCalib = phaseCalib = gainCalib = 0;
        needsFullRecal = false;
    }

    /** Marca el telescopio como "dañado" — necesita recalibración total. */
    public void markBearingFailure() {
        // Degradar a la mitad de la calibración anterior
        azimuthCalib   = azimuthCalib   / 2;
        elevationCalib = elevationCalib / 2;
        frequencyCalib = frequencyCalib / 2;
        phaseCalib     = phaseCalib     / 2;
        gainCalib      = gainCalib      / 2;
        needsFullRecal = true;
    }

    // ── Estado global ────────────────────────────────────────────────────────

    /**
     * @return true si todos los parámetros están al 100 %.
     */
    public boolean isFullyCalibrated() {
        return azimuthCalib == 100 && elevationCalib == 100
            && frequencyCalib == 100 && phaseCalib == 100
            && gainCalib == 100;
    }

    /**
     * Media aritmética de los 5 parámetros (0.0–1.0).
     * Usada para calcular la contribución del telescopio al array.
     */
    public double getCalibrationFactor() {
        return (azimuthCalib + elevationCalib + frequencyCalib + phaseCalib + gainCalib) / 500.0;
    }

    public boolean needsFullRecal() { return needsFullRecal; }
    public void clearRecalFlag()    { needsFullRecal = false; }

    public int getCalibCount()      { return calibCount; }
    public void incrementCalibCount() { calibCount++; }

    @Nonnull public Location getLoc() { return loc.clone(); }

    // ── Bar util ─────────────────────────────────────────────────────────────

    /**
     * Genera una barra de progreso de 10 chars para un valor 0–100.
     */
    @Nonnull
    public static String bar(int value) {
        int filled = value / 10;
        return (value == 100 ? "§a" : value >= 50 ? "§e" : "§c")
            + "█".repeat(filled) + "§8" + "█".repeat(10 - filled)
            + " §7(" + value + "%)";
    }

    private static int clamp(int v) { return Math.max(0, Math.min(100, v)); }

    @Override
    public String toString() {
        return "TelescopeState{az=" + azimuthCalib + ",el=" + elevationCalib
            + ",fr=" + frequencyCalib + ",ph=" + phaseCalib + ",ga=" + gainCalib + "}";
    }
}
