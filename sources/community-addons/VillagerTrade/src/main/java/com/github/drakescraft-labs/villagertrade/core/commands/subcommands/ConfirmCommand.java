package com.github.drakescraft-labs.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.villagertrade.VillagerTrade;
import com.github.drakescraft-labs.villagertrade.core.tasks.ConfirmationTask;

public final class ConfirmCommand extends AdminPlayerCommand {

    public ConfirmCommand() {
        super("confirm", false, "");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        final Player player = (Player) sender;
        final ConfirmationTask task = VillagerTrade.getRegistry().getConfirmationTasks().get(player.getUniqueId());

        if (task == null || !task.execute()) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.confirm.no-active");
        }
    }
}
