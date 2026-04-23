package me.gallowsdove.foxymachines.implementation.tools;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.handlers.EntityInteractHandler;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import me.gallowsdove.foxymachines.Items;
import me.gallowsdove.foxymachines.implementation.materials.GhostBlock;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class GhostBlockRemover extends SlimefunItem {

    private static String formatMaterialName(Material material) {
        String[] words = material.name().toLowerCase().split("_");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (words[i].isEmpty()) {
                continue;
            }

            if (builder.length() > 0) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(words[i].charAt(0)));
            if (words[i].length() > 1) {
                builder.append(words[i].substring(1));
            }
        }

        return builder.toString();
    }

    public GhostBlockRemover() {
        super(Items.TOOLS_ITEM_GROUP, Items.GHOST_BLOCK_REMOVER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.DAMIENIUM, Items.DAMIENIUM, Items.DAMIENIUM,
                Items.SWEET_INGOT, SlimefunItems.BASIC_CIRCUIT_BOARD, Items.SWEET_INGOT,
                Items.DAMIENIUM, Items.DAMIENIUM, Items.DAMIENIUM
        });
    }

    @Override
    public void preRegister() {
        addItemHandler(onInteract());
    }

    @Nonnull
    protected EntityInteractHandler onInteract() {
        return (e, itemStack, b) -> {
            if (e.getRightClicked() instanceof FallingBlock block &&
                    block.getPersistentDataContainer().has(GhostBlock.KEY, PersistentDataType.STRING)) {
                Material material = block.getBlockData().getMaterial();
                SlimefunItemStack stack = new SlimefunItemStack(
                        "GHOST_BLOCK_" + material.name().toUpperCase(),
                        material,
                        "Ghost Block: &6" + formatMaterialName(material),
                        "",
                        "&7An intangible block.");

                block.getWorld().dropItemNaturally(block.getLocation(), stack);
                block.remove();
            }
        };
    }
}
