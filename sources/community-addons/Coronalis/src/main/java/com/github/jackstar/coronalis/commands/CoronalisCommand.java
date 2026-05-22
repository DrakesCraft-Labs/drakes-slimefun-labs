package com.github.jackstar.coronalis.commands;

import com.github.jackstar.coronalis.Coronalis;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Terminal textual mínimo para flujos que no conviene resolver dentro del GUI.
 */
public class CoronalisCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[Coronalis] Solo jugadores pueden usar este terminal.");
            return true;
        }
        if (args.length == 0 || !"auth".equalsIgnoreCase(args[0])) {
            player.sendMessage("§5[Coronalis] §7Uso: §d/coronalis auth §7tras intentar abrir una consola protegida.");
            return true;
        }
        Coronalis.instance().getAccessManager().promptLastDeniedPassword(player);
        return true;
    }
}
