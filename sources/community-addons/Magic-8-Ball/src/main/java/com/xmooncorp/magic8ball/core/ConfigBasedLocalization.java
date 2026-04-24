package com.xmooncorp.magic8ball.core;

import dev.drake.dough.config.Config;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigBasedLocalization {

    private final Config config;
    private final String name;
    private final String root; // path to get to the root entry of that language in the config

    public ConfigBasedLocalization (@Nonnull String defaultLocalization, @Nonnull Config config) {
        this.config = config;
        String lang = config.getString("options.lang");
        this.name = Objects.requireNonNullElse(lang, defaultLocalization);
        this.root = String.format("languages.%s.", this.name);
    }

    @Nonnull
    public String getString(@Nonnull String path) {
        return Objects.requireNonNullElse(this.config.getString(this.root + path), "Locale Error: NotFound. Path: \"" + path + "\"");
    }

    @Nonnull
    public List<String> getStringList (@Nonnull String path) {
        return Objects.requireNonNullElse(this.config.getStringList(this.root + path), Arrays.stream(new String[] {"Locale Error: NotFound. Path: \"" + path + "\""}).toList());
    }

    @Nonnull
    public String getName () {
        return this.name;
    }
}
