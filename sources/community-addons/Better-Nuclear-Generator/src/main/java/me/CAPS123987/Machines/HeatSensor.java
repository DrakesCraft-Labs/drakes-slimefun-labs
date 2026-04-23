package me.CAPS123987.Machines;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import me.CAPS123987.Item.Items;

public class HeatSensor extends SlimefunItem {
    public HeatSensor() {
        super(Items.betterReactor, Items.HEAT_SENSOR, RecipeType.ENHANCED_CRAFTING_TABLE, Items.recipe_HEAT_SENSOR);
    }
}
