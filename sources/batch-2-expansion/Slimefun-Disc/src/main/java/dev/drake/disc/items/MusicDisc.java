package dev.drake.disc.items;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

import dev.drake.disc.SlimefunDisc;
import dev.drake.disc.setup.SongLoader;

public class MusicDisc extends SlimefunItem implements Listener {

    private final String songKey;
    private final String discName;
    private static final Map<Location, SongPlayer> activePlayers = new ConcurrentHashMap<>();
    private static final Map<Location, ItemStack> discRecords = new ConcurrentHashMap<>();

    public MusicDisc(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String songKey) {
        super(itemGroup, item, recipeType, recipe);
        this.songKey = songKey;
        ItemMeta meta = item.getItemMeta();
        this.discName = meta != null && meta.hasDisplayName() ? meta.getDisplayName() : null;
        SlimefunDisc.getInstance().getServer().getPluginManager().registerEvents(this, SlimefunDisc.getInstance());
    }

    @EventHandler
    public void onJukeboxInteract(PlayerInteractEvent e) {
        if (e.isCancelled()) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null || block.getType() != Material.JUKEBOX) return;

        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        Location loc = block.getLocation();

        if (hand == null || hand.getType() == Material.AIR) {
            if (discRecords.containsKey(loc)) {
                e.setCancelled(true);

                stopPlaying(loc);
                ItemStack disc = discRecords.remove(loc);

                HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(disc);
                for (ItemStack item : leftover.values()) {
                    block.getWorld().dropItemNaturally(loc, item);
                }

                player.sendMessage("§aRemoved disc from jukebox!");
            }
            return;
        }

        if (discName == null) return;
        ItemMeta handMeta = hand.getItemMeta();
        if (handMeta == null || !handMeta.hasDisplayName()) return;
        if (!discName.equals(handMeta.getDisplayName())) return;

        e.setCancelled(true);

        Jukebox jukebox = (Jukebox) block.getState();
        if (jukebox.hasRecord() || discRecords.containsKey(loc)) {
            player.sendMessage("§cThe jukebox already has a disc inside!");
            return;
        }

        ItemStack record = hand.clone();
        record.setAmount(1);

        if (hand.getAmount() <= 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            hand.setAmount(hand.getAmount() - 1);
        }

        discRecords.put(loc, record);

        stopPlaying(loc);

        var song = SongLoader.getSong(songKey);
        if (song != null) {
            SongPlayer playerTask = new SongPlayer(loc, song);
            activePlayers.put(loc, playerTask);
            playerTask.start(SlimefunDisc.getInstance());
            player.sendMessage("§6Now playing: §e" + song.getName());
        }
    }

    @EventHandler
    public void onJukeboxBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType() != Material.JUKEBOX) return;

        Location loc = block.getLocation();
        stopPlaying(loc);

        ItemStack customDisc = discRecords.remove(loc);
        if (customDisc != null) {
            block.getWorld().dropItemNaturally(loc, customDisc);
        }

        Jukebox jukebox = (Jukebox) block.getState();
        if (jukebox.hasRecord()) {
            block.getWorld().dropItemNaturally(loc, jukebox.getRecord());
            jukebox.setRecord(null);
            jukebox.update();
        }
    }

    public static void stopPlaying(@Nonnull Location loc) {
        SongPlayer task = activePlayers.remove(loc);
        if (task != null) {
            task.stop();
        }
    }

    public static void stopAll() {
        for (SongPlayer task : activePlayers.values()) {
            task.stop();
        }
        activePlayers.clear();
        discRecords.clear();
    }

    @Nullable
    public static SongPlayer getActivePlayer(@Nonnull Location loc) {
        return activePlayers.get(loc);
    }
}
