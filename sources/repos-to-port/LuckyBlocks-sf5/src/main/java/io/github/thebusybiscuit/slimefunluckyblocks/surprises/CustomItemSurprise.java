package io.github.thebusybiscuit.slimefunluckyblocks.surprises;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A configurable surprise that drops custom items and executes commands.
 *
 * @author TheBusyBiscuit
 */
public class CustomItemSurprise implements Surprise {

    private final String name;
    private final LuckLevel luckLevel;
    private final List<ItemStack> items;
    private final List<String> commands;

    /**
     * Creates a new {@link CustomItemSurprise}.
     *
     * @param name      the name of this surprise
     * @param items     the items to drop
     * @param commands  the commands to execute
     * @param luckLevel the luck level of this surprise
     */
    public CustomItemSurprise(@Nonnull String name, @Nonnull List<ItemStack> items, @Nonnull List<String> commands, @Nonnull LuckLevel luckLevel) {
        this.name = name;
        this.luckLevel = luckLevel;
        this.items = items;
        this.commands = commands;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void activate(@Nonnull Random random, @Nonnull Player p, @Nonnull Location l) {
        if (!items.isEmpty()) {
            for (ItemStack i : items) {
                l.getWorld().dropItemNaturally(l, i);
            }
        }
        if (!commands.isEmpty()) {
            for (String cmd : commands) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), applyPlaceholders(cmd, p, l));
            }
        }
    }

    @Nonnull
    @Override
    public LuckLevel getLuckLevel() {
        return luckLevel;
    }

    @Nonnull
    private String applyPlaceholders(@Nonnull String str, @Nonnull Player p, @Nonnull Location l) {
        return str.replace("{player}", p.getName()).replace("{world}", l.getWorld().getName()).replace("{x}", l.getBlockX() + "").replace("{y}", l.getBlockY() + "").replace("{z}", l.getBlockZ() + "");
    }

}
