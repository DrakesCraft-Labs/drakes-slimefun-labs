package dev.j3fftw.worldeditslimefun.listeners;

import dev.j3fftw.worldeditslimefun.utils.PositionManager;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        SlimefunItem slimefunItem = SlimefunItem.getByItem(event.getItem());
        if (slimefunItem == null || !slimefunItem.getId().equals("WESF_WAND")) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = event.getPlayer();
        if (!Slimefun.getProtectionManager().hasPermission(player, block, Interaction.PLACE_BLOCK)
                || !Slimefun.getProtectionManager().hasPermission(player, block, Interaction.BREAK_BLOCK)
                || !Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK)
                || !player.hasPermission("wesf.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission!");
            return;
        }

        event.setCancelled(true);

        Action action = event.getAction();
        BlockPosition position = new BlockPosition(block);
        if (action == Action.LEFT_CLICK_BLOCK) {
            PositionManager.addPositionOne(player, position);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            PositionManager.addPositionTwo(player, position);
        }
    }
}
