package me.kaiyan.missilewarfare.items;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockPlaceHandler;
import me.kaiyan.missilewarfare.MissileWarfare;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class Mine extends SlimefunItem implements Listener {
    public Mine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);


        BlockPlaceHandler placeHandler = new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent blockPlaceEvent) {
                Material type = blockPlaceEvent.getBlockAgainst().getType();
                if (type == Material.BEDROCK || type == Material.ICE){
                    return;
                }
                blockPlaceEvent.getBlockPlaced().setType(type);
            }
        };
        addItemHandler(placeHandler);

        MissileWarfare.getInstance().getServer().getPluginManager().registerEvents(this, MissileWarfare.getInstance());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (BlockStorage.check(event.getFrom().getBlock().getRelative(BlockFace.DOWN), getId())){
            event.getPlayer().getWorld().createExplosion(event.getTo(), MissileWarfare.getInstance().getConfig().getInt("mine.explosivepower"));
            event.getPlayer().damage(Math.random()*MissileWarfare.getInstance().getConfig().getLong("mine.maxranddamage"));
        }
    }
}
