package me.kaiyan.missilewarfare.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.legacy.api.BlockStorage;
import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

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
 * @param itemGroup the item group this item belongs to
 * @param item the Slimefun item stack
 * @param recipeType the recipe type
 * @param recipe the crafting recipe
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
Block block = event.getFrom().getBlock().getRelative(BlockFace.DOWN);
if (BlockStorage.check(block, getId())) {
Player player = event.getPlayer();
Location loc = block.getLocation();

BlockStorage.getStorage(loc.getWorld()).clearBlockInfo(loc);
block.setType(Material.AIR);

for (int i = 0; i < 50; i++) {
player.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc.clone().add(0.5, 0.5, 0.5), 0, Math.random() - 0.5, Math.random() * 2, Math.random() - 0.5, 0.25, null, true);
player.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0.5, 0.5, 0.5), 0, Math.random() - 0.5, Math.random() * 2, Math.random() - 0.5, 0.25, null, true);
}

org.bukkit.entity.TNTPrimed tnt = (org.bukkit.entity.TNTPrimed) player.getWorld().spawnEntity(loc.clone().add(0.5, 0.5, 0.5), org.bukkit.entity.EntityType.TNT);
tnt.setFuseTicks(0);

player.damage(Math.random() * MissileWarfare.getInstance().getConfig().getDouble("mine.maxranddamage"));
}
}
}
