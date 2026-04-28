package io.github.thebusybiscuit.slimefunluckyblocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefunluckyblocks.surprises.Surprise;

/**
 * Represents a Lucky Block {@link SlimefunItem} that triggers a random
 * {@link Surprise} when broken by a player.
 *
 * @author TheBusyBiscuit
 */
public class LuckyBlock extends SlimefunItem {

    private Collection<Surprise> surprises;
    private Predicate<Surprise> predicate;

    /**
     * Creates a new {@link LuckyBlock}.
     *
     * @param itemGroup  the {@link ItemGroup} this item belongs to
     * @param item       the {@link SlimefunItemStack} representing this item
     * @param recipeType the {@link RecipeType} used to craft this item
     * @param recipe     the crafting recipe
     */
    public LuckyBlock(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onBlockBreak());
    }

    @Nonnull
    private BlockBreakHandler onBlockBreak() {
        return new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                Random random = ThreadLocalRandom.current();
                List<Surprise> luckySurprises = surprises.stream().filter(predicate).collect(Collectors.toList());

                Player p = e.getPlayer();
                Location loc = e.getBlock().getLocation();
                luckySurprises.get(random.nextInt(luckySurprises.size())).activate(random, p, loc);
            }
        };
    }

    @Nonnull
    @Override
    public Collection<ItemStack> getDrops() {
        // Disable any drops from Lucky blocks (Air is not dropped but still counts as "overridden drops")
        return Arrays.asList(new ItemStack(Material.AIR));
    }

    /**
     * Registers this {@link LuckyBlock} with the given surprises and filter predicate.
     *
     * @param plugin    the {@link SlimefunLuckyBlocks} plugin instance
     * @param surprises the collection of available surprises
     * @param predicate the filter for selecting valid surprises
     */
    public void register(@Nonnull SlimefunLuckyBlocks plugin, @Nonnull Collection<Surprise> surprises, @Nonnull Predicate<Surprise> predicate) {
        this.surprises = surprises;
        this.predicate = predicate;
        super.register(plugin);
    }

}
