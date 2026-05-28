package me.voper.slimeframe.implementation.items.tools;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.libraries.dough.skins.PlayerHead;
import com.github.drakescraft_labs.slimefun4.libraries.dough.skins.PlayerSkin;
import com.github.drakescraft_labs.slimefun4.utils.NumberUtils;

import me.voper.slimeframe.SlimeFrame;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractTickingContainer;
import me.voper.slimeframe.utils.ArmorStandUtils;
import me.voper.slimeframe.utils.HeadTextures;

import lombok.Getter;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import net.md_5.bungee.api.ChatColor;

@Getter
@ParametersAreNonnullByDefault
public class ItemProjector extends AbstractTickingContainer {

    private static final Map<BlockPosition, Boolean> IS_SPINNING_MAP = new HashMap<>();
    private static final Map<BlockPosition, ItemStack> HOLOGRAM_MAP = new HashMap<>();
    private static final Map<BlockPosition, BukkitTask> SPINNING_TASK_MAP = new HashMap<>();

    private static final ItemStack INCREMENT_HEIGHT = new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64(HeadTextures.GRAY_PLUS)), ChatColor.AQUA + "Click to increment the height");
    private static final ItemStack DECREMENT_HEIGHT = new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromBase64(HeadTextures.GRAY_MINUS)), ChatColor.AQUA + "Click to decrement the height");
    private static final ItemStack SPIN = new CustomItemStack(Material.MUSIC_DISC_5, ChatColor.AQUA + "Click to start/stop spinning");
    private static final ItemStack SIZE = new CustomItemStack(Material.BONE_MEAL, ChatColor.AQUA + "Click to increase/decrease the size");

    private static final String OFFSET_KEY = "offset";
    private static final String SPINNING_KEY = "spinning";

    private final int itemSlot = 22;
    private final int spinSlot = 19;
    private final int sizeSlot = 25;
    private final int decrementHeightSlot = 3;
    private final int incrementHeightSlot = 5;

    public ItemProjector(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(Groups.UTILS_AND_TOOLS, item, recipeType, recipe);
    }

    @Override
    protected void tick(BlockMenu menu, Block b) {
        final BlockPosition bp = new BlockPosition(b);
        ArmorStand armorStand = getArmorStand(b, true);

        ItemStack previousItem = HOLOGRAM_MAP.get(bp);
        ItemStack currentItem = menu.getItemInSlot(getItemSlot());

        if (previousItem == null) {
            if (currentItem == null || currentItem.getType() == Material.AIR) return;
            armorStand.setHelmet(currentItem);
        } else if (!previousItem.equals(currentItem)) {
            armorStand.setHelmet(currentItem);
        }

        HOLOGRAM_MAP.put(bp, currentItem);
    }

    @Override
    protected void createMenu(@Nonnull BlockMenuPreset preset) {
        preset.drawBackground(new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE, " "), new int[]{12, 13, 14, 21, 23, 30, 31, 32});
        preset.drawBackground(new int[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 15, 16, 17,
                18, 19, 20, 24, 25, 26,
                27, 28, 29, 33, 34, 35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        });
        preset.addItem(getDecrementHeightSlot(), DECREMENT_HEIGHT);
        preset.addItem(getIncrementHeightSlot(), INCREMENT_HEIGHT);
        preset.addItem(getSpinSlot(), SPIN);
        preset.addItem(getSizeSlot(), SIZE);
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        super.onNewInstance(menu, b);
        final BlockPosition bp = new BlockPosition(b);

        String spinning = BlockStorage.getLocationInfo(b.getLocation(), SPINNING_KEY);
        if (spinning != null) {
            Boolean isSpinning = Boolean.parseBoolean(spinning);
            IS_SPINNING_MAP.put(bp, isSpinning);
        }

        menu.addMenuClickHandler(getSpinSlot(), (player, i, itemStack, clickAction) -> {
            Boolean isSpinning = !IS_SPINNING_MAP.get(bp);
            IS_SPINNING_MAP.put(bp, isSpinning);
            BlockStorage.addBlockInfo(b, SPINNING_KEY, isSpinning.toString());

            BukkitTask spinningTask = SPINNING_TASK_MAP.get(bp);
            if (isSpinning) {
                if (spinningTask == null || spinningTask.isCancelled()) {
                    spinningTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ArmorStand armorStand = getArmorStand(b, true);
                            armorStand.setRotation(armorStand.getLocation().getYaw() + 25f, armorStand.getLocation().getPitch() + 25f);
                        }
                    }.runTaskTimer(SlimeFrame.getInstance(), 0L, 5L);
                }
            } else {
                if (spinningTask != null && !spinningTask.isCancelled()) {
                    spinningTask.cancel();
                }
            }

            SPINNING_TASK_MAP.put(bp, spinningTask);
            return false;
        });

        menu.addMenuClickHandler(getSizeSlot(), (player, i, itemStack, clickAction) -> {
            ArmorStand armorStand = getArmorStand(b, true);
            if (armorStand != null) {
                armorStand.setSmall(!armorStand.isSmall());
            }
            return false;
        });

        menu.addMenuClickHandler(getIncrementHeightSlot(), (p, i, itemStack, clickAction) -> addHeight(b, 0.1F));
        menu.addMenuClickHandler(getDecrementHeightSlot(), (p, i, itemStack, clickAction) -> addHeight(b, -0.1F));

        HOLOGRAM_MAP.put(bp, menu.getItemInSlot(getItemSlot()));
    }

    private boolean addHeight(Block b, float value) {
        ArmorStand armorStand = getArmorStand(b, true);
        if (armorStand == null) return false;
        double offset = NumberUtils.reparseDouble(Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), OFFSET_KEY)) + value);
        Location l = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + offset, b.getZ() + 0.5);
        armorStand.teleport(l);
        BlockStorage.addBlockInfo(b, OFFSET_KEY, String.valueOf(offset));
        return false;
    }

    @Override
    protected void onPlace(BlockPlaceEvent e, Block b) {
        super.onPlace(e, b);
        BlockStorage.addBlockInfo(b, OFFSET_KEY, "0.5");
        BlockStorage.addBlockInfo(b, SPINNING_KEY, Boolean.toString(false));
        getArmorStand(b, true);
    }

    @Override
    protected void onBreak(BlockBreakEvent e, BlockMenu menu, Location l) {
        super.onBreak(e, menu, l);

        BukkitTask bukkitTask = SPINNING_TASK_MAP.get(new BlockPosition(menu.getBlock()));
        if (bukkitTask != null) bukkitTask.cancel();

        menu.dropItems(l, getItemSlot());
        killArmorStand(menu.getBlock());
    }

    @Nullable
    private static ArmorStand getArmorStand(@Nonnull Block itemProjector, boolean createIfNoneExists) {
        double offset = Double.parseDouble(BlockStorage.getLocationInfo(itemProjector.getLocation(), OFFSET_KEY));
        Location l = new Location(itemProjector.getWorld(), itemProjector.getX() + 0.5, itemProjector.getY() + offset, itemProjector.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand armorStand && l.distanceSquared(n.getLocation()) < 0.4) {
                return armorStand;
            }
        }

        if (!createIfNoneExists) {
            return null;
        }

        return ArmorStandUtils.spawnArmorStand(l);
    }

    private static void killArmorStand(@Nonnull Block b) {
        ArmorStand hologram = getArmorStand(b, false);

        if (hologram != null) {
            hologram.remove();
        }
    }

    @Override
    public int getStatusSlot() {
        return -1;
    }

    @Override
    protected int[] slotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Nonnull
    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

}
