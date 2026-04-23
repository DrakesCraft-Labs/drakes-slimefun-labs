package io.github.sefiraat.crystamaehistoria.slimefun.items.gadgets;

import io.github.sefiraat.crystamaehistoria.slimefun.items.tools.Displacer;
import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotConfigurable;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotHopperable;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class PhilosophersSpray extends SlimefunItem implements NotHopperable, NotConfigurable {
    @ParametersAreNonnullByDefault
    public PhilosophersSpray(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockUseHandler() {
            @Override
            public void onRightClick(PlayerRightClickEvent e) {
                e.cancel();
            }
        });
    }

    public static void triggerChange(@Nonnull Block block) {
        final Block blockToChange = block.getRelative(BlockFace.UP);
        Displacer.convertBlock(blockToChange);
    }
}
