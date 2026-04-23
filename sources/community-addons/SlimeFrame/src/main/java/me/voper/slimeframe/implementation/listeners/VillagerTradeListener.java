package me.voper.slimeframe.implementation.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.core.managers.SettingsManager;
import me.voper.slimeframe.implementation.items.resources.SpecialOre;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

@ParametersAreNonnullByDefault
public class VillagerTradeListener implements Listener {

    private static final EnumeratedDistribution<ItemStack> specialOreDistribution;
    private static final List<ItemStack> ingredientsPool;

    private final SlimeFrame plugin;

    public VillagerTradeListener(SlimeFrame plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onVillagerGetTrade(VillagerAcquireTradeEvent e) {
        int random = ThreadLocalRandom.current().nextInt(100);
        if (random < SlimeFrame.getSettingsManager().getInt(SettingsManager.ConfigField.ORE_TRADE_CHANCE)) {
            MerchantRecipe randomTrade = generateRandomTrade();
            e.setRecipe(randomTrade);
        }
    }

    // A workaround to undo the cancellation of slimefun trades
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlimefunTrade(InventoryClickEvent e) {
        if (!e.isCancelled()) {
            return;
        }

        final Inventory clickedInventory = e.getClickedInventory();
        final Inventory topInventory = e.getView().getTopInventory();

        if (clickedInventory != null && topInventory.getType() == InventoryType.MERCHANT) {
            SlimefunItem sfItem;
            if (clickedInventory.getType() == InventoryType.MERCHANT) {
                sfItem = SlimefunItem.getByItem(e.getCursor());
            } else {
                sfItem = SlimefunItem.getByItem(e.getCurrentItem());
            }

            if (sfItem == null) {
                return;
            }

            e.setCancelled(false);
        }
    }

    @Nonnull
    private static MerchantRecipe createTrade(ItemStack result, ItemStack ingredient) {
        return createTrade(result, ingredient, new ItemStack(Material.AIR));
    }

    @Nonnull
    private static MerchantRecipe createTrade(ItemStack result, ItemStack... ingredients) {
        Preconditions.checkArgument(ingredients.length == 2, "Ingredients length must 2");

        MerchantRecipe merchantRecipe = new MerchantRecipe(result, 0, 2, true);
        merchantRecipe.setIngredients(List.of(ingredients));
        return merchantRecipe;
    }

    @Nonnull
    private static MerchantRecipe generateRandomTrade() {
        ItemStack result = specialOreDistribution.sample();
        int random = ThreadLocalRandom.current().nextInt(ingredientsPool.size());
        return createTrade(result, ingredientsPool.get(random));
    }

    static {
        Collection<SpecialOre> ores = SpecialOre.SOURCE_ORE_MAP.values();

        double sumOfProbabilities = ores.stream()
                .mapToDouble(ore -> ore.getRarity().getValue() / 100.0)
                .sum();

        List<Pair<ItemStack, Double>> specialOresProbabilities = new ArrayList<>(ores.stream()
                .map(ore -> new Pair<>(ore.getItem(), (ore.getRarity().getValue() / 100.0) / sumOfProbabilities))
                .toList());

        specialOreDistribution = new EnumeratedDistribution<>(specialOresProbabilities);

        ingredientsPool = new ArrayList<>(Slimefun.getRegistry().getAllSlimefunItems().stream()
                .filter(item -> ChatColor.stripColor(item.getItemName()).contains("Ancient Rune"))
                .map(SlimefunItem::getItem)
                .toList());

    }

}
