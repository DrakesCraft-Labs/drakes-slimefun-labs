package com.github.drakescraft_labs.villagertrade.implementation.managers;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.villagertrade.VillagerTrade;
import com.github.drakescraft_labs.villagertrade.core.commands.VtCommand;
import com.github.drakescraft_labs.villagertrade.core.commands.VtTabCompleter;

public final class CommandManager {

    public CommandManager(@Nonnull VillagerTrade plugin) {
        VtCommand vtCommand = new VtCommand();
        plugin.getCommand("villagertrade").setExecutor(vtCommand);
        plugin.getCommand("villagertrade").setTabCompleter(new VtTabCompleter(vtCommand));
    }
}
