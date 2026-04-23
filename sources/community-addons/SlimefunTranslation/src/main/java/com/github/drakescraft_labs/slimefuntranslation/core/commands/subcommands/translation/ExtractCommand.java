package com.github.drakescraft_labs.slimefuntranslation.core.commands.subcommands.translation;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import com.github.drakescraft_labs.guizhanlib.minecraft.commands.AbstractCommand;
import com.github.drakescraft_labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft_labs.slimefuntranslation.core.commands.AbstractSubCommand;
import com.github.drakescraft_labs.slimefuntranslation.utils.constant.Permissions;

public class ExtractCommand extends AbstractSubCommand {

    public ExtractCommand(@Nonnull AbstractCommand parent) {
        super(parent, "extract", (cmd, sender) -> getDescription("translation.extract", sender), "<replace:true/false>");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!Permissions.COMMAND_TRANSLATION_EXTRACT.hasPermission(sender)) {
            MESSAGE_FACTORY.sendMessage(sender, "no-permission");
            return;
        }

        boolean replace = Boolean.parseBoolean(args[0]);
        SlimefunTranslation.getTranslationService().extractTranslations(replace);
        MESSAGE_FACTORY.sendMessage(sender, "commands.translation.extract.success", replace);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public List<String> onTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return List.of("true", "false");
        } else {
            return List.of();
        }
    }
}
