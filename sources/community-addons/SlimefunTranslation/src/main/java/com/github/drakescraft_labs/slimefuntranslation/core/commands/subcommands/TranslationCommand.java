package com.github.drakescraft_labs.slimefuntranslation.core.commands.subcommands;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.guizhanlib.minecraft.commands.AbstractCommand;
import com.github.drakescraft_labs.slimefuntranslation.core.commands.AbstractSubCommand;
import com.github.drakescraft_labs.slimefuntranslation.core.commands.subcommands.translation.ExtractCommand;
import com.github.drakescraft_labs.slimefuntranslation.core.commands.subcommands.translation.GenerateCommand;
import com.github.drakescraft_labs.slimefuntranslation.core.commands.subcommands.translation.ReloadCommand;

public class TranslationCommand extends AbstractSubCommand {
    public TranslationCommand(@Nonnull AbstractCommand parent) {
        super(parent, "translation", (cmd, sender) -> getDescription("translation", sender), "<subcommands>");
        addSubCommand(new ExtractCommand(this));
        addSubCommand(new GenerateCommand(this));
        addSubCommand(new ReloadCommand(this));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        // we have subcommands so this method doesn't need to do anything
    }
}
