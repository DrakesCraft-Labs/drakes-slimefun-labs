package com.github.drakescraft_labs.slimefun4.core.services;

import java.io.File;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import com.github.drakescraft_labs.slimefun4.api.SlimefunBranch;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

/**
 * This Class represents the version and branch information for Slimefun.
 * Auto-update functionality has been removed for security and stability reasons.
 */
public class UpdaterService {

    private final Slimefun plugin;
    private final SlimefunBranch branch;

    public UpdaterService(@Nonnull Slimefun plugin, @Nonnull String version, @Nonnull File file) {
        this.plugin = plugin;

        if (version.contains("UNOFFICIAL")) {
            branch = SlimefunBranch.UNOFFICIAL;
        } else if (version.startsWith("Dev - ")) {
            branch = SlimefunBranch.DEVELOPMENT;
        } else if (version.startsWith("RC - ")) {
            branch = SlimefunBranch.STABLE;
        } else {
            branch = SlimefunBranch.UNKNOWN;
        }
    }

    public @Nonnull SlimefunBranch getBranch() {
        return branch;
    }

    public int getBuildNumber() {
        return -1;
    }

    public int getLatestVersion() {
        return -1;
    }

    public boolean isLatestVersion() {
        return true;
    }

    public void start() {
    }

    public boolean isEnabled() {
        return false;
    }

    public void disable() {
    }

    private void printBorder() {
        plugin.getLogger().log(Level.WARNING, "#######################################################");
    }

}
