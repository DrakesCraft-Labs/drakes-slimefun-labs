package com.github.drakescraft-labs.slimefuntranslation.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.drakescraft-labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft-labs.slimefuntranslation.api.events.TranslationsLoadEvent;

public class TranslationsLoadListener implements Listener {
    public TranslationsLoadListener(@Nonnull SlimefunTranslation plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(@Nonnull TranslationsLoadEvent e) {
        SlimefunTranslation.getTranslationService().clearTranslations();
        SlimefunTranslation.getTranslationService().loadTranslations();
    }
}
