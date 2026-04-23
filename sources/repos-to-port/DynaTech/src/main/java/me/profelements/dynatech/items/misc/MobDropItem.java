package me.profelements.dynatech.items.misc;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.ItemSetting;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.RandomMobDrop;
import org.bukkit.inventory.ItemStack;

public class MobDropItem extends SlimefunItem implements RandomMobDrop {

    private final ItemSetting<Boolean> dropSetting = new ItemSetting<>(this, "drop-from-mob", true);
    private int dropChance = 0;

    public MobDropItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int dropChance) {
        super(itemGroup, item, recipeType, recipe);
        this.dropChance = dropChance;

        addItemSetting(dropSetting);
    }

    public boolean isDroppedFromMob() {
        return dropSetting.getValue();
    }

    @Override
    public int getMobDropChance() {
        return dropChance;
    }
    
}
