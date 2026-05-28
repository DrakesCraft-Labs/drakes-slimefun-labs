package me.voper.slimeframe.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.managers.RelicInventoryManager;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.implementation.items.relics.Relic;
import me.voper.slimeframe.implementation.items.relics.RelicInventory;
import me.voper.slimeframe.utils.ChatUtils;
import me.voper.slimeframe.utils.Keys;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("%slimeframe")
@Description("Root command for SlimeFrame.")
public class Commands extends BaseCommand {

    @Dependency
    private SlimeFrame plugin;
    @Dependency
    private SettingsManager sm;
    @Dependency
    private RelicInventoryManager relicMan;

    @HelpCommand
    public void help(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("%inventory")
    @CommandPermission("slimeframe.anyone.inventory")
    @Description("Show your relic inventory")
    public void showRelicInventory(CommandSender sender) {
        if (!(sender instanceof Player p)) return;
        RelicInventory relicInventory = relicMan.getRelicInventory(p);
        relicInventory.open();
    }

    @Subcommand("%invsee")
    @CommandPermission("slimeframe.admin.invsee")
    @Description("Show a player's relic inventory")
    public void showPlayerInv(CommandSender sender, OnlinePlayer player) {
        if (!(sender instanceof Player p)) return;
        RelicInventory relicInventory = relicMan.getRelicInventory(player.getPlayer());
        p.openInventory(relicInventory.getInventory());
    }

    @Subcommand("%traces")
    @CommandPermission("slimeframe.anyone.traces")
    @Description("Show the amount of traces a player has")
    public void showVoidTraces(CommandSender sender, @Optional OnlinePlayer onlinePlayer) {
        if (!(sender instanceof Player p)) return;

        int voidTracesOwned = PersistentDataAPI.getInt(onlinePlayer != null ? onlinePlayer.getPlayer() : p, Keys.VOID_TRACES_OWNED, 0);

        if (onlinePlayer == null) {
            ChatUtils.sendMessage(p, ChatColor.GREEN + "You have " + ChatColor.AQUA + voidTracesOwned + ChatColor.GREEN + " void traces");
        } else {
            ChatUtils.sendMessage(p, ChatColor.GREEN + "The player " + ChatColor.WHITE + onlinePlayer.getPlayer().getName() + " has " + voidTracesOwned + ChatColor.GREEN + " void traces");
        }

    }

    @Subcommand("%refine")
    @CommandCompletion("@refinement")
    @CommandPermission("slimeframe.anyone.refine")
    @Description("Refine the relic in your hand")
    public void refineRelic(CommandSender sender, Relic.Refinement refinement) {
        if (!(sender instanceof Player p)) return;

        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        SlimefunItem byItem = SlimefunItem.getByItem(itemInMainHand);

        if (byItem == null || !(byItem instanceof Relic)) {
            ChatUtils.sendMessage(p, ChatColor.RED + "You do not have a relic in your hand to refine");
            return;
        }

        if (itemInMainHand.getAmount() > 1) {
            ChatUtils.sendMessage(p, ChatColor.RED + "You can only refine one relic at a time");
            return;
        }

        Relic.refineRelic(p, itemInMainHand, refinement);
    }

}