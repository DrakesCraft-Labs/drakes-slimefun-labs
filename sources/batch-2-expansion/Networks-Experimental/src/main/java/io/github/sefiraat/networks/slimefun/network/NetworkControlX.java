package io.github.sefiraat.networks.slimefun.network;

import dev.drake.sefilib.misc.ParticleUtils;
import dev.drake.sefilib.world.LocationUtils;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.utils.ItemCreator;
import io.github.sefiraat.networks.utils.Theme;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.blocks.BlockPosition;
import dev.drake.dough.protection.Interaction;
import dev.drake.dough.protection.ProtectionManager;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NetworkControlX extends NetworkDirectional {

    public static final ItemStack TEMPLATE_BACKGROUND_STACK = ItemCreator.create(
            Material.BLUE_STAINED_GLASS_PANE,
            Theme.PASSIVE + "Cut items matching template.",
            Theme.PASSIVE + "Leaving blank will cut anything"
    );
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 15, 17, 18, 20, 22, 23, 24, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int[] TEMPLATE_BACKGROUND = new int[]{16};
    private static final int TEMPLATE_SLOT = 25;
    private static final int NORTH_SLOT = 11;
    private static final int SOUTH_SLOT = 29;
    private static final int EAST_SLOT = 21;
    private static final int WEST_SLOT = 19;
    private static final int UP_SLOT = 14;
    private static final int DOWN_SLOT = 32;
    private static final int REQUIRED_POWER = 100;
    private static final Particle.DustOptions DUST_OPTIONS = new Particle.DustOptions(Color.GRAY, 1);
    private final Set<BlockPosition> blockCache = new HashSet<>();

    public NetworkControlX(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.CUTTER);
    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        if (blockMenu != null) {
            tryBreakBlock(blockMenu);
        }
    }

    @Override
    protected void onUniqueTick() {
        this.blockCache.clear();
    }

    private void tryBreakBlock(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        if (definition.getNode().getRoot().getRootPower() < REQUIRED_POWER) {
            return;
        }

        final BlockFace direction = getCurrentDirection(blockMenu);

        if (direction == BlockFace.SELF) {
            return;
        }

        final Block targetBlock = blockMenu.getBlock().getRelative(direction);
        final BlockPosition targetPosition = new BlockPosition(targetBlock);

        if (this.blockCache.contains(targetPosition)) {
            return;
        }

        final Material material = targetBlock.getType();

        if (material.getHardness() < 0 || material.isAir()) {
            return;
        }

        final SlimefunItem slimefunItem = BlockStorage.check(targetBlock);

        if (slimefunItem != null) {
            return;
        }

        final ItemStack templateStack = blockMenu.getItemInSlot(TEMPLATE_SLOT);
        boolean hasTemplate = templateStack != null && !templateStack.getType().isAir();

        if (hasTemplate) {
            if (targetBlock.getType() != templateStack.getType() || SlimefunItem.getByItem(templateStack) != null) {
                return;
            }
        }

        final UUID uuid = UUID.fromString(BlockStorage.getLocationInfo(blockMenu.getLocation(), OWNER_KEY));
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if (!Slimefun.getProtectionManager().hasPermission(offlinePlayer, targetBlock, Interaction.BREAK_BLOCK)) {
            return;
        }

        if (material == Material.CHEST
                || material == Material.TRAPPED_CHEST
                || material == Material.ENDER_CHEST
                || material == Material.BARREL
                || material == Material.SHULKER_BOX
                || material.name().endsWith("_SHULKER_BOX")
                || material.name().endsWith("_SHELF")
                || material == Material.FURNACE
                || material == Material.BLAST_FURNACE
                || material == Material.SMOKER
                || material == Material.HOPPER
                || material == Material.DROPPER
                || material == Material.DISPENSER
                || material == Material.BREWING_STAND
        ) {
            return;
        }

        final ItemStack resultStack = new ItemStack(material, 1);

        definition.getNode().getRoot().addItemStack0(blockMenu.getBlock().getLocation(), resultStack);

        if (resultStack.getAmount() == 0) {
            this.blockCache.add(targetPosition);

            Bukkit.getScheduler().runTask(Networks.getInstance(), bukkitTask -> {
                targetBlock.setType(Material.AIR);

                ParticleUtils.displayParticleRandomly(
                    LocationUtils.centre(targetBlock.getLocation()),
                    1.0,
                    5,
                    DUST_OPTIONS
                );

                definition.getNode().getRoot().removeRootPower(REQUIRED_POWER);
            });
        }
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @Nullable
    @Override
    protected int[] getOtherBackgroundSlots() {
        return TEMPLATE_BACKGROUND;
    }

    @Nullable
    @Override
    protected ItemStack getOtherBackgroundStack() {
        return TEMPLATE_BACKGROUND_STACK;
    }

    @Override
    public int getNorthSlot() {
        return NORTH_SLOT;
    }

    @Override
    public int getSouthSlot() {
        return SOUTH_SLOT;
    }

    @Override
    public int getEastSlot() {
        return EAST_SLOT;
    }

    @Override
    public int getWestSlot() {
        return WEST_SLOT;
    }

    @Override
    public int getUpSlot() {
        return UP_SLOT;
    }

    @Override
    public int getDownSlot() {
        return DOWN_SLOT;
    }

    @Override
    public int[] getItemSlots() {
        return new int[]{TEMPLATE_SLOT};
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return DUST_OPTIONS;
    }
}
