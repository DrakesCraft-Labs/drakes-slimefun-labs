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

public class Oxygen extends SimpleSlimefunItem<ItemUseHandler> {
    protected Oxygen(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @NotNull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            giveAir(p);
        };
    }

    private void giveAir(Player p) {
        if (p.getRemainingAir() < 10 * 20) {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (p.getGameMode() != GameMode.CREATIVE && itemStack.getAmount()>=4) {
                itemStack.setAmount(itemStack.getAmount() - 4);
            }
            p.setRemainingAir(p.getRemainingAir() + 20);
        }
    }
}
