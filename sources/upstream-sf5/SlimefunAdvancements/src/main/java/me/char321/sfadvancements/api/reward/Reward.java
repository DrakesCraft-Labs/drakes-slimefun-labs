package me.char321.sfadvancements.api.reward;

import org.bukkit.entity.Player;

@FunctionalInterface
/**
 * @author char321
 */
public interface Reward {
    void give(Player p);
}
