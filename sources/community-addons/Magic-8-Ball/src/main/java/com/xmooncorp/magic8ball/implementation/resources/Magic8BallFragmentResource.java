package com.xmooncorp.magic8ball.implementation.resources;

import com.github.drakescraft-labs.slimefun4.api.geo.GEOResource;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

public class Magic8BallFragmentResource implements GEOResource {

    private final NamespacedKey key;
    private final ItemStack item;

    public Magic8BallFragmentResource(Plugin plugin, ItemStack item, String key) {
        this.key = new NamespacedKey(plugin, key);
        this.item = item;
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return item.clone();
    }

    @Nonnull
    @Override
    public String getName() {
        return "Magic 8 Ball Fragment";
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }

    @Override
    public int getDefaultSupply(@Nonnull World.Environment environment, @Nonnull Biome biome) {
        return switch (environment) {
            case NORMAL, NETHER -> 1;
            case THE_END -> 3;
            default -> 0;
        };
    }

    @Override
    public int getMaxDeviation() {
        return 4;
    }

}
