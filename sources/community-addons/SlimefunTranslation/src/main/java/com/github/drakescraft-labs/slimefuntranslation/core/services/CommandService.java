package com.github.drakescraft-labs.slimefuntranslation.core.services;

import com.github.drakescraft-labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft-labs.slimefuntranslation.core.commands.MainCommand;

public final class CommandService {
    public CommandService(SlimefunTranslation plugin) {
        new MainCommand(plugin.getCommand("sftranslation")).register();
    }
}
