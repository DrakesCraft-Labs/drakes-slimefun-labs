package com.github.jackstar.coronalis.implementation.items;

import com.github.jackstar.coronalis.Coronalis;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Radiotelescopio de Antena Coronalis.
 *
 * <p>No tiene GUI propia: su función es existir en el mundo y ser detectado
 * por la {@link ControlConsole} para aumentar la sensibilidad del array.
 * Al colocarse o romperse, recalcula el conteo de telescopios en todas las
 * Consolas de Control dentro de un radio de 15 bloques horizontales × 3 verticales.</p>
 *
 * <p>ID de Slimefun: {@code CORONALIS_RADIO_TELESCOPE}</p>
 */
public class RadioTelescope extends SlimefunItem implements Listener {

    public RadioTelescope(@Nonnull ItemGroup itemGroup,
                          @Nonnull SlimefunItemStack item,
                          @Nonnull RecipeType recipeType,
                          @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        Coronalis.instance().getServer()
            .getPluginManager().registerEvents(this, Coronalis.instance());
    }

    // ── Eventos de bloque ────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(@Nonnull BlockPlaceEvent event) {
        SlimefunItem sf = SlimefunItem.getByItem(event.getItemInHand());
        if (sf == null || !sf.getId().equals("CORONALIS_RADIO_TELESCOPE")) return;

        Block  block  = event.getBlockPlaced();
        Player player = event.getPlayer();

        block.getWorld().spawnParticle(
            Particle.END_ROD,
            block.getLocation().add(0.5, 1.2, 0.5),
            10, 0.3, 0.3, 0.3, 0.05
        );
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.6f, 1.5f);
        player.sendMessage("§5[Coronalis] §d📡 Telescopio conectado a la red de Coronalis.");

        Location loc = block.getLocation().clone();
        Coronalis.instance().getServer().getScheduler()
            .runTaskLater(Coronalis.instance(), () -> refreshNearbyConsoles(loc, player), 2L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(@Nonnull BlockBreakEvent event) {
        Block        block = event.getBlock();
        SlimefunItem sf    = BlockStorage.check(block);
        if (sf == null || !sf.getId().equals("CORONALIS_RADIO_TELESCOPE")) return;

        Player   player = event.getPlayer();
        Location loc    = block.getLocation().clone();

        block.getWorld().spawnParticle(
            Particle.SMOKE,
            loc.clone().add(0.5, 0.5, 0.5),
            8, 0.2, 0.2, 0.2, 0.05
        );
        block.getWorld().playSound(loc, Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.8f, 0.8f);
        player.sendMessage("§5[Coronalis] §7📡 Telescopio desconectado de la red.");

        Coronalis.instance().getServer().getScheduler()
            .runTaskLater(Coronalis.instance(), () -> refreshNearbyConsoles(loc, null), 2L);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    /**
     * Actualiza el conteo de telescopios en todas las Consolas activas
     * dentro del radio de alcance.
     */
    static void refreshNearbyConsoles(@Nonnull Location loc, @Nullable Player player) {
        for (Location consoleLoc : ControlConsole.ACTIVE_CONSOLES) {
            if (consoleLoc.getWorld() == null
                    || !consoleLoc.getWorld().equals(loc.getWorld())) continue;

            if (Math.abs(consoleLoc.getX() - loc.getX()) <= 15
             && Math.abs(consoleLoc.getY() - loc.getY()) <= 3
             && Math.abs(consoleLoc.getZ() - loc.getZ()) <= 15) {

                int count = ControlConsole.countTelescopesAt(consoleLoc);
                BlockStorage.addBlockInfo(consoleLoc, "connected_telescopes", String.valueOf(count));

                var menu = BlockStorage.getInventory(consoleLoc);
                if (menu != null) ControlConsole.updateMenuVisuals(menu, consoleLoc);

                if (player != null) {
                    player.sendMessage("  §7└─ Red actualizada: §a" + count + " telescopio(s) detectado(s).");
                }
            }
        }
    }
}
