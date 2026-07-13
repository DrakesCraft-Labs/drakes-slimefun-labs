package io.github.thebusybiscuit.sensibletoolbox.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.desht.dhutils.DHUtilsException;
import me.desht.dhutils.MiscUtil;
import me.desht.dhutils.commands.AbstractCommand;

public class SoundCommand extends AbstractCommand {

    public SoundCommand() {
        super("stb sound", 1, 3);
        setUsage("/<command> sound <sound-name> [<volume>] [<pitch>]");
        setPermissionNode("stb.commands.sound");
    }

    @Override
    public boolean execute(Plugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            MiscUtil.errorMessage(sender, "This command can't be run from the console.");
            return true;
        }

        try {
            String soundKey = args[0].toLowerCase(Locale.ROOT).replace('_', '.');
            Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(soundKey));
            if (sound == null) {
                throw new IllegalArgumentException("Unknown sound: " + args[0]);
            }
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1.0F;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1.0F;
            ((Player) sender).playSound(((Player) sender).getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            throw new DHUtilsException(e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(Plugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> sounds = Registry.SOUNDS.stream()
                    .map(sound -> sound.getKey().getKey().replace('.', '_').toUpperCase(Locale.ROOT))
                    .collect(Collectors.toList());
            return filterPrefix(sender, sounds, args[0]);
        } else {
            return noCompletions(sender);
        }
    }
}

