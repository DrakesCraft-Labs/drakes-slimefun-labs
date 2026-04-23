package com.github.drakescraft_labs.slimefuntranslation.core.commands.subcommands;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.drakescraft_labs.slimefun4.api.player.PlayerProfile;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuideMode;

import com.github.drakescraft_labs.guizhanlib.minecraft.commands.AbstractCommand;
import com.github.drakescraft_labs.slimefuntranslation.core.commands.AbstractSubCommand;
import com.github.drakescraft_labs.slimefuntranslation.implementation.groups.SearchResultGroup;
import com.github.drakescraft_labs.slimefuntranslation.utils.constant.Permissions;

/**
 * The subcommand that searches for Slimefun items with player's language.
 */
public class SearchCommand extends AbstractSubCommand {
    public SearchCommand(@Nonnull AbstractCommand parent) {
        super(parent, "search", (cmd, sender) -> getDescription("search", sender), "<query> [.] [.] [.] [.] [.]");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) {
            MESSAGE_FACTORY.sendMessage(sender, "player-only");
            return;
        }
        if (!Permissions.COMMAND_SEARCH.hasPermission(p)) {
            MESSAGE_FACTORY.sendMessage(sender, "no-permission");
            return;
        }

        final String query = String.join(" ", args);

        PlayerProfile.get(p, profile ->
            SlimefunGuide.openItemGroup(profile, new SearchResultGroup(query), SlimefunGuideMode.SURVIVAL_MODE, 1)
        );
    }
}
