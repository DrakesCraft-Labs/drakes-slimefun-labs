package me.voper.slimeframe.implementation.listeners;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.data.persistent.PersistentDataAPI;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.managers.RelicInventoryManager;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.implementation.items.relics.Relic;
import me.voper.slimeframe.implementation.items.relics.RelicInventory;
import me.voper.slimeframe.utils.ChatUtils;
import me.voper.slimeframe.utils.Keys;

import net.md_5.bungee.api.ChatColor;

public final class RelicsListener implements Listener {

    private static final RelicInventoryManager relicInvManager = SlimeFrame.getRelicInventoryManager();
    private static final SettingsManager settings = SlimeFrame.getSettingsManager();

    private final SlimeFrame plugin;

    public RelicsListener(SlimeFrame plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void updateFisheries(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        final Player p = e.getPlayer();

        int fishesCaught = PersistentDataAPI.getInt(p, Keys.FISHERIES_COUNTER, 0) + 1;

        if (fishesCaught % settings.getInt(SettingsManager.ConfigField.LITH_RELIC) == 0) {
            fishesCaught = 0;
            giveRelic(p, Relic.Era.LITH);
        }

        PersistentDataAPI.setInt(p, Keys.FISHERIES_COUNTER, fishesCaught);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void updateKills(EntityDeathEvent e) {
        final Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        // Increment the relic reactants
        if (e.getEntity().getType() == EntityType.ENDERMAN) {
            int random = ThreadLocalRandom.current().nextInt(100);
            if (random < settings.getInt(SettingsManager.ConfigField.REACTANTS_CHANCE)) {

                // Support for bedrock players
                boolean isBedrockPlayer = SlimeFrame.getGeyserApi() != null && SlimeFrame.getGeyserApi().isBedrockPlayer(killer.getUniqueId());
                ItemStack relic = isBedrockPlayer ? killer.getInventory().getItem(0) : killer.getInventory().getItemInOffHand();
                String slot = isBedrockPlayer ? "first slot" : "off hand";

                SlimefunItem sfItem = SlimefunItem.getByItem(relic);
                if (sfItem instanceof Relic) {
                    if (relic.getAmount() > 1) {
                        ChatUtils.sendMessage(killer, ChatColor.RED + "It wasn't possible to deliver a reactant to you",
                                ChatColor.RED + "Make sure that you only have one relic in your " + slot);
                    } else {
                        Relic.incrementReactants(killer, relic);
                    }
                }
            }
        }

        int kills = PersistentDataAPI.getInt(killer, Keys.KILL_COUNTER, 0) + 1;

        if (kills % settings.getInt(SettingsManager.ConfigField.MESO_RELIC) == 0) {
            kills = 0;
            giveRelic(killer, Relic.Era.MESO);
        }

        PersistentDataAPI.setInt(killer, Keys.KILL_COUNTER, kills);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void updateBlocksBroken(BlockBreakEvent e) {
        Player p = e.getPlayer();
        int blocksBroken = PersistentDataAPI.getInt(p, Keys.BLOCKS_BROKEN_COUNTER, 0) + 1;

        if (blocksBroken % settings.getInt(SettingsManager.ConfigField.NEO_RELIC) == 0) {
            blocksBroken = 0;
            giveRelic(p, Relic.Era.NEO);
        }

        PersistentDataAPI.setInt(p, Keys.BLOCKS_BROKEN_COUNTER, blocksBroken);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void updateBlocksPlaced(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        int blocksPlaced = PersistentDataAPI.getInt(p, Keys.BLOCKS_PLACED_COUNTER, 0) + 1;

        if (blocksPlaced % settings.getInt(SettingsManager.ConfigField.AXI_RELIC) == 0) {
            blocksPlaced = 0;
            giveRelic(p, Relic.Era.AXI);
        }

        PersistentDataAPI.setInt(p, Keys.BLOCKS_PLACED_COUNTER, blocksPlaced);
    }

    private void giveRelic(Player player, Relic.Era era) {
        RelicInventory relicInventory = relicInvManager.getRelicInventory(player);
        relicInventory.addRandomRelic(era);
    }
}