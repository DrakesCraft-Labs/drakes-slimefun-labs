package me.schntgaispock.wildernether.managers;


import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.schntgaispock.wildernether.Wildernether;
import me.schntgaispock.wildernether.slimefun.WildernetherGroups;
import me.schntgaispock.wildernether.slimefun.WildernetherRecipes;
import me.schntgaispock.wildernether.slimefun.WildernetherStacks;
import me.schntgaispock.wildernether.slimefun.items.BlackstoneStove;
import me.schntgaispock.wildernether.slimefun.items.NetherComposter;
import me.schntgaispock.wildernether.slimefun.items.Scythe;
import me.schntgaispock.wildernether.slimefun.items.WildernetherCuisine;
import me.schntgaispock.wildernether.slimefun.items.WildernetherPlant;

/**
 * Item manager for Wildernether
 * 
 * @author SchnTgaiSpock
 */
public class ItemManager {

    public static SlimefunItem CRIMSON_FRAME, WARPED_FRAME, SOUL_STONE,
        FUNGAL_IRON_SCRAP, FUNGAL_GOLD_SCRAP;

    public static SlimefunItem BLACKSTONE_SCYTHE, SOUL_SCYTHE, NETHER_COMPOSTER, BLACKSTONE_STOVE;

    // Common
    public static SlimefunItem WARPED_CACTUS, BLAZESPROUT, NETHER_TUBERS, CRYSTAL_MUSHROOM,
        SHROOMLIGHT_SPORES, WARPED_ROSE, RED_CARROT;
    // Uncommon
    public static SlimefunItem CRIMSON_DREADLOCKS, TWISTED_BEAN_SPROUT, RED_SPIDER_LILY, WARPED_LOTUS;
    // Rare
    public static SlimefunItem SMOLDERING_HERBS, IMMOLATED_VINES, OCTARINE_NETHERCAP, EMERALD_FIREBLOOM;
    // Secret
    public static SlimefunItem GARDEN_OF_THE_LOST_SOUL, TULIP_OF_PARTINGS, BLOOM_OF_UNHEARD_CRIES,
    BLOSSOM_OF_SOLITUDE, LAMENT_OF_THE_DAMNED;

    // Ingredients
    public static SlimefunItem HOGLIN_SPARE_RIB, HOGLIN_BELLY, HOGLIN_TROTTERS, HOGLIN_BOUILLON, CACTUS_JUICE;
    // Crafted
    public static SlimefunItem WARPED_SALAD, MUSHROOM_SLICES;
    // Oven
    public static SlimefunItem BAKED_NETHER_TUBERS, GLOWING_BEANS;
    // Frying
    public static SlimefunItem NETHER_CHIPS, FRIED_WARPED_FUNGUS, FRIED_CRIMSON_FUNGUS, SPICY_FRIED_LOTUS;
    // Pot
    public static SlimefunItem CRIMSON_STEW, WARPED_STEW, NETHER_HOTPOT, PORK_BONE_SOUP,
        BOILED_NETHER_TUBERS, BOILED_WARPED_CACTUS;
    // Brews
    public static SlimefunItem BREW_OF_FIRE_WARDING, BREW_OF_FARSIGHT, BREW_OF_CELERITY;


    public static void setup() {

        Scythe.setup();


        SlimefunAddon wn = Wildernether.getInstance();

        WildernetherGroups.WILDERNETHER.register(wn);

        // ---------- Materials ----------
        CRIMSON_FRAME = new SlimefunItem(
            WildernetherGroups.MATERIALS,
            WildernetherStacks.CRIMSON_FRAME,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.CRIMSON_FRAME
        );

        WARPED_FRAME = new SlimefunItem(
            WildernetherGroups.MATERIALS,
            WildernetherStacks.WARPED_FRAME,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.WARPED_FRAME
        );

        SOUL_STONE = new SlimefunItem(
            WildernetherGroups.MATERIALS,
            WildernetherStacks.SOUL_STONE,
            RecipeType.ANCIENT_ALTAR,
            WildernetherRecipes.SOUL_STONE
        );

        FUNGAL_IRON_SCRAP = new WildernetherPlant(
            WildernetherGroups.MATERIALS,
            WildernetherStacks.FUNGAL_IRON_SCRAP,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        FUNGAL_GOLD_SCRAP = new WildernetherPlant(
            WildernetherGroups.MATERIALS,
            WildernetherStacks.FUNGAL_GOLD_SCRAP,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        // ---------- Tools ----------
        BLACKSTONE_SCYTHE = new Scythe(
            WildernetherGroups.TOOLS,
            WildernetherStacks.BLACKSTONE_SCYTHE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.BLACKSTONE_SCYTHE
        );

        SOUL_SCYTHE = new Scythe(
            WildernetherGroups.TOOLS,
            WildernetherStacks.SOUL_SCYTHE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.SOUL_SCYTHE
        );

        NETHER_COMPOSTER = new NetherComposter(
            WildernetherGroups.TOOLS,
            WildernetherStacks.NETHER_COMPOSTER,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.NETHER_COMPOSTER
        );

        BLACKSTONE_STOVE = new BlackstoneStove(
            WildernetherGroups.TOOLS,
            WildernetherStacks.BLACKSTONE_STOVE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.BLACKSTONE_STOVE
        );

        // ---------- Plants ----------
        // Common
        WARPED_CACTUS = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.WARPED_CACTUS,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        BLAZESPROUT = new SlimefunItem(
            WildernetherGroups.PLANTS,
            WildernetherStacks.BLAZESPROUT,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        NETHER_TUBERS = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.NETHER_TUBERS,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        CRYSTAL_MUSHROOM = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.CRYSTAL_MUSHROOM,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        SHROOMLIGHT_SPORES = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.SHROOMLIGHT_SPORES,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        WARPED_ROSE = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.WARPED_ROSE,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        RED_CARROT = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.RED_CARROT,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        // Uncommon
        CRIMSON_DREADLOCKS = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.CRIMSON_DREADLOCKS,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        TWISTED_BEAN_SPROUT = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.TWISTED_BEAN_SPROUT,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        RED_SPIDER_LILY = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.RED_SPIDER_LILY,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        WARPED_LOTUS = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.WARPED_LOTUS,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        // Rare
        SMOLDERING_HERBS = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.SMOLDERING_HERBS,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );
        
        IMMOLATED_VINES = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.IMMOLATED_VINES,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );
        
        OCTARINE_NETHERCAP = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.OCTARINE_NETHERCAP,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );
        
        EMERALD_FIREBLOOM = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.EMERALD_FIREBLOOM,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        // Secret 
        GARDEN_OF_THE_LOST_SOUL = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.GARDEN_OF_THE_LOST_SOUL,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        TULIP_OF_PARTINGS = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.TULIP_OF_PARTINGS,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        BLOOM_OF_UNHEARD_CRIES = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.BLOOM_OF_UNHEARD_CRIES,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        BLOSSOM_OF_SOLITUDE = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.BLOSSOM_OF_SOLITUDE,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        LAMENT_OF_THE_DAMNED = new WildernetherPlant(
            WildernetherGroups.PLANTS,
            WildernetherStacks.LAMENT_OF_THE_DAMNED,
            WildernetherRecipes.RecipeTypes.BREAK_NETHER_PLANTS,
            new ItemStack[9]
        );

        // ---------- Cuisine ----------

        // Ingredients
        HOGLIN_SPARE_RIB = new UnplaceableBlock(
            WildernetherGroups.CUISINE,
            WildernetherStacks.HOGLIN_SPARE_RIB,
            WildernetherRecipes.RecipeTypes.KILL_MOB,
            WildernetherRecipes.HOGLIN_SPARE_RIB
        );

        HOGLIN_BELLY = new UnplaceableBlock(
            WildernetherGroups.CUISINE,
            WildernetherStacks.HOGLIN_BELLY,
            WildernetherRecipes.RecipeTypes.KILL_MOB,
            WildernetherRecipes.HOGLIN_BELLY
        );

        HOGLIN_TROTTERS = new UnplaceableBlock(
            WildernetherGroups.CUISINE,
            WildernetherStacks.HOGLIN_TROTTERS,
            WildernetherRecipes.RecipeTypes.KILL_MOB,
            WildernetherRecipes.HOGLIN_TROTTERS
        );

        HOGLIN_BOUILLON = new UnplaceableBlock(
            WildernetherGroups.CUISINE,
            WildernetherStacks.HOGLIN_BOUILLON,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.HOGLIN_BOUILLON_GUIDE
        );
        
        BOILED_WARPED_CACTUS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.BOILED_WARPED_CACTUS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.BOILED_WARPED_CACTUS,
            2,
            2.0
        );

        CACTUS_JUICE = new UnplaceableBlock(
            WildernetherGroups.CUISINE,
            WildernetherStacks.CACTUS_JUICE,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.CACTUS_JUICE
        );

        // Crafted
        WARPED_SALAD = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.WARPED_SALAD,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.WARPED_SALAD,
            8,
            3.0
        );

        MUSHROOM_SLICES = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.MUSHROOM_SLICES,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            WildernetherRecipes.MUSHROOM_SLICES,
            6,
            2.0
        );

        // Oven
        BAKED_NETHER_TUBERS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.BAKED_NETHER_TUBERS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_OVEN,
            WildernetherRecipes.BAKED_NETHER_TUBERS,
            6,
            5.0
        );

        GLOWING_BEANS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.GLOWING_BEANS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_OVEN,
            WildernetherRecipes.GLOWING_BEANS,
            1,
            2.0
        );
        
        // Frying
        NETHER_CHIPS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.NETHER_CHIPS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_FRYING,
            WildernetherRecipes.NETHER_CHIPS,
            3,
            2.0
        );
        
        FRIED_WARPED_FUNGUS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.FRIED_WARPED_FUNGUS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_FRYING,
            WildernetherRecipes.FRIED_WARPED_FUNGUS,
            2,
            2.0
        );
        
        FRIED_CRIMSON_FUNGUS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.FRIED_CRIMSON_FUNGUS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_FRYING,
            WildernetherRecipes.FRIED_CRIMSON_FUNGUS,
            2,
            2.0
        );
        
        SPICY_FRIED_LOTUS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.SPICY_FRIED_LOTUS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_FRYING,
            WildernetherRecipes.SPICY_FRIED_LOTUS,
            7,
            10.0
        );

        // Pot
        CRIMSON_STEW = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.CRIMSON_STEW,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.CRIMSON_STEW,
            10,
            20.0
        );
        
        WARPED_STEW = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.WARPED_STEW,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.WARPED_STEW,
            10,
            20.0
        );
        
        NETHER_HOTPOT = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.NETHER_HOTPOT,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.NETHER_HOTPOT,
            12,
            20.0
        );
        
        PORK_BONE_SOUP = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.PORK_BONE_SOUP,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.PORK_BONE_SOUP,
            17,
            20.0
        );
        
        BOILED_NETHER_TUBERS = new WildernetherCuisine(
            WildernetherGroups.CUISINE,
            WildernetherStacks.BOILED_NETHER_TUBERS,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_POT,
            WildernetherRecipes.BOILED_NETHER_TUBERS,
            6,
            5.0
        );

        // Brews
        BREW_OF_FIRE_WARDING = new SlimefunItem(
            WildernetherGroups.BREWS,
            WildernetherStacks.BREW_OF_FIRE_WARDING,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_BREWING,
            WildernetherRecipes.BREW_OF_FIRE_WARDING_GUIDE
        );
        
        BREW_OF_FARSIGHT = new SlimefunItem(
            WildernetherGroups.BREWS,
            WildernetherStacks.BREW_OF_FARSIGHT,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_BREWING,
            WildernetherRecipes.BREW_OF_FARSIGHT_GUIDE
        );
        
        BREW_OF_CELERITY = new SlimefunItem(
            WildernetherGroups.BREWS,
            WildernetherStacks.BREW_OF_CELERITY,
            WildernetherRecipes.RecipeTypes.BLACKSTONE_STOVE_BREWING,
            WildernetherRecipes.BREW_OF_CELERITY_GUIDE
        );



        // ---------- Registration ----------
        FUNGAL_IRON_SCRAP.setUseableInWorkbench(true);
        FUNGAL_GOLD_SCRAP.setUseableInWorkbench(true);
        CACTUS_JUICE.setUseableInWorkbench(true);
        
        GARDEN_OF_THE_LOST_SOUL.setHidden(true);
        TULIP_OF_PARTINGS.setHidden(true);
        BLOOM_OF_UNHEARD_CRIES.setHidden(true);
        BLOSSOM_OF_SOLITUDE.setHidden(true);
        LAMENT_OF_THE_DAMNED.setHidden(true);

        // ----- Materials -----
        CRIMSON_FRAME.register(wn);
        // This doesn't have any uses atm, so disabling
        // WARPED_FRAME.register(wn);
        SOUL_STONE.register(wn);
        FUNGAL_IRON_SCRAP.register(wn);
        FUNGAL_GOLD_SCRAP.register(wn);

        // ----- Tools -----
        BLACKSTONE_SCYTHE.register(wn);
        SOUL_SCYTHE.register(wn);
        NETHER_COMPOSTER.register(wn);
        BLACKSTONE_STOVE.register(wn);

        // ----- Plants -----
        WARPED_CACTUS.register(wn);
        BLAZESPROUT.register(wn);
        NETHER_TUBERS.register(wn);
        CRYSTAL_MUSHROOM.register(wn);
        SHROOMLIGHT_SPORES.register(wn);
        WARPED_ROSE.register(wn);
        RED_CARROT.register(wn);

        CRIMSON_DREADLOCKS.register(wn);
        TWISTED_BEAN_SPROUT.register(wn);
        RED_SPIDER_LILY.register(wn);
        WARPED_LOTUS.register(wn);

        SMOLDERING_HERBS.register(wn);
        IMMOLATED_VINES.register(wn);
        OCTARINE_NETHERCAP.register(wn);
        EMERALD_FIREBLOOM.register(wn);

        GARDEN_OF_THE_LOST_SOUL.register(wn);
        TULIP_OF_PARTINGS.register(wn);
        BLOOM_OF_UNHEARD_CRIES.register(wn);
        BLOSSOM_OF_SOLITUDE.register(wn);
        LAMENT_OF_THE_DAMNED.register(wn);
        
        // ----- Cuisine -----
        HOGLIN_SPARE_RIB.register(wn);
        HOGLIN_BELLY.register(wn);
        HOGLIN_TROTTERS.register(wn);
        BOILED_WARPED_CACTUS.register(wn);
        HOGLIN_BOUILLON.register(wn);
        CACTUS_JUICE.register(wn);

        WARPED_SALAD.register(wn);
        MUSHROOM_SLICES.register(wn);
        
        BAKED_NETHER_TUBERS.register(wn);
        GLOWING_BEANS.register(wn);

        NETHER_CHIPS.register(wn);
        FRIED_WARPED_FUNGUS.register(wn);
        FRIED_CRIMSON_FUNGUS.register(wn);
        SPICY_FRIED_LOTUS.register(wn);

        CRIMSON_STEW.register(wn);
        WARPED_STEW.register(wn);
        NETHER_HOTPOT.register(wn);
        PORK_BONE_SOUP.register(wn);
        BOILED_NETHER_TUBERS.register(wn);

        // ----- Brews -----
        BREW_OF_FIRE_WARDING.register(wn);
        BREW_OF_FARSIGHT.register(wn);
        BREW_OF_CELERITY.register(wn);
    }
}
