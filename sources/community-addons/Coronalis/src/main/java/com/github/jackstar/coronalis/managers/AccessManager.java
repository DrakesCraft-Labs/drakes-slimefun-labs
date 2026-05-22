package com.github.jackstar.coronalis.managers;

import com.github.jackstar.coronalis.Coronalis;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sistema de control de acceso por propietario para Consolas de Control Coronalis.
 *
 * <h3>Modelo de seguridad:</h3>
 * <ul>
 *   <li><b>Propietario (Owner)</b> — el jugador que colocó la consola. Control total.</li>
 *   <li><b>Lista blanca (Whitelist)</b> — jugadores invitados por el propietario. Acceso completo excepto gestión de acceso.</li>
 *   <li><b>Contraseña</b> — si está configurada, un visitante puede "auto-invitarse" introduciéndola.</li>
 *   <li><b>Visitante no autorizado</b> — ve el menú bloqueado con un terminal de acceso.</li>
 * </ul>
 *
 * <h3>Almacenamiento en BlockStorage:</h3>
 * <ul>
 *   <li>{@code cc_owner} — UUID del propietario</li>
 *   <li>{@code cc_whitelist} — UUIDs separados por coma</li>
 *   <li>{@code cc_password} — hash SHA-256 de la contraseña (o vacío = sin contraseña)</li>
 * </ul>
 */
public class AccessManager implements Listener {

    // ── Jugadores esperando input de contraseña ───────────────────────────────
    // key = UUID del jugador, value = ubicación de la consola pendiente
    private final Map<UUID, Location> awaitingPassword = new ConcurrentHashMap<>();

    // Jugadores en proceso de invitación: owner → consola pendiente de invitar
    private final Map<UUID, Location> awaitingInvite = new ConcurrentHashMap<>();

    // Jugadores en proceso de cambio de contraseña: owner → consola
    private final Map<UUID, Location> awaitingNewPassword = new ConcurrentHashMap<>();

    // Última consola bloqueada que intentó abrir cada jugador.
    private final Map<UUID, Location> lastDeniedConsole = new ConcurrentHashMap<>();

    // ── Inicialización ────────────────────────────────────────────────────────

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Coronalis.instance());
        Coronalis.log("[AccessManager] Sistema de control de acceso activado.");
    }

    // ── API pública ───────────────────────────────────────────────────────────

    /**
     * Registra al primer jugador que interactúa con una consola nueva como propietario.
     * Solo actúa si la consola aún no tiene propietario.
     */
    public void claimOwnership(@Nonnull Location consoleLoc, @Nonnull Player player) {
        if (BlockStorage.getLocationInfo(consoleLoc, "cc_owner") == null) {
            BlockStorage.addBlockInfo(consoleLoc, "cc_owner", player.getUniqueId().toString());
            BlockStorage.addBlockInfo(consoleLoc, "cc_whitelist", "");
            BlockStorage.addBlockInfo(consoleLoc, "cc_password", "");
            player.sendMessage("§5[Coronalis] §a✔ Eres el propietario de esta consola.");
            Coronalis.log("[AccessManager] Consola en " + fmtLoc(consoleLoc)
                + " reclamada por " + player.getName());
        }
    }

    /**
     * Comprueba si un jugador tiene acceso a la consola.
     *
     * @return {@code AccessResult} con el resultado y el motivo.
     */
    @Nonnull
    public AccessResult checkAccess(@Nonnull Location consoleLoc, @Nonnull Player player) {
        if (player.hasPermission("coronalis.admin")) return AccessResult.GRANTED_ADMIN;
        String ownerStr = BlockStorage.getLocationInfo(consoleLoc, "cc_owner");
        if (ownerStr == null) return AccessResult.GRANTED_NO_OWNER; // consola sin dueño

        UUID ownerUuid = parseUUID(ownerStr);
        if (ownerUuid != null && ownerUuid.equals(player.getUniqueId())) return AccessResult.GRANTED_OWNER;
        if (isWhitelisted(consoleLoc, player)) return AccessResult.GRANTED_WHITELIST;

        // Contraseña configurada → se puede intentar con ella
        String hash = BlockStorage.getLocationInfo(consoleLoc, "cc_password");
        if (hash != null && !hash.isBlank()) return AccessResult.NEED_PASSWORD;

        return AccessResult.DENIED;
    }

    /** @return true si el jugador es el propietario de la consola. */
    public boolean isOwner(@Nonnull Location consoleLoc, @Nonnull Player player) {
        String ownerStr = BlockStorage.getLocationInfo(consoleLoc, "cc_owner");
        if (ownerStr == null) return false;
        UUID ownerUuid = parseUUID(ownerStr);
        return ownerUuid != null && ownerUuid.equals(player.getUniqueId());
    }

    /** @return true si el jugador está en la lista blanca. */
    public boolean isWhitelisted(@Nonnull Location consoleLoc, @Nonnull Player player) {
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_whitelist");
        if (raw == null || raw.isBlank()) return false;
        for (String part : raw.split(",")) {
            UUID uuid = parseUUID(part.trim());
            if (uuid != null && uuid.equals(player.getUniqueId())) return true;
        }
        return false;
    }

    // ── Flujo de contraseña (chat interactivo) ────────────────────────────────

    /**
     * Inicia el flujo de autenticación por contraseña.
     * El próximo mensaje de chat del jugador se intercepta como contraseña.
     */
    public void promptPassword(@Nonnull Player player, @Nonnull Location consoleLoc) {
        awaitingPassword.put(player.getUniqueId(), consoleLoc.clone());
        player.sendMessage("");
        player.sendMessage("§5┌─────────────────────────────────────┐");
        player.sendMessage("§5│  §d§lCoronalis Array Command Terminal  §5│");
        player.sendMessage("§5│                                     §5│");
        player.sendMessage("§5│  §7Esta consola está §cprotegida§7.       §5│");
        player.sendMessage("§5│  §7Escribe la contraseña en el chat.  §5│");
        player.sendMessage("§5│  §8(Escribe §7'cancelar' §8para salir)     §5│");
        player.sendMessage("§5└─────────────────────────────────────┘");
        player.sendMessage("§5> §f");
    }

    public boolean promptLastDeniedPassword(@Nonnull Player player) {
        Location consoleLoc = lastDeniedConsole.get(player.getUniqueId());
        if (consoleLoc == null) {
            player.sendMessage("§5[Coronalis] §cNo tienes una consola protegida pendiente. Intenta abrirla primero.");
            return false;
        }
        promptPassword(player, consoleLoc);
        return true;
    }

    // ── Gestión de whitelist ──────────────────────────────────────────────────

    /**
     * Inicia el flujo de invitación: el owner escribe el nombre del jugador en el chat.
     */
    public void promptInvite(@Nonnull Player owner, @Nonnull Location consoleLoc) {
        if (!isOwner(consoleLoc, owner)) {
            owner.sendMessage("§5[Coronalis] §cSolo el propietario puede invitar jugadores.");
            return;
        }
        awaitingInvite.put(owner.getUniqueId(), consoleLoc.clone());
        owner.sendMessage("§5[Coronalis] §7Escribe el §dnombre §7del jugador a invitar (o §8'cancelar'§7):");
        owner.sendMessage("§5> §f");
    }

    /**
     * Añade un jugador a la lista blanca directamente.
     */
    public void addToWhitelist(@Nonnull Location consoleLoc, @Nonnull UUID target,
                               @Nonnull String targetName, @Nonnull Player owner) {
        if (!isOwner(consoleLoc, owner)) {
            owner.sendMessage("§5[Coronalis] §cSin permisos para gestionar el acceso.");
            return;
        }
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_whitelist");
        raw = (raw == null || raw.isBlank()) ? target.toString() : raw + "," + target;
        BlockStorage.addBlockInfo(consoleLoc, "cc_whitelist", raw);
        owner.sendMessage("§5[Coronalis] §a✔ §e" + targetName + " §aañadido a la lista blanca.");

        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer != null) {
            targetPlayer.sendMessage("§5[Coronalis] §d✉ §e" + owner.getName()
                + " §7te ha invitado a su consola de observatorio en §b"
                + fmtLoc(consoleLoc) + "§7.");
        }
        Coronalis.log("[AccessManager] " + targetName + " invitado a consola " + fmtLoc(consoleLoc)
            + " por " + owner.getName());
    }

    /**
     * Añade acceso tras introducir contraseña sin requerir que el propietario esté online.
     */
    public void grantPasswordAccess(@Nonnull Location consoleLoc, @Nonnull UUID target,
                                    @Nonnull String targetName) {
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_whitelist");
        List<String> entries = raw == null || raw.isBlank()
            ? new ArrayList<>()
            : new ArrayList<>(Arrays.asList(raw.split(",")));
        String uuid = target.toString();
        if (!entries.contains(uuid)) {
            entries.add(uuid);
            BlockStorage.addBlockInfo(consoleLoc, "cc_whitelist", String.join(",", entries));
        }
        Coronalis.log("[AccessManager] Acceso por contraseña concedido a " + targetName
            + " en consola " + fmtLoc(consoleLoc));
    }

    /**
     * Revoca el acceso de un jugador de la lista blanca.
     */
    public void removeFromWhitelist(@Nonnull Location consoleLoc, @Nonnull UUID target,
                                    @Nonnull Player owner) {
        if (!isOwner(consoleLoc, owner)) {
            owner.sendMessage("§5[Coronalis] §cSin permisos.");
            return;
        }
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_whitelist");
        if (raw == null || raw.isBlank()) return;

        List<String> parts = new ArrayList<>(Arrays.asList(raw.split(",")));
        parts.removeIf(p -> {
            UUID u = parseUUID(p.trim());
            return u != null && u.equals(target);
        });
        BlockStorage.addBlockInfo(consoleLoc, "cc_whitelist", String.join(",", parts));
        owner.sendMessage("§5[Coronalis] §7Acceso revocado para §e" + target + "§7.");
    }

    // ── Contraseña ────────────────────────────────────────────────────────────

    /**
     * Inicia el flujo de cambio de contraseña.
     */
    public void promptSetPassword(@Nonnull Player owner, @Nonnull Location consoleLoc) {
        if (!isOwner(consoleLoc, owner)) {
            owner.sendMessage("§5[Coronalis] §cSolo el propietario puede cambiar la contraseña.");
            return;
        }
        awaitingNewPassword.put(owner.getUniqueId(), consoleLoc.clone());
        owner.sendMessage("§5[Coronalis] §7Escribe la §dnueva contraseña §7(o §8'quitar'§7 para eliminarla, §8'cancelar'§7 para salir):");
        owner.sendMessage("§5> §f");
    }

    /**
     * Establece una contraseña para la consola (guarda el hash SHA-256).
     */
    public void setPassword(@Nonnull Location consoleLoc, @Nonnull String password) {
        if (password.isBlank()) {
            BlockStorage.addBlockInfo(consoleLoc, "cc_password", "");
        } else {
            BlockStorage.addBlockInfo(consoleLoc, "cc_password", sha256(password));
        }
    }

    /**
     * Comprueba si la contraseña introducida coincide con la almacenada.
     */
    public boolean checkPassword(@Nonnull Location consoleLoc, @Nonnull String attempt) {
        String stored = BlockStorage.getLocationInfo(consoleLoc, "cc_password");
        if (stored == null || stored.isBlank()) return true; // sin contraseña
        return stored.equals(sha256(attempt));
    }

    // ── Listeners de chat ────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(@Nonnull AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String msg = event.getMessage().trim();

        // ── Flujo de contraseña
        if (awaitingPassword.containsKey(uuid)) {
            event.setCancelled(true);
            Location consoleLoc = awaitingPassword.remove(uuid);
            if ("cancelar".equalsIgnoreCase(msg)) {
                player.sendMessage("§5[Coronalis] §7Acceso cancelado.");
                return;
            }
            if (checkPassword(consoleLoc, msg)) {
                Bukkit.getScheduler().runTask(Coronalis.instance(), () -> {
                    grantPasswordAccess(consoleLoc, uuid, player.getName());
                    player.sendMessage("§5[Coronalis] §a✔ Contraseña correcta. Acceso concedido.");
                    player.sendMessage("§5[Coronalis] §7Abre la consola de nuevo para operar.");
                });
            } else {
                player.sendMessage("§5[Coronalis] §c✘ Contraseña incorrecta. Acceso denegado.");
                Coronalis.log("[AccessManager] Intento fallido de contraseña de "
                    + player.getName() + " en " + fmtLoc(consoleLoc));
            }
            return;
        }

        // ── Flujo de invitación
        if (awaitingInvite.containsKey(uuid)) {
            event.setCancelled(true);
            Location consoleLoc = awaitingInvite.remove(uuid);
            if ("cancelar".equalsIgnoreCase(msg)) {
                player.sendMessage("§5[Coronalis] §7Invitación cancelada.");
                return;
            }
            Bukkit.getScheduler().runTask(Coronalis.instance(), () -> {
                @SuppressWarnings("deprecation")
                Player target = Bukkit.getPlayerExact(msg);
                if (target == null) {
                    player.sendMessage("§5[Coronalis] §cJugador '§e" + msg + "§c' no encontrado o no está en línea.");
                } else if (target.equals(player)) {
                    player.sendMessage("§5[Coronalis] §cNo puedes invitarte a ti mismo.");
                } else {
                    addToWhitelist(consoleLoc, target.getUniqueId(), target.getName(), player);
                }
            });
            return;
        }

        // ── Flujo de nueva contraseña
        if (awaitingNewPassword.containsKey(uuid)) {
            event.setCancelled(true);
            Location consoleLoc = awaitingNewPassword.remove(uuid);
            if ("cancelar".equalsIgnoreCase(msg)) {
                player.sendMessage("§5[Coronalis] §7Cambio cancelado.");
                return;
            }
            Bukkit.getScheduler().runTask(Coronalis.instance(), () -> {
                if ("quitar".equalsIgnoreCase(msg)) {
                    setPassword(consoleLoc, "");
                    player.sendMessage("§5[Coronalis] §aContraseña eliminada. La consola es ahora de acceso libre (whitelist).");
                } else if (msg.length() < 4) {
                    player.sendMessage("§5[Coronalis] §cLa contraseña debe tener al menos 4 caracteres.");
                } else {
                    setPassword(consoleLoc, msg);
                    player.sendMessage("§5[Coronalis] §a✔ Contraseña establecida. Los visitantes deberán introducirla para acceder.");
                }
            });
            return;
        }
    }

    @EventHandler
    public void onQuit(@Nonnull PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        awaitingPassword.remove(uuid);
        awaitingInvite.remove(uuid);
        awaitingNewPassword.remove(uuid);
    }

    // ── Consultas de display ──────────────────────────────────────────────────

    /**
     * Nombre del propietario para mostrar en la GUI.
     */
    @Nonnull
    public String getOwnerName(@Nonnull Location consoleLoc) {
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_owner");
        if (raw == null || raw.isBlank()) return "§7Sin dueño";
        UUID uuid = parseUUID(raw);
        if (uuid == null) return "§7Desconocido";
        String cached = Bukkit.getOfflinePlayer(uuid).getName();
        return cached != null ? cached : uuid.toString().substring(0, 8) + "...";
    }

    /**
     * Número de jugadores en la lista blanca.
     */
    public int getWhitelistCount(@Nonnull Location consoleLoc) {
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_whitelist");
        if (raw == null || raw.isBlank()) return 0;
        return (int) Arrays.stream(raw.split(",")).filter(s -> !s.isBlank()).count();
    }

    /**
     * @return true si la consola tiene contraseña configurada.
     */
    public boolean hasPassword(@Nonnull Location consoleLoc) {
        String pw = BlockStorage.getLocationInfo(consoleLoc, "cc_password");
        return pw != null && !pw.isBlank();
    }

    // ── Mensaje de terminal denegado ──────────────────────────────────────────

    /**
     * Muestra el mensaje de denegación de acceso estilo terminal al jugador.
     */
    public void showAccessDenied(@Nonnull Player player, @Nonnull Location consoleLoc,
                                 @Nonnull AccessResult result) {
        String ownerName = getOwnerName(consoleLoc);
        lastDeniedConsole.put(player.getUniqueId(), consoleLoc.clone());
        player.sendMessage("");
        player.sendMessage("§5┌─────────────────────────────────────┐");
        player.sendMessage("§5│  §d§lCoronalis Array Command Terminal  §5│");
        player.sendMessage("§5├─────────────────────────────────────┤");
        player.sendMessage("§5│  §cACCESO DENEGADO                     §5│");
        player.sendMessage("§5│                                     §5│");
        player.sendMessage("§5│  §7Propietario: §e" + padRight(ownerName, 22) + "§5│");

        if (result == AccessResult.NEED_PASSWORD) {
            player.sendMessage("§5│  §7Estado: §6Protegida con contraseña    §5│");
            player.sendMessage("§5│                                     §5│");
            player.sendMessage("§5│  §7Usa §d/coronalis auth §7para acceder.   §5│");
        } else {
            player.sendMessage("§5│  §7Estado: §cNo estás en la lista blanca §5│");
            player.sendMessage("§5│                                     §5│");
            player.sendMessage("§5│  §7Pide al propietario que te invite.  §5│");
        }
        player.sendMessage("§5└─────────────────────────────────────┘");
        player.sendMessage("");
        Coronalis.instance().getSoundManager().play(player, SoundManager.CoronalisSound.FAULT);
    }

    // ── Enum de resultado ────────────────────────────────────────────────────

    public enum AccessResult {
        GRANTED_OWNER,    // Es el propietario
        GRANTED_WHITELIST,// En lista blanca
        GRANTED_NO_OWNER, // Consola sin dueño (recién colocada)
        GRANTED_ADMIN,    // Tiene permiso coronalis.admin
        NEED_PASSWORD,    // Tiene contraseña — puede intentar
        DENIED            // Sin contraseña y no está en whitelist
    }

    public boolean isGranted(@Nonnull AccessResult result) {
        return result == AccessResult.GRANTED_OWNER
            || result == AccessResult.GRANTED_WHITELIST
            || result == AccessResult.GRANTED_NO_OWNER
            || result == AccessResult.GRANTED_ADMIN;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    @Nullable
    private static UUID parseUUID(@Nullable String s) {
        if (s == null || s.isBlank()) return null;
        try { return UUID.fromString(s.trim()); } catch (IllegalArgumentException e) { return null; }
    }

    @Nonnull
    private static String sha256(@Nonnull String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return input; // fallback sin hash (no debería ocurrir)
        }
    }

    @Nonnull
    private static String fmtLoc(@Nonnull Location loc) {
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    @Nullable
    private static Player getOwnerPlayer(@Nonnull Location consoleLoc) {
        String raw = BlockStorage.getLocationInfo(consoleLoc, "cc_owner");
        if (raw == null) return null;
        UUID uuid = parseUUID(raw);
        return uuid != null ? Bukkit.getPlayer(uuid) : null;
    }

    @Nonnull
    private static String padRight(@Nonnull String s, int length) {
        if (s.length() >= length) return s.substring(0, length);
        return s + " ".repeat(length - s.length());
    }
}
