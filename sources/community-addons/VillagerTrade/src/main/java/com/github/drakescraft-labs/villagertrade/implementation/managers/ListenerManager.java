package com.github.drakescraft-labs.villagertrade.implementation.managers;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.github.drakescraft-labs.villagertrade.VillagerTrade;
import com.github.drakescraft-labs.villagertrade.implementation.listeners.TradeInventoryListener;
import com.github.drakescraft-labs.villagertrade.implementation.listeners.VillagerListener;
import com.github.drakescraft-labs.villagertrade.implementation.listeners.WanderingTraderListener;

public final class ListenerManager {
    private final VillagerTrade plugin;

    public ListenerManager(@Nonnull VillagerTrade plugin) {
        this.plugin = plugin;

        register(new TradeInventoryListener());
        register(new VillagerListener());
        register(new WanderingTraderListener());
    }

    private void register(@Nonnull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
