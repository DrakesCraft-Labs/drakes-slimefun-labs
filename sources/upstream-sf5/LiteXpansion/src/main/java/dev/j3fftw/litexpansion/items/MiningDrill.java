package dev.j3fftw.litexpansion.items;

import dev.j3fftw.litexpansion.Items;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * The {@link MiningDrill} is a {@link SimpleSlimefunItem} that breaks
 * stone quickly
 *
 * @author FluffyBear
 */
public class MiningDrill extends SimpleSlimefunItem<ItemUseHandler> implements Listener, Rechargeable {

    public MiningDrill(Type type) {
        super(Items.LITEXPANSION, type.getItem(), RecipeType.ENHANCED_CRAFTING_TABLE, type.getRecipe());
    }

    @Nonnull
    public ItemUseHandler getItemHandler() {
        return e -> e.setUseBlock(Event.Result.DENY);
    }

    @Override
    public float getMaxItemCharge(ItemStack itemStack) {
        return 1000;
    }

    public enum Type {

        MINING(Items.MINING_DRILL, new ItemStack[] {
            null, Items.IRON_PLATE.item(), null,
            Items.IRON_PLATE.item(), Items.IRON_PLATE.item(), Items.IRON_PLATE.item(),
            Items.IRON_PLATE.item(), Items.POWER_UNIT.item(), Items.IRON_PLATE.item()
        }),
        DIAMOND(Items.DIAMOND_DRILL, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.DIAMOND), null,
            new ItemStack(Material.DIAMOND), Items.MINING_DRILL.item(), new ItemStack(Material.DIAMOND)
        });

        @Nonnull
        private final SlimefunItemStack item;
        private final ItemStack[] recipe;

        Type(@Nonnull SlimefunItemStack item, @Nonnull ItemStack[] recipe) {
            this.item = item;
            this.recipe = recipe;
        }

        @Nonnull
        public SlimefunItemStack getItem() {
            return item;
        }

        @Nonnull
        public ItemStack[] getRecipe() {
            return recipe;
        }
    }
}
