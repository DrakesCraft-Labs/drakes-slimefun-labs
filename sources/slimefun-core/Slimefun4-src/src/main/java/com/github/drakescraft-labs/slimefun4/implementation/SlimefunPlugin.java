package com.github.drakescraft-labs.slimefun4.implementation;

import com.github.drakescraft-labs.slimefun4.core.SlimefunRegistry;
import com.github.drakescraft-labs.slimefun4.core.services.LocalizationService;
import com.github.drakescraft-labs.slimefun4.core.services.MinecraftRecipeService;
import dev.drake.dough.protection.ProtectionManager;

/**
 * Compatibility class for legacy addons.
 * Delegates to the new Slimefun class.
 * 
 * @author Drake
 */
public final class SlimefunPlugin {

    private SlimefunPlugin() {}

    public static SlimefunRegistry getRegistry() {
        return Slimefun.instance().getRegistry();
    }

    public static LocalizationService getLocalization() {
        return Slimefun.getLocalization();
    }

    public static MinecraftRecipeService getMinecraftRecipeService() {
        return Slimefun.getMinecraftRecipeService();
    }

    public static ProtectionManager getProtectionManager() {
        return Slimefun.instance().getProtectionManager();
    }
}
