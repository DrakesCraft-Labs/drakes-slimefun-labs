package com.github.drakescraft_labs.coloredenderchests;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.researches.Research;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.libraries.dough.config.Config;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

public class ColoredEnderChests extends JavaPlugin implements SlimefunAddon, Listener {

    private static ColoredEnderChests instance;
    protected Config cfg;
    protected Map<Integer, String> colors = new HashMap<>();
    protected ItemGroup itemGroup;
    private PrivateEnderStorage privateStorage;

    public static ColoredEnderChests getInstance() {
        return instance;
    }

    public PrivateEnderStorage getPrivateStorage() {
        return privateStorage;
    }

    @Override
    public void onEnable() {
        instance = this;
        cfg = new Config(this);

        // Setting up the Auto-Updater
        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/ColoredEnderChests/master").start();
        }

        privateStorage = new PrivateEnderStorage(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        Research enderChestsResearch = new Research(new NamespacedKey(this, "colored_enderchests"), 2610, "Colored Ender Chests", 20);
        Research bigEnderChestsResearch = new Research(new NamespacedKey(this, "big_colored_enderchests"), 2611, "Big Colored Ender Chests", 30);

        enderChestsResearch.register();
        bigEnderChestsResearch.register();

        colors.put(0, "&rWhite");
        colors.put(1, "&6Orange");
        colors.put(2, "&dMagenta");
        colors.put(3, "&bLight Blue");
        colors.put(4, "&eYellow");
        colors.put(5, "&aLime");
        colors.put(6, "&dPink");
        colors.put(7, "&8Dark Gray");
        colors.put(8, "&7Light Gray");
        colors.put(9, "&3Cyan");
        colors.put(10, "&5Purple");
        colors.put(11, "&9Blue");
        colors.put(12, "&6Brown");
        colors.put(13, "&2Green");
        colors.put(14, "&4Red");
        colors.put(15, "&8Black");

        itemGroup = new ItemGroup(new NamespacedKey(this, "colored_enderchests"), new CustomItemStack(Material.ENDER_CHEST, "&5Colored Ender Chests"), 2);

        for (int c1 = 0; c1 < 16; c1++) {
            for (int c2 = 0; c2 < 16; c2++) {
                for (int c3 = 0; c3 < 16; c3++) {
                    registerEnderChest(enderChestsResearch, bigEnderChestsResearch, c1, c2, c3);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (privateStorage != null) {
            privateStorage.onDisable();
        }
        instance = null;
    }

    private void registerEnderChest(Research smallResearch, Research bigResearch, final int c1, final int c2, final int c3) {
        if (cfg.getBoolean("small_chests")) {
            ColoredEnderChest item = new ColoredEnderChest(this, 27, c1, c2, c3);
            item.register(this);
            smallResearch.addItems(item);
        }

        if (cfg.getBoolean("big_chests")) {
            ColoredEnderChest item = new ColoredEnderChest(this, 54, c1, c2, c3);
            item.register(this);
            bigResearch.addItems(item);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = event.getClickedBlock();
        if (b == null || b.getType() != Material.ENDER_CHEST) return;

        SlimefunItem sfItem = BlockStorage.check(b);
        if (!(sfItem instanceof ColoredEnderChest chest)) return;

        Player player = event.getPlayer();
        ItemStack inHand = event.getItem();
        boolean hasDiamond = inHand != null && inHand.getType() == Material.DIAMOND;

        int c1 = chest.getC1();
        int c2 = chest.getC2();
        int c3 = chest.getC3();
        int size = chest.getSize();

        if (hasDiamond) {
            event.setCancelled(true);
            if (!PrivateEnderStorage.isPrivate(b)) {
                // Lock with Diamond to Private Mode
                if (player.getGameMode() != GameMode.CREATIVE) {
                    inHand.setAmount(inHand.getAmount() - 1);
                }
                PrivateEnderStorage.setPrivate(b, player);
                int yaw = ColoredEnderChest.getFacingYaw(b);
                ColorIndicator.updateIndicator(b, c1, c2, c3, yaw + 45, true);

                player.playSound(b.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.8f);
                b.getWorld().spawnParticle(Particle.END_ROD, b.getLocation().add(0.5, 0.6, 0.5), 15, 0.2, 0.2, 0.2, 0.05);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[ColoredEnderChests] &a¡Cofre asegurado en modo PRIVADO con Diamante!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Solo tú puedes abrir esta frecuencia privada. Agáchate con un diamante para devolverlo a público."));
                return;
            } else {
                // Already private
                UUID owner = PrivateEnderStorage.getOwner(b);
                if (owner != null && owner.equals(player.getUniqueId())) {
                    if (player.isSneaking()) {
                        // Unlock back to Public
                        PrivateEnderStorage.removePrivate(b);
                        int yaw = ColoredEnderChest.getFacingYaw(b);
                        ColorIndicator.updateIndicator(b, c1, c2, c3, yaw + 45, false);

                        if (player.getGameMode() != GameMode.CREATIVE) {
                            player.getInventory().addItem(new ItemStack(Material.DIAMOND)).values()
                                  .forEach(rem -> player.getWorld().dropItemNaturally(player.getLocation(), rem));
                        }
                        player.playSound(b.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[ColoredEnderChests] &eCofre restaurado a modo PÚBLICO. Diamante reembolsado."));
                        return;
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[ColoredEnderChests] &eEste cofre ya es tu cofre privado. Agáchate con un diamante para desbloquearlo."));
                        return;
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ColoredEnderChests] Este cofre pertenece a &e" + PrivateEnderStorage.getOwnerName(b) + "&c!"));
                    player.playSound(b.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 0.5f);
                    return;
                }
            }
        }

        // Opening without Diamond
        if (PrivateEnderStorage.isPrivate(b)) {
            event.setCancelled(true);
            UUID owner = PrivateEnderStorage.getOwner(b);
            if (owner != null && (owner.equals(player.getUniqueId()) || player.hasPermission("slimefun.inventory.bypass"))) {
                privateStorage.openPrivateChest(player, b.getWorld(), owner, size, c1, c2, c3);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ColoredEnderChests] Este cofre es privado de &e" + PrivateEnderStorage.getOwnerName(b) + "&c!"));
                player.playSound(b.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 0.5f);
            }
            return;
        }

        // Intercept public chests in non-survival modalities (e.g. bskyblock, aoneblock)
        // to strictly isolate island inventories from Survival's global inventory.
        String modality = PrivateEnderStorage.resolveModality(b.getWorld());
        if (!"survival".equalsIgnoreCase(modality)) {
            event.setCancelled(true);
            privateStorage.openScopedPublicChest(player, b.getWorld(), size, c1, c2, c3);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (b.getType() != Material.ENDER_CHEST) return;

        SlimefunItem sfItem = BlockStorage.check(b);
        if (!(sfItem instanceof ColoredEnderChest)) return;

        if (PrivateEnderStorage.isPrivate(b)) {
            Player player = event.getPlayer();
            UUID owner = PrivateEnderStorage.getOwner(b);
            if (owner != null && !owner.equals(player.getUniqueId()) && !player.hasPermission("slimefun.inventory.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ColoredEnderChests] No puedes romper el cofre privado de &e" + PrivateEnderStorage.getOwnerName(b) + "&c!"));
                return;
            }

            // Dropping diamond refunded from the latch lock
            b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.DIAMOND));
            PrivateEnderStorage.removePrivate(b);
        }
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/ColoredEnderChests/issues";
    }
}
