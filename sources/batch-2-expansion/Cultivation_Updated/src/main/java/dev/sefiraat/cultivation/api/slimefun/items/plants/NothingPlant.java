package dev.sefiraat.cultivation.api.slimefun.items.plants;

import dev.sefiraat.cultivation.api.slimefun.plant.Growth;
import dev.sefiraat.cultivation.api.slimefun.plant.PlantTheme;
import dev.sefiraat.cultivation.implementation.utils.Keys;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A plant that does nothing by itself. Used for breeding purposes only.
 */
public class NothingPlant extends CultivationPlant {

    @ParametersAreNonnullByDefault
    public NothingPlant(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Growth growth) {
        super(item, recipeType, recipe, growth);
    }

    @ParametersAreNonnullByDefault
    public NothingPlant(SlimefunItemStack item, Growth growth) {
        super(item, growth);
    }

    @Override
    public void updateGrowthStage(@Nonnull Block block, int growthStage) {
        // todo Fuck numbers
        if (growthStage == 0) {
            PlantTheme theme = growth.getTheme();
            if (theme != null) {
                // Use native Bukkit API instead of PlayerHead.setSkin() which doesn't work in
                // 1.20.6
                org.bukkit.block.Skull skull = (org.bukkit.block.Skull) block.getState();
                org.bukkit.profile.PlayerProfile profile = org.bukkit.Bukkit
                        .createPlayerProfile(java.util.UUID.randomUUID());
                org.bukkit.profile.PlayerTextures textures = profile.getTextures();

                try {
                    // Convert hash to texture URL
                    String hash = theme.getSeed().getHash();
                    java.net.URL url = new java.net.URL("http://textures.minecraft.net/texture/" + hash);
                    textures.setSkin(url);
                    profile.setTextures(textures);
                    skull.setOwnerProfile(profile);
                    skull.update(true, false);
                } catch (java.net.MalformedURLException e) {
                    e.printStackTrace();
                }
                BlockStorage.addBlockInfo(block, Keys.FLORA_GROWTH_STAGE, String.valueOf(growthStage));
                growthDisplay(block.getLocation());
            }
        } else if (growthStage == 1) {
            addDisplayPlant(block.getLocation());
            block.setType(Material.AIR);
        }
        BlockStorage.addBlockInfo(block, Keys.FLORA_GROWTH_STAGE, String.valueOf(growthStage));
    }

    @Override
    protected boolean validateFlora() {
        return true;
    }
}
