package me.schntgaispock.wildernether.slimefun;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder;
import me.schntgaispock.wildernether.util.ItemUtil;
import me.schntgaispock.wildernether.util.Theme;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WildernetherStacks {

    // Guide Use Only
    public static final CustomItemStack GUIDE_WILDERNETHER, GUIDE_MATERIALS, GUIDE_PLANTS, GUIDE_CUISINE, 
        GUIDE_TOOLS, GUIDE_BREWS, GUIDE_RECIPE_HOGLIN_BOUILLON;
    
    public static final CustomItemStack RECIPE_BREAK_NETHER_PLANTS, RECIPE_BLACKSTONE_STOVE_OVEN,
        RECIPE_BLACKSTONE_STOVE_FRYING, RECIPE_BLACKSTONE_STOVE_POT, RECIPE_BLACKSTONE_STOVE_BREWING,
        RECIPE_KILL_MOB, RECIPE_HOGLIN;

    // Utility
    public static final ItemStack WATER_BOTTLE;

    // Materials
    public static final SlimefunItemStack CRIMSON_FRAME, WARPED_FRAME, SOUL_STONE;
    public static final SlimefunItemStack FUNGAL_IRON_SCRAP, FUNGAL_GOLD_SCRAP;

    // Tools
    public static final SlimefunItemStack BLACKSTONE_SCYTHE, SOUL_SCYTHE, NETHER_COMPOSTER, BLACKSTONE_STOVE;

    // Common
    public static final SlimefunItemStack WARPED_CACTUS, BLAZESPROUT, NETHER_TUBERS, CRYSTAL_MUSHROOM,
        SHROOMLIGHT_SPORES, WARPED_ROSE, RED_CARROT;
    // Uncommon
    public static final SlimefunItemStack CRIMSON_DREADLOCKS, TWISTED_BEAN_SPROUT, RED_SPIDER_LILY, WARPED_LOTUS;
    // Rare
    public static final SlimefunItemStack SMOLDERING_HERBS, IMMOLATED_VINES, OCTARINE_NETHERCAP, EMERALD_FIREBLOOM;
    // Secret
    public static final SlimefunItemStack GARDEN_OF_THE_LOST_SOUL, TULIP_OF_PARTINGS, BLOOM_OF_UNHEARD_CRIES,
    BLOSSOM_OF_SOLITUDE, LAMENT_OF_THE_DAMNED;

    // Ingredients
    public static final SlimefunItemStack HOGLIN_SPARE_RIB, HOGLIN_BELLY, HOGLIN_TROTTERS, HOGLIN_BOUILLON, CACTUS_JUICE;
    // Crafted
    public static final SlimefunItemStack WARPED_SALAD, MUSHROOM_SLICES;
    // Oven
    public static final SlimefunItemStack BAKED_NETHER_TUBERS, GLOWING_BEANS;
    // Frying
    public static final SlimefunItemStack NETHER_CHIPS, FRIED_WARPED_FUNGUS, FRIED_CRIMSON_FUNGUS, SPICY_FRIED_LOTUS;
    // Pot
    public static final SlimefunItemStack CRIMSON_STEW, WARPED_STEW, NETHER_HOTPOT, PORK_BONE_SOUP,
        BOILED_NETHER_TUBERS, BOILED_WARPED_CACTUS;
    // Brews
    public static final SlimefunItemStack BREW_OF_FIRE_WARDING, BREW_OF_FARSIGHT, BREW_OF_CELERITY;

    static {
        // ---------- Guide Items ----------
        GUIDE_WILDERNETHER = new CustomItemStack(
            Material.WARPED_ROOTS,
            Theme.WARPED.getColor() + "Wildernether"
        );

        GUIDE_MATERIALS = new CustomItemStack(
            Material.MAGMA_CREAM,
            Theme.CRIMSON.getColor() + "Materials"
        );

        GUIDE_TOOLS = new CustomItemStack(
            Material.STONE_HOE,
            Theme.BLACKSTONE.getColor() + "Tools"
        );

        GUIDE_PLANTS = new CustomItemStack(
            Material.NETHER_SPROUTS,
            Theme.WARPED.getColor() + "Plants"
        );
        
        GUIDE_CUISINE = new CustomItemStack(
            Material.COOKED_PORKCHOP,
            Theme.CUISINE.getColor() + "Cuisine"
        );
        
        GUIDE_BREWS = new CustomItemStack(
            Material.GLASS_BOTTLE,
            Theme.BREW.getColor() + "Brews"
        );

        GUIDE_RECIPE_HOGLIN_BOUILLON = new CustomItemStack(
            Material.PORKCHOP,
            Theme.CUISINE.getColor() + "Any Hoglin meat"
        );

        RECIPE_BREAK_NETHER_PLANTS = new CustomItemStack(
            Material.NETHER_SPROUTS,
            "&bBreak Nether Plants",
            "&7This resource is obtained by harvesting",
            "&7(breaking) plants in the nether with a",
            "&7scythe. The available plants are Nether Sprouts,",
            "&7Warped Roots, Warped Fungus, Twisting Vines,",
            "&7Crimson Root, Crimson Fungus, Weeping Vines,",
            "&7and Shroomlight"
        );

        RECIPE_BLACKSTONE_STOVE_OVEN = new CustomItemStack(
            Material.BLAST_FURNACE,
            "&bBlackstone Stove (Oven)",            
            "&7This recipe is crafted using the Blackstone",
            "&7Stove's oven mode. This is the default."
        );

        RECIPE_BLACKSTONE_STOVE_FRYING = new CustomItemStack(
            Material.BLAST_FURNACE,
            "&bBlackstone Stove (Frying)",
            "&7This recipe is crafted using the Blackstone",
            "&7Stove's frying mode. Place a Heavy Weighted",
            "&7Pressure Plate (Iron Pressure Plate) on top",
            "&7of the stove to use this mode."
        );

        RECIPE_BLACKSTONE_STOVE_POT = new CustomItemStack(
            Material.BLAST_FURNACE,
            "&bBlackstone Stove (Pot)",
            "&7This recipe is crafted using the Blackstone",
            "&7Stove's pot mode. Place a Cauldron on top",
            "&7of the stove to use this mode."
        );

        RECIPE_BLACKSTONE_STOVE_BREWING = new CustomItemStack(
            Material.BLAST_FURNACE,
            "&bBlackstone Stove (Brewing)",
            "&7This recipe is crafted using the Blackstone",
            "&7Stove's brewing mode. Place a Light Weighted",
            "&7Pressure Plate (Gold Pressure Plate) on top",
            "&7of the stove to use this mode. Water Bottles",
            "&7can be substituted with Cactus Juice."
        );

        RECIPE_KILL_MOB = new CustomItemStack(
            Material.GOLDEN_SWORD,
            "&bHunting",
            "&7This item is obtained by killing the",
            "&7Specified mob",
            "&7Pressure Plate (Gold Pressure Plate) on top",
            "&7of the stove to use this mode. Water Bottles",
            "&7can be substituted with Cactus Juice."
        );

        RECIPE_HOGLIN = new CustomItemStack(
            Material.HOGLIN_SPAWN_EGG,
            "&fHoglin"
        );

        WATER_BOTTLE = ItemUtil.getWaterBottle();

        // ---------- Materials ----------
        CRIMSON_FRAME = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "CRIMSON_FRAME",
            Material.CRIMSON_FENCE_GATE,
            "Crimson Frame",
            "",
            "A sturdy frame made from the stem of",
            "a large Crimson Fungus"
        );

        WARPED_FRAME = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED,
            "WARPED_FRAME",
            Material.WARPED_FENCE_GATE,
            "Warped Frame",
            "",
            "A sturdy frame made from the stem of",
            "a large Warped Fungus"
        );

        SOUL_STONE = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "SOUL_STONE",
            Material.LODESTONE,
            Theme.SOUL.getColor() + "Soul Stone",
            "",
            "You can hear strange whisperings coming",
            "from deep within"
        );

        FUNGAL_IRON_SCRAP = ItemUtil.formattedSlimefunItemStack(
            Theme.NEUTRAL,
            "FUNGAL_IRON_SCRAP",
            Material.IRON_NUGGET,
            Theme.NEUTRAL.getColor() + "Fungal Iron Scrap",
            "",
            "A piece of scrap iron overgrown",
            "with fungus",
            "",
            "&7&oUsable in Workbench"
        );

        FUNGAL_GOLD_SCRAP = ItemUtil.formattedSlimefunItemStack(
            Theme.NEUTRAL,
            "FUNGAL_GOLD_SCRAP",
            Material.GOLD_NUGGET,
            Theme.NEUTRAL.getColor() + "Fungal Gold Scrap",
            "",
            "A piece of scrap gold overgrown",
            "with fungus",
            "",
            "&7&oUsable in Workbench"
        );
        
        // ---------- Tools ----------
        BLACKSTONE_SCYTHE = ItemUtil.formattedSlimefunItemStack(
            Theme.BLACKSTONE,
            "BLACKSTONE_SCYTHE",
            Material.STONE_HOE,
            Theme.BLACKSTONE.getColor() + "Blackstone Scythe",
            "",
            "Its extra-sharp blade makes it perfect",
            "for cutting the tough plants that grow",
            "in the nether"
        );
        
        SOUL_SCYTHE = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "SOUL_SCYTHE",
            Material.NETHERITE_HOE,
            Theme.SOUL.getColor() + "Soul Scythe",
            "",
            "Its blade quivers with soft whispers.",
            "If you use this to harvest plants in the",
            "nether, you might discover plants with",
            "secrets to tell!"
        );

        NETHER_COMPOSTER = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "NETHER_COMPOSTER",
            Material.COMPOSTER,
            "Nether Composter",
            "",
            "Crafted with the wood of the underworld,",
            "it is suitable for breaking down even",
            "the hardiest of plants"
        );

        BLACKSTONE_STOVE = ItemUtil.formattedSlimefunItemStack(
            Theme.BLACKSTONE,
            "BLACKSTONE_STOVE",
            Material.BLAST_FURNACE,
            Theme.BLACKSTONE.getColor() + "Blackstone Stove",
            "",
            "Using netherrack, it is able to keep",
            "itself lit forever!"
        );

        // ---------- Plants ----------
        WARPED_CACTUS = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED,
            "WARPED_CACTUS",
            Material.CACTUS,
            "Warped Cactus",
            "",
            "A tiny, twisted cactus growing from the",
            "hypha of a warped fungus. Be careful not",
            "to step on it!"
        );

        BLAZESPROUT = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "BLAZESPROUT",
            Material.RED_DYE,
            "Blazesprout",
            "",
            "The hypha of a nether fungus has",
            "absorbed some powder from a blaze,",
            "leading to the formation of a fiery",
            "blister.",
            "",
            "&7&oUse in the Nether Composter"
        );

        NETHER_TUBERS = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED,
            "NETHER_TUBERS",
            Material.POTATO,
            "Nether Tubers",
            "",
            "It's a potato."
        );

        CRYSTAL_MUSHROOM = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "CRYSTAL_MUSHROOM",
            Material.RED_MUSHROOM,
            "Crystal Mushroom",
            "",
            "An excess of quartz in its surroundings",
            "has given rise to this strange mushroom,",
            "growing crystals of quartz in its cap"
        );

        SHROOMLIGHT_SPORES = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "SHROOMLIGHT_SPORES",
            Material.PUMPKIN_SEEDS,
            "Shroomlight Spores",
            "",
            "These spores don't appear to glow, unlike",
            "the bulbs that they dropped from."
        );

        WARPED_ROSE = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED,
            "WARPED_ROSE",
            Material.WITHER_ROSE,
            "Warped Rose",
            "",
            "This rose's thorns are bigger than its",
            "leaves"
        );

        RED_CARROT = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "RED_CARROT",
            Material.CARROT,
            "Red Carrot",
            "",
            "It's red"
        );

        // Uncommon
        CRIMSON_DREADLOCKS = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "CRIMSON_DREADLOCKS",
            Material.WEEPING_VINES,
            "Crimson Dreadlocks",
            "",
            "Each one of these vines is as thick as",
            "your arm..."
        );

        TWISTED_BEAN_SPROUT = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED,
            "TWISTED_BEAN_SPROUT",
            Material.TWISTING_VINES,
            "Twisted Bean Sprout",
            "",
            "High source of fibre!"
        );

        RED_SPIDER_LILY = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON,
            "RED_SPIDER_LILY",
            Material.POPPY,
            "Red Spider Lily",
            "",
            "Its curling petals seem to be grabbing",
            "at you"
        );

        WARPED_LOTUS = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED,
            "WARPED_LOTUS",
            Material.SPORE_BLOSSOM,
            "Warped Lotus",
            "",
            "Don't they normally grow on water?"
        );

        // Rare
        SMOLDERING_HERBS = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON_DARK,
            "SMOLDERING_HERBS",
            Material.FERN,
            "Smoldering Herbs",
            "",
            "They don't actually burn"
        );

        IMMOLATED_VINES = ItemUtil.formattedSlimefunItemStack(
            Theme.CRIMSON_DARK,
            "IMMOLATED_VINES",
            Material.WEEPING_VINES,
            "Immolated Vines",
            "",
            "Even after being cut from the fungus,",
            "it pulsates like a heart"
        );

        OCTARINE_NETHERCAP = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED_DARK,
            "OCTARINE_NETHERCAP",
            Material.WARPED_FUNGUS,
            "Octarine Nethercap",
            "",
            "A fluorescent greenish-yellow purple"
        );

        EMERALD_FIREBLOOM = ItemUtil.formattedSlimefunItemStack(
            Theme.WARPED_DARK,
            "EMERALD_FIREBLOOM",
            Material.FERN,
            "Emerald Firebloom",
            "",
            "Its petals undulate like the flames",
            "of a fire"
        );
        

        // Secret
        GARDEN_OF_THE_LOST_SOUL = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "GARDEN_OF_THE_LOST_SOUL",
            Material.GRASS,
            "Garden of the Lost Soul",
            "&7Chapter 1",
            "",
            "The only refuge in this blazing land",
            "for the adventurer trapped in hell.", 
            "He cultivated this garden with the",
            "flora of a peaceful world, escaping",
            "into a transient dream"
        );

        TULIP_OF_PARTINGS = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "TULIP_OF_PARTINGS",
            Material.WHITE_TULIP,
            "Tulip of Partings",
            "&7Chapter 2",
            "",
            "His oasis, the winds of the wastes",
            "scattered and warped. In the end",
            "the adventurer was left with a single",
            "fragile tulip, the only reminder of",
            "the world from which he came"
        );

        BLOOM_OF_UNHEARD_CRIES = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "BLOOM_OF_UNHEARD_CRIES",
            Material.FLOWERING_AZALEA,
            "Bloom of Unheard Cries",
            "&7Chapter 3",
            "",
            "Gone was his sanctuary, every last", 
            "bloom warped into horror. Fearing the",
            "dangers of staying there, he set out",
            "to find a way home. Alas, his shouts",
            "reached nothing but the domineering",
            "netherrack sky, and the brown sand",
            "dragging down his feet"
        );

        BLOSSOM_OF_SOLITUDE = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "BLOSSOM_OF_SOLITUDE",
            Material.CRIMSON_FUNGUS,
            "Blossom of Solitude",
            "&7Chapter 4",
            "",
            "After days of walking, the adventurer",
            "could move no longer. He collapsed on the",
            "hard rock, scarlet blood flowing from",
            "festering wounds. No more did he feel the",
            "itch of a stinging scrape, nor the aching",
            "of an empty stomach."
        );

        LAMENT_OF_THE_DAMNED = ItemUtil.formattedSlimefunItemStack(
            Theme.SOUL,
            "LAMENT_OF_THE_DAMNED",
            Material.WEEPING_VINES,
            "Lament of the Damned",
            "&7Chapter 5",
            "",
            "Once fiery brimstone, this land became",
            "overgrown with the twisted forms of flora",
            "that flew on the wind, and mutated with",
            "time. However, no more could his soul be",
            "soothed, for he was damned to this",
            "unknown land for the rest of eternity"
        );

        
        // ---------- Cuisine ----------

        // Ingredients
        HOGLIN_SPARE_RIB = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "HOGLIN_SPARE_RIB",
            Material.BEEF,
            "Hoglin Spare Rib",
            "",
            "A thick cut of meat from the side",
            "of a hoglin"
        );
        
        HOGLIN_BELLY = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "HOGLIN_BELLY",
            Material.PORKCHOP,
            "Hoglin Belly",
            "",
            "A fatty cut of meat from the underside",
            "of a hoglin"
        );
        
        HOGLIN_TROTTERS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "HOGLIN_TROTTERS",
            Material.RABBIT_FOOT,
            "Hoglin Trotters",
            "",
            "The foot of a hoglin"
        );

        HOGLIN_BOUILLON = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "HOGLIN_BOUILLON",
            Material.WATER_BUCKET,
            "Hoglin Bouillon",
            "",
            "A flavourful broth made by simmering",
            "hoglin meat"
        );

        BOILED_WARPED_CACTUS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "BOILED_WARPED_CACTUS",
            Material.GREEN_DYE,
            "Boiled Warped Cactus",
            "",
            "You can eat cactus?",
            "",
            LoreBuilder.hunger(1)
        );

        CACTUS_JUICE = ItemUtil.modifyWaterBottle(
            ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
                "CACTUS_JUICE",
                Material.POTION,
                "Cactus Juice",
                "",
                "A regular water bottle that smells",
                "faintly of sulfur and cactus"
            ),
            Color.fromRGB(0x7c, 0xd3, 0x9c)
        );

        // Crafted
        WARPED_SALAD = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "WARPED_SALAD",
            Material.GREEN_DYE,
            "Warped Salad",
            "",
            "Salads are green, so its okay to eat",
            "",
            LoreBuilder.hunger(4)
        );

        MUSHROOM_SLICES = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "MUSHROOM_SLICES",
            Material.BEETROOT_SEEDS,
            "Mushroom Slices",
            "",
            "An assortment of oddly coloured fungi",
            "",
            LoreBuilder.hunger(3)
        );

        BAKED_NETHER_TUBERS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "BAKED_NETHER_TUBERS",
            Material.BAKED_POTATO,
            "Baked Nether Tubers",
            "",
            "It's a baked potato.",
            "",
            LoreBuilder.hunger(3)
        );

        GLOWING_BEANS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "GLOWING_BEANS",
            Material.GLOW_BERRIES,
            "Glowing Beans",
            "",
            "It looks like cooking has brought out",
            "The luminescent elements in the spores",
            "",
            LoreBuilder.hunger(0.5)
        );

        // Frying
        NETHER_CHIPS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "NETHER_CHIPS",
            Material.BREAD,
            "Nether Chips™",
            "",
            "You'll get fat if you eat too much",
            "",
            LoreBuilder.hunger(1.5)
        );

        FRIED_WARPED_FUNGUS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "FRIED_WARPED_FUNGUS",
            Material.WARPED_FUNGUS,
            "Fried Warped Fungus",
            "",
            "Crunchy!",
            "",
            LoreBuilder.hunger(1)
        );

        FRIED_CRIMSON_FUNGUS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "FRIED_CRIMSON_FUNGUS",
            Material.CRIMSON_FUNGUS,
            "Fried Crimson Fungus",
            "",
            "Crunchy!",
            "",
            LoreBuilder.hunger(1)
        );

        SPICY_FRIED_LOTUS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "SPICY_FRIED_LOTUS",
            Material.COOKIE,
            "Spicy Fried Lotus",
            "",
            "Sweet, savoury, &oand spicy!",
            "",
            LoreBuilder.hunger(3.5)
        );

        // Pot
        CRIMSON_STEW = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "CRIMSON_STEW",
            Material.BEETROOT_SOUP,
            "Crimson Stew",
            "",
            "A thick clump of fungus floats on the top.",
            "It should be safe to eat, right?",
            "",
            LoreBuilder.hunger(5)
        );

        WARPED_STEW = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "WARPED_STEW",
            Material.SUSPICIOUS_STEW,
            "Warped Stew",
            "",
            "The surface of the soup shines green.",
            "It should be safe to eat, right?",
            "",
            LoreBuilder.hunger(5)
        );

        NETHER_HOTPOT = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "NETHER_HOTPOT",
            Material.RABBIT_STEW,
            "Nether Hotpot",
            "",
            "Piping hot!",
            "",
            LoreBuilder.hunger(6)
        );

        PORK_BONE_SOUP = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "PORK_BONE_SOUP",
            Material.MUSHROOM_STEW,
            "Pork Bone Soup",
            "",
            "Very filling!",
            "",
            LoreBuilder.hunger(8.5)
        );

        BOILED_NETHER_TUBERS = ItemUtil.formattedSlimefunItemStack(
            Theme.CUISINE,
            "BOILED_NETHER_TUBERS",
            Material.POTATO,
            "Boiled Nether Tubers",
            "",
            "It's a boiled potato.",
            "",
            LoreBuilder.hunger(3)
        );

        // ---------- Brews ----------
        BREW_OF_FIRE_WARDING = ItemUtil.modifyPotionStack(
            ItemUtil.formattedSlimefunItemStack(
                Theme.BREW,
                "BREW_OF_FIRE_WARDING",
                Material.POTION,
                "Brew of Fire Warding"
            ),
            Color.fromRGB(0xfc, 0x8d, 0x0f),
            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300*20, 0)
        );

        BREW_OF_FARSIGHT = ItemUtil.modifyPotionStack(
            ItemUtil.formattedSlimefunItemStack(
                Theme.BREW,
                "BREW_OF_FARSIGHT",
                Material.POTION,
                "Brew of Farsight"
            ),
            Color.fromRGB(0x0f, 0x7d, 0xfc),
            new PotionEffect(PotionEffectType.NIGHT_VISION, 300*20, 0)
        );

        BREW_OF_CELERITY = ItemUtil.modifyPotionStack(
            ItemUtil.formattedSlimefunItemStack(
                Theme.BREW,
                "BREW_OF_CELERITY",
                Material.POTION,
                "Brew of Celerity"
            ),
            Color.fromRGB(0xfc, 0xf4, 0xfa),
            new PotionEffect(PotionEffectType.FAST_DIGGING, 10*20, 1),
            new PotionEffect(PotionEffectType.SPEED, 10*20, 1),
            new PotionEffect(PotionEffectType.JUMP, 10*20, 0),
            new PotionEffect(PotionEffectType.CONFUSION, 10*20, 0)
        );
       
    }
}