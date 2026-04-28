package io.github.thebusybiscuit.chestterminal;

import javax.annotation.Nonnull;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.chestterminal.items.AccessTerminal;
import io.github.thebusybiscuit.chestterminal.items.ExportBus;
import io.github.thebusybiscuit.chestterminal.items.ImportBus;
import io.github.thebusybiscuit.chestterminal.items.MilkyQuartz;
import io.github.thebusybiscuit.chestterminal.items.WirelessTerminal;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

/**
 * ChestTerminal is a Slimefun addon that adds a chest terminal system,
 * inspired by Applied Energistics. It allows remote access to items
 * stored in a Cargo network.
 *
 * @author TheBusyBiscuit
 */
public class ChestTerminal extends JavaPlugin implements SlimefunAddon {

    @Override
    public void onEnable() {
        // Setting up bStats
        new Metrics(this, 5503);

        SlimefunItemStack milkyQuartz = new SlimefunItemStack("MILKY_QUARTZ", Material.QUARTZ, "&fMilky Quartz");
        SlimefunItemStack ctPanel = new SlimefunItemStack("CT_PANEL", "7a44ff3a5f49c69cab676bad8d98a063fa78cfa61916fdef3e267557fec18283", "&3CT Illuminated Panel", "&7Crafting Component");

        SlimefunItemStack chestTerminal = new SlimefunItemStack("CHEST_TERMINAL", "7a44ff3a5f49c69cab676bad8d98a063fa78cfa61916fdef3e267557fec18283", "&3CT Access Terminal", "&7If this Block is connected to a", "&7Cargo Network, it will allow you to remotely", "&7interact with any Items supplied by", "&7Nodes tuned into the ChestTerminal Channel");
        SlimefunItemStack importBus = new SlimefunItemStack("CT_IMPORT_BUS", "113db2e7e72ea4432eefbd6e58a85eaa2423f83e642ca41abc6a9317757b889", "&3CT Import Bus", "&7If this Block is connected to a", "&7Cargo Network, it will pull any Items from", "&7the Inventory it is attached to and place it", "&7into the CT Network Channel");
        SlimefunItemStack exportBus = new SlimefunItemStack("CT_EXPORT_BUS", "113db2e7e72ea4432eefbd6e58a85eaa2423f83e642ca41abc6a9317757b889", "&3CT Export Bus", "&7If this Block is connected to a", "&7Cargo Network, it will pull any Items from", "&7the CT Network Channel and place these", "&7into the Inventory it is attached to");

        SlimefunItemStack wirelessTerminal16 = new SlimefunItemStack("CT_WIRELESS_ACCESS_TERMINAL_16", Material.ITEM_FRAME, "&3CT Wireless Access Terminal &b(16)", "&8\u21E8 &7Linked to: &cNowhere", "&8\u21E8 &7Range: &e16 Blocks", "&c&o&8\u21E8 &e\u26A1 &70 / 10 J", "", "&7If this Block is linked to an Access Terminal", "&7it will be able to remotely access that Terminal", "", "&7&eRight Click on an Access Terminal &7to link", "&7&eRight Click&7 to open the linked Terminal");
        SlimefunItemStack wirelessTerminal64 = new SlimefunItemStack("CT_WIRELESS_ACCESS_TERMINAL_64", Material.ITEM_FRAME, "&3CT Wireless Access Terminal &b(64)", "&8\u21E8 &7Linked to: &cNowhere", "&8\u21E8 &7Range: &e64 Blocks", "&c&o&8\u21E8 &e\u26A1 &70 / 25 J", "", "&7If this Block is linked to an Access Terminal", "&7it will be able to remotely access that Terminal", "", "&7&eRight Click on an Access Terminal &7to link", "&7&eRight Click&7 to open the linked Terminal");
        SlimefunItemStack wirelessTerminal128 = new SlimefunItemStack("CT_WIRELESS_ACCESS_TERMINAL_128", Material.ITEM_FRAME, "&3CT Wireless Access Terminal &b(128)", "&8\u21E8 &7Linked to: &cNowhere", "&8\u21E8 &7Range: &e128 Blocks", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "", "&7If this Block is linked to an Access Terminal", "&7it will be able to remotely access that Terminal", "", "&7&eRight Click on an Access Terminal &7to link", "&7&eRight Click&7 to open the linked Terminal");
        SlimefunItemStack wirelessTerminalTransdimensional = new SlimefunItemStack("CT_WIRELESS_ACCESS_TERMINAL_TRANSDIMENSIONAL", Material.ITEM_FRAME, "&3CT Wireless Access Terminal &b(Transdimensional)", "&8\u21E8 &7Linked to: &cNowhere", "&8\u21E8 &7Range: &eUnlimited", "&c&o&8\u21E8 &e\u26A1 &70 / 50 J", "", "&7If this Block is linked to an Access Terminal", "&7it will be able to remotely access that Terminal", "", "&7&eRight Click on an Access Terminal &7to link", "&7&eRight Click&7 to open the linked Terminal");

        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(this, "chest_terminal"), CustomItemStack.create(chestTerminal.item(), "&5Chest Terminal", "", "&a> Click to open"));

        new SlimefunItem(itemGroup, milkyQuartz, RecipeType.GEO_MINER,
                new ItemStack[0])
                .register(this);

        new SlimefunItem(itemGroup, ctPanel, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {milkyQuartz.item(), SlimefunItems.BLISTERING_INGOT_3.item(), milkyQuartz.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.REDSTONE_ALLOY.item(), milkyQuartz.item(), SlimefunItems.BLISTERING_INGOT_3.item(), milkyQuartz.item()})
                .register(this);

        new AccessTerminal(itemGroup, chestTerminal, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {milkyQuartz.item(), SlimefunItems.GPS_TRANSMITTER_3.item(), milkyQuartz.item(), SlimefunItems.POWER_CRYSTAL.item(), ctPanel.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.ENERGY_REGULATOR.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(this);

        new ImportBus(itemGroup, importBus, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.POWER_CRYSTAL.item(), SlimefunItems.REDSTONE_ALLOY.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.CARGO_INPUT_NODE.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.CARGO_MOTOR.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(this);

        new ExportBus(itemGroup, exportBus, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), null, SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), importBus.item(), SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), SlimefunItems.PLASTIC_SHEET.item(), SlimefunItems.GOLD_10K.item(), SlimefunItems.PLASTIC_SHEET.item()})
                .register(this);

        new WirelessTerminal(itemGroup, wirelessTerminal16, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {milkyQuartz.item(), SlimefunItems.GPS_TRANSMITTER.item(), milkyQuartz.item(), SlimefunItems.COBALT_INGOT.item(), chestTerminal.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.BATTERY.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.BATTERY.item()}) {

            @Override
            public int getRange() {
                return 16;
            }

            @Override
            public float getMaxItemCharge(ItemStack item) {
                return 10;
            }

        }.register(this);

        new WirelessTerminal(itemGroup, wirelessTerminal64, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {milkyQuartz.item(), SlimefunItems.GPS_TRANSMITTER.item(), milkyQuartz.item(), SlimefunItems.COBALT_INGOT.item(), wirelessTerminal16.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.BATTERY.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.BATTERY.item()}) {

            @Override
            public int getRange() {
                return 64;
            }

            @Override
            public float getMaxItemCharge(ItemStack item) {
                return 25;
            }

        }.register(this);

        new WirelessTerminal(itemGroup, wirelessTerminal128, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {milkyQuartz.item(), SlimefunItems.GPS_TRANSMITTER_2.item(), milkyQuartz.item(), SlimefunItems.COBALT_INGOT.item(), wirelessTerminal64.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.BATTERY.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.BATTERY.item()}) {

            @Override
            public int getRange() {
                return 128;
            }

            @Override
            public float getMaxItemCharge(ItemStack item) {
                return 50;
            }

        }.register(this);

        new WirelessTerminal(itemGroup, wirelessTerminalTransdimensional, RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[] {milkyQuartz.item(), SlimefunItems.GPS_TRANSMITTER_4.item(), milkyQuartz.item(), SlimefunItems.COBALT_INGOT.item(), wirelessTerminal128.item(), SlimefunItems.COBALT_INGOT.item(), SlimefunItems.BATTERY.item(), SlimefunItems.BLISTERING_INGOT_3.item(), SlimefunItems.BATTERY.item()}) {

            @Override
            public int getRange() {
                return -1;
            }

            @Override
            public float getMaxItemCharge(ItemStack item) {
                return 50;
            }

        }.register(this);

        new MilkyQuartz(this, milkyQuartz.item()).register();
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nonnull
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/ChestTerminal/issues";
    }
}
