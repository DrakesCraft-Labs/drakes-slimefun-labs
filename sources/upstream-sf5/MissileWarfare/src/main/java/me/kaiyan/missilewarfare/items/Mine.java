package me.kaiyan.missilewarfare.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.kaiyan.missilewarfare.MissileWarfare;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A Slimefun item representing a landmine that disguises itself as the
 * block it is placed against and detonates when a player walks over it.
 *
 * @author MissileWarfare contributors
 */
public class Mine extends SlimefunItem implements Listener {

    /**
     * Creates a new mine item with placement and movement detection handlers.
     *
     * @param itemGroup  the item group this item belongs to
     * @param item       the Slimefun item stack
     * @param recipeType the recipe type
     * @param recipe     the crafting recipe
     */
    public Mine(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item,
                @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);


        BlockPlaceHandler placeHandler = new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent blockPlaceEvent) {
                Material type = blockPlaceEvent.getBlockAgainst().getType();
                if (type == Material.BEDROCK || type == Material.ICE) {
                    return;
                }
                blockPlaceEvent.getBlockPlaced().setType(type);
            }
        };
        addItemHandler(placeHandler);

        MissileWarfare.getInstance().getServer().getPluginManager().registerEvents(this, MissileWarfare.getInstance());
    }

    /**
     * Handles player movement to detect when a player steps on a mine.
     *
     * @param event the player move event
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (BlockStorage.check(event.getFrom().getBlock().getRelative(BlockFace.DOWN), getId())) {
            event.getPlayer().getWorld().createExplosion(event.getTo(), MissileWarfare.getInstance().getConfig().getInt("mine.explosivepower"));
            event.getPlayer().damage(Math.random() * MissileWarfare.getInstance().getConfig().getLong("mine.maxranddamage"));
        }
    }
}
