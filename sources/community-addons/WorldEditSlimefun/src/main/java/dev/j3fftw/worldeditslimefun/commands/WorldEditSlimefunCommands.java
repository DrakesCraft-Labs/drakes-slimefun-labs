package dev.j3fftw.worldeditslimefun.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.j3fftw.worldeditslimefun.WorldEditSlimefun;
import dev.j3fftw.worldeditslimefun.commands.flags.CommandFlag;
import dev.j3fftw.worldeditslimefun.commands.flags.CommandFlags;
import dev.j3fftw.worldeditslimefun.utils.PositionManager;
import dev.j3fftw.worldeditslimefun.utils.Utils;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import dev.drake.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
@CommandAlias("wesf|sfedit")
@CommandPermission("wesf.admin")
public class WorldEditSlimefunCommands extends BaseCommand {

    public static void init(WorldEditSlimefun plugin) {
        PaperCommandManager manager = new PaperCommandManager(plugin);
        CommandCompletions<BukkitCommandCompletionContext> completions = manager.getCommandCompletions();
        CommandContexts<BukkitCommandExecutionContext> contexts = manager.getCommandContexts();

        completions.registerStaticCompletion("slimefun_blocks", Utils.SLIMEFUN_BLOCKS);
        completions.registerAsyncCompletion("command_flags", context -> {
            List<String> args = new ArrayList<>(Arrays.asList(context.getContextValueByName(String[].class, "commandFlags")));
            List<String> availableFlags = new ArrayList<>(CommandFlags.getFlagTypes().keySet());
            availableFlags.removeAll(args);

            if (args.isEmpty()) {
                return availableFlags;
            }

            String currentArg = args.remove(args.size() - 1);
            if (args.isEmpty()) {
                return availableFlags;
            }

            String lastArg = args.get(args.size() - 1);
            if (CommandFlags.getFlagTypes().containsKey(lastArg)) {
                return CommandFlags.getFlagTypes().get(lastArg).getTabSuggestions(context);
            }

            if (args.size() % 2 == 0) {
                return availableFlags;
            }

            return List.of("invalid_flag");
        });

        manager.registerCommand(new WorldEditSlimefunCommands());
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatColor.RED + "Please provide a valid subcommand.");
    }

    @Subcommand("wand")
    public void onWand(Player player) {
        ItemStack wand = SlimefunItem.getOptionalById("WESF_WAND").map(SlimefunItem::getItem).orElse(null);

        // This should never be reached
        if (wand == null) {
            player.sendMessage(ChatColor.RED + "Wand not found!");
            return;
        }

        player.getInventory().addItem(wand);
    }

    @Subcommand("pos1")
    public void onPos1(Player player) {
        PositionManager.addPositionOne(player);
    }

    @Subcommand("pos2")
    public void onPos2(Player player) {
        PositionManager.addPositionTwo(player);
    }

    @Subcommand("paste")
    @CommandCompletion("@slimefun_blocks @command_flags")
    public void paste(Player player, @Default("INVALID") String sfId, String[] commandFlags) {
        BlockPosition pos1 = PositionManager.getPositionOne(player);
        BlockPosition pos2 = PositionManager.getPositionTwo(player);
        if (pos1 == null || pos2 == null) {
            player.sendMessage(ChatColor.RED + "Select two positions first!");
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getById(sfId);
        if (sfItem == null || sfItem instanceof UnplaceableBlock) {
            player.sendMessage(ChatColor.RED + "Invalid Slimefun item!");
            return;
        }

        List<CommandFlag<?>> flags = CommandFlags.getFlags(Arrays.asList(commandFlags));
        flags.removeIf(flag -> flag == null || !flag.canApply(sfItem));

        ItemStack item = sfItem.getItem();
        long start = System.currentTimeMillis();
        int amountOfBlocks = loopThroughSelection(pos1, pos2, block -> {
            if (BlockStorage.hasBlockInfo(block)) {
                BlockStorage.deleteLocationInfoUnsafely(block.getLocation(), true);
            }

            block.setType(item.getType());
            BlockStorage.store(block, sfId);
            sfItem.callItemHandler(BlockPlaceHandler.class, handler -> {
                BlockPlaceEvent event = new BlockPlaceEvent(block, block.getState(),
                        block.getRelative(BlockFace.DOWN), item, player, true, EquipmentSlot.HAND);
                handler.onPlayerPlace(event);
            });

            for (CommandFlag<?> flag : flags) {
                flag.apply(player, flags, sfItem, block);
            }
        });
        long time = System.currentTimeMillis() - start;

        player.sendMessage("Pasted " + amountOfBlocks + " " + sfItem.getItemName() + ChatColor.WHITE + " (s)");
        player.sendMessage("Took " + time + "ms to paste!");
    }

    @Subcommand("clear")
    @CommandCompletion("true|false")
    public void clear(Player player, @Default("false") boolean callEvent) {
        BlockPosition pos1 = PositionManager.getPositionOne(player);
        BlockPosition pos2 = PositionManager.getPositionTwo(player);
        if (pos1 == null || pos2 == null) {
            return;
        }

        long start = System.currentTimeMillis();
        int amountOfBlocks = loopThroughSelection(pos1, pos2,  block -> {
            if (callEvent && BlockStorage.hasBlockInfo(block)) {
                SlimefunItem sfItem = BlockStorage.check(block);
                sfItem.callItemHandler(BlockBreakHandler.class, handler -> {
                    BlockBreakEvent event = new BlockBreakEvent(block, player);
                    handler.onPlayerBreak(event, new ItemStack(Material.AIR), new ArrayList<>());
                });
            }
            block.setType(Material.AIR);
            BlockStorage.deleteLocationInfoUnsafely(block.getLocation(), true);
        });
        long time = System.currentTimeMillis() - start;

        player.sendMessage("Cleared " + amountOfBlocks + " blocks");
        player.sendMessage("Took " + time + "ms to clear!");
    }

    /**
     * @param pos1 The first corner of the selection
     * @param pos2 The second corner of the selection
     * @param blockRunnable What should happen to every block
     * @return The amount of blocks acted upon
     */
    private int loopThroughSelection(BlockPosition pos1, BlockPosition pos2, Consumer<Block> blockRunnable) {
        int amountOfBlocks = 0;
        int upperX = Math.max(pos1.getX(), pos2.getX());
        int upperY = Math.max(pos1.getY(), pos2.getY());
        int upperZ = Math.max(pos1.getZ(), pos2.getZ());
        int lowerX = Math.min(pos1.getX(), pos2.getX());
        int lowerY = Math.min(pos1.getY(), pos2.getY());
        int lowerZ = Math.min(pos1.getZ(), pos2.getZ());
        for (int x = lowerX; x <= upperX; x++) {
            for (int z = lowerZ; z <= upperZ; z++) {
                for (int y = lowerY; y <= upperY; y++) {
                    blockRunnable.accept(pos1.getWorld().getBlockAt(x, y, z));
                    amountOfBlocks++;
                }
            }
        }
        return amountOfBlocks;
    }
}
