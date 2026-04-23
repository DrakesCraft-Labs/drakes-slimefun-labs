package me.schntgaispock.wildernether.integration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import io.github.schntgaispock.gastronomicon.api.food.FoodEffect;
import io.github.schntgaispock.gastronomicon.api.food.GastroFoodBuilder;
import io.github.schntgaispock.gastronomicon.api.items.FoodItemStack;
import io.github.schntgaispock.gastronomicon.api.items.FoodItemStackBuilder;
import io.github.schntgaispock.gastronomicon.api.recipes.GastroRecipe.RecipeShape;
import io.github.schntgaispock.gastronomicon.core.slimefun.GastroStacks;
import io.github.schntgaispock.gastronomicon.core.slimefun.recipes.GastroRecipeType;
import io.github.schntgaispock.gastronomicon.util.item.HeadTextures;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import me.schntgaispock.wildernether.Wildernether;
import me.schntgaispock.wildernether.slimefun.WildernetherStacks;

@UtilityClass
public class GastronomiconSetup {
    
    public static void setup() {

        final FoodItemStack CRIMSON_SALAD = new FoodItemStackBuilder()
            .id("WN_CRIMSON_SALAD")
            .name("Crimson Salad")
            .texture(HeadTextures.SALAD)
            .hunger(8)
            .effects(FoodEffect.positivePotionEffect(PotionEffectType.FIRE_RESISTANCE, 30))
            .build();

        new GastroFoodBuilder()
            .type(GastroRecipeType.CULINARY_WORKBENCH)
            .item(CRIMSON_SALAD)
            .ingredients(
                GastroStacks.KETCHUP,
                WildernetherStacks.SHROOMLIGHT_SPORES,
                WildernetherStacks.BLAZESPROUT,
                WildernetherStacks.RED_CARROT,
                WildernetherStacks.SMOLDERING_HERBS)
            .shape(RecipeShape.SHAPELESS)
            .tools(GastroStacks.KITCHEN_KNIFE)
            .container(new ItemStack(Material.BOWL))
            .register(Wildernether.getInstance()); 


        final FoodItemStack WARPED_CACTUS_ICE_CREAM = new FoodItemStackBuilder()
            .id("WN_WARPED_CACTUS_ICE_CREAM")
            .name("Warped Cactus Ice Cream")
            .texture(HeadTextures.ICE_CREAM_GREEN)
            .hunger(3, 0.75)
            .effects(FoodEffect.positivePotionEffect(PotionEffectType.CONFUSION, 5))
            .build();

        new GastroFoodBuilder()
            .type(GastroRecipeType.REFRIGERATOR)
            .item(WARPED_CACTUS_ICE_CREAM)
            .amount(2)
            .ingredients(new ItemStack(Material.MILK_BUCKET), SlimefunItems.HEAVY_CREAM, new ItemStack(Material.SUGAR), WildernetherStacks.CACTUS_JUICE)
            .register(Wildernether.getInstance()); 

    }
}
