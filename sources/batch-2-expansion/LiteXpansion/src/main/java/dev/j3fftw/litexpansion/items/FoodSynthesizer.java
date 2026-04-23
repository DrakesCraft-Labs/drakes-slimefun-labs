package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.attributes.Rechargeable;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FoodSynthesizer extends SlimefunItem implements Rechargeable, NotPlaceable {

    public FoodSynthesizer() {
        super(Items.LITEXPANSION, Items.FOOD_SYNTHESIZER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
            SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF), SlimefunItems.PLASTIC_SHEET,
            new ItemStack(Material.APPLE), SlimefunItems.COOLER, new ItemStack(Material.APPLE),
            SlimefunItems.PLASTIC_SHEET, new ItemStack(Material.COOKED_BEEF), SlimefunItems.PLASTIC_SHEET
        });
    }

    @Override
    public void preRegister() {
        addItemHandler((ItemUseHandler) PlayerRightClickEvent::cancel);
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 100;
    }
}
