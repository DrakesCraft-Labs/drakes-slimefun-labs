package com.github.drakescraft_labs.coloredenderchests;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;

/**
 * Handles private per-player storage for Colored Ender Chests locked with a Diamond.
 * Prevents players from copying color combinations and stealing other players' chests.
 */
public class PrivateEnderStorage implements Listener {

    private final ColoredEnderChests plugin;
    private final File dataFolder;

    private static class OpenContext {
        final UUID owner;
        final int size;
        final int c1;
        final int c2;
        final int c3;
        final File file;

        OpenContext(UUID owner, int size, int c1, int c2, int c3, File file) {
            this.owner = owner;
            this.size = size;
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
            this.file = file;
        }
    }

    private final Map<UUID, OpenContext> openViewers = new ConcurrentHashMap<>();

    public PrivateEnderStorage(ColoredEnderChests plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "private-chests");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static boolean isPrivate(Block b) {
        String isPriv = BlockStorage.getLocationInfo(b.getLocation(), "is_private");
        return "true".equalsIgnoreCase(isPriv);
    }

    public static UUID getOwner(Block b) {
        String uuidStr = BlockStorage.getLocationInfo(b.getLocation(), "owner");
        if (uuidStr == null || uuidStr.isEmpty()) return null;
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String getOwnerName(Block b) {
        String name = BlockStorage.getLocationInfo(b.getLocation(), "owner_name");
        return name != null && !name.isEmpty() ? name : "Unknown";
    }

    public static void setPrivate(Block b, Player owner) {
        BlockStorage.addBlockInfo(b, "owner", owner.getUniqueId().toString());
        BlockStorage.addBlockInfo(b, "owner_name", owner.getName());
        BlockStorage.addBlockInfo(b, "is_private", "true");
    }

    public static void removePrivate(Block b) {
        BlockStorage.addBlockInfo(b, "owner", "");
        BlockStorage.addBlockInfo(b, "owner_name", "");
        BlockStorage.addBlockInfo(b, "is_private", "false");
    }

    public void openPrivateChest(Player player, UUID owner, int size, int c1, int c2, int c3) {
        File userDir = new File(dataFolder, owner.toString());
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        String fileName = (size == 27 ? "small" : "big") + "_" + c1 + "_" + c2 + "_" + c3 + ".yml";
        File file = new File(userDir, fileName);

        String title = ChatColor.translateAlternateColorCodes('&', "&5&lPrivate Ender Chest &7(" + (size == 27 ? "Small" : "Big") + ") #" + c1 + "-" + c2 + "-" + c3);
        Inventory inv = Bukkit.createInventory(player, size, title);

        if (file.exists()) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (int i = 0; i < size; i++) {
                if (yaml.contains("slot." + i)) {
                    ItemStack is = yaml.getItemStack("slot." + i);
                    if (is != null) {
                        inv.setItem(i, is);
                    }
                }
            }
        }

        openViewers.put(player.getUniqueId(), new OpenContext(owner, size, c1, c2, c3, file));
        player.openInventory(inv);
        try {
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.8F, 1.6F);
        } catch (Throwable ignored) {}
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        OpenContext ctx = openViewers.remove(player.getUniqueId());
        if (ctx == null) return;

        Inventory inv = event.getInventory();
        YamlConfiguration yaml = new YamlConfiguration();
        for (int i = 0; i < ctx.size; i++) {
            ItemStack is = inv.getItem(i);
            if (is != null && is.getType() != Material.AIR) {
                yaml.set("slot." + i, is);
            }
        }

        try {
            yaml.save(ctx.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save private ender chest for " + ctx.owner, e);
        }

        try {
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.8F, 1.6F);
        } catch (Throwable ignored) {}
    }

    public void onDisable() {
        for (Map.Entry<UUID, OpenContext> entry : openViewers.entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            if (p != null && p.isOnline()) {
                p.closeInventory();
            }
        }
        openViewers.clear();
    }
}
