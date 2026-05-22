package com.github.jackstar.coronalis.implementation.items;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.managers.SoundManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Bloque simple de infraestructura Coronalis.
 *
 * <p>Se usa para cable coaxial y núcleos SU: al colocarse o romperse pide una
 * reconstrucción acotada de redes cercanas, sin escaneos profundos de mundo.</p>
 */
public class ArrayNetworkBlock extends SlimefunItem implements Listener {

    private static final Set<String> NETWORK_BLOCK_IDS = Set.of(
        "CORONALIS_COAXIAL_CABLE",
        "CORONALIS_SIGNAL_CORE"
    );

    public ArrayNetworkBlock(@Nonnull ItemGroup itemGroup,
                             @Nonnull SlimefunItemStack item,
                             @Nonnull RecipeType recipeType,
                             @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        Coronalis.instance().getServer().getPluginManager().registerEvents(this, Coronalis.instance());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(@Nonnull BlockPlaceEvent event) {
        SlimefunItem sf = SlimefunItem.getByItem(event.getItemInHand());
        if (sf == null || !NETWORK_BLOCK_IDS.contains(sf.getId())) return;

        Block block = event.getBlockPlaced();
        Player player = event.getPlayer();
        block.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, block.getLocation().add(0.5, 0.6, 0.5),
            10, 0.25, 0.25, 0.25, 0.03);
        Coronalis.instance().getSoundManager().playAt(block.getLocation(), SoundManager.CoronalisSound.CABLE_CONNECT);
        player.sendMessage("§5[Coronalis] §7Infraestructura de red instalada: §d" + sf.getItemName());
        refreshNearby(block.getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(@Nonnull BlockBreakEvent event) {
        SlimefunItem sf = BlockStorage.check(event.getBlock());
        if (sf == null || !NETWORK_BLOCK_IDS.contains(sf.getId())) return;

        Location loc = event.getBlock().getLocation().clone();
        event.getPlayer().sendMessage("§5[Coronalis] §7Red Coronalis alterada. Recalculando enlaces...");
        Coronalis.instance().getServer().getScheduler().runTaskLater(
            Coronalis.instance(), () -> refreshNearby(loc), 2L);
    }

    private static void refreshNearby(@Nonnull Location loc) {
        Coronalis.instance().getServer().getScheduler().runTaskLater(
            Coronalis.instance(),
            () -> Coronalis.instance().getNetworkRegistry().rebuildNetworksNear(loc, 64),
            2L
        );
    }
}
