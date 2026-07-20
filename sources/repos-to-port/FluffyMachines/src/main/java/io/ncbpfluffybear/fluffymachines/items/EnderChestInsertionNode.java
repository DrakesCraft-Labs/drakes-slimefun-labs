package io.ncbpfluffybear.fluffymachines.items;

import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockUseHandler;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import io.ncbpfluffybear.fluffymachines.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * This {@link SlimefunItem} transfers items to the facing
 * {@link EnderChest} from the {@link Container} behind it
 *
 * @author NCBPFluffyBear
 */
public class EnderChestInsertionNode extends SlimefunItem {

    private static final Material material = Material.ENDER_CHEST;

    public EnderChestInsertionNode(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
                                   ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(onPlace());
        addItemHandler(onInteract());
    }

    @Override
    public void preRegister() {
        this.addItemHandler(new BlockTicker() {
            public void tick(Block b, SlimefunItem sf, Config data) {
                EnderChestInsertionNode.this.tick(b);
            }

            public boolean isSynchronized() {
                return true;
            }
        });
    }

    private void tick(@Nonnull Block b) {
        BlockFace face;

        if (b.getRelative(BlockFace.NORTH).getType() == material) {
            face = BlockFace.SOUTH;

        } else if (b.getRelative(BlockFace.SOUTH).getType() == material) {
            face = BlockFace.NORTH;


        } else if (b.getRelative(BlockFace.EAST).getType() == material) {
            face = BlockFace.WEST;


        } else if (b.getRelative(BlockFace.WEST).getType() == material) {
            face = BlockFace.EAST;

        } else {
            return;
        }

        BlockState state = PaperLib.getBlockState(b.getRelative(face), false).getState();

        if (state instanceof InventoryHolder) {
            Player p = EnderChestNodeUtils.getOnlineOwner(b);

            if (p == null || !Utils.canOpen(b.getRelative(face), p)) {
                return;
            }

            Inventory containerInv = ((InventoryHolder) state).getInventory();
            EnderChestNodeUtils.moveFirstMatching(containerInv, p.getEnderChest(), item -> true);
        }
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Player p = e.getPlayer();
                Block b = e.getBlock();

                if (!e.isCancelled()) {
                    BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
                    BlockStorage.addBlockInfo(b, "playername", p.getDisplayName());
                    Utils.send(p, "&aEnder Chest Insertion Node registered to " + p.getDisplayName()
                        + " &7(UUID: " + p.getUniqueId() + ")");
                }
            }
        };
    }

    private BlockUseHandler onInteract() {
        return e -> {
            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();
            Utils.send(p, "&eThis Ender Chest Insertion Node belongs to " +
                BlockStorage.getLocationInfo(b.getLocation(), "playername")
                + " &7(UUID: " + BlockStorage.getLocationInfo(b.getLocation(), "owner") + ")");
        };
    }
}
