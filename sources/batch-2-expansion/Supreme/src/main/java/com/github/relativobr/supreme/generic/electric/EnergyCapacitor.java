package com.github.relativobr.supreme.generic.electric;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.items.electric.Capacitor;
import org.bukkit.inventory.ItemStack;

public class EnergyCapacitor extends Capacitor {

  public EnergyCapacitor(ItemGroup categories, SlimefunItemStack item, ItemStack[] recipe,
      int capacity) {
    super(categories, capacity, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
  }

}
