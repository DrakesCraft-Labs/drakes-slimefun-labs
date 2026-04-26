package com.github.drakescraft_labs.bump.core.services;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.bump.utils.ConfigUtils;

import com.github.drakescraft_labs.bump.core.config.BumpPluginYaml;

/**
 * This service will update config when there is a new version.
 *
 * @author ybw0014
 */
public final class ConfigUpdateService {
    private static final int CURRENT_VERSION = 4;

    public ConfigUpdateService(@Nonnull BumpPluginYaml config) {
        if (config.getInt("version", 1) < CURRENT_VERSION) {
            ConfigUtils.addMissingOptions(config);
            config.set("version", CURRENT_VERSION);
            config.save();
        }
    }
}
