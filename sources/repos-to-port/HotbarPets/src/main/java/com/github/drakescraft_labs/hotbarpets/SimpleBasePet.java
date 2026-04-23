package com.github.drakescraft_labs.hotbarpets;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;

public abstract class SimpleBasePet extends HotbarPet {

    public SimpleBasePet(ItemGroup itemGroup, SlimefunItemStack item, ItemStack food, ItemStack[] recipe) {
        super(itemGroup, item, food, recipe);
    }

    public abstract void onUseItem(Player p);

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler(onClick());
    }

    private ItemUseHandler onClick() {
        return e -> {
            if (checkAndConsumeFood(e.getPlayer())) {
                onUseItem(e.getPlayer());
            }
        };
    }

}
