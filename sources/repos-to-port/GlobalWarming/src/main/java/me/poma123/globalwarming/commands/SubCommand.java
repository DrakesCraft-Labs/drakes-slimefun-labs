/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.ParametersAreNonnullByDefault
 *  org.bukkit.command.CommandSender
 */
package me.poma123.globalwarming.commands;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.commands.GlobalWarmingCommand;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    protected final GlobalWarmingPlugin plugin;
    protected final GlobalWarmingCommand cmd;
    private final String name;
    private final String description;
    private final boolean hidden;

    @ParametersAreNonnullByDefault
    protected SubCommand(GlobalWarmingPlugin plugin, GlobalWarmingCommand cmd, String name, String description, boolean hidden) {
        this.plugin = plugin;
        this.cmd = cmd;
        this.name = name;
        this.description = description;
        this.hidden = hidden;
    }

    @Nonnull
    public final String getName() {
        return this.name;
    }

    public final boolean isHidden() {
        return this.hidden;
    }

    protected void recordUsage(@Nonnull Map<SubCommand, Integer> commandUsage) {
        commandUsage.merge(this, 1, Integer::sum);
    }

    public abstract void onExecute(@Nonnull CommandSender var1, @Nonnull String[] var2);

    @Nonnull
    protected String getDescription() {
        return this.description;
    }
}

