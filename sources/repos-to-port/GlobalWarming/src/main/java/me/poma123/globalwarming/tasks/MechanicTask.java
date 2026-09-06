/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package me.poma123.globalwarming.tasks;

import me.poma123.globalwarming.GlobalWarmingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class MechanicTask
implements Runnable {
    protected int id;

    public void setID(int id) {
        this.id = id;
    }

    public void schedule(long delay) {
        this.setID(Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)GlobalWarmingPlugin.getInstance(), (Runnable)this, delay));
    }

    public void scheduleRepeating(long delay, long interval) {
        this.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)GlobalWarmingPlugin.getInstance(), (Runnable)this, delay, interval));
    }

    public void scheduleAsyncRepeating(long delay, long interval) {
        this.setID(Bukkit.getScheduler().scheduleAsyncRepeatingTask((Plugin)GlobalWarmingPlugin.getInstance(), (Runnable)this, delay, interval));
    }
}

