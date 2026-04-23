package me.schntgaispock.wildernether.loot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.libraries.dough.collections.Pair;
import lombok.Getter;
import me.schntgaispock.wildernether.util.GeneralUtil;

/**
 * Holds a group of loot tables
 * 
 * @author SchnTgaiSpock
 */
public class LootTableCollection {
    
    @Getter Map<Material, LootTable> lootTables;
    @Getter double[] defaultRates;
    private double defaultRatesTotal;
    // Pair of rates by rarity, and total; to save on calculation
    @Getter Map<String, Pair<double[], Double>> toolRates = new HashMap<>();


    public LootTableCollection() {
        this.lootTables = new HashMap<Material, LootTable>();
    }

    public LootTableCollection setDefaultRates(double[] rates) {
        this.defaultRates = rates;
        defaultRatesTotal = GeneralUtil.sum(rates);

        return this;
    }

    private LootTableCollection addTool(String tool, double[] rates, double total) {
        toolRates.put(tool, new Pair<double[], Double>(rates, total));
        return this;
    }

    public LootTableCollection addTool(String tool, double[] rates) {
        double[] newRates = GeneralUtil.zipMult(defaultRates, rates);
        return this.addTool(tool, newRates, GeneralUtil.sum(newRates));
    }

    public LootTableCollection addTool(String tool) {
        return this.addTool(tool, defaultRates, defaultRatesTotal);
    }

    @ParametersAreNonnullByDefault
    public LootTableCollection put(Material blockType, ItemStack[][] loot) {
        this.lootTables.put(blockType, new LootTable(loot));
        return this;
    }

    public boolean hasTable(Material blockType) {
        return this.lootTables.containsKey(blockType);
    }

    @ParametersAreNonnullByDefault
    public ItemStack getDrop(Material blockType, String toolName) {
        LootTable table = lootTables.get(blockType);
        Pair<double[], Double> rates = toolRates.get(toolName);

        if (table == null) {
            return null;
        }        
        if (rates == null) {
            return null;
        }

        return table.getDrop(rates.getFirstValue(), rates.getSecondValue()).clone();
    }
}
