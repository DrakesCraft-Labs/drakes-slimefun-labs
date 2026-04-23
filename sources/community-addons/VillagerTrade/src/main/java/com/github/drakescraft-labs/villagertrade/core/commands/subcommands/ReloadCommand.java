package com.github.drakescraft-labs.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import com.github.drakescraft-labs.villagertrade.VillagerTrade;
import com.github.drakescraft-labs.villagertrade.core.commands.SubCommand;
import com.github.drakescraft-labs.villagertrade.implementation.menu.TradeListMenu;
import com.github.drakescraft-labs.villagertrade.utils.constants.Permissions;

public final class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super("reload", false);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.ADMIN)) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "no-permission");
            return;
        }

        VillagerTrade.getRegistry().reset();
        VillagerTrade.getLocalization().reloadAll();
        VillagerTrade.getCustomItemService().reload();
        VillagerTrade.getConfigManager().reloadAll();
        TradeListMenu.closeAll();
        VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.reload.success");
    }
}
