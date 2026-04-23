package com.github.drakescraft_labs.villagertrade.core.commands.subcommands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.drakescraft_labs.villagertrade.VillagerTrade;
import com.github.drakescraft_labs.villagertrade.api.trades.TradeConfiguration;
import com.github.drakescraft_labs.villagertrade.implementation.menu.TradeMenu;
import com.github.drakescraft_labs.villagertrade.utils.constants.Keys;

public final class EditCommand extends AdminPlayerCommand implements TradeKeyCompletion {

    public EditCommand() {
        super("edit", false, "<tradeKey>");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        final Player player = (Player) sender;
        String tradeKey = args[1];
        TradeConfiguration tradeConfig = VillagerTrade.getRegistry().getTradeConfigurations().get(tradeKey);
        if (tradeConfig != null) {
            new TradeMenu(player, tradeConfig);
        } else {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.edit.not-found",
                msg -> msg.replace(Keys.VAR_TRADE_KEY, tradeKey));
        }
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return tabComplete(sender, args, 1);
    }
}
