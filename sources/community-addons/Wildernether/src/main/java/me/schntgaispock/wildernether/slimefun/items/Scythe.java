package me.schntgaispock.wildernether.slimefun.items;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ToolUseHandler;
import me.schntgaispock.wildernether.Wildernether;
import me.schntgaispock.wildernether.loot.LootTableCollection;
import me.schntgaispock.wildernether.managers.LootManager;
import me.schntgaispock.wildernether.util.Calc;

public class Scythe extends SlimefunItem {

    private static double plantDropChance;

    public static void setup() {
        plantDropChance = Wildernether.getInstance().getConfig().getDouble("plant-harvest.drop-chance", 0.3);
    }

    @ParametersAreNonnullByDefault
    public Scythe(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc) {
        super(ig, is, rt, rc);
    }

    @Override
    public void preRegister() {
        addItemHandler((ToolUseHandler) this::onToolUse);
    }

    @EventHandler(ignoreCancelled = true)
    @ParametersAreNonnullByDefault
    private void onToolUse(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
        if (!item.hasItemMeta()) {
            return;
        }
        SlimefunItem tool = SlimefunItem.getByItem(item);

        if (tool == null) {
            return;
        }

        final LootTableCollection netherPlantHarvestLoot = LootManager.getNetherPlantHarvest();

        switch (tool.getId()) {
            case "BLACKSTONE_SCYTHE":
            case "SOUL_SCYTHE":
                break;
        
            default:
                Wildernether.getInstance().getLogger().log(Level.INFO, String.format(
                    "Scythe#onToolUse: Slimefun Item '%s' does not match any existing Scythes", tool.getId()
                ));
                return;
        }

        if (netherPlantHarvestLoot.hasTable(e.getBlock().getType())) {

            // To prevent players from just breaking and replanting for items
            e.setDropItems(false);

            if (Calc.flip(Scythe.plantDropChance)) {
                @Nullable ItemStack toDrop = netherPlantHarvestLoot.getDrop(e.getBlock().getType(), tool.getId());
                if (toDrop == null) {
                    return;
                }
                toDrop.setAmount(Calc.clamp(1, fortune, 5));
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), toDrop);
            }
        }
    }

}
