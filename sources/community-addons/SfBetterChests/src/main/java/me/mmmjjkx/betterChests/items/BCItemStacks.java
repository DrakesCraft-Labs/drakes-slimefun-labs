package me.mmmjjkx.betterChests.items;

import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft-labs.slimefun4.utils.LoreBuilder;
import me.mmmjjkx.betterChests.BetterChests;
import me.mmmjjkx.betterChests.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BCItemStacks {
    public static final ItemStack TIP = new ItemStackBuilder(Material.PAPER, BetterChests.INSTANCE.getLang().getMsg("items.tip.name"), BetterChests.INSTANCE.getLang().getMsgList("items.tip.lore")).getItemStack();

    public static final ItemStack GROUP_MAIN_ITEM = new ItemStackBuilder(Material.CHEST, BetterChests.INSTANCE.getLang().getMsg("groups.main")).getItemStack();
    public static final ItemStack GROUP_STORAGE_ITEM = new ItemStackBuilder(Material.BARREL, BetterChests.INSTANCE.getLang().getMsg("groups.storages")).getItemStack();
    public static final ItemStack GROUP_TOOL_ITEM = new ItemStackBuilder(Material.BLAZE_ROD, BetterChests.INSTANCE.getLang().getMsg("groups.tool_machines")).getItemStack();
    public static final ItemStack GROUP_MATERIAL_ITEM = new ItemStackBuilder(Material.IRON_INGOT, BetterChests.INSTANCE.getLang().getMsg("groups.materials")).getItemStack();
    public static final ItemStack GROUP_CARGO_ITEM = new ItemStackBuilder(Material.CHEST, BetterChests.INSTANCE.getLang().getMsg("groups.cargo")).getItemStack();

    public static final ItemStack GEAR_WHEEL = new ItemStackBuilder(Material.REDSTONE, BetterChests.INSTANCE.getLang().getMsg("items.gear_wheel.name"), BetterChests.INSTANCE.getLang().getMsg("items.gear_wheel.lore")).getItemStack();
    public static final ItemStack TIGHTLY_BLISTERING_INGOT = new ItemStackBuilder(Material.IRON_INGOT, BetterChests.INSTANCE.getLang().getMsg("items.tightly_blistering_ingot.name"), BetterChests.INSTANCE.getLang().getMsg("items.tightly_blistering_ingot.lore"), LoreBuilder.radioactive(Radioactivity.VERY_HIGH)).getItemStack();
    public static final ItemStack TIGHTLY_BLISTERING_PLATE = new ItemStackBuilder(Material.IRON_BLOCK, "&eTightly Blistering Plate", "&7A high-hardness metal plate", LoreBuilder.radioactive(Radioactivity.HIGH)).getItemStack();

    public static final ItemStack LOCATION_RECORDER = new ItemStackBuilder(Material.COMPASS, "&aLocation Recorder", "&bShift + Right-click to record location", "&bRight-click to open the corresponding machine").getItemStack();
    public static final ItemStack CHEST_COLOR_CHANGER = new ItemStackBuilder(Material.AMETHYST_SHARD, "&aChest Color Changer", "&aRight-click to change the color of the BC chest", "&aShift + Right-click to cycle through colors", "", "&dCurrent color: &6NoColor", "", LoreBuilder.powerCharged(0, 200)).getItemStack();

    public static final ItemStack CHEST_DISASSEMBLER = new ItemStackBuilder(Material.YELLOW_CONCRETE, "&cChest Disassembler", "&e&lDisassemble BC chests", LoreBuilder.powerBuffer(128), LoreBuilder.powerPerSecond(16)).getItemStack();

    public static final ItemStack DRAWER_1 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 1", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 1024").getItemStack();
    public static final ItemStack DRAWER_2 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 2", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 4096").getItemStack();
    public static final ItemStack DRAWER_3 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 3", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 16384").getItemStack();
    public static final ItemStack DRAWER_4 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 4", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 262K").getItemStack();
    public static final ItemStack DRAWER_5 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 5", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 1M").getItemStack();
    public static final ItemStack DRAWER_6 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 6", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 4M").getItemStack();
    public static final ItemStack DRAWER_7 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 7", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 16M").getItemStack();
    public static final ItemStack DRAWER_8 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 8", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 64M").getItemStack();
    public static final ItemStack DRAWER_9 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 9", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 256M").getItemStack();
    public static final ItemStack DRAWER_10 = new ItemStackBuilder(Material.BARREL, "&aDrawer &6Lvl 10", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 1B").getItemStack();
    public static final ItemStack DRAWER_MAX = new ItemStackBuilder(Material.BARREL, "&aDrawer &6MAX", "&bRight-click grab 64 items (when your hand is empty)", "&bRight-click to store items that you hand in", "&dCapacity: 2.14B").getItemStack();

    public static final ItemStack IE_STORAGE_UNIT_1 = new ItemStackBuilder(Material.OAK_LOG, "&eIE Storage Unit &6Lvl 1", "&bInfinityExpansion-like storage unit", "&dCapacity: 32,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_2 = new ItemStackBuilder(Material.DARK_OAK_LOG, "&eIE Storage Unit &6Lvl 2", "&bInfinityExpansion-like storage unit", "&dCapacity: 128,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_3 = new ItemStackBuilder(Material.ACACIA_LOG, "&eIE Storage Unit &6Lvl 3", "&bInfinityExpansion-like storage unit", "&dCapacity: 1,000,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_4 = new ItemStackBuilder(Material.BIRCH_LOG, "&eIE Storage Unit &6Lvl 4", "&bInfinityExpansion-like storage unit", "&dCapacity: 4,000,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_5 = new ItemStackBuilder(Material.JUNGLE_LOG, "&eIE Storage Unit &6Lvl 5", "&bInfinityExpansion-like storage unit", "&dCapacity: 64,000,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_6 = new ItemStackBuilder(Material.MANGROVE_LOG, "&eIE Storage Unit &6Lvl 6", "&bInfinityExpansion-like storage unit", "&dCapacity: 256,000,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_7 = new ItemStackBuilder(Material.CRIMSON_STEM, "&eIE Storage Unit &6Lvl 7", "&bInfinityExpansion-like storage unit", "&dCapacity: 1,000,000,000").getItemStack();
    public static final ItemStack IE_STORAGE_UNIT_8 = new ItemStackBuilder(Material.WARPED_STEM, "&eIE Storage Unit &6Lvl 8", "&bInfinityExpansion-like storage unit", "&dCapacity: 2,140,000,000").getItemStack();

    public static final ItemStack POINT_TO_POINT_TRANSFER = new ItemStackBuilder(Material.END_ROD, "&aPoint-to-Point Transfer", "&b&lA great tool for transferring items between two blocks").getItemStack();
}
