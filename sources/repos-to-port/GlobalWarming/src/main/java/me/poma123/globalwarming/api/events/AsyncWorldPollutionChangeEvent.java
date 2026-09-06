/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.ParametersAreNonnullByDefault
 *  org.bukkit.World
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.poma123.globalwarming.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncWorldPollutionChangeEvent
extends Event {
    public static final HandlerList handlers = new HandlerList();
    private final World world;
    private final double oldValue;
    private final double newValue;

    @ParametersAreNonnullByDefault
    public AsyncWorldPollutionChangeEvent(World world, double oldValue, double newValue) {
        super(true);
        this.world = world;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Nonnull
    public World getWorld() {
        return this.world;
    }

    @Nonnull
    public double getOldValue() {
        return this.oldValue;
    }

    @Nonnull
    public double getNewValue() {
        return this.newValue;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return AsyncWorldPollutionChangeEvent.getHandlerList();
    }
}

