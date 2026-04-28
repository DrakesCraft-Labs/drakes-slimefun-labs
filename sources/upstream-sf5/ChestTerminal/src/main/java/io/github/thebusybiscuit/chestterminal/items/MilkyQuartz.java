package io.github.thebusybiscuit.chestterminal.items;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.chestterminal.ChestTerminal;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;

/**
 * Milky Quartz is a GEO resource used as a crafting ingredient
 * for ChestTerminal items.
 *
 * @author TheBusyBiscuit
 */
public class MilkyQuartz implements GEOResource {

    private final NamespacedKey key;
    private final ItemStack item;

    public MilkyQuartz(@Nonnull ChestTerminal plugin, @Nonnull ItemStack item) {
        this.key = new NamespacedKey(plugin, "milky_quartz");
        this.item = item;
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getDefaultSupply(@Nonnull Environment environment, @Nonnull Biome biome) {
        switch (environment) {
            case THE_END:
                return 0;
            case NETHER:
                return ThreadLocalRandom.current().nextInt(12);
            default:
                return ThreadLocalRandom.current().nextInt(8);
        }
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public int getMaxDeviation() {
        return 5;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Milky Quartz";
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }
}
