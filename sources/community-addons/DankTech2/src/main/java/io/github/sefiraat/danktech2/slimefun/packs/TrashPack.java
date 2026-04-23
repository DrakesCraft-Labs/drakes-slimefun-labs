package io.github.sefiraat.danktech2.slimefun.packs;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.DistinctiveItem;
import com.github.drakescraft-labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class TrashPack extends UnplaceableBlock implements DistinctiveItem {

    @Getter
    private final int tier;
    @Getter
    private final int slots;

    public TrashPack(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int tier) {
        super(itemGroup, item, recipeType, recipe);
        this.tier = tier;
        this.slots = tier * 2;
    }

    @Override
    public boolean canStack(@NotNull ItemMeta itemMeta, @NotNull ItemMeta itemMeta1) {
        return itemMeta1.getPersistentDataContainer().equals(itemMeta.getPersistentDataContainer());
    }
}
