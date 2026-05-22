package com.github.jackstar.coronalis.implementation.items;

import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.discovery.DiscoveryService;
import com.github.jackstar.coronalis.implementation.data.CelestialTarget;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Eco de Fase Coronalis — ítem consumible que el jugador analiza con clic derecho.
 *
 * <p>Lee los datos del lore (objeto, Az/El, fase, dificultad) y entrega XP
 * multiplicado por el tier del objetivo y el multiplicador del evento cósmico activo.</p>
 */
public class DataRecordItem extends SlimefunItem implements Listener {

    public DataRecordItem(@Nonnull ItemGroup itemGroup,
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

    @EventHandler
    public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
         && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        SlimefunItem sf = SlimefunItem.getByItem(item);
        if (sf != null && sf.getId().equals(this.getId())) {
            event.setCancelled(true);
            analyzeRecord(player, item);
        }
    }

    private void analyzeRecord(@Nonnull Player player, @Nonnull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // ── Extraer datos del lore ────────────────────────────────────────────
        String objectName = "Señal Desconocida";
        String objectDesc = "Una misteriosa oscilación de fase sin clasificar en el espacio interestelar.";
        String azStr      = "120.4";
        String elStr      = "45.2";
        String phaseStr   = "99.8";
        String tierLabel  = "§7—";

        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                String clean = ChatColor.stripColor(line);
                if (clean.startsWith("Objeto: "))      objectName = clean.substring(8).trim();
                else if (clean.startsWith("Az/El: ")) {
                    String[] parts = clean.substring(7).split("/");
                    if (parts.length == 2) {
                        azStr = parts[0].replace("°", "").trim();
                        elStr = parts[1].replace("°", "").trim();
                    }
                }
                else if (clean.startsWith("Descripción: ")) objectDesc = clean.substring(13).trim();
                else if (clean.startsWith("Fase: "))        phaseStr  = clean.substring(6).replace("%","").trim();
                else if (clean.startsWith("Dificultad: "))  tierLabel = line.substring("Dificultad: ".length()).trim();
            }
        }

        // ── Buscar el CelestialTarget para el multiplicador de tier ───────────
        CelestialTarget target = CelestialTarget.byName(objectName);
        double tierMult = target.xpMultiplier;

        // ── Efectos visuales ──────────────────────────────────────────────────
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
        player.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 0.8f);
        player.getWorld().spawnParticle(Particle.PORTAL,  loc, 35, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().spawnParticle(Particle.END_ROD, loc, 15, 0.3, 0.3, 0.3, 0.05);

        // ── Descubrimiento y XP ───────────────────────────────────────────────
        DiscoveryService ds     = Coronalis.instance().getDiscoveryService();
        double           evMult = Coronalis.instance().getCosmicEventManager().getXpMultiplier();

        String recordId   = "record_" + DiscoveryService.targetDiscoveryId(objectName);
        boolean firstTime = ds.tryDiscoverWithMultiplier(
            player, recordId, "Análisis: " + objectName,
            "discovery-xp.record_analysis", tierMult * evMult);

        if (!firstTime) {
            ds.tryDiscoverWithMultiplier(
                player, recordId + "_repeat", "Re-análisis: " + objectName,
                "discovery-xp.record_repeat", evMult);
        }

        // ── Mensaje detallado al jugador ──────────────────────────────────────
        player.sendMessage("");
        player.sendMessage("§5§l⭐ [Coronalis] ⭐");
        player.sendMessage("§d¡Has descifrado los datos del Eco de Fase Coronalis!");
        player.sendMessage("§8══════════════════════════════════════════");
        player.sendMessage("§d✨ Objeto Celeste:    §6§l" + objectName);
        player.sendMessage("§d📡 Coordenadas:       §7Az: §e" + azStr + "°  §7El: §e" + elStr + "°");
        player.sendMessage("§d🌀 Coherencia de Fase: §b" + phaseStr + "%");
        player.sendMessage("§d⭐ Dificultad:         " + tierLabel);
        if (evMult > 1.0) {
            player.sendMessage("§a✧ Bonus evento cósmico: §l×" + evMult + " XP");
        }
        player.sendMessage("§d📖 Descripción: §e§o" + objectDesc);
        player.sendMessage("§8══════════════════════════════════════════");
        player.sendMessage("");

        // Consumir 1 ítem
        item.setAmount(item.getAmount() - 1);
    }
}
