package com.github.jackstar.coronalis.implementation.setup;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.github.jackstar.coronalis.Coronalis;
import com.github.jackstar.coronalis.implementation.Items;
import com.github.jackstar.coronalis.implementation.items.ControlConsole;
import com.github.jackstar.coronalis.implementation.items.DataRecordItem;
import com.github.jackstar.coronalis.implementation.items.RadioTelescope;
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

        /* 1. Componentes Básicos */
        SlimefunItem antenna = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_ANTENNA_DISH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.REDSTONE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.REDSTONE)
        });
        antenna.setResearch(radioEngineering);
        antenna.register(plugin);

        SlimefunItem receiver = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_RECEIVER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.GOLD_INGOT),
                new ItemStack(Material.REDSTONE), new ItemStack(Material.QUARTZ), new ItemStack(Material.REDSTONE),
                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.REDSTONE), new ItemStack(Material.COPPER_INGOT)
        });
        receiver.setResearch(radioEngineering);
        receiver.register(plugin);

        SlimefunItem pid = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_PID_CONTROLLER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.REPEATER), new ItemStack(Material.COMPARATOR), new ItemStack(Material.REPEATER),
                new ItemStack(Material.LAPIS_LAZULI), new ItemStack(Material.REDSTONE), new ItemStack(Material.LAPIS_LAZULI),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.REDSTONE), new ItemStack(Material.IRON_INGOT)
        });
        pid.setResearch(pidControl);
        pid.register(plugin);

        SlimefunItem dataCell = new SlimefunItem(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_DATA_CELL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                new ItemStack(Material.REDSTONE), new ItemStack(Material.DIAMOND), new ItemStack(Material.REDSTONE),
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS)
        });
        dataCell.setResearch(interferometry);
        dataCell.register(plugin);

        /* 2. Bloques Interactivos */
        RadioTelescope telescope = new RadioTelescope(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_RADIO_TELESCOPE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.CORONALIS_ANTENNA_DISH, Items.CORONALIS_RECEIVER, Items.CORONALIS_ANTENNA_DISH,
                new ItemStack(Material.IRON_BLOCK), Items.CORONALIS_PID_CONTROLLER, new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.REDSTONE), new ItemStack(Material.IRON_BLOCK)
        });
        telescope.setResearch(pidControl);
        telescope.register(plugin);

        ControlConsole console = new ControlConsole(ItemGroups.CORONALIS_GROUP, Items.CORONALIS_CONTROL_CONSOLE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.LAPIS_BLOCK), Items.CORONALIS_PID_CONTROLLER, new ItemStack(Material.LAPIS_BLOCK),
                new ItemStack(Material.IRON_BLOCK), Items.CORONALIS_RECEIVER, new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.REDSTONE_BLOCK), Items.CORONALIS_DATA_CELL, new ItemStack(Material.REDSTONE_BLOCK)
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

        deepSkyOps.addItems(console, record);
        deepSkyOps.register();
    }
}
