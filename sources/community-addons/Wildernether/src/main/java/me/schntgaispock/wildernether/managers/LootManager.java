package me.schntgaispock.wildernether.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.drake.infinitylib.core.AddonConfig;
import lombok.Getter;
import me.schntgaispock.wildernether.Wildernether;
import me.schntgaispock.wildernether.loot.LootTableCollection;
import me.schntgaispock.wildernether.slimefun.WildernetherStacks;

/**
 * Manages the loot for Wildernether
 * 
 * @author SchnTgaiSpock
 */
public class LootManager {
    
    @Getter
    final static LootTableCollection netherPlantHarvest = new LootTableCollection();

    public static void setup() {
        AddonConfig config = Wildernether.getInstance().getConfig();

        netherPlantHarvest
            .setDefaultRates(new double[]{
                config.getDouble("plant-harvest.drop-weights.common", 1000.0),
                config.getDouble("plant-harvest.drop-weights.uncommon", 100.0),
                config.getDouble("plant-harvest.drop-weights.rare", 10.0),
                config.getDouble("plant-harvest.drop-weights.secret", 1.0)
            })
            .addTool("BLACKSTONE_SCYTHE")
            .addTool("SOUL_SCYTHE", new double[]{
                config.getDouble("soul-scythe.weight-multipliers.common", 1.0),
                config.getDouble("soul-scythe.weight-multipliers.uncommon", 1.0),
                config.getDouble("soul-scythe.weight-multipliers.rare", 1.0),
                config.getDouble("soul-scythe.weight-multipliers.secret", 1.0)
            })
            .put(
                Material.NETHER_SPROUTS,
                new ItemStack[][] {
                    {
                        WildernetherStacks.BLAZESPROUT,
                        WildernetherStacks.NETHER_TUBERS,
                        WildernetherStacks.SHROOMLIGHT_SPORES,
                        WildernetherStacks.RED_CARROT
                    }, {
                        WildernetherStacks.TWISTED_BEAN_SPROUT,
                        WildernetherStacks.FUNGAL_IRON_SCRAP
                    }, {}, {
                        WildernetherStacks.GARDEN_OF_THE_LOST_SOUL,
                    }
                }
            ).put(
                Material.WARPED_ROOTS,
                new ItemStack[][] {
                    {
                        WildernetherStacks.WARPED_CACTUS,
                        WildernetherStacks.BLAZESPROUT,
                        WildernetherStacks.NETHER_TUBERS,
                        WildernetherStacks.WARPED_ROSE,
                        WildernetherStacks.RED_CARROT
                    }, {
                        WildernetherStacks.WARPED_LOTUS,
                        WildernetherStacks.FUNGAL_IRON_SCRAP
                    }, {
                        WildernetherStacks.EMERALD_FIREBLOOM
                    }, {
                        WildernetherStacks.TULIP_OF_PARTINGS
                    }
                }
            ).put(
                Material.WARPED_FUNGUS,
                new ItemStack[][] {
                    {
                        WildernetherStacks.WARPED_CACTUS,
                        WildernetherStacks.CRYSTAL_MUSHROOM,
                        WildernetherStacks.WARPED_ROSE
                    }, {
                        WildernetherStacks.WARPED_LOTUS,
                        WildernetherStacks.FUNGAL_IRON_SCRAP
                    }, {
                        WildernetherStacks.OCTARINE_NETHERCAP
                    }, {
                        WildernetherStacks.TULIP_OF_PARTINGS
                    }
                }
            ).put(
                Material.TWISTING_VINES,
                new ItemStack[][] {
                    {
                        WildernetherStacks.WARPED_CACTUS
                    }, {
                        WildernetherStacks.TWISTED_BEAN_SPROUT
                    }, {
                        WildernetherStacks.OCTARINE_NETHERCAP,
                        WildernetherStacks.EMERALD_FIREBLOOM
                    }, {
                        WildernetherStacks.BLOOM_OF_UNHEARD_CRIES
                    }
                }
            ).put(
                Material.CRIMSON_ROOTS,
                new ItemStack[][] {
                    {
                        WildernetherStacks.BLAZESPROUT,
                        WildernetherStacks.NETHER_TUBERS,
                        WildernetherStacks.RED_CARROT
                    }, {
                        WildernetherStacks.RED_SPIDER_LILY,
                        WildernetherStacks.FUNGAL_GOLD_SCRAP
                    }, {
                        WildernetherStacks.SMOLDERING_HERBS
                    }, {
                        WildernetherStacks.BLOSSOM_OF_SOLITUDE,
                    }
                }
            ).put(
                Material.CRIMSON_FUNGUS,
                new ItemStack[][] {
                    {
                        WildernetherStacks.CRYSTAL_MUSHROOM
                    }, {
                        WildernetherStacks.CRIMSON_DREADLOCKS,
                        WildernetherStacks.RED_SPIDER_LILY,
                        WildernetherStacks.FUNGAL_GOLD_SCRAP
                    }, {
                        WildernetherStacks.IMMOLATED_VINES
                    }, {
                        WildernetherStacks.GARDEN_OF_THE_LOST_SOUL
                    }
                }
            ).put(
                Material.WEEPING_VINES,
                new ItemStack[][] {
                    {
                        WildernetherStacks.SHROOMLIGHT_SPORES
                    }, {
                        WildernetherStacks.CRIMSON_DREADLOCKS,
                        WildernetherStacks.RED_SPIDER_LILY
                    }, {
                        WildernetherStacks.SMOLDERING_HERBS,
                        WildernetherStacks.IMMOLATED_VINES
                    }, {
                        WildernetherStacks.LAMENT_OF_THE_DAMNED
                    }
                }
            ).put(
                Material.SHROOMLIGHT,
                new ItemStack[][] {
                    {
                        WildernetherStacks.SHROOMLIGHT_SPORES
                    }, {}, {}, {
                        WildernetherStacks.BLOOM_OF_UNHEARD_CRIES
                    }
                }
            );
    }

}
