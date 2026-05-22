package com.github.jackstar.coronalis.implementation.setup;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.implementation.Items;
import com.github.jackstar.coronalis.implementation.items.ArrayNetworkBlock;
import com.github.jackstar.coronalis.implementation.items.ControlConsole;
import com.github.jackstar.coronalis.implementation.items.DataRecordItem;
import com.github.jackstar.coronalis.implementation.items.RadioTelescope;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.researches.Research;

public class ItemSetup {

    private static boolean registered = false;

    private ItemSetup() {}

    public static void setup(@Nonnull Coronalis plugin) {
        if (registered) {
            throw new UnsupportedOperationException("Coronalis items can only be registered once!");
        }
        registered = true;

        Research radioEngineering = new Research(new NamespacedKey(plugin, "radio_engineering"), 88620, "Ingeniería de Radio", 14);
        Research pidControl = new Research(new NamespacedKey(plugin, "pid_control"), 88621, "Control PID del Vacío", 24);
        Research interferometry = new Research(new NamespacedKey(plugin, "interferometry"), 88622, "Interferometría VLBI", 34);
        Research deepSkyOps = new Research(new NamespacedKey(plugin, "deep_sky_ops"), 88623, "Operaciones de Cielo Profundo", 44);
        Research arrayInfrastructure = new Research(new NamespacedKey(plugin, "array_infrastructure"), 88624, "Infraestructura de Array", 18);

        /* 1. Componentes Básicos */
        SlimefunItem antenna = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_ANTENNA_DISH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.STEEL_PLATE, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.STEEL_PLATE,
                SlimefunItems.ALUMINUM_INGOT, new ItemStack(Material.GLASS_PANE), SlimefunItems.ALUMINUM_INGOT,
                SlimefunItems.COPPER_WIRE, new ItemStack(Material.GLASS_PANE), SlimefunItems.COPPER_WIRE
        });
        antenna.setResearch(radioEngineering);
        antenna.register(plugin);

        SlimefunItem receiver = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_RECEIVER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.GOLD_24K, SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.GOLD_24K,
                SlimefunItems.COPPER_WIRE, SlimefunItems.SILICON, SlimefunItems.COPPER_WIRE,
                SlimefunItems.COPPER_INGOT, SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.COPPER_INGOT
        });
        receiver.setResearch(radioEngineering);
        receiver.register(plugin);

        SlimefunItem pid = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_PID_CONTROLLER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.REPEATER), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.COMPARATOR),
                SlimefunItems.COPPER_WIRE, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.COPPER_WIRE,
                SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BATTERY, SlimefunItems.ELECTRIC_MOTOR
        });
        pid.setResearch(pidControl);
        pid.register(plugin);

        SlimefunItem dataCell = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_DATA_CELL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.GLASS), SlimefunItems.SILICON, new ItemStack(Material.GLASS),
                SlimefunItems.COPPER_WIRE, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.COPPER_WIRE,
                new ItemStack(Material.GLASS), SlimefunItems.BASIC_CIRCUIT_BOARD, new ItemStack(Material.GLASS)
        });
        dataCell.setResearch(interferometry);
        dataCell.register(plugin);

        ArrayNetworkBlock cable = new ArrayNetworkBlock(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_COAXIAL_CABLE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_WIRE,
                new ItemStack(Material.CHAIN), SlimefunItems.SILICON, new ItemStack(Material.CHAIN),
                SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_WIRE
        });
        cable.setResearch(arrayInfrastructure);
        cable.register(plugin);

        ArrayNetworkBlock signalCore = new ArrayNetworkBlock(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_SIGNAL_CORE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.BATTERY, Items.CORONALIS_RECEIVER, SlimefunItems.BATTERY,
                SlimefunItems.ENERGY_CONNECTOR, SlimefunItems.SOLAR_PANEL, SlimefunItems.ENERGY_CONNECTOR,
                SlimefunItems.REINFORCED_PLATE, Items.CORONALIS_PID_CONTROLLER, SlimefunItems.REINFORCED_PLATE
        });
        signalCore.setResearch(arrayInfrastructure);
        signalCore.register(plugin);

        /* 2. Bloques Interactivos */
        RadioTelescope telescope = new RadioTelescope(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_RADIO_TELESCOPE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.CORONALIS_ANTENNA_DISH, Items.CORONALIS_RECEIVER, Items.CORONALIS_ANTENNA_DISH,
                SlimefunItems.REINFORCED_PLATE, Items.CORONALIS_PID_CONTROLLER, SlimefunItems.REINFORCED_PLATE,
                SlimefunItems.ELECTRIC_MOTOR, Items.CORONALIS_COAXIAL_CABLE, SlimefunItems.ELECTRIC_MOTOR
        });
        telescope.setResearch(pidControl);
        telescope.register(plugin);

        ControlConsole console = new ControlConsole(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_CONTROL_CONSOLE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.LAPIS_BLOCK), Items.CORONALIS_PID_CONTROLLER, new ItemStack(Material.LAPIS_BLOCK),
                SlimefunItems.GPS_CONTROL_PANEL, Items.CORONALIS_RECEIVER, SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                Items.CORONALIS_SIGNAL_CORE, Items.CORONALIS_DATA_CELL, Items.CORONALIS_SIGNAL_CORE
        });
        console.setResearch(deepSkyOps);
        console.register(plugin);

        /* 3. Recompensas (Descubrimientos) */
        DataRecordItem record = new DataRecordItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_RECORD_DISCOVERED, RecipeType.NULL, new ItemStack[9]);
        record.register(plugin);

        radioEngineering.addItems(antenna, receiver);
        radioEngineering.register();

        pidControl.addItems(pid, telescope);
        pidControl.register();

        interferometry.addItems(dataCell);
        interferometry.register();

        arrayInfrastructure.addItems(cable, signalCore);
        arrayInfrastructure.register();

        deepSkyOps.addItems(console, record);
        deepSkyOps.register();
    }
}
