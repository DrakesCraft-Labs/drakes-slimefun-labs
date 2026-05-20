package me.voper.slimeframe.implementation.items.machines;

import java.util.*;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.blocks.BlockPosition;
import dev.drake.dough.items.CustomItemStack;
import dev.drake.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.utils.tags.SlimefunTag;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractMachine;
import me.voper.slimeframe.implementation.items.multiblocks.Foundry;
import me.voper.slimeframe.utils.ChatUtils;
import me.voper.slimeframe.utils.Cuboid;
import me.voper.slimeframe.utils.MachineUtils;
import me.voper.slimeframe.utils.Utils;

import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import net.md_5.bungee.api.ChatColor;

@ParametersAreNonnullByDefault
public class ChunkEater extends AbstractMachine {

    private static final int TIME = Utils.secondsToSfTicks(1);

    private static final Map<BlockPosition, Integer> PROGRESS_MAP = new HashMap<>();
    private static final Map<BlockPosition, UUID> OWNERS_MAP = new HashMap<>();
    private static final Map<BlockPosition, PeekingIterator<Block>> ITERATOR_MAP = new HashMap<>();

    private static final Comparator<Block> blockComparator = Comparator
            .comparing(Block::getY, Comparator.reverseOrder())
            .thenComparing(Block::getX)
            .thenComparing(Block::getZ);

    private boolean collectDestroyedBlocks = false;
    private Mode mode = Mode.LINE;

    public ChunkEater(SlimefunItemStack item, ItemStack[] recipe) {
        super(Groups.MACHINES, item, Foundry.RECIPE_TYPE, recipe);
    }

    @Override
    protected boolean process(BlockMenu menu, Block b) {
        final BlockPosition blockPosition = new BlockPosition(b);
        int progress = PROGRESS_MAP.getOrDefault(blockPosition, 0);
        PeekingIterator<Block> iterator = ITERATOR_MAP.get(blockPosition);

        if (!iterator.hasNext()) {
            menu.replaceExistingItem(getStatusSlot(), MachineUtils.FINISHED);
            return false;
        }

        if (progress >= TIME) {
            List<Block> line = new ArrayList<>();
            Block initial = iterator.next();
            iterator.remove();
            line.add(initial);

            while (iterator.hasNext()) {
                Block peek = iterator.peek();

                if (mode == Mode.LINE && (initial.getY() != peek.getY() || initial.getX() != peek.getX())) {
                    break;
                } else if (initial.getY() != peek.getY()) {
                    break;
                }

                Block next = iterator.next();
                line.add(next);
                iterator.remove();
            }

            for (Block block : line) {
                if (!canBreak(blockPosition, block)) continue;

                if (collectDestroyedBlocks) {
                    ItemStack blockItem = new ItemStack(block.getType());
                    if (menu.fits(blockItem, getOutputSlots())) {
                        menu.pushItem(blockItem, getOutputSlots());
                        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.TNT, ChatColor.GREEN + "Working..."));
                    } else {
                        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), MachineUtils.NO_SPACE);
                    }
                }

                block.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, block.getLocation(), 1);
                block.getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                block.setType(Material.AIR);
            }

            line.clear();

            PROGRESS_MAP.put(blockPosition, 0);
            ITERATOR_MAP.put(blockPosition, iterator);
            return true;
        }

        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.TNT, ChatColor.GREEN + "Working..."));
        PROGRESS_MAP.put(blockPosition, ++progress);
        ITERATOR_MAP.put(blockPosition, iterator);
        return true;
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        super.onNewInstance(menu, b);

        Chunk chunk = b.getChunk();
        if (BlockStorage.hasChunkInfo(chunk.getWorld(), chunk.getX(), chunk.getZ())) {
            int chunkEaterAmount = getChunkInfo(chunk);
            if (chunkEaterAmount > 1) {
                b.breakNaturally();
                return;
            }
        }

        String locationInfo = BlockStorage.getLocationInfo(b.getLocation(), "owner");
        if (locationInfo == null) return;

        final BlockPosition blockPosition = new BlockPosition(b);
        OWNERS_MAP.put(blockPosition, UUID.fromString(locationInfo));

        Location corner1 = new Location(chunk.getWorld(), chunk.getX() << 4, b.getY() - 1, chunk.getZ() << 4);
        Location corner2 = corner1.clone().add(15, 0, 15);
        corner2.setY(-64);

        Cuboid cuboid = new Cuboid(corner1, corner2);
        List<Block> blockList = cuboid.blockList().stream()
                .filter(block -> canBreak(blockPosition, block))
                .filter(block -> !block.getType().isAir())
                .sorted(blockComparator)
                .toList();

        ITERATOR_MAP.put(blockPosition, Iterators.peekingIterator(new ArrayList<>(blockList).iterator()));

    }

    protected boolean canBreak(BlockPosition bp, Block block) {
        return Slimefun.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(OWNERS_MAP.get(bp)), block, Interaction.BREAK_BLOCK) &&
                !(block.getState() instanceof InventoryHolder) &&
                !SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(block.getType()) &&
                !SlimefunTag.COMMAND_BLOCKS.isTagged(block.getType()) &&
                !SlimefunTag.SENSITIVE_MATERIALS.isTagged(block.getType()) &&
                BlockStorage.check(block.getLocation()) == null &&
                !block.getType().isAir() &&
                block.getType().isSolid();
    }

    @Override
    protected boolean synchronous() {
        return true;
    }

    @Override
    protected void onPlace(BlockPlaceEvent e, Block b) {
        super.onPlace(e, b);

        Chunk chunk = b.getChunk();
        if (BlockStorage.hasChunkInfo(chunk.getWorld(), chunk.getX(), chunk.getZ())) {
            int chunkEaterAmount = getChunkInfo(chunk);
            if (chunkEaterAmount == 0) {
                setChunkInfo(chunk, 1);
            } else {
                e.setCancelled(true);
                BlockStorage.clearBlockInfo(b);
                ChatUtils.sendMessage(e.getPlayer(), ChatColor.RED + "You can only place 1 Chunk Eater per chunk");
                return;
            }
        } else {
            setChunkInfo(chunk, 1);
        }

        Player player = e.getPlayer();
        BlockStorage.addBlockInfo(b, "owner", player.getUniqueId().toString());
    }

    @Override
    protected void onBreak(BlockBreakEvent e, BlockMenu menu, Location l) {
        super.onBreak(e, menu, l);

        Chunk chunk = l.getChunk();
        setChunkInfo(chunk, getChunkInfo(chunk) - 1);
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53});
        preset.addItem(getStatusSlot(), MachineUtils.STATUS);
    }

    public ChunkEater setCollectDestroyedBlocks() {
        this.collectDestroyedBlocks = true;
        return this;
    }

    public ChunkEater setLayerMode() {
        this.mode = Mode.LAYER;
        return this;
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    public int getStatusSlot() {
        return 4;
    }

    protected void setChunkInfo(Chunk chunk, int value) {
        BlockStorage.setChunkInfo(chunk.getWorld(), chunk.getX(), chunk.getZ(), "chunkEaterAmount", String.valueOf(value));
    }

    protected int getChunkInfo(Chunk chunk) {
        String chunkEaterAmount = BlockStorage.getChunkInfo(chunk.getWorld(), chunk.getX(), chunk.getZ(), "chunkEaterAmount");
        return chunkEaterAmount == null ? 0 : Integer.parseInt(chunkEaterAmount);
    }

    public enum Mode {
        LINE,
        LAYER
    }

}
