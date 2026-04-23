package io.github.addoncommunity.galactifun.api.items.spacesuit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import dev.drake.dough.items.ItemUtils;

public final class SpaceSuitHelmet extends SpaceSuit implements NotPlaceable {

    /**
     * Keep in mind you need 1 oxygen per second irl
     *
     */
    public SpaceSuitHelmet(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int maxUpgrades, int maxOxygen) {
        super(category, item, recipeType, recipe, maxUpgrades, maxOxygen);
        addItemHandler(getItemHandler());
    }

    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();
            PlayerInventory inventory = p.getInventory();
            ItemStack existing = inventory.getHelmet();
            inventory.setHelmet(e.getItem().asOne());
            ItemUtils.consumeItem(e.getItem(), true);

            if (existing != null && !existing.getType().isAir()) {
                for (ItemStack stack : inventory.addItem(existing).values()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), stack);
                }
            }
        };
    }

}
