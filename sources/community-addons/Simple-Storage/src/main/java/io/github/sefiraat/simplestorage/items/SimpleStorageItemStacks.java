package io.github.sefiraat.simplestorage.items;

import com.github.drakescraft_labs.infinitylib.machines.MachineLore;
import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.items.machines.DiePress;
import io.github.sefiraat.simplestorage.items.machines.Enderator;
import io.github.sefiraat.simplestorage.items.machines.MetalPressBasic;
import io.github.sefiraat.simplestorage.utils.Theme;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Material;

public final class SimpleStorageItemStacks {

    private SimpleStorageItemStacks() {
        throw new IllegalStateException("Utility class");
    }

    public static final SlimefunItemStack ANGELITE = new SlimefunItemStack(
            "MATERIAL_ANGELITE",
            Skulls.ITEM_ANGELITE,
            Theme.ITEM_CRAFTING + "Angelite Crystal",
            "",
            Theme.PASSIVE + "Angelite crystals are a rare resource that",
            Theme.PASSIVE + "are used for simple data transfer.",
            Theme.PASSIVE + "More plentiful in colder climates.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack PROUSTITE = new SlimefunItemStack(
            "MATERIAL_PROUSTITE",
            Skulls.ITEM_PROUSTITE,
            Theme.ITEM_CRAFTING + "Proustite Crystal",
            "",
            Theme.PASSIVE + "Proustite crystals are a rare resource that",
            Theme.PASSIVE + "are used for more advanced data transfer.",
            Theme.PASSIVE + "Only found in the hottest locations.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack SHORTITE = new SlimefunItemStack(
            "MATERIAL_SHORTITE",
            Skulls.ITEM_SHORTITE,
            Theme.ITEM_CRAFTING + "Shortite Crystal",
            "",
            Theme.PASSIVE + "Shortite crystals are a rare resource that",
            Theme.PASSIVE + "are used for the most advanced data transfers.",
            Theme.PASSIVE + "Locations unknown.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack CLEARQUARTZ = new SlimefunItemStack(
            "MATERIAL_QUARTZGEM",
            Skulls.ITEM_CLEAR_QUARTZ,
            Theme.ITEM_CRAFTING + "Crystal Clear Quartz",
            "",
            Theme.PASSIVE + "This form of quartz is very rare",
            Theme.PASSIVE + "and used for creating displays.",
            Theme.PASSIVE + "Location unknown.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack CHUNK_CHROMITE = new SlimefunItemStack(
            "CHUNK_CHROMITE",
            Skulls.ITEM_CHROMITE_CHUNK,
            Theme.ITEM_CRAFTING + "Chromite Chunk",
            "",
            Theme.PASSIVE + "A very rare ore. Can be processed",
            Theme.PASSIVE + "into Chromium.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack CHUNK_BASTNAESITE = new SlimefunItemStack(
            "MATERIAL_BASTNAESITE_CHUNK",
            Skulls.ITEM_BASTNAESITE_CHUNK,
            Theme.ITEM_CRAFTING + "Bastnaesite Chunk",
            "",
            Theme.PASSIVE + "A very rare ore. Can be processed",
            Theme.PASSIVE + "into Cerium.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack CHUNK_ANTIMONY = new SlimefunItemStack(
            "MATERIAL_ANTIMONY_CHUNK",
            Skulls.ITEM_ANTIMONY_CHUNK,
            Theme.ITEM_CRAFTING + "Antimony Shard",
            "",
            Theme.PASSIVE + "A very rare metal. Can be processed",
            Theme.PASSIVE + "into Antimony.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack RHENIUM_CHUNK = new SlimefunItemStack(
            "MATERIAL_RHENIUM_CHUNK",
            Skulls.ITEM_RHENIUM_CHUNK,
            Theme.ITEM_CRAFTING + "Rhenium Shard",
            "",
            Theme.PASSIVE + "A very rare metal. Can be processed",
            Theme.PASSIVE + "into Rhenium.",
            "",
            Theme.LORE_TYPE_BASE
    );

    public static final SlimefunItemStack CHROMIUM_DUST = new SlimefunItemStack(
            "MATERIAL_CHROMIUM_DUST",
            Material.GUNPOWDER,
            Theme.ITEM_CRAFTING + "Chromium Dust",
            "",
            Theme.PASSIVE + "A very rare metal. Needs to be worked",
            Theme.PASSIVE + "into ingots.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack BASTNAESITE_DUST = new SlimefunItemStack(
            "MATERIAL_BASTNAESITE_DUST",
            Material.GUNPOWDER,
            Theme.ITEM_CRAFTING + "Bastnaesite Dust",
            "",
            Theme.PASSIVE + "A very rare ore. Needs to be worked",
            Theme.PASSIVE + "into ingots.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ANTIMONY_DUST = new SlimefunItemStack(
            "MATERIAL_ANTIMONY_DUST",
            Material.GUNPOWDER,
            Theme.ITEM_CRAFTING + "Antimony Dust",
            "",
            Theme.PASSIVE + "A very rare metal. Needs to be worked",
            Theme.PASSIVE + "into ingots.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack RHENIUM_DUST = new SlimefunItemStack(
            "MATERIAL_RHENIUM_DUST",
            Material.GUNPOWDER,
            Theme.ITEM_CRAFTING + "Rhenium Dust",
            "",
            Theme.PASSIVE + "A very rare metal. Needs to be worked",
            Theme.PASSIVE + "into ingots.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CHROMIUM_INGOT = new SlimefunItemStack(
            "MATERIAL_CHROMIUM_INGOT",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Chromium Ingot",
            "",
            Theme.PASSIVE + "A very rare metal. Used in making",
            Theme.PASSIVE + "alloys.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack BASTNAESITE_INGOT = new SlimefunItemStack(
            "MATERIAL_BASTNAESITE_INGOT",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Carium Ingot",
            "",
            Theme.PASSIVE + "A very rare metal. Used in making",
            Theme.PASSIVE + "reinforced alloys.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ANTIMONY_INGOT = new SlimefunItemStack(
            "MATERIAL_ANTIMONY_INGOT",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Antimony Ingot",
            "",
            Theme.PASSIVE + "A very rare metal. Used in making",
            Theme.PASSIVE + "advanced alloys.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack RHENIUM_INGOT = new SlimefunItemStack(
            "MATERIAL_RHENIUM_INGOT",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Rhenium Ingot",
            "",
            Theme.PASSIVE + "An incredibly rare metal. Used in making",
            Theme.PASSIVE + "the very advanced alloys.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ELECTRUM_INGOT = new SlimefunItemStack(
            "MATERIAL_ELECTRUM",
            Material.GOLD_INGOT,
            Theme.ITEM_CRAFTING + "Electrum Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of gold and silver that is",
            Theme.PASSIVE + "basically useless, but we might find",
            Theme.PASSIVE + "some use for it?",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CUPRONICKEL_INGOT = new SlimefunItemStack(
            "MATERIAL_CUPRONICKEL",
            Material.GOLD_INGOT,
            Theme.ITEM_CRAFTING + "Cupronickel Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Copper and Nickel that is",
            Theme.PASSIVE + "used for its strength and ability to",
            Theme.PASSIVE + "withstand high pressures.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack PEWTER_INGOT = new SlimefunItemStack(
            "MATERIAL_PEWTER",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Pewter Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Tin and Silver that is",
            Theme.PASSIVE + "used for its durability.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack NICKEL_SUPER_INGOT = new SlimefunItemStack(
            "MATERIAL_SUPER_INGOT",
            Material.GOLD_INGOT,
            Theme.ITEM_CRAFTING + "Nickel-Superalloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Nickel and Rhenium that",
            Theme.PASSIVE + "used for its extreme temperature",
            Theme.PASSIVE + "resistance.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ZAMAK_1 = new SlimefunItemStack(
            "MATERIAL_ZAMAK1",
            Material.NETHERITE_INGOT,
            Theme.ITEM_CRAFTING + "Zamak-1",
            "",
            Theme.PASSIVE + "An alloy of Zinc, Aluminium and",
            Theme.PASSIVE + "Copper. While this alloy is not",
            Theme.PASSIVE + "the strongest, it is useful for",
            Theme.PASSIVE + "machine dies.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ZAMAK_2 = new SlimefunItemStack(
            "MATERIAL_ZAMAK2",
            Material.NETHERITE_INGOT,
            Theme.ITEM_CRAFTING + "Zamak-2",
            "",
            Theme.PASSIVE + "An alloy of Zinc, Aluminium and",
            Theme.PASSIVE + "Copper. Zamak-2 is reinforced with",
            Theme.PASSIVE + "additional Copper for further",
            Theme.PASSIVE + "strength making it capable of acting",
            Theme.PASSIVE + "as a die for more metals.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ZAMAK_3 = new SlimefunItemStack(
            "MATERIAL_ZAMAK3",
            Material.NETHERITE_INGOT,
            Theme.ITEM_CRAFTING + "Zamak-3",
            "",
            Theme.PASSIVE + "An alloy of Zinc, Aluminium and",
            Theme.PASSIVE + "Copper. Zamak-3 is infused with",
            Theme.PASSIVE + "Magnesium for stability.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack NISIL = new SlimefunItemStack(
            "MATERIAL_NISIL",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Nisil Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Nickel and Silicon.",
            Theme.PASSIVE + "Forms part of a thermocouple.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack NICROSIL = new SlimefunItemStack(
            "MATERIAL_NICROSIL",
            Material.IRON_INGOT,
            Theme.ITEM_CRAFTING + "Nicrosil Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Nickel, Silicon and",
            Theme.PASSIVE + "Chromium. Forms part of a thermocouple.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CUPRONICKEL_REINFORCED = new SlimefunItemStack(
            "MATERIAL_REINFORCED_CUPRONICKEL",
            Material.GOLD_INGOT,
            Theme.ITEM_CRAFTING + "Reinforced Cupronickel Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Copper and Nickel that is",
            Theme.PASSIVE + "used for its strength and ability to",
            Theme.PASSIVE + "withstand high pressures. This variant",
            Theme.PASSIVE + "has been reinforced with Carium.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack PEWTER_REINFORCED = new SlimefunItemStack(
            "MATERIAL_REINFORCED_PEWTER",
            Material.IRON_INGOT ,
            Theme.ITEM_CRAFTING + "Hardened Pewter Alloy Ingot",
            "",
            Theme.PASSIVE + "An alloy of Tin and Silver that is",
            Theme.PASSIVE + "used for its durability. This variant",
            Theme.PASSIVE + "has been hardened with Antimony.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack ELECTRUM_PLATE = new SlimefunItemStack(
            "MATERIAL_ELECTRUM_PLATE",
            Material.YELLOW_CARPET,
            Theme.ITEM_CRAFTING + "Electrum Alloy Plate",
            "",
            Theme.PASSIVE + "A plate made out of Electrum used as",
            Theme.PASSIVE + "a shell for basic storage components.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CUPRONICKEL_PLATE = new SlimefunItemStack(
            "MATERIAL_CUPRONICKEL_PLATE",
            Material.ORANGE_CARPET,
            Theme.ITEM_CRAFTING + "Reinforced Cupronickel Alloy Plate",
            "",
            Theme.PASSIVE + "A plate made out of Cupronickel used as",
            Theme.PASSIVE + "a shell for intermediate storage components.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack PEWTER_PLATE = new SlimefunItemStack(
            "MATERIAL_PEWTER_PLATE",
            Material.LIGHT_GRAY_CARPET,
            Theme.ITEM_CRAFTING + "Hardened Pewter Alloy Plate",
            "",
            Theme.PASSIVE + "A plate made out of Pewter used as",
            Theme.PASSIVE + "a shell for advanced storage components.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack NICKEL_SUPERALLOY_PLATE = new SlimefunItemStack(
            "MATERIAL_SUPER_PLATE",
            Material.BROWN_CARPET,
            Theme.ITEM_CRAFTING + "Nickel-Superalloy Plate",
            "",
            Theme.PASSIVE + "A plate made out of Nickel and Rhenium used",
            Theme.PASSIVE + "as a shell for advanced storage components.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack COMBINED_PLATE = new SlimefunItemStack(
            "MATERIAL_COMBINED_PLATE",
            Material.PINK_CARPET,
            Theme.ITEM_CRAFTING + "Combination Plate",
            "",
            Theme.PASSIVE + "A plate made out of Nickel and Rhenium",
            Theme.PASSIVE + "layered with pewter.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack DIE_BASIC = new SlimefunItemStack(
            "MATERIAL_DIE_BASIC",
            Material.LIGHT_GRAY_CARPET,
            Theme.ITEM_CRAFTING + "Basic Die",
            "",
            Theme.PASSIVE + "A weak die made out of Zamak-1.",
            Theme.PASSIVE + "Can be used to create rudimentary",
            Theme.PASSIVE + "injected shells.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack DIE_REINFORCED = new SlimefunItemStack(
            "MATERIAL_DIE_REINFORCED",
            Material.GRAY_CARPET,
            Theme.ITEM_CRAFTING + "Reinforced Die",
            "",
            Theme.PASSIVE + "A reinforced die made out of Zamak-2.",
            Theme.PASSIVE + "Can be used to create intermediate",
            Theme.PASSIVE + "injected shells.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack DIE_ADVANCED = new SlimefunItemStack(
            "MATERIAL_DIE_ADVANCED",
            Material.BLACK_CARPET,
            Theme.ITEM_CRAFTING + "Advanced Die",
            "",
            Theme.PASSIVE + "An advanced die made out of Zamak-3.",
            Theme.PASSIVE + "Can be used to create top-tier",
            Theme.PASSIVE + "injected shells.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack THERMOCOUPLE = new SlimefunItemStack(
            "MATERIAL_THERMOCOUPLE",
            Material.REPEATER,
            Theme.ITEM_CRAFTING + "Thermocouple",
            "",
            Theme.PASSIVE + "Used to test heat transfer",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CHEST_CORE_BASIC = new SlimefunItemStack(
            "MATERIAL_CHEST_CORE_BASIC",
            Material.HEART_OF_THE_SEA,
            Theme.ITEM_CRAFTING + "Basic Chest Core",
            "",
            Theme.PASSIVE + "Used as the core for an iron chest.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CHEST_CORE_REINFORCED = new SlimefunItemStack(
            "MATERIAL_CHEST_CORE_REINFORCED",
            Material.HEART_OF_THE_SEA,
            Theme.ITEM_CRAFTING + "Reinforced Chest Core",
            "",
            Theme.PASSIVE + "Used as the core for a gold chest.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack CHEST_CORE_ADVANCED = new SlimefunItemStack(
            "MATERIAL_CHEST_CORE_ADVANCED",
            Material.HEART_OF_THE_SEA,
            Theme.ITEM_CRAFTING + "Advanced Chest Core",
            "",
            Theme.PASSIVE + "Used as the core for a diamond chest.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack COMMUNICATION_BLOCK = new SlimefunItemStack(
            "BLOCK_COMMUNICATION",
            Material.BLACK_GLAZED_TERRACOTTA,
            Theme.ITEM_MACHINE + "Communication Block",
            "",
            Theme.PASSIVE + "Allows the networking of various",
            Theme.PASSIVE + "inventories to the Master.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack DISPLAY_PANEL = new SlimefunItemStack(
            "MATERIAL_DISPLAY",
            Material.GRAY_STAINED_GLASS_PANE,
            Theme.ITEM_CRAFTING + "Display Panel",
            "",
            Theme.PASSIVE + "A display panel. Used to craft the",
            Theme.PASSIVE + "the Chest Master.",
            "",
            Theme.LORE_TYPE_CRAFT
    );

    public static final SlimefunItemStack DIE_PRESS = new SlimefunItemStack(
            "MACHINE_DIE_PRESS",
            Material.PISTON,
            Theme.ITEM_MACHINE + "Die Press",
            "",
            Theme.PASSIVE + "A mechanical die press that can",
            Theme.PASSIVE + "create dies from certain metals.",
            Theme.PASSIVE + "dies are used to shape other materials.",
            "",
            MachineLore.energyPerSecond(DiePress.CONSUMPTION),
            "",
            Theme.LORE_TYPE_MACHINE
    );
    public static final SlimefunItemStack METAL_PRESS_BASIC = new SlimefunItemStack(
            "MACHINE_METAL_PRESS_BASIC",
            Material.STICKY_PISTON,
            Theme.ITEM_MACHINE + "Basic Metal Press",
            "",
            Theme.PASSIVE + "A mechanical metal press that can",
            Theme.PASSIVE + "create shaped metal plates from",
            Theme.PASSIVE + "basic dies and soft alloys.",
            "",
            MachineLore.energyPerSecond(MetalPressBasic.CONSUMPTION),
            "",
            Theme.LORE_TYPE_MACHINE
    );
    public static final SlimefunItemStack ENDERATOR = new SlimefunItemStack(
            "MACHINE_ENDERATOR",
            Material.RESPAWN_ANCHOR,
            Theme.ITEM_MACHINE + "Enderator",
            "",
            Theme.PASSIVE + "Utilises the End Crystal and a large",
            Theme.PASSIVE + "amount channelled energy to twist the",
            Theme.PASSIVE + "very fabric of an object into something",
            Theme.PASSIVE + "different...",
            "",
            MachineLore.energyPerSecond(Enderator.CONSUMPTION),
            "",
            Theme.LORE_TYPE_MACHINE
    );
    public static final SlimefunItemStack CHEST_IRON = new SlimefunItemStack(
            "BLOCK_CHEST_IRON",
            Skulls.BLOCK_CHEST_IRON,
            Theme.ITEM_MACHINE + "Iron Chest",
            "",
            Theme.PASSIVE + "A chest with 90 item slots.",
            "",
            Theme.LORE_TYPE_CHEST
    );
    public static final SlimefunItemStack CHEST_GOLD = new SlimefunItemStack(
            "BLOCK_CHEST_GOLD",
            Skulls.BLOCK_CHEST_GOLD,
            Theme.ITEM_MACHINE + "Gold Chest",
            "",
            Theme.PASSIVE + "A chest with 180 item slots.",
            "",
            Theme.LORE_TYPE_CHEST
    );
    public static final SlimefunItemStack CHEST_DIAMOND = new SlimefunItemStack(
            "BLOCK_CHEST_DIAMOND",
            Skulls.BLOCK_CHEST_DIAMOND,
            Theme.ITEM_MACHINE + "Diamond Chest",
            "",
            Theme.PASSIVE + "A chest with 270 item slots.",
            "",
            Theme.LORE_TYPE_CHEST
    );
    public static final SlimefunItemStack INVENTORY_CELL = new SlimefunItemStack(
            "BLOCK_CELL_BASIC",
            Skulls.BLOCK_CELL_BASIC,
            Theme.ITEM_MACHINE + "Inventory Cell",
            "",
            Theme.PASSIVE + "An inventory cell with 270 item slots.",
            "",
            Theme.LORE_TYPE_CHEST
    );
    public static final SlimefunItemStack CHEST_NETWORK = new SlimefunItemStack(
            "BLOCK_CHEST_NETWORK",
            Skulls.BLOCK_CHEST_MASTER,
            Theme.ITEM_MACHINE + "Network Chest",
            "",
            Theme.PASSIVE + "This chest can access all Inventory",
            Theme.PASSIVE + "Cells within range.",
            "",
            Theme.PASSIVE + "Cells must be within " + Theme.ITEM_TYPEDESC + SimpleStorage.inst().getManagerConfiguration().getVals().getNetworkRange() + Theme.PASSIVE + " blocks.",
            Theme.PASSIVE + "of the Network Chest in any direction",
            "",
            Theme.LORE_TYPE_CHEST
    );
    public static final SlimefunItemStack UPGRADE_BARREL = new SlimefunItemStack(
            "UPGRADE_BARREL",
            Skulls.ITEM_UPGRADE_BARREL,
            Theme.ITEM_AUGMENT + "Augment: Barrels",
            "",
            Theme.PASSIVE + "Augment your Network Chest with the",
            Theme.PASSIVE + "ability to scan nearby, compatible,",
            Theme.PASSIVE + "barrels.",
            "",
            Theme.LORE_TYPE_AUGMENT
    );
}
