package com.github.drakescraft-labs.gcereborn.items.chicken;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft-labs.gcereborn.items.GCEItems;
import com.github.drakescraft-labs.gcereborn.utils.ChickenUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ChickenTypes {

    private static final Map<Integer, ChickenProduct> TYPES = new LinkedHashMap<>();

    static {
        TYPES.put(63, new ChickenProduct(new ItemStack(Material.FEATHER)));
        TYPES.put(31, new ChickenProduct(new ItemStack(Material.BONE)));
        TYPES.put(47, new ChickenProduct(new ItemStack(Material.COBBLESTONE)));
        TYPES.put(55, new ChickenProduct(new ItemStack(Material.DIRT)));
        TYPES.put(59, new ChickenProduct(new ItemStack(Material.FLINT)));
        TYPES.put(61, new ChickenProduct(new ItemStack(Material.SAND)));
        TYPES.put(62, new ChickenProduct("WATER", GCEItems.WATER_EGG));
        TYPES.put(15, new ChickenProduct(new ItemStack(Material.COAL)));
        TYPES.put(23, new ChickenProduct(new ItemStack(Material.STRING)));
        TYPES.put(27, new ChickenProduct(new ItemStack(Material.LEATHER)));
        TYPES.put(29, new ChickenProduct(new ItemStack(Material.SUGAR)));
        TYPES.put(30, new ChickenProduct(new ItemStack(Material.SPONGE)));
        TYPES.put(39, new ChickenProduct(new ItemStack(Material.DIORITE)));
        TYPES.put(43, new ChickenProduct(new ItemStack(Material.ANDESITE)));
        TYPES.put(45, new ChickenProduct(new ItemStack(Material.GRAVEL)));
        TYPES.put(46, new ChickenProduct(new ItemStack(Material.ICE)));
        TYPES.put(51, new ChickenProduct(new ItemStack(Material.GRANITE)));
        TYPES.put(53, new ChickenProduct(new ItemStack(Material.CLAY)));
        TYPES.put(54, new ChickenProduct(new ItemStack(Material.OAK_LOG)));
        TYPES.put(57, new ChickenProduct(new ItemStack(Material.GUNPOWDER)));
        TYPES.put(58, new ChickenProduct(new ItemStack(Material.KELP)));
        TYPES.put(60, new ChickenProduct(new ItemStack(Material.SLIME_BALL)));
        TYPES.put(7, new ChickenProduct("GOLD", new ItemStack(Material.GOLD_INGOT)));
        TYPES.put(11, new ChickenProduct(new ItemStack(Material.NETHERRACK)));
        TYPES.put(13, new ChickenProduct(new ItemStack(Material.GLASS)));
        TYPES.put(14, new ChickenProduct("LAPIS", new ItemStack(Material.LAPIS_LAZULI)));
        TYPES.put(19, new ChickenProduct("IRON", new ItemStack(Material.IRON_INGOT)));
        TYPES.put(21, new ChickenProduct(SlimefunItems.IRON_DUST));
        TYPES.put(22, new ChickenProduct(SlimefunItems.GOLD_DUST));
        TYPES.put(25, new ChickenProduct(SlimefunItems.SILVER_DUST));
        TYPES.put(26, new ChickenProduct(SlimefunItems.ZINC_DUST));
        TYPES.put(28, new ChickenProduct(new ItemStack(Material.CAKE)));
        TYPES.put(35, new ChickenProduct(new ItemStack(Material.OBSIDIAN)));
        TYPES.put(37, new ChickenProduct(SlimefunItems.COPPER_DUST));
        TYPES.put(38, new ChickenProduct(SlimefunItems.MAGNESIUM_DUST));
        TYPES.put(41, new ChickenProduct("LAVA", GCEItems.LAVA_EGG));
        TYPES.put(42, new ChickenProduct(SlimefunItems.TIN_DUST));
        TYPES.put(44, new ChickenProduct(new ItemStack(Material.SNOWBALL)));
        TYPES.put(49, new ChickenProduct(new ItemStack(Material.REDSTONE)));
        TYPES.put(50, new ChickenProduct(new ItemStack(Material.CACTUS)));
        TYPES.put(52, new ChickenProduct(SlimefunItems.ALUMINUM_DUST));
        TYPES.put(56, new ChickenProduct(SlimefunItems.LEAD_DUST));
        TYPES.put(3, new ChickenProduct(new ItemStack(Material.BLACKSTONE)));
        TYPES.put(5, new ChickenProduct(new ItemStack(Material.SOUL_SOIL)));
        TYPES.put(9, new ChickenProduct(new ItemStack(Material.BLAZE_ROD)));
        TYPES.put(17, new ChickenProduct(new ItemStack(Material.GHAST_TEAR)));
        TYPES.put(33, new ChickenProduct(SlimefunItems.SULFATE));
        TYPES.put(6, new ChickenProduct(new ItemStack(Material.SHROOMLIGHT)));
        TYPES.put(10, new ChickenProduct(new ItemStack(Material.QUARTZ)));
        TYPES.put(18, new ChickenProduct(new ItemStack(Material.BASALT)));
        TYPES.put(34, new ChickenProduct(new ItemStack(Material.CRYING_OBSIDIAN)));
        TYPES.put(12, new ChickenProduct(new ItemStack(Material.SOUL_SAND)));
        TYPES.put(20, new ChickenProduct(new ItemStack(Material.ENDER_PEARL)));
        TYPES.put(36, new ChickenProduct(new ItemStack(Material.NETHER_WART)));
        TYPES.put(24, new ChickenProduct(new ItemStack(Material.PHANTOM_MEMBRANE)));
        TYPES.put(40, new ChickenProduct(new ItemStack(Material.MAGMA_CREAM)));
        TYPES.put(48, new ChickenProduct(new ItemStack(Material.GLOWSTONE_DUST)));
        TYPES.put(1, new ChickenProduct(new ItemStack(Material.DIAMOND)));
        TYPES.put(2, new ChickenProduct(new ItemStack(Material.END_STONE)));
        TYPES.put(4, new ChickenProduct(new ItemStack(Material.PRISMARINE_CRYSTALS)));
        TYPES.put(8, new ChickenProduct(new ItemStack(Material.PRISMARINE_SHARD)));
        TYPES.put(16, new ChickenProduct("EXPERIENCE", new ItemStack(Material.EXPERIENCE_BOTTLE)));
        TYPES.put(32, new ChickenProduct(new ItemStack(Material.EMERALD)));
        TYPES.put(0, new ChickenProduct("NETHERITE", new ItemStack(Material.NETHERITE_INGOT)));
    }

    @Nonnull
    public static ChickenProduct get(int typing) {
        return TYPES.get(typing);
    }

    @Nonnull
    public static String getName(int typing) {
        return TYPES.get(typing).getName();
    }

    @Nonnull
    public static String getDisplayName(int typing) {
        return TYPES.get(typing).getProductName();
    }

    @Nonnull
    public static ItemStack getProduct(int typing) {
        return TYPES.get(typing).getProduct();
    }

    public static void registerChickens() {
        for (int i = TYPES.size() - 1; i >= 0; i--) {
            ChickenUtils.createProductDisplay(i);
        }
        GeneticChickengineering.log(Level.INFO,
            GeneticChickengineering.getLocalization().getString("console.load.chickens", TYPES.size()));
    }

}
