package io.github.sefiraat.crystamaehistoria.slimefun.items.gadgets;

import io.github.sefiraat.crystamaehistoria.utils.GeneralUtils;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import dev.drake.dough.protection.Interaction;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class AngelBlock extends SlimefunItem {

    public AngelBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public void preRegister() {
        addItemHandler(onItemUse());
    }

    @Nonnull
    private ItemUseHandler onItemUse() {
        return e -> {
            Location location = e.getPlayer().getEyeLocation();
            Block block = location.add(location.getDirection()).getBlock();
            if (block.isEmpty()
                && block.getLocation().getY() <= block.getWorld().getMaxHeight()
                && block.getLocation().getY() >= block.getWorld().getMinHeight()
                && GeneralUtils.hasPermission(e.getPlayer(), block, Interaction.PLACE_BLOCK)
            ) {
                block.setType(e.getItem().getType());
                BlockStorage.store(block, e.getItem());
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            }

            e.cancel();
        };
    }
}
