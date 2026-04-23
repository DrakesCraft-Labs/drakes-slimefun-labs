package com.github.drakescraft_labs.gcereborn.core.commands.subcommands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.commands.AbstractSubCommand;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.core.genetics.Gene;
import net.guizhanss.guizhanlib.minecraft.commands.AbstractCommand;

public final class CalcChanceCommand extends AbstractSubCommand implements DnaCompletion {

    public CalcChanceCommand(@Nonnull AbstractCommand parent) {
        super(parent, "calcchance", "<parent1DNA> <parent2DNA> <childDNA>");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "no-permission");
            return;
        }

        for (int i = 0; i < 3; i++) {
            if (!DNA.isValidSequence(args[i])) {
                GeneticChickengineering.getLocalization().sendMessage(sender, "invalid-dna-notation", args[i]);
                return;
            }
        }

        DNA p1dna = new DNA(args[0].toCharArray());
        DNA p2dna = new DNA(args[1].toCharArray());
        DNA cdna = new DNA(args[2].toCharArray());
        double chanceTotal = 1.0;
        for (int i = 0; i < 6; i++) {
            Gene p1g = p1dna.getGene(i);
            Gene p2g = p2dna.getGene(i);
            Gene cg = cdna.getGene(i);
            double matches = 0.0;
            for (char p1a : p1g.getAlleles()) {
                for (char p2a : p2g.getAlleles()) {
                    if (new Gene(new char[] {p1a, p2a}).getState() == cg.getState()) {
                        matches += 1.0;
                    }
                }
            }
            chanceTotal *= matches * 0.25;
        }
        long readableChance = Math.round(chanceTotal * 100);
        GeneticChickengineering.getLocalization().sendMessage(sender, "chance-result", readableChance, p1dna, p2dna, cdna);
    }

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTab(CommandSender sender, String[] args) {
        return tabComplete(sender, args, 0, 1, 2);
    }
}
