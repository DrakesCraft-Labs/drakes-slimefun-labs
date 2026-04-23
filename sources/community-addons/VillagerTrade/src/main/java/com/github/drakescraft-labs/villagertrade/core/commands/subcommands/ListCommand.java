package com.github.drakescraft-labs.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.villagertrade.implementation.menu.TradeListMenu;

public final class ListCommand extends AdminPlayerCommand {

    public ListCommand() {
        super("list", false, "");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        new TradeListMenu((Player) sender);
    }
}
