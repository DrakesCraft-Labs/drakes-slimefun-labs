/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.geo.GEOResource
 *  javax.annotation.Nonnull
 *  org.bukkit.NamespacedKey
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Biome
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.poma123.globalwarming.items;

import com.github.drakescraft_labs.slimefun4.api.geo.GEOResource;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.Items;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CinnabariteResource
implements GEOResource {
    private final NamespacedKey key = new NamespacedKey((Plugin)GlobalWarmingPlugin.getInstance(), "cinnabarite");

    public int getDefaultSupply(World.Environment environment, Biome biome) {
        return ThreadLocalRandom.current().nextInt(2) + 2;
    }

    public NamespacedKey getKey() {
        return this.key;
    }

    public int getMaxDeviation() {
        return 1;
    }

    @Nonnull
    public String getName() {
        return "Cinabrio";
    }

    @Nonnull
    public ItemStack getItem() {
        return Items.CINNABARITE.clone();
    }

    public boolean isObtainableFromGEOMiner() {
        return true;
    }
}

