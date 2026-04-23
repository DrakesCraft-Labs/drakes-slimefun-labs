package io.github.sefiraat.simplestorage.items.materials;

import io.github.sefiraat.simplestorage.SimpleStorage;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class GemAngelite extends AbstractGem {

    private final NamespacedKey key;

    public GemAngelite(SimpleStorage plugin, ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super("Angelite Crystal", category, item, recipeType, recipe, 5);
        this.key = new NamespacedKey(SimpleStorage.inst(), "angelite_crystal");
        register();
        register(plugin);
    }

    @Override
    public int getDefaultSupply(@Nonnull World.Environment environment, @Nonnull Biome biome) {
        if (environment == World.Environment.NORMAL) {
            if (biome == Biome.SNOWY_BEACH || biome == Biome.SNOWY_PLAINS || biome == Biome.SNOWY_TAIGA || biome == Biome.COLD_OCEAN || biome == Biome.DEEP_COLD_OCEAN) {
                return 25;
            } else if (biome == Biome.ICE_SPIKES || biome == Biome.FROZEN_OCEAN || biome == Biome.FROZEN_RIVER || biome == Biome.DEEP_FROZEN_OCEAN) {
                return 40;
            } else {
                return 5;
            }
        } else if (environment == World.Environment.NETHER) {
            return 0;
        } else {
            return 15;
        }
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

}
