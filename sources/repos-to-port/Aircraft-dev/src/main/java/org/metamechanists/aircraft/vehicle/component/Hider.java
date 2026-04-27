package org.metamechanists.aircraft.vehicle.component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.aircraft.Aircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public final class Hider implements Listener {
    private static class HideSpecification {
        private final @NotNull List<UUID> toHide;
        private @Nullable UUID playerToExempt;

        private HideSpecification(@NotNull List<UUID> toHide, @Nullable UUID playerToExempt) {
            this.toHide = toHide;
            this.playerToExempt = playerToExempt;
        }
    }

    private static final Map<UUID, HideSpecification> hideSpecifications = new HashMap<>();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new Hider(), Aircraft.getInstance());
    }

    private static void update(@NotNull Player player, @NotNull HideSpecification hideSpecification) {
        for (UUID uuid : hideSpecification.toHide) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null) {
                continue;
            }

            if (player.getUniqueId().equals(hideSpecification.playerToExempt)) {
                player.showEntity(Aircraft.getInstance(), entity);
            } else {
                player.hideEntity(Aircraft.getInstance(), entity);
            }
        }
    }

    public static void hideIfNotAlreadyHidden(@NotNull UUID uuid, @NotNull List<UUID> toHide, @Nullable Player playerToExempt) {
        if (hideSpecifications.containsKey(uuid)) {
            return;
        }

        UUID playerToExemptUuid = playerToExempt == null ? null : playerToExempt.getUniqueId();

        HideSpecification hideSpecification = new HideSpecification(toHide, playerToExemptUuid);
        hideSpecifications.put(uuid, hideSpecification);
        for (Player player : Bukkit.getOnlinePlayers()) {
            update(player, hideSpecification);
        }
    }

    public static void setExempt(@NotNull UUID uuid, @Nullable Player player) {
        UUID oldPlayerUuid = hideSpecifications.get(uuid).playerToExempt;
        hideSpecifications.get(uuid).playerToExempt = (player == null) ? null : player.getUniqueId();

        // Update old player
        if (oldPlayerUuid != null) {
            Player oldPlayer = Bukkit.getPlayer(oldPlayerUuid);
            if (oldPlayer != null) {
                update(oldPlayer, hideSpecifications.get(uuid));
            }
        }

        // Update new player
        if (player != null) {
            update(player, hideSpecifications.get(uuid));
        }
    }

    @EventHandler
    private static void onJoin(PlayerJoinEvent event) {
        for (HideSpecification hideSpecification : hideSpecifications.values()) {
            update(event.getPlayer(), hideSpecification);
        }
    }
}
