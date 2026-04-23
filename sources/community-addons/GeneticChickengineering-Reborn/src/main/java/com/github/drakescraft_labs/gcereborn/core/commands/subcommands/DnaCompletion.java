package com.github.drakescraft_labs.gcereborn.core.commands.subcommands;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import com.github.drakescraft_labs.gcereborn.utils.DnaUtils;

interface DnaCompletion {

    @Nonnull
    @ParametersAreNonnullByDefault
    default List<String> tabComplete(CommandSender sender, String[] args, int... indices) {
        for (int index : indices) {
            if (args.length == index + 1) {
                List<String> result = new LinkedList<>();
                for (String dna : DnaUtils.getPossibleDNA()) {
                    if (dna.startsWith(args[index])) {
                        result.add(dna);
                    }
                }
                return result;
            }
        }
        return List.of();
    }
}
