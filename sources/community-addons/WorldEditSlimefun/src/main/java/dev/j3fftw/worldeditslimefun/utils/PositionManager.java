package dev.j3fftw.worldeditslimefun.utils;

import com.github.drakescraft-labs.slimefun4.libraries.dough.blocks.BlockPosition;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PositionManager {

    @Nonnull
    private static final Map<UUID, BlockPosition> STORED_POSITION_ONE = new HashMap<>();
    @Nonnull
    private static final Map<UUID, BlockPosition> STORED_POSITION_TWO = new HashMap<>();

    public static void addPositionOne(@Nonnull Player player) {
        addPositionOne(player, new BlockPosition(player.getLocation()));
    }

    @ParametersAreNonnullByDefault
    public static void addPositionOne(Player player, BlockPosition position) {
        STORED_POSITION_ONE.put(player.getUniqueId(), position);
        player.sendMessage("Set position 1 to " + Utils.beautifyBlockPosition(position));
    }

    public static void addPositionTwo(@Nonnull Player player) {
        addPositionTwo(player, new BlockPosition(player.getLocation()));
    }

    @ParametersAreNonnullByDefault
    public static void addPositionTwo(Player player, BlockPosition position) {
        STORED_POSITION_TWO.put(player.getUniqueId(), position);
        player.sendMessage("Set position 2 to " + Utils.beautifyBlockPosition(position));
    }

    @Nullable
    public static BlockPosition getPositionOne(@Nonnull Player player) {
        return STORED_POSITION_ONE.get(player.getUniqueId());
    }

    @Nullable
    public static BlockPosition getPositionTwo(@Nonnull Player player) {
        return STORED_POSITION_TWO.get(player.getUniqueId());
    }
}
