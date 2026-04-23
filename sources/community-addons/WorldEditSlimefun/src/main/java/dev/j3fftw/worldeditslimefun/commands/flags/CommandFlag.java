package dev.j3fftw.worldeditslimefun.commands.flags;

import co.aikar.commands.BukkitCommandCompletionContext;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public abstract class CommandFlag<T> {
    protected T value;

    public CommandFlag<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public T getValue() {
        return value;
    }

    public abstract void apply(Player player, List<CommandFlag<?>> flags, SlimefunItem sfItem, Block block);
    public abstract boolean canApply(SlimefunItem sfItem);
    public abstract Collection<String> getTabSuggestions(BukkitCommandCompletionContext context);

    public abstract CommandFlag<T> ofValue(String value);
}