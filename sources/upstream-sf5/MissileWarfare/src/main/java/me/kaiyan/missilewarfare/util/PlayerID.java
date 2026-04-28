package me.kaiyan.missilewarfare.util;

import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Manages persistent player ID groups used for missile targeting permissions.
 *
 * @author MissileWarfare contributors
 */
public class PlayerID {
    public static HashMap<String, List<OfflinePlayer>> players = new HashMap<>();
    public static List<Player> targets = new ArrayList<>();

    /**
     * Loads player ID groups from the given configuration file.
     *
     * @param file the config file containing saved player IDs
     */
    public static void loadPlayers(@Nonnull Config file) {
        Set<String> keys = file.getKeys("id.");
        for (String key : keys) {
            List<OfflinePlayer> _players = new ArrayList<>();
            List<String> strs = file.getStringList("id." + key + ".players");
            for (String uuidstr : strs) {
                uuidstr = uuidstr.trim();
                _players.add(Bukkit.getOfflinePlayer(UUID.fromString(uuidstr)));
            }
            players.put(key, _players);
        }
    }

    /**
     * Saves player ID groups to the given configuration file.
     *
     * @param file the config file to save player IDs into
     */
    public static void savePlayers(@Nonnull Config file) {
        for (Map.Entry<String, List<OfflinePlayer>> entry : players.entrySet()) {
            List<String> uuids = new ArrayList<>();
            for (OfflinePlayer player : entry.getValue()) {
                uuids.add(player.getUniqueId().toString());
            }
            file.setValue("id." + entry.getKey() + ".players", uuids);
        }
        file.save();
    }
}
