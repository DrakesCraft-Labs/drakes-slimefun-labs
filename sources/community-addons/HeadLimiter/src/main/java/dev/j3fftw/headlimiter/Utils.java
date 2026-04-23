package dev.j3fftw.headlimiter;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.palmergames.bukkit.towny.TownyAPI;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class Utils {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(
        HeadLimiter.getInstance().getConfig().getInt("thread-pool-size", 4),
        new ThreadFactoryBuilder().setNameFormat("HeadLimiter-pool-%d").build()
    );

    private static final Set<String> permissionNodes = new LinkedHashSet<>();
    private static final String PERMISSION_PREFIX = "headlimiter.permission.";
    private static final String BYPASS_PERMISSION = "headlimiter.bypass";

    private Utils() {}

    protected static void loadPermissions() {
        final ConfigurationSection permissionSection = HeadLimiter.getInstance().getConfig()
            .getConfigurationSection("permission");
        if (permissionSection != null) {
            permissionNodes.addAll(permissionSection.getKeys(false));
        }
    }

    public static int getMaxHeads(@Nonnull Player player) {
        if (permissionsEnabled() && !permissionNodes.isEmpty()) {
            for (String permissionNode : permissionNodes) {
                if (player.hasPermission(PERMISSION_PREFIX + permissionNode)) {
                    return HeadLimiter.getInstance().getConfig().getInt("permission." + permissionNode);
                }
            }
        }
        return getDefaultMax();
    }

    public static boolean permissionsEnabled() {
        return HeadLimiter.getInstance().getConfig().getBoolean("permissions", false);
    }

    public static boolean canBypass(@Nonnull Player player) {
        return player.hasPermission(BYPASS_PERMISSION);
    }

    public static int getDefaultMax() {
        return HeadLimiter.getInstance().getConfig().getInt("amount", 25);
    }

    public static void count(@Nonnull Chunk chunk, @Nonnull Consumer<CountResult> consumer) {
        final BlockState[] tileEntities = chunk.getTileEntities();

        SERVICE.submit(() -> {
            final Map<String, Integer> counts = new HashMap<>();
            int total = 0;
            for (BlockState state : tileEntities) {
                final SlimefunItem slimefunItem = BlockStorage.check(state.getLocation());
                //TODO remove is cargo check
                if (slimefunItem != null && HeadLimiter.getInstance().isCargo(slimefunItem)) {
                    counts.merge(slimefunItem.getId(), 1, Integer::sum);
                    total++;
                }
            }

            consumer.accept(new CountResult(total, counts));
        });
    }

    @ParametersAreNonnullByDefault
    public static void onCheck(Player player, Block block, int maxAmount, int count, SlimefunItem sfItem) {
        boolean isPlacingRestricted = isPlacingRestricted(block);
        if (count > maxAmount || isPlacingRestricted) {
            Bukkit.getScheduler().runTask(HeadLimiter.getInstance(), () -> {
                if (block.getType() != Material.AIR) {
                    block.setType(Material.AIR);
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        block.getWorld().dropItemNaturally(block.getLocation(), sfItem.getItem());
                    }
                }
            });

            BlockStorage.clearBlockInfo(block.getLocation());
            if (isPlacingRestricted) {
                player.sendMessage(ChatColor.RED + "You can't place Cargo nodes in unclaimed areas!");
            } else {
                player.sendMessage(ChatColor.RED + "You hit the limit of Cargo nodes in this chunk");
            }
        }
    }

    /**
     * Whether the block placement outside claimed areas is prohibited or not by protection plugins
     * @param block The block to be checked
     * @return Whether the placement is prohibited or not
     */
    public static boolean isPlacingRestricted(@Nonnull Block block) {
        if (HeadLimiter.getInstance().getConfig().getBoolean("block-wilderness-cargo", false)) {
            boolean isTownyWilderness = Bukkit.getServer().getPluginManager().isPluginEnabled("Towny")
                    && TownyAPI.getInstance().isWilderness(block);

            // This is intentionally redundant to allow for expandability by adding more booleans and returning their || chain
            return isTownyWilderness;
        } else {
            return false;
        }
    }
}
