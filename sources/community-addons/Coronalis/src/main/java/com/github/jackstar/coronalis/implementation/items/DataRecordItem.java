package com.github.jackstar.coronalis.implementation.items;

import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.discovery.DiscoveryService;
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
import java.util.Random;

public class DataRecordItem extends SlimefunItem implements Listener {

    private final Random random = new Random();

    public DataRecordItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        Coronalis.instance().getServer().getPluginManager().registerEvents(this, Coronalis.instance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem != null && sfItem.getId().equals(this.getId())) {
            event.setCancelled(true);
            consumeRecord(player, item);
        }
    }

    private void consumeRecord(@Nonnull Player player, @Nonnull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String objectName = "Señal Desconocida";
        String objectDesc = "Una misteriosa oscilación de fase sin clasificar en el espacio interestelar.";
        String azStr = "120.4";
        String elStr = "45.2";
        String phaseStr = "99.8";

        // Intentar leer los datos guardados en el lore
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                String cleanLine = ChatColor.stripColor(line);
                if (cleanLine.startsWith("Objeto: ")) {
                    objectName = cleanLine.substring(8).trim();
                } else if (cleanLine.startsWith("Az/El: ")) {
                    String[] parts = cleanLine.substring(7).split("/");
                    if (parts.length == 2) {
                        azStr = parts[0].replace("°", "").trim();
                        elStr = parts[1].replace("°", "").trim();
                    }
                } else if (cleanLine.startsWith("Descripción: ")) {
                    objectDesc = cleanLine.substring(13).trim();
                } else if (cleanLine.startsWith("Fase: ")) {
                    phaseStr = cleanLine.substring(6).replace("%", "").trim();
                }
            }
        }

        // Si no hay datos (ej. se obtuvo con /give), dar uno aleatorio
        if (objectName.equals("Señal Desconocida") && random.nextInt(3) == 0) {
            objectName = "Sagitario A*";
            objectDesc = "El agujero negro supermasivo en el centro de nuestra galaxia, emitiendo intensas ondas de radio.";
            azStr = "177.3";
            elStr = "29.1";
            phaseStr = "98.9";
        }

        // Efectos visuales y de sonido premium
        Location loc = player.getLocation().add(0, 1, 0);
        player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
        player.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 0.8f);
        player.getWorld().spawnParticle(Particle.PORTAL, loc, 35, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().spawnParticle(Particle.END_ROD, loc, 15, 0.3, 0.3, 0.3, 0.05);

        DiscoveryService discoveries = Coronalis.instance().getDiscoveryService();
        String recordId = "record_" + DiscoveryService.targetDiscoveryId(objectName);
        boolean firstAnalysis = discoveries.tryDiscover(
            player,
            recordId,
            "Análisis: " + objectName,
            "discovery-xp.record_analysis"
        );
        if (!firstAnalysis) {
            discoveries.tryDiscover(
                player,
                recordId + "_repeat",
                "Re-análisis: " + objectName,
                "discovery-xp.record_repeat"
            );
        }

        player.sendMessage("");
        player.sendMessage("§5§l⭐ [Coronalis] ⭐");
        player.sendMessage("§d¡Has descifrado los datos del Registro Científico!");
        player.sendMessage("§8==================================================");
        player.sendMessage("§d✨ Objeto Celeste: §6§l" + objectName);
        player.sendMessage("§d📡 Coordenadas de Apuntado: §7Azimut: §e" + azStr + "° §7/ Elevación: §e" + elStr + "°");
        player.sendMessage("§d🌀 Coherencia de Fase: §b" + phaseStr + "%");
        player.sendMessage("§d📖 Detalles: §e§o" + objectDesc);
        player.sendMessage("§8==================================================");
        player.sendMessage("");

        // Consumir 1 item de la mano
        item.setAmount(item.getAmount() - 1);
    }
}
