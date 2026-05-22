package com.github.jackstar.coronalis.implementation.data;

import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa una red de observación Coronalis: una Consola de Control
 * conectada mediante cables coaxiales a un conjunto de Radiotelescopios.
 *
 * <p>Las redes se crean y destruyen dinámicamente en {@link com.github.jackstar.coronalis.managers.NetworkRegistry}
 * cada vez que se coloca o rompe un bloque de la red.</p>
 *
 * <p>Límite de telescopios por red: {@value #MAX_TELESCOPES}.</p>
 */
public final class CoronalisNetwork {

    /** Límite absoluto de telescopios por red. */
    public static final int MAX_TELESCOPES = 50;

    /** ID único de la red (toString de la ubicación de la consola). */
    private final String id;

    /** Ubicación de la Consola de Control que ancla esta red. */
    private final Location consoleLoc;

    /**
     * Telescopios válidos (conectados por cable a la consola).
     * key = ubicación del telescopio, value = estado de calibración.
     */
    private final Map<Location, TelescopeState> telescopes = new ConcurrentHashMap<>();

    /** Núcleos de energía SU conectados por cable a la consola. */
    private final Set<Location> energyNodes = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /** Energía actual en Signal Units (SU). */
    private volatile int signalUnits = 0;

    /** Capacidad máxima de SU en la red. */
    private int maxSignalUnits = 1000;

    /** UUID de los jugadores actualmente usando la consola. */
    private final Set<UUID> activePlayers = Collections.synchronizedSet(new HashSet<>());

    /** Energía necesaria por ciclo PID (slew). */
    public static final int SU_COST_SLEW    = 10;
    /** Energía necesaria por click de correlación. */
    public static final int SU_COST_CORRELATE = 50;
    /** Energía necesaria por paso de calibración individual. */
    public static final int SU_COST_CALIBRATE = 20;
    /** Consumo de standby por telescopio cada tick (2s). */
    public static final int SU_DRAIN_PER_SCOPE = 2;
    /** Energía generada por cada núcleo SU en cada tick de red (4s). */
    public static final int SU_GENERATION_PER_CORE = 80;

    public CoronalisNetwork(@Nonnull Location consoleLoc) {
        this.consoleLoc = consoleLoc.clone();
        this.id = consoleLoc.getWorld().getName()
            + ":" + consoleLoc.getBlockX()
            + ":" + consoleLoc.getBlockY()
            + ":" + consoleLoc.getBlockZ();
    }

    // ── Telescopios ──────────────────────────────────────────────────────────

    /**
     * Añade un telescopio si hay espacio (< MAX_TELESCOPES).
     * @return true si fue añadido, false si el límite está alcanzado.
     */
    public boolean addTelescope(@Nonnull Location loc) {
        if (telescopes.size() >= MAX_TELESCOPES) return false;
        telescopes.putIfAbsent(loc.clone(), new TelescopeState(loc));
        return true;
    }

    public void removeTelescope(@Nonnull Location loc) {
        telescopes.remove(loc);
    }

    public void clearTelescopes() {
        telescopes.clear();
    }

    @Nonnull
    public Map<Location, TelescopeState> getTelescopes() {
        return Collections.unmodifiableMap(telescopes);
    }

    public int getTelescopeCount() {
        return telescopes.size();
    }

    /**
     * Número de telescopios completamente calibrados.
     */
    public int getCalibratedCount() {
        int count = 0;
        for (TelescopeState s : telescopes.values()) {
            if (s.isFullyCalibrated()) count++;
        }
        return count;
    }

    /**
     * Factor de eficiencia del array (0.0–1.0):
     * calibrados / total. Un array 100 % calibrado es el más eficiente.
     */
    public double getArrayEfficiency() {
        if (telescopes.isEmpty()) return 0.0;
        return (double) getCalibratedCount() / telescopes.size();
    }

    public double getAverageCalibrationFactor() {
        if (telescopes.isEmpty()) return 0.0;
        double total = 0.0;
        for (TelescopeState state : telescopes.values()) {
            total += state.getCalibrationFactor();
        }
        return total / telescopes.size();
    }

    @Nullable
    public TelescopeState getTelescopeState(@Nonnull Location loc) {
        return telescopes.get(loc);
    }

    // ── Energía SU ───────────────────────────────────────────────────────────

    public int getSignalUnits() { return signalUnits; }
    public int getMaxSignalUnits() { return maxSignalUnits; }
    public void setMaxSignalUnits(int max) { this.maxSignalUnits = max; }

    public void clearEnergyNodes() {
        energyNodes.clear();
    }

    public void addEnergyNode(@Nonnull Location loc) {
        energyNodes.add(loc.clone());
    }

    public int getEnergyNodeCount() {
        return energyNodes.size();
    }

    @Nonnull
    public Set<Location> getEnergyNodes() {
        return Collections.unmodifiableSet(energyNodes);
    }

    /** Añade SU (producidos por generadores). No excede el máximo. */
    public synchronized void addSU(int amount) {
        signalUnits = Math.min(maxSignalUnits, signalUnits + amount);
    }

    /**
     * Drena SU para una operación.
     * @return true si había suficientes y se consumieron, false si no hay energía.
     */
    public synchronized boolean drainSU(int amount) {
        if (signalUnits < amount) return false;
        signalUnits -= amount;
        return true;
    }

    /** Consume el standby por telescopio. Si no hay SU suficiente, no hace nada (red en pausa). */
    public synchronized void drainStandbySU() {
        int cost = telescopes.size() * SU_DRAIN_PER_SCOPE;
        signalUnits = Math.max(0, signalUnits - cost);
    }

    public synchronized void generateSU() {
        addSU(energyNodes.size() * SU_GENERATION_PER_CORE);
    }

    public boolean hasSU(int amount) {
        return signalUnits >= amount;
    }

    public double getSUPercent() {
        return maxSignalUnits == 0 ? 0 : (double) signalUnits / maxSignalUnits * 100.0;
    }

    // ── Jugadores activos ────────────────────────────────────────────────────

    public void addPlayer(@Nonnull UUID uuid) { activePlayers.add(uuid); }
    public void removePlayer(@Nonnull UUID uuid) { activePlayers.remove(uuid); }
    public boolean isPlayerActive(@Nonnull UUID uuid) { return activePlayers.contains(uuid); }

    @Nonnull
    public Set<UUID> getActivePlayers() {
        return Collections.unmodifiableSet(activePlayers);
    }

    // ── Accesores ────────────────────────────────────────────────────────────

    @Nonnull public String getId() { return id; }
    @Nonnull public Location getConsoleLoc() { return consoleLoc.clone(); }

    @Override
    public String toString() {
        return "CoronalisNetwork{id=" + id + ", scopes=" + telescopes.size() + ", SU=" + signalUnits + "}";
    }
}
