package ne.fnfal113.relicsofcthonia.listeners;

import com.github.drakescraft-labs.slimefun4.api.events.BlockPlacerPlaceEvent;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.RelicsRegistry;
import ne.fnfal113.relicsofcthonia.slimefun.relics.AbstractRelic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ne.fnfal113.relicsofcthonia.core.Keys.PLACED_BLOCK;

public class MiningListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled() || event.getPlayer().getWorld().getEnvironment() != World.Environment.NETHER) {
            return;
        }

        Block block = event.getBlock();

        // Only naturally generated blocks are accepted to prevent place and break farming
        if (block.hasMetadata(PLACED_BLOCK)) {
            block.removeMetadata(PLACED_BLOCK, RelicsOfCthonia.getInstance());
            return;
        }

        int dropped = 0;
        List<AbstractRelic> relics = RelicsRegistry.BLOCK_SOURCES.get(block.getType());
        if (relics == null || relics.isEmpty()) {
            return;
        }

        Collections.shuffle(relics);
        for (AbstractRelic relic : relics) {
            if (relic.isDisabledIn(event.getPlayer().getWorld()) || relic.isDisabled()) {
                continue;
            }

            if (ThreadLocalRandom.current().nextDouble() < relic.getDropChance()) {
                ItemStack drop = relic.randomRelic();
                block.getWorld().dropItemNaturally(block.getLocation(), drop);
                if (++dropped >= 2) {
                    break;
                }
            }
        }
    }

    /*
     * Prevent players from block place farming any relics by tracking blocks placed by players/block placers
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata(PLACED_BLOCK, new FixedMetadataValue(RelicsOfCthonia.getInstance(), "placed"));
    }

    /*
     * Prevent players from block place farming any relics by tracking blocks placed by players/block placers
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPlacerPlaced(BlockPlacerPlaceEvent event) {
        event.getBlock().setMetadata(PLACED_BLOCK, new FixedMetadataValue(RelicsOfCthonia.getInstance(), "placed"));
    }
}
