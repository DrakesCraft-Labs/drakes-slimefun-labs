package com.github.drakescraft-labs.slimefun4.core.commands.subcommands;

import java.util.Arrays;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft-labs.slimefun4.core.commands.SlimefunCommand;
import com.github.drakescraft-labs.slimefun4.core.commands.SubCommand;
import com.github.drakescraft-labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft-labs.slimefun4.core.guide.SlimefunGuideMode;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;

class SearchCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    SearchCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "search", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("slimefun.command.search")) {
                if (args.length > 1) {
                    String query = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    PlayerProfile.get(player, profile -> SlimefunGuide.openSearch(profile, query, SlimefunGuideMode.SURVIVAL_MODE, true));
                } else {
                    Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf search <SearchTerm>"));
                }
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }

}
