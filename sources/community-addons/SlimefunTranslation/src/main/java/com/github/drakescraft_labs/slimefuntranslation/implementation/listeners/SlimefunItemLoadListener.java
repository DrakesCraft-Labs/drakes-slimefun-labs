package com.github.drakescraft_labs.slimefuntranslation.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.drakescraft_labs.slimefun4.api.events.SlimefunItemRegistryFinalizedEvent;

import com.github.drakescraft_labs.slimefuntranslation.SlimefunTranslation;

public class SlimefunItemLoadListener implements Listener {
    public SlimefunItemLoadListener(@Nonnull SlimefunTranslation plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSlimefunItemLoad(@Nonnull SlimefunItemRegistryFinalizedEvent e) {
        SlimefunTranslation.getTranslationService().clearTranslations();
        SlimefunTranslation.getScheduler().run(() -> SlimefunTranslation.getTranslationService().callLoadEvent());
    }
}
