package com.github.drakescraft_labs.gcereborn.core.commands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.commands.subcommands.CalcChanceCommand;
import com.github.drakescraft_labs.gcereborn.core.commands.subcommands.MakeChickenCommand;
import com.github.drakescraft_labs.guizhanlib.minecraft.commands.BaseCommand;

import lombok.Getter;

@Getter
public final class GCECommand extends BaseCommand {

    @ParametersAreNonnullByDefault
    public GCECommand(PluginCommand command) {
        super(command, (cmd, sender) -> "", "<subcommand>");

        var config = GeneticChickengineering.getConfigService();
        if (config.isSubCommandEnabled("makechicken")) {
            addSubCommand(new MakeChickenCommand(this));
        }
        if (config.isSubCommandEnabled("calcchance")) {
            addSubCommand(new CalcChanceCommand(this));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        // does nothing, handled by subcommands
    }
}
