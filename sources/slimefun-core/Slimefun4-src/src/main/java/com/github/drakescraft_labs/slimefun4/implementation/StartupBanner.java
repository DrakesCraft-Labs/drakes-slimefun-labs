package com.github.drakescraft_labs.slimefun4.implementation;

import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.bukkit.plugin.PluginDescriptionFile;

final class StartupBanner {

    private static final String GREEN = "\u001B[92m";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    private StartupBanner() {}

    static void print(@Nonnull Logger logger, @Nonnull PluginDescriptionFile description) {
        String color = Boolean.getBoolean("drakes.banner.noColor") ? "" : GREEN + BOLD;
        String reset = Boolean.getBoolean("drakes.banner.noColor") ? "" : RESET;

        for (String line : lines(description.getVersion())) {
            logger.info(color + line + reset);
        }
    }

    private static String[] lines(@Nonnull String version) {
        return new String[] {
            "",
            "============================================================",
            "      JACKSTAR  x  DRAKESCRAFT LABS  x  CHAGUI68",
            "============================================================",
            "      DRAKES SLIMEFUN LABS runtime smoke marker",
            "      Slimefun Drake build: " + version,
            "      Repo: https://github.com/DrakesCraft-Labs/drakes-slimefun-labs",
            "      JackStar: https://github.com/JackStar6677-1",
            "      Chagui68: thanks for the porting support",
            "============================================================",
            ""
        };
    }
}
