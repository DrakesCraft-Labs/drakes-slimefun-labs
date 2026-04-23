package com.github.drakescraft-labs.gcereborn.core.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft-labs.guizhanlib.minecraft.commands.AbstractCommand;
import com.github.drakescraft-labs.guizhanlib.minecraft.commands.SubCommand;

import lombok.Getter;

@Getter
public abstract class AbstractSubCommand extends SubCommand {

    protected AbstractSubCommand(@Nullable AbstractCommand parent, @Nonnull String name, @Nonnull String usage, SubCommand... subCommands) {
        super(parent, name, (cmd, sender) -> getDescription(name, sender), usage, subCommands);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private static String getDescription(String name, CommandSender sender) {
        return GeneticChickengineering.getLocalization().getString("commands." + name + ".description");
    }

    protected boolean hasPermission(@Nonnull CommandSender sender) {
        return sender.hasPermission("geneticchickengineering.command." + getName());
    }
}
