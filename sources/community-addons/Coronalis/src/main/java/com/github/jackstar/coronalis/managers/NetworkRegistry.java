package com.github.jackstar.coronalis.managers;

import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.implementation.data.CoronalisNetwork;
import com.github.jackstar.coronalis.implementation.data.TelescopeState;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Registro global de todas las redes Coronalis activas en el servidor.
 *
 * <p>Una "red" es un grafo de bloques: una Consola de Control conectada mediante
 * cables coaxiales ({@code CORONALIS_COAXIAL_CABLE}) a uno o más Radiotelescopios
 * ({@code CORONALIS_RADIO_TELESCOPE}).</p>
 *
 * <h3>Validación de conexión por BFS:</h3>
 * Un telescopio se considera conectado a una consola si existe al menos un camino
 * continuo de cables coaxiales desde el telescopio hasta la consola.
 * El BFS explora los 6 vecinos cardinales de cada bloque cable.
 *
 * <h3>Detección de redes rivales:</h3>
 * Dos redes son "rivales" si sus consolas están dentro de 200 bloques (mismo mundo)
 * y apuntan al mismo objetivo celeste simultáneamente.
 */
public class NetworkRegistry {

    /** Redes indexadas por ID de red (= ubicación de la consola). */
    private final Map<String, CoronalisNetwork> networks = new ConcurrentHashMap<>();

    /** Límite BFS de bloques a explorar por validación de cable. */
    private static final int BFS_LIMIT = 500;

    /** Radio de detección de redes rivales (bloques). */
    public static final int RIVAL_DETECT_RADIUS = 200;

    // ── Gestión de redes ─────────────────────────────────────────────────────

    /**
     * Crea o recupera la red asociada a una consola.
     */
    @Nonnull
    public CoronalisNetwork getOrCreate(@Nonnull Location consoleLoc) {
        String id = networkId(consoleLoc);
        return networks.computeIfAbsent(id, k -> {
            CoronalisNetwork net = new CoronalisNetwork(consoleLoc);
            Coronalis.log("[NetworkRegistry] Nueva red creada: " + id);
            return net;
        });
    }

    /**
     * Elimina la red de una consola (consola destruida).
     */
    public void remove(@Nonnull Location consoleLoc) {
        CoronalisNetwork removed = networks.remove(networkId(consoleLoc));
        if (removed != null) {
            Coronalis.log("[NetworkRegistry] Red eliminada: " + removed.getId());
        }
    }

    /**
     * @return Red de la consola, o null si no existe.
     */
    @Nullable
    public CoronalisNetwork get(@Nonnull Location consoleLoc) {
        return networks.get(networkId(consoleLoc));
    }

    @Nonnull
    public Collection<CoronalisNetwork> getAllNetworks() {
        return Collections.unmodifiableCollection(networks.values());
    }

    public int getNetworkCount() { return networks.size(); }

    // ── Validación de cable por BFS ──────────────────────────────────────────

    /**
     * Realiza un BFS desde {@code start} buscando la {@code targetConsole}.
     * Solo atraviesa bloques de tipo {@code CORONALIS_COAXIAL_CABLE}.
     *
     * @param start          Ubicación del telescopio.
     * @param targetConsole  Ubicación de la consola que se quiere alcanzar.
     * @return true si hay un camino de cable continuo entre los dos bloques.
     */
    public boolean isConnectedBycable(@Nonnull Location start, @Nonnull Location targetConsole) {
        if (start.getWorld() == null || !start.getWorld().equals(targetConsole.getWorld())) return false;

        Set<Location> visited = new HashSet<>();
        Queue<Location> queue = new ArrayDeque<>();

        // Los vecinos del telescopio son el punto de inicio del BFS
        addNeighbors(start, queue, visited);
        int steps = 0;

        while (!queue.isEmpty() && steps < BFS_LIMIT) {
            Location current = queue.poll();
            steps++;

            // ¿Llegamos a la consola?
            if (current.equals(targetConsole.toBlockLocation())) return true;

            // Solo seguimos por cables
            String id = slimefunId(current.getBlock());
            if ("CORONALIS_COAXIAL_CABLE".equals(id)) {
                addNeighbors(current, queue, visited);
            }
        }
        return false;
    }

    /**
     * Dado un conjunto de ubicaciones de telescopios candidatos,
     * devuelve solo los que están conectados a la consola por cable.
     *
     * @param consoleLoc     Consola central de la red.
     * @param candidates     Telescopios candidatos (radio de búsqueda).
     * @return Lista de telescopios con conexión válida.
     */
    @Nonnull
    public List<Location> findConnectedTelescopes(@Nonnull Location consoleLoc,
                                                   @Nonnull List<Location> candidates) {
        List<Location> connected = new ArrayList<>();
        for (Location candidate : candidates) {
            if (isConnectedBycable(candidate, consoleLoc)) {
                connected.add(candidate);
            }
        }
        return connected;
    }

    /**
     * Reconstruye completamente la lista de telescopios conectados a una red.
     * Llamado cuando se coloca/rompe un cable o telescopio.
     */
    public void rebuildNetwork(@Nonnull Location consoleLoc) {
        try {
            CoronalisNetwork net = getOrCreate(consoleLoc);

            // Limpiar lista actual
            List<Location> oldScopes = new ArrayList<>(net.getTelescopes().keySet());
            for (Location loc : oldScopes) net.removeTelescope(loc);

            // Escanear telescopios en radio 50 bloques
            List<Location> candidates = findTelescopesInRadius(consoleLoc, 50);
            List<Location> connected  = findConnectedTelescopes(consoleLoc, candidates);

            int added = 0;
            for (Location loc : connected) {
                if (net.addTelescope(loc)) {
                    added++;
                } else {
                    Coronalis.log("[NetworkRegistry] Límite de " + CoronalisNetwork.MAX_TELESCOPES
                        + " telescopios alcanzado en " + net.getId());
                    break;
                }
            }

            Coronalis.log("[NetworkRegistry] Red " + net.getId()
                + " reconstruida: " + added + " telescopio(s) conectado(s).");

        } catch (Exception e) {
            Coronalis.instance().getLogger().log(Level.WARNING,
                "[NetworkRegistry] Error al reconstruir red en " + consoleLoc, e);
        }
    }

    // ── Detección de redes rivales ───────────────────────────────────────────

    /**
     * Busca redes rivales dentro del radio de detección de {@code network}.
     * Una red rival es cualquier otra red activa en el mismo mundo
     * dentro de {@value #RIVAL_DETECT_RADIUS} bloques de la consola.
     *
     * @return Lista de redes rivales detectadas.
     */
    @Nonnull
    public List<CoronalisNetwork> findRivalNetworks(@Nonnull CoronalisNetwork network) {
        List<CoronalisNetwork> rivals = new ArrayList<>();
        Location myLoc = network.getConsoleLoc();
        if (myLoc.getWorld() == null) return rivals;

        for (CoronalisNetwork other : networks.values()) {
            if (other.getId().equals(network.getId())) continue;
            Location otherLoc = other.getConsoleLoc();
            if (otherLoc.getWorld() == null) continue;
            if (!otherLoc.getWorld().equals(myLoc.getWorld())) continue;
            if (myLoc.distance(otherLoc) <= RIVAL_DETECT_RADIUS) {
                rivals.add(other);
            }
        }
        return rivals;
    }

    /**
     * Determina si hay interferencia entre dos redes:
     * ambas apuntan al mismo objetivo celeste y están a menos de 200 bloques.
     *
     * @param net1 Red propia.
     * @param net2 Red rival.
     * @return true si se produce interferencia.
     */
    public boolean isInterferingWith(@Nonnull CoronalisNetwork net1, @Nonnull CoronalisNetwork net2) {
        String target1 = getConsoleTarget(net1.getConsoleLoc());
        String target2 = getConsoleTarget(net2.getConsoleLoc());
        if (target1.isBlank() || target2.isBlank() || "Ninguno".equals(target1) || "Ninguno".equals(target2)) {
            return false;
        }
        return target1.equals(target2)
            && net1.getConsoleLoc().distance(net2.getConsoleLoc()) <= RIVAL_DETECT_RADIUS;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    @Nonnull
    private static String networkId(@Nonnull Location loc) {
        return loc.getWorld().getName()
            + ":" + loc.getBlockX()
            + ":" + loc.getBlockY()
            + ":" + loc.getBlockZ();
    }

    @Nullable
    private static String slimefunId(@Nonnull Block block) {
        var sf = BlockStorage.check(block);
        return sf != null ? sf.getId() : null;
    }

    private static void addNeighbors(@Nonnull Location loc,
                                     @Nonnull Queue<Location> queue,
                                     @Nonnull Set<Location> visited) {
        for (BlockFace face : new BlockFace[]{
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST,
            BlockFace.WEST, BlockFace.UP, BlockFace.DOWN
        }) {
            Location neighbor = loc.getBlock().getRelative(face).getLocation();
            if (visited.add(neighbor)) queue.offer(neighbor);
        }
    }

    @Nonnull
    private static List<Location> findTelescopesInRadius(@Nonnull Location center, int radius) {
        List<Location> result = new ArrayList<>();
        if (center.getWorld() == null) return result;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -10; y <= 10; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    Block b = center.getWorld().getBlockAt(
                        center.getBlockX() + x,
                        center.getBlockY() + y,
                        center.getBlockZ() + z);
                    if ("CORONALIS_RADIO_TELESCOPE".equals(slimefunId(b))) {
                        result.add(b.getLocation());
                    }
                }
            }
        }
        return result;
    }

    @Nonnull
    private static String getConsoleTarget(@Nonnull Location consoleLoc) {
        String t = BlockStorage.getLocationInfo(consoleLoc, "selected_target");
        return t != null ? t : "";
    }
}
