package com.github.drakescraft_labs.galactifun.core.commands;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.drakescraft_labs.galactifun.Galactifun;
import com.github.drakescraft_labs.galactifun.api.worlds.PlanetaryWorld;
import com.github.drakescraft_labs.galactifun.base.items.knowledge.KnowledgeLevel;
import dev.drake.infinitylib.commands.SubCommand;
import com.github.drakescraft_labs.slimefun4.libraries.paperlib.PaperLib;

/**
 * Command to teleport to world spawns
 *
 * @author Seggan
 * @author Mooy1
 */
public final class GalactiportCommand extends SubCommand {

    public GalactiportCommand() {
        super("world", "Teleports you to the spawn of the specified world", true);
    }

    @Override
    public void execute(@Nonnull CommandSender commandSender, @Nonnull String[] strings) {
        if (!(commandSender instanceof Player p) || strings.length != 1) {
            return;
        }

        World world = Bukkit.getWorld(strings[0]);

        if (world == null) {
            p.sendMessage(ChatColor.RED + "Invalid World!");
            return;
        }

        PaperLib.teleportAsync(p, world.getSpawnLocation());

        PlanetaryWorld planetaryWorld = Galactifun.worldManager().getWorld(world);
        if (planetaryWorld != null && KnowledgeLevel.get(p, planetaryWorld) == KnowledgeLevel.NONE) {
            KnowledgeLevel.BASIC.set(p, planetaryWorld);
        }
    }

    @Override
    public void complete(@Nonnull CommandSender commandSender, @Nonnull String[] strings, @Nonnull List<String> worlds) {
        if (strings.length == 1) {
            for (World world : Bukkit.getWorlds()) {
                worlds.add(world.getName());
            }
        }
    }

}
