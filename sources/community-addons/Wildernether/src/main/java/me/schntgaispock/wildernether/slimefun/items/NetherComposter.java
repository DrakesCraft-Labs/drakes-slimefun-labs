package me.schntgaispock.wildernether.slimefun.items;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

public class NetherComposter extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public NetherComposter(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc) {
        super(ig, is, rt, rc);
    }

    @Override
    public void preRegister() {
        addItemHandler((BlockUseHandler) this::onRightClick);
    }

    @EventHandler(ignoreCancelled = true)
    @ParametersAreNonnullByDefault
    private void onRightClick(PlayerRightClickEvent e) {
        if (e.getClickedBlock().isEmpty()) {
            return;
        }
        
        ItemStack item = e.getItem();
        SlimefunItem i = null;
        if ((i = SlimefunItem.getByItem(item)) == null ||
            i.getId() != "BLAZESPROUT") {
                e.cancel();
            return;
        }

        item.setAmount(item.getAmount() - 1);
        Block b = e.getClickedBlock().get();
        b.getWorld().dropItemNaturally(b.getLocation(), SlimefunItems.INFERNAL_BONEMEAL);
    }
}
