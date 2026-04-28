package dev.j3fftw.litexpansion.armor;

import dev.j3fftw.litexpansion.Items;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets.SolarHelmet;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Represents the {@link AdvancedSolarHelmet} class.
 *
 * @author j3fftw
 */
public class AdvancedSolarHelmet extends SolarHelmet {

    @Nonnull
    private final Type type;

    public AdvancedSolarHelmet(Type type) {
        super(Items.LITEXPANSION, type.getItem(), RecipeType.ENHANCED_CRAFTING_TABLE, type.getRecipe(),
            type.getGenerationRate()
        );
        this.type = type;
        Items.ADVANCED_SOLAR_HELMET.item().addEnchantment(Enchantment.UNBREAKING, 1);
        Items.CARBONADO_SOLAR_HELMET.item().addEnchantment(Enchantment.UNBREAKING, 2);
        Items.ENERGIZED_SOLAR_HELMET.item().addEnchantment(Enchantment.UNBREAKING, 3);
        Items.ADVANCEDLX_SOLAR_HELMET.item().addUnsafeEnchantment(Enchantment.UNBREAKING, 4);
        Items.HYBRID_SOLAR_HELMET.item().addUnsafeEnchantment(Enchantment.UNBREAKING, 5);
        Items.ULTIMATE_SOLAR_HELMET.item().addUnsafeEnchantment(Enchantment.UNBREAKING, 6);
    }

    public enum Type {

        ADVANCED(Items.ADVANCED_SOLAR_HELMET, 5, new ItemStack[] {
            null, SlimefunItems.SOLAR_GENERATOR.item(), null,
            Items.IRON_PLATE.item(), SlimefunItems.SOLAR_HELMET.item(), Items.IRON_PLATE.item(),
            Items.IRON_PLATE.item(), null, Items.IRON_PLATE.item()
        }),

        CARBONADO(Items.CARBONADO_SOLAR_HELMET, 10, new ItemStack[] {
            null, SlimefunItems.SOLAR_GENERATOR_2.item(), null,
            Items.COPPER_PLATE.item(), Items.ADVANCED_SOLAR_HELMET.item(), Items.COPPER_PLATE.item(),
            Items.COPPER_PLATE.item(), null, Items.COPPER_PLATE.item()
        }),
        ENERGIZED(Items.ENERGIZED_SOLAR_HELMET, 20, new ItemStack[] {
            null, SlimefunItems.SOLAR_GENERATOR_3.item(), null,
            Items.GOLD_PLATE.item(), Items.CARBONADO_SOLAR_HELMET.item(), Items.GOLD_PLATE.item(),
            Items.GOLD_PLATE.item(), null, Items.GOLD_PLATE.item()
        }),
        ADVANCEDLX(Items.ADVANCEDLX_SOLAR_HELMET, 50, new ItemStack[] {
            null, SlimefunItems.SOLAR_GENERATOR_4.item(), null,
            Items.DIAMOND_PLATE.item(), Items.ENERGIZED_SOLAR_HELMET.item(), Items.DIAMOND_PLATE.item(),
            Items.DIAMOND_PLATE.item(), null, Items.DIAMOND_PLATE.item()
        }),
        HYBRID(Items.HYBRID_SOLAR_HELMET, 100, new ItemStack[] {
            null, Items.ADVANCED_SOLAR_PANEL.item(), null,
            Items.THORIUM_PLATE.item(), Items.ADVANCEDLX_SOLAR_HELMET.item(), Items.THORIUM_PLATE.item(),
            Items.THORIUM_PLATE.item(), null, Items.THORIUM_PLATE.item()
        }),
        ULTIMATE(Items.ULTIMATE_SOLAR_HELMET, 250, new ItemStack[] {
            null, Items.ULTIMATE_SOLAR_PANEL.item(), null,
            Items.IRIDIUM_PLATE.item(), Items.HYBRID_SOLAR_HELMET.item(), Items.IRIDIUM_PLATE.item(),
            Items.IRIDIUM_PLATE.item(), null, Items.IRIDIUM_PLATE.item()
        });

        @Nonnull
        private final SlimefunItemStack item;
        private final int generationRate;

        @Nonnull
        private final ItemStack[] recipe;

        Type(@Nonnull SlimefunItemStack item, int generationRate, @Nonnull ItemStack[] recipe) {
            this.item = item;
            this.generationRate = generationRate;
            this.recipe = recipe;
        }

        @Nonnull
        public SlimefunItemStack getItem() {
            return item;
        }

        public int getGenerationRate() {
            return generationRate;
        }

        @Nonnull
        public ItemStack[] getRecipe() {
            return recipe;
        }
    }
}
