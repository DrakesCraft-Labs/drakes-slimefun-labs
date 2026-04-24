package me.profelements.dynatech.items.tools;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.collections.RandomizedSet;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import me.profelements.dynatech.DynaTech;
import me.profelements.dynatech.items.electric.abstracts.AMachine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

//I feel like this can somehow be much better :O (review)

public class Orechid extends AMachine implements RecipeDisplayItem {

    private static final Map<Material, RandomizedSet<ItemStack>> ORE_MAP = new EnumMap<>(Material.class);
    //private static final List<Material> END_ORES = new ArrayList<>();

    public Orechid(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        registerDefaultOres();
    }

    @Override
    public void tick(Block b) {
        if (DynaTech.getInstance().getTickInterval() % 10 == 0) {
            for (BlockFace relative : BlockFace.values()) {
                if (getCharge(b.getLocation()) < getEnergyConsumption()) {
                    break;
                }
    
                if (relative == BlockFace.UP || relative == BlockFace.DOWN) {
                    continue;
                }
                
                Block relBlock = b.getRelative(relative);
    
                RandomizedSet<ItemStack> set = ORE_MAP.get(relBlock.getType());
                if (set != null) {
                    ItemStack item = set.getRandom();

                    SlimefunItem sfi = SlimefunItem.getByItem(item);

                    DynaTech.runSync(() -> {
                        relBlock.setType(item.getType());
                        if (sfi != null) {
                            BlockStorage.addBlockInfo(relBlock, "id", sfi.getId());
                        }
                    });

                    removeCharge(b.getLocation(), getEnergyConsumption());
                }
            }        
        }
    }

    public static void registerOre(@Nonnull Material from, @Nonnull Material result, float weight) {
        ORE_MAP.computeIfAbsent(from, k -> new RandomizedSet<>()).add(new ItemStack(result), weight);
    }

    /**
     * For Slimefun items
     */
    public static void registerOre(@Nonnull Material from, @Nonnull SlimefunItemStack result, float weight) {
        ORE_MAP.computeIfAbsent(from, k -> new RandomizedSet<>()).add(result, weight);
    }

    private static void registerDefaultOres() {
        registerOre(Material.STONE, Material.COAL_ORE, 3);
        registerOre(Material.STONE, Material.IRON_ORE, 2);
        registerOre(Material.STONE, Material.GOLD_ORE, 2);
        registerOre(Material.STONE, Material.DIAMOND_ORE, 1);
        registerOre(Material.STONE, Material.EMERALD_ORE, 1);
        registerOre(Material.STONE, Material.REDSTONE_ORE, 3);
        registerOre(Material.STONE, Material.LAPIS_ORE, 3);
        registerOre(Material.STONE, Material.COPPER_ORE, 3);

        registerOre(Material.NETHERRACK, Material.NETHER_QUARTZ_ORE, 3);
        registerOre(Material.NETHERRACK, Material.NETHER_GOLD_ORE, 3);
        registerOre(Material.NETHERRACK, Material.ANCIENT_DEBRIS, 1);
        registerOre(Material.NETHERRACK, Material.BASALT, 5);
        registerOre(Material.NETHERRACK, Material.BLACKSTONE, 5);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayList = new ArrayList<>();

        for (Material m : ORE_MAP.keySet()) {
            for (ItemStack item : ORE_MAP.get(m)) {
                displayList.add(new ItemStack(m));
                displayList.add(item);
            }
        }

        return displayList;
    }

    @Override
    public boolean isGraphical() {
        return false;
    }

    @Override
    public String getMachineIdentifier() {
        return "ORECHID";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WITHER_ROSE);
    }
    
}
