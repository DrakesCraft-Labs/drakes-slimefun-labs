package com.github.drakescraft_labs.slimefuntranslation.core.services;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.slimefuntranslation.SlimefunTranslation;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.PlayerJoinListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.PlayerQuitListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.SearchListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.SlimefunBlockRightClickListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.SlimefunItemLoadListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.SlimefunLanguageChangeListener;
import com.github.drakescraft_labs.slimefuntranslation.implementation.listeners.TranslationsLoadListener;

public final class ListenerService {
    public ListenerService(@Nonnull SlimefunTranslation plugin) {
        new PlayerJoinListener(plugin);
        new PlayerQuitListener(plugin);
        new SearchListener(plugin);
        new SlimefunBlockRightClickListener(plugin);
        new SlimefunItemLoadListener(plugin);
        new SlimefunLanguageChangeListener(plugin);
        new TranslationsLoadListener(plugin);
    }
}
