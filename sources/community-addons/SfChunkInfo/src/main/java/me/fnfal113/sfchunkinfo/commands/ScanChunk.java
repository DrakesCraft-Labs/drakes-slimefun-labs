package me.fnfal113.sfchunkinfo.commands;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScanChunk implements TabExecutor {

    private final Map<String, Integer> AMOUNT = new HashMap<>();
    private final Map<String, String> INFO = new HashMap<>();
    private final Map<String, Double> TIMINGS = new HashMap<>();
    private final Map<String, Integer> POWER = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0) {
                if (player.hasPermission("sfchunkinfo.scan")) {
                    Chunk chunk = player.getLocation().getChunk();

                    getAmount(chunk, player);

                } else {
                    player.sendMessage("You don't have permission to use this command (perm mode: sfchunkinfo.scan)");
                }
            } else {
                if (player.hasPermission("sfchunkinfo.scan.others")) {
                    Player target = Bukkit.getPlayer(args[0]);

                    if(target == null){
                        player.sendMessage("Player should not be null nor offline");

                        return true;
                    }

                    Chunk chunk = target.getLocation().getChunk();

                    getAmountOthers(chunk, target, player);
                } else {
                    player.sendMessage("You don't have permission to use this command (perm mode: sfchunkinfo.scan.others)");
                }
            }
        }

        return true;
    }

    public void getAmount(Chunk chunk, Player player){
        if (!Slimefun.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(player.getUniqueId()), player.getLocation(),
                Interaction.PLACE_BLOCK)) {
            player.sendMessage("You don't have the permission to scan this chunk (Grief Protected), ask for permission or override using the protection plugin command");

            return;
        }

        scanChunk(chunk);

        player.sendMessage(ChatColor.GOLD + "# of Slimefun blocks on this chunk:", "");

        if (AMOUNT.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "No Slimefun blocks on this chunk");

            return;
        }

        sendResults(player);
    }

    public void getAmountOthers(Chunk chunk, Player player, Player sender){
        scanChunk(chunk);

        sender.sendMessage(ChatColor.GOLD + "# of Slimefun blocks on " + ChatColor.WHITE + player.getName() + ChatColor.GOLD + " chunk:", "");

        if (AMOUNT.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "No Slimefun blocks on " + ChatColor.WHITE + player.getName() + ChatColor.GOLD + " chunk");

            return;
        }

        sendResults(sender);
    }

    public void scanChunk(Chunk chunk){
        for(int y = chunk.getWorld().getMinHeight(); y <= chunk.getWorld().getMaxHeight() - 1; y++) {
            for(int x = 0; x <= 15; x++) {
                for(int z = 0; z <= 15; z++) {
                    Block block = chunk.getBlock(x, y, z);

                    if(block.getType() == Material.AIR){
                        continue;
                    }

                    if(BlockStorage.check(block) != null) {
                        SlimefunItem sfItem = BlockStorage.check(block);
                        String sfBlockName = Objects.requireNonNull(sfItem).getItemName();

                        getPowerUsage(sfItem, block.getLocation());

                        TIMINGS.put(sfBlockName, TIMINGS.getOrDefault(sfBlockName, (double) 0)
                                + Double.parseDouble(Slimefun.getProfiler().getTime(block).substring(0, Slimefun.getProfiler().getTime(block).length() - 2)));
                        INFO.put(sfBlockName, Objects.requireNonNull(BlockStorage.check(block)).getAddon().getName());
                        AMOUNT.put(sfBlockName,  AMOUNT.getOrDefault(sfBlockName, 0) + 1);
                    }
                }
            }
        }
    }

    public void getPowerUsage(SlimefunItem sfItem, Location loc){
        if(!(sfItem instanceof EnergyNetComponent)){
            return;
        }

        EnergyNetComponent energyComponent = (EnergyNetComponent) sfItem;

        int capacity = energyComponent.getCapacity();
        int charge = energyComponent.getCharge(loc);
        int demand = capacity - charge;

        if(charge != 0 && demand != 0 && demand != capacity){
            POWER.put(sfItem.getItemName(), POWER.getOrDefault(sfItem.getItemName(), 0) + demand);
        }

    }

    public void sendResults(Player player){
        AMOUNT.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(e -> player.sendMessage(e.getKey() + ": " + ChatColor.GREEN + e.getValue()));

        player.spigot().sendMessage(hoverInfo(INFO));
        player.spigot().sendMessage(hoverInfoTimings(TIMINGS));
        player.spigot().sendMessage(hoverInfoPower(POWER));

        AMOUNT.clear();
        INFO.clear();
        TIMINGS.clear();
        POWER.clear();
    }

    public TextComponent hoverInfo(Map<String, String> info){
        TextComponent infoAddon = new TextComponent("\nHover for some info");

        infoAddon.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
        infoAddon.setItalic(true);
        infoAddon.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, info.isEmpty() ? new Text(ChatColor.GOLD + "No data collected") : new Text(info.toString()
                .replace("{","")
                    .replace("}","")
                        .replace(", ", "\n")
                            .replace("=", ChatColor.WHITE + " | from: "))));

        return infoAddon;
    }

    public TextComponent hoverInfoTimings(Map<String, Double> timings){
        Map<String, Double> sortedTimings = new LinkedHashMap<>();
        if(!timings.isEmpty()) {
            timings.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue(Double::compare)).forEach(e -> sortedTimings.put(e.getKey(), e.getValue()));
        }

        TextComponent infoChunk = new TextComponent("Hover for block total timings");

        infoChunk.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
        infoChunk.setItalic(true);
        infoChunk.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, sortedTimings.isEmpty() ? new Text(ChatColor.GOLD + "No data collected") : new Text(ChatColor.GOLD + "Total Timings" + "\n\n" + sortedTimings.toString()
                .replace("{","")
                    .replace("}","")
                        .replace(", ", " ms\n")
                            .replace("=", ChatColor.WHITE + ": ")
                                .concat(ChatColor.WHITE + " ms"))));

        return infoChunk;
    }

    public TextComponent hoverInfoPower(Map<String, Integer> power){
        Map<String, Integer> sortedPower = new LinkedHashMap<>();
        if(!power.isEmpty()){
            power.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue(Integer::compare)).forEach(e -> sortedPower.put(e.getKey(), e.getValue()));
        }

        TextComponent infoPower = new TextComponent("Hover for total power consumption");

        infoPower.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
        infoPower.setItalic(true);
        infoPower.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, power.isEmpty() ? new Text(ChatColor.GOLD + "No data collected") : new Text(sortedPower.toString()
                .replace("{","")
                    .replace("}","")
                        .replace(", ", " J/t\n")
                            .replace("=", ChatColor.WHITE + " | Total Consumption: ")
                                .concat(ChatColor.WHITE + " J/t"))));

        return infoPower;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1){
            List<String> playerNames = new ArrayList<>();

            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);

            for (Player player : players) {
                playerNames.add(player.getName());
            }

            return playerNames;
        }

        return null;
    }
}
