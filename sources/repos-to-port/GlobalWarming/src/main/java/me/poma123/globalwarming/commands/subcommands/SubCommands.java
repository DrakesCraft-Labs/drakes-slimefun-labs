/*
 * Decompiled with CFR 0.152.
 */
package me.poma123.globalwarming.commands.subcommands;

import java.util.Collection;
import java.util.LinkedList;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.commands.GlobalWarmingCommand;
import me.poma123.globalwarming.commands.SubCommand;
import me.poma123.globalwarming.commands.subcommands.PollutionCommand;
import me.poma123.globalwarming.commands.subcommands.SilenciarCommand;

public class SubCommands {
    private SubCommands() {
    }

    public static Collection<SubCommand> getAllCommands(GlobalWarmingCommand cmd) {
        GlobalWarmingPlugin plugin = cmd.getPlugin();
        LinkedList<SubCommand> commands = new LinkedList<SubCommand>();
        commands.add(new PollutionCommand(plugin, cmd));
        commands.add(new SilenciarCommand(plugin, cmd));
        return commands;
    }
}

