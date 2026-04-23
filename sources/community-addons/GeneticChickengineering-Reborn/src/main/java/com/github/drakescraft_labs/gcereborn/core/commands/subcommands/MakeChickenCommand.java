package com.github.drakescraft_labs.gcereborn.core.commands.subcommands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.guizhanss.guizhanlib.minecraft.commands.AbstractCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.commands.AbstractSubCommand;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import net.guizhanss.guizhanlib.minecraft.utils.InventoryUtil;

public final class MakeChickenCommand extends AbstractSubCommand implements DnaCompletion {

    public MakeChickenCommand(@Nonnull AbstractCommand parent) {
        super(parent, "makechicken", "<DNA> [baby]");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "no-permission");
            return;
        }
        if (!(sender instanceof Player p)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "no-console");
            return;
        }

        String notation = args[0];
        if (!DNA.isValidSequence(notation)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "invalid-dna-notation", notation);
            return;
        }
        boolean isBaby = true;
        if (args.length == 2) {
            isBaby = Boolean.parseBoolean(args[1]);
        }

        DNA dna = new DNA(notation.toCharArray());
        ItemStack chicken = ChickenUtils.fromDNA(dna, isBaby);
        InventoryUtil.push(p, chicken);
    }

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTab(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("true", "false");
        }
        return tabComplete(sender, args, 0);
    }
}
