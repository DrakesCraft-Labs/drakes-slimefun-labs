package me.lucasgithuber.elementmanipulation.elements;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Nitrogen extends SimpleSlimefunItem<ItemUseHandler> {
    protected Nitrogen(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @NotNull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            removeAir(p);
        };
    }

    private void removeAir(Player p) {
        if (p.getRemainingAir() < 0) {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (p.getGameMode() != GameMode.CREATIVE ) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
            p.setRemainingAir(p.getRemainingAir() - 20);
        }
    }
}
