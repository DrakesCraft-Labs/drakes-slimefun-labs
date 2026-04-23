package ne.fnfal113.relicsofcthonia.core;

import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.api.Rarity;
import ne.fnfal113.relicsofcthonia.slimefun.RelicVoider;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.CthonianPearl;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.FishingSack;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.HealingPotion;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.LeatherHelmet;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.Mushroom;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.PettyMarbleBlock;
import ne.fnfal113.relicsofcthonia.slimefun.relics.common.RingOfSuffering;
import ne.fnfal113.relicsofcthonia.slimefun.relics.epic.BottleOPower;
import ne.fnfal113.relicsofcthonia.slimefun.relics.epic.FervorHelmet;
import ne.fnfal113.relicsofcthonia.slimefun.relics.epic.FlawlessAquaGem;
import ne.fnfal113.relicsofcthonia.slimefun.relics.epic.LuminousPearl;
import ne.fnfal113.relicsofcthonia.slimefun.relics.epic.ThunderInABottle;
import ne.fnfal113.relicsofcthonia.slimefun.relics.epic.ValiantTalisman;
import ne.fnfal113.relicsofcthonia.slimefun.relics.legendary.EyeOfSauron;
import ne.fnfal113.relicsofcthonia.slimefun.relics.legendary.GlazingPearl;
import ne.fnfal113.relicsofcthonia.slimefun.relics.legendary.MagmaGauntlet;
import ne.fnfal113.relicsofcthonia.slimefun.relics.legendary.MysteriousHolocron;
import ne.fnfal113.relicsofcthonia.slimefun.relics.legendary.SapphireRing;
import ne.fnfal113.relicsofcthonia.slimefun.relics.legendary.SkullOfPrometheus;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.BlazeAshes;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.CeruleanGem;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.CrossedSwords;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.GoldenJar;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.HornOfTaurus;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.MarriageRing;
import ne.fnfal113.relicsofcthonia.slimefun.relics.rare.TerracottaPot;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.AgedWine;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.BlueGlowstone;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.CthonianToken;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.GrayBerry;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.PinkSalt;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.SkullHat;
import ne.fnfal113.relicsofcthonia.slimefun.relics.uncommon.TanzaniteBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {
    private static boolean initialised = false;

    public static void register() {
        if (initialised) {
            return;
        }
        initialised = true;

        RelicsOfCthonia instance = RelicsOfCthonia.getInstance();

        // Common relics
        new CthonianPearl(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_CTHONIAN_PEARL, 12.57, 3, 3).register(instance);
        new FishingSack(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_FISHING_SACK, 12.28, 3, 3).register(instance);
        new HealingPotion(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_HEALING_POTION, 12.32, 3, 3).register(instance);
        new LeatherHelmet(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_LEATHER_HELMET, 12.24, 3, 3).register(instance);
        new Mushroom(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_MUSHROOM, 12.18, 3, 3).register(instance);
        new PettyMarbleBlock(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_PETTY_MARBLE_BLOCK, 12.53, 3, 3).register(instance);
        new RingOfSuffering(Groups.COMMON_RELIC, ItemStacks.COMMON_RELIC_RING_OF_SUFFERING, 12.54, 3, 3).register(instance);

        // Uncommon Relics
        new AgedWine(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_AGED_WINE, 9.32, 2, 3).register(instance);
        new BlueGlowstone(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_BLUE_GLOWSTONE,9.02, 2, 3).register(instance);
        new CthonianToken(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_CTHONIAN_TOKEN, 9.01, 2, 3).register(instance);
        new GrayBerry(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_GRAY_BERRY, 9.38, 2, 3).register(instance);
        new PinkSalt(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_PINK_SALT, 9.15, 2, 3).register(instance);
        new SkullHat(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_SKULL_HAT, 9.62, 2, 3).register(instance);
        new TanzaniteBlock(Groups.UNCOMMON_RELIC, ItemStacks.UNCOMMON_RELIC_TANZANITE_BLOCK, 9.32, 2, 3).register(instance);

        // Rare Relics
        new BlazeAshes(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_BLAZE_ASHES, 7.14, 1, 2).register(instance);
        new CeruleanGem(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_CERULEAN_GEM, 7.05, 1, 2).register(instance);
        new CrossedSwords(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_CROSSED_SWORDS, 7.54, 1, 2).register(instance);
        new GoldenJar(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_GOLDEN_JAR, 7.32, 1, 2).register(instance);
        new HornOfTaurus(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_HORN_OF_TAURUS, 7.54, 1, 2).register(instance);
        new MarriageRing(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_MARRIAGE_RING, 7.12, 1, 3).register(instance);
        new TerracottaPot(Groups.RARE_RELIC, ItemStacks.RARE_RELIC_TERRACOTTA_POT, 7.91, 1, 3).register(instance);

        // Epic Relics
        new BottleOPower(Groups.EPIC_RELIC, ItemStacks.EPIC_RELIC_BOTTLE_O_POWER, 4.42, 1, 2).register(instance);
        new FlawlessAquaGem(Groups.EPIC_RELIC, ItemStacks.EPIC_RELIC_FLAWLESS_AQUA_GEM, 4.94, 1, 2).register(instance);
        new LuminousPearl(Groups.EPIC_RELIC, ItemStacks.EPIC_RELIC_LUMINOUS_PEARL, 4.67, 1, 2).register(instance);
        new ValiantTalisman(Groups.EPIC_RELIC, ItemStacks.EPIC_RELIC_VALIANT_TALISMAN, 4.32, 1, 2).register(instance);
        new FervorHelmet(Groups.EPIC_RELIC, ItemStacks.EPIC_RELIC_FERVOR_HELMET, 4.13, 1, 2).register(instance);
        new ThunderInABottle(Groups.EPIC_RELIC, ItemStacks.EPIC_RELIC_THUNDER_IN_A_BOTTLE, 4.34, 1, 2).register(instance);

        // Legendary Relics
        new EyeOfSauron(Groups.LEGENDARY_RELIC, ItemStacks.LEGENDARY_RELIC_EYE_OF_SAURON, 1.12, 2, 2).register(instance);
        new MysteriousHolocron(Groups.LEGENDARY_RELIC, ItemStacks.LEGENDARY_RELIC_MYSTERIOUS_HOLOCRON, 1.24, 2, 2).register(instance);
        new SkullOfPrometheus(Groups.LEGENDARY_RELIC, ItemStacks.LEGENDARY_RELIC_SKULL_OF_PROMETHEUS, 1.06, 2, 2).register(instance);
        new MagmaGauntlet(Groups.LEGENDARY_RELIC, ItemStacks.LEGENDARY_RELIC_MAGMA_GAUNTLET, 1.42, 2, 2).register(instance);
        new GlazingPearl(Groups.LEGENDARY_RELIC, ItemStacks.LEGENDARY_RELIC_GLAZING_PEARL, 1.35, 2, 2).register(instance);
        new SapphireRing(Groups.LEGENDARY_RELIC, ItemStacks.LEGENDARY_RELIC_SAPPHIRE_RING, 1.02, 2, 2).register(instance);

        // Relic Voiders
        new RelicVoider(Groups.RELIC_VOIDER, ItemStacks.COMMON_RELIC_VOIDER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                new ItemStack(Material.NETHERRACK), null, new ItemStack(Material.NETHERRACK),
                SlimefunItems.MAGIC_LUMP_1, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_1,
                new ItemStack(Material.NETHERRACK), null, new ItemStack(Material.NETHERRACK)
        }, Rarity.COMMON).register(instance);

        new RelicVoider(Groups.RELIC_VOIDER, ItemStacks.UNCOMMON_RELIC_VOIDER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                new ItemStack(Material.NETHERRACK), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.NETHERRACK),
                SlimefunItems.MAGIC_LUMP_1, ItemStacks.COMMON_RELIC_VOIDER, SlimefunItems.MAGIC_LUMP_1,
                new ItemStack(Material.NETHERRACK), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.NETHERRACK)
        }, Rarity.UNCOMMON).register(instance);

        new RelicVoider(Groups.RELIC_VOIDER, ItemStacks.RARE_RELIC_VOIDER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                new ItemStack(Material.END_STONE), new ItemStack(Material.BONE), new ItemStack(Material.END_STONE),
                SlimefunItems.MAGIC_LUMP_2, ItemStacks.UNCOMMON_RELIC_VOIDER, SlimefunItems.MAGIC_LUMP_2,
                new ItemStack(Material.NETHERRACK), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.NETHERRACK)
        }, Rarity.RARE).register(instance);

        new RelicVoider(Groups.RELIC_VOIDER, ItemStacks.EPIC_RELIC_VOIDER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.FERMENTED_SPIDER_EYE), new ItemStack(Material.ROTTEN_FLESH),
                SlimefunItems.MAGIC_LUMP_2, ItemStacks.RARE_RELIC_VOIDER, SlimefunItems.MAGIC_LUMP_2,
                new ItemStack(Material.NETHERRACK), new ItemStack(Material.BONE), new ItemStack(Material.NETHERRACK)
        }, Rarity.EPIC).register(instance);

        new RelicVoider(Groups.RELIC_VOIDER, ItemStacks.LEGENDARY_RELIC_VOIDER, RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                new ItemStack(Material.BLAZE_POWDER), new ItemStack(Material.ROTTEN_FLESH), new ItemStack(Material.BLAZE_POWDER),
                SlimefunItems.MAGIC_LUMP_3, ItemStacks.EPIC_RELIC_VOIDER, SlimefunItems.MAGIC_LUMP_3,
                new ItemStack(Material.SOUL_SAND), new ItemStack(Material.FERMENTED_SPIDER_EYE), new ItemStack(Material.NETHERRACK)
        }, Rarity.LEGENDARY).register(instance);

    }
}
