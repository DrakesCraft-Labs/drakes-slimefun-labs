/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.libraries.dough.common.ChatColors
 *  javax.annotation.Nonnull
 *  javax.annotation.ParametersAreNonnullByDefault
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.poma123.globalwarming.commands.subcommands;

import com.github.drakescraft_labs.slimefun4.libraries.dough.common.ChatColors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.commands.GlobalWarmingCommand;
import me.poma123.globalwarming.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SilenciarCommand
extends SubCommand {
    @ParametersAreNonnullByDefault
    public SilenciarCommand(GlobalWarmingPlugin plugin, GlobalWarmingCommand cmd) {
        super(plugin, cmd, "silenciar", "Activa o desactiva los mensajes del clima para ti", false);
    }

    @Override
    public void onExecute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColors.color((String)"&cEste comando solo lo puede usar un jugador."));
            return;
        }
        Player p = (Player)sender;
        boolean silenciado = GlobalWarmingPlugin.getSilenciados().alternar(p);
        if (silenciado) {
            p.sendMessage(ChatColors.color((String)"&7Ya no recibiras mensajes del clima. Usa &e/globalwarming silenciar &7para volver a activarlos."));
        } else {
            p.sendMessage(ChatColors.color((String)"&aVuelves a recibir los mensajes del clima."));
        }
    }
}

