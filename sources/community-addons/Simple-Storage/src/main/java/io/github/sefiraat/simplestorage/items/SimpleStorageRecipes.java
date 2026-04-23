package io.github.sefiraat.simplestorage.items;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class SimpleStorageRecipes {

    private SimpleStorageRecipes() {
        throw new IllegalStateException("Utility class");
    }

    protected static final ItemStack[] CHROMIUM_DUST = new ItemStack[] {
                SimpleStorageItemStacks.CHUNK_CHROMITE,    null,   null,
                null,                   null,   null,
                null,                   null,   null
    };
    protected static final ItemStack[] BASTNAESITE_DUST = new ItemStack[] {
                SimpleStorageItemStacks.CHUNK_BASTNAESITE,    null,   null,
                null,                   null,   null,
                null,                   null,   null
    };
    protected static final ItemStack[] ANTIMONY_DUST = new ItemStack[] {
                SimpleStorageItemStacks.CHUNK_ANTIMONY,    null,   null,
                null,                   null,   null,
                null,                   null,   null
    };
    protected static final ItemStack[] RHENIUM_DUST = new ItemStack[] {
                SimpleStorageItemStacks.RHENIUM_CHUNK,     null,   null,
                null,                   null,   null,
                null,                   null,   null
    };
    protected static final ItemStack[] CHROMIUM_INGOT = new ItemStack[] {
                SimpleStorageItemStacks.CHROMIUM_DUST, null,      null,
                null,               null,      null,
                null,               null,      null
    };
    protected static final ItemStack[] BASTNAESITE_INGOT = new ItemStack[] {
                SimpleStorageItemStacks.BASTNAESITE_DUST,  null,      null,
                null,                   null,      null,
                null,                   null,      null
    };
    protected static final ItemStack[] ANTIMONY_INGOT = new ItemStack[] {
                SimpleStorageItemStacks.ANTIMONY_DUST,  null,      null,
                null,                   null,      null,
                null,                   null,      null
    };
    protected static final ItemStack[] RHENIUM_INGOT = new ItemStack[] {
                SimpleStorageItemStacks.RHENIUM_DUST,    null,      null,
                null,                 null,      null,
                null,                 null,      null
    };
    protected static final ItemStack[] ELECTRUM_INGOT = new ItemStack[] {
                SlimefunItems.GOLD_6K,  SlimefunItems.SILVER_DUST,  null,
                null,                   null,                       null,
                null,                   null,                       null
    };
    protected static final ItemStack[] CUPRONICKEL_INGOT = new ItemStack[] {
                SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_DUST, SlimefunItems.NICKEL_INGOT,
                null,                       null,                       null,
                null,                       null,                       null
    };
    protected static final ItemStack[] PEWTER_INGOT = new ItemStack[] {
            SlimefunItems.TIN_INGOT,    SlimefunItems.TIN_DUST, SlimefunItems.SILVER_INGOT,
            SlimefunItems.SILVER_DUST,  null,                   null,
            null,                       null,                   null
    };
    protected static final ItemStack[] NICKEL_SUPERALLOY_INGOT = new ItemStack[] {
            SimpleStorageItemStacks.RHENIUM_INGOT,  SlimefunItems.NICKEL_INGOT, SlimefunItems.COBALT_INGOT,
            SlimefunItems.COPPER_INGOT,             SlimefunItems.IRON_DUST,    null,
            null,                                   null,                       null
    };
    protected static final ItemStack[] ZAMAK_1 = new ItemStack[] {
                SlimefunItems.ZINC_INGOT,   SlimefunItems.ALUMINUM_DUST,    SlimefunItems.COPPER_DUST,
                null,                       null,                           null,
                null,                       null,                           null
    };
    protected static final ItemStack[] ZAMAK_2 = new ItemStack[] {
                SimpleStorageItemStacks.ZAMAK_1, SlimefunItems.COPPER_DUST,  null,
                null,         null,                       null,
                null,         null,                       null
    };
    protected static final ItemStack[] ZAMAK_3 = new ItemStack[] {
                SimpleStorageItemStacks.ZAMAK_2, SlimefunItems.MAGNESIUM_INGOT,  SlimefunItems.MAGNESIUM_DUST,
                null,                               null,                           null,
                null,                               null,                           null
    };
    protected static final ItemStack[] NISIL = new ItemStack[] {
                SlimefunItems.NICKEL_INGOT, SlimefunItems.SILICON,      new ItemStack(Material.SAND),
                null,                       null,                       null,
                null,                       null,                       null
    };
    protected static final ItemStack[] NICROSIL = new ItemStack[] {
                SlimefunItems.NICKEL_INGOT, SlimefunItems.SILICON,      SimpleStorageItemStacks.CHROMIUM_INGOT,
                null,                       null,                       null,
                null,                       null,                       null
    };
    protected static final ItemStack[] CUPRONICKEL_REINFORCED = new ItemStack[] {
                SimpleStorageItemStacks.CUPRONICKEL_INGOT, SimpleStorageItemStacks.BASTNAESITE_INGOT, null,
                null,              null,                   null,
                null,              null,                   null
    };
    protected static final ItemStack[] PEWTER_REINFORCED = new ItemStack[] {
                SimpleStorageItemStacks.PEWTER_INGOT,   SimpleStorageItemStacks.ANTIMONY_INGOT, null,
                null,           null,                null,
                null,           null,                null
    };
    protected static final ItemStack[] ELECTRUM_PLATE = new ItemStack[] {
                SimpleStorageItemStacks.DIE_BASIC,    new SlimefunItemStack(SimpleStorageItemStacks.ELECTRUM_INGOT, 4),   null,
                null,              null,                                              null,
                null,              null,                                              null
    };
    protected static final ItemStack[] CUPRONICKEL_PLATE = new ItemStack[] {
                SimpleStorageItemStacks.DIE_REINFORCED, new SlimefunItemStack(SimpleStorageItemStacks.CUPRONICKEL_REINFORCED, 4), null,
                null,                null,                                                          null,
                null,                null,                                                          null
    };
    protected static final ItemStack[] PEWTER_PLATE = new ItemStack[] {
                SimpleStorageItemStacks.DIE_ADVANCED,   new SlimefunItemStack(SimpleStorageItemStacks.PEWTER_REINFORCED, 4), null,
                null,                null,                                             null,
                null,                null,                                             null
    };
    protected static final ItemStack[] NICKEL_SUPERALLOY_PLATE = new ItemStack[] {
                SimpleStorageItemStacks.DIE_ADVANCED,   new SlimefunItemStack(SimpleStorageItemStacks.NICKEL_SUPER_INGOT, 4), null,
                null,                null,                                             null,
                null,                null,                                             null
    };
    protected static final ItemStack[] COMBINED_PLATE = new ItemStack[] {
                SimpleStorageItemStacks.PEWTER_PLATE,   SimpleStorageItemStacks.NICKEL_SUPERALLOY_PLATE, null,
                null,                null,                                             null,
                null,                null,                                             null
    };
    protected static final ItemStack[] DIE_BASIC = new ItemStack[] {
            SimpleStorageItemStacks.ZAMAK_1,   null,   null,
            null,           null,   null,
            null,           null,   null
    };
    protected static final ItemStack[] DIE_REINFORCED = new ItemStack[] {
            SimpleStorageItemStacks.ZAMAK_2,   null,   null,
            null,           null,   null,
            null,           null,   null
    };
    protected static final ItemStack[] DIE_ADVANCED = new ItemStack[] {
            SimpleStorageItemStacks.ZAMAK_3,   null,   null,
            null,           null,   null,
            null,           null,   null
    };
    protected static final ItemStack[] THERMOCOUPLE = new ItemStack[] {
                SimpleStorageItemStacks.NISIL,    SimpleStorageItemStacks.NICROSIL, null,
                null,           null,           null,
                null,           null,           null
    };
    protected static final ItemStack[] CHEST_CORE_BASIC = new ItemStack[] {
            SimpleStorageItemStacks.ELECTRUM_PLATE, SimpleStorageItemStacks.ELECTRUM_PLATE, SimpleStorageItemStacks.ELECTRUM_PLATE,
            SimpleStorageItemStacks.ELECTRUM_PLATE, SimpleStorageItemStacks.ANGELITE,   SimpleStorageItemStacks.ELECTRUM_PLATE,
            SimpleStorageItemStacks.ELECTRUM_PLATE, SimpleStorageItemStacks.THERMOCOUPLE,  SimpleStorageItemStacks.ELECTRUM_PLATE
    };
    protected static final ItemStack[] CHEST_CORE_REINFORCED = new ItemStack[] {
            SimpleStorageItemStacks.CUPRONICKEL_PLATE, SimpleStorageItemStacks.CUPRONICKEL_PLATE, SimpleStorageItemStacks.CUPRONICKEL_PLATE,
            SimpleStorageItemStacks.CUPRONICKEL_PLATE, SimpleStorageItemStacks.PROUSTITE,     SimpleStorageItemStacks.CUPRONICKEL_PLATE,
            SimpleStorageItemStacks.CUPRONICKEL_PLATE, SimpleStorageItemStacks.THERMOCOUPLE,     SimpleStorageItemStacks.CUPRONICKEL_PLATE
    };
    protected static final ItemStack[] CHEST_CORE_ADVANCED = new ItemStack[] {
            SimpleStorageItemStacks.PEWTER_PLATE, SimpleStorageItemStacks.PEWTER_PLATE, SimpleStorageItemStacks.PEWTER_PLATE,
            SimpleStorageItemStacks.PEWTER_PLATE, SimpleStorageItemStacks.SHORTITE, SimpleStorageItemStacks.PEWTER_PLATE,
            SimpleStorageItemStacks.PEWTER_PLATE, SimpleStorageItemStacks.THERMOCOUPLE, SimpleStorageItemStacks.PEWTER_PLATE
    };
    protected static final ItemStack[] COMMUNICATION_BLOCK = new ItemStack[] {
            SimpleStorageItemStacks.ELECTRUM_PLATE,    SlimefunItems.GOLD_24K_BLOCK,     SimpleStorageItemStacks.ELECTRUM_PLATE,
            SlimefunItems.GOLD_24K, SimpleStorageItemStacks.CHEST_CORE_BASIC,             SlimefunItems.GOLD_24K,
            SimpleStorageItemStacks.ELECTRUM_PLATE,    new ItemStack(Material.REPEATER), SimpleStorageItemStacks.ELECTRUM_PLATE
    };
    protected static final ItemStack[] DISPLAY_PANEL = new ItemStack[] {
            SimpleStorageItemStacks.CLEARQUARTZ,   SimpleStorageItemStacks.CLEARQUARTZ,                  SimpleStorageItemStacks.CLEARQUARTZ,
            SimpleStorageItemStacks.PEWTER_PLATE, new ItemStack(Material.REPEATER), SimpleStorageItemStacks.PEWTER_PLATE,
            SimpleStorageItemStacks.PEWTER_PLATE, SlimefunItems.BLISTERING_INGOT_3, SimpleStorageItemStacks.PEWTER_PLATE
    };
    protected static final ItemStack[] DIE_PRESS = new ItemStack[] {
            SimpleStorageItemStacks.ZAMAK_1, new ItemStack(Material.PISTON), SimpleStorageItemStacks.ZAMAK_1,
            SimpleStorageItemStacks.ZAMAK_1, null,                           SimpleStorageItemStacks.ZAMAK_1,
            SimpleStorageItemStacks.ZAMAK_2, new ItemStack(Material.PISTON), SimpleStorageItemStacks.ZAMAK_2
    };
    protected static final ItemStack[] METAL_PRESS_BASIC = new ItemStack[] {
            SimpleStorageItemStacks.ZAMAK_3, new ItemStack(Material.STICKY_PISTON), SimpleStorageItemStacks.ZAMAK_3,
            SimpleStorageItemStacks.ZAMAK_3, SimpleStorageItemStacks.DIE_BASIC,                        SimpleStorageItemStacks.ZAMAK_3,
            SimpleStorageItemStacks.ZAMAK_3, new ItemStack(Material.STICKY_PISTON), SimpleStorageItemStacks.ZAMAK_3
    };
    protected static final ItemStack[] ENDERATOR = new ItemStack[] {
            SimpleStorageItemStacks.PEWTER_REINFORCED,      new ItemStack(Material.END_CRYSTAL), SimpleStorageItemStacks.PEWTER_REINFORCED,
            SimpleStorageItemStacks.CUPRONICKEL_REINFORCED, null,                                SimpleStorageItemStacks.CUPRONICKEL_REINFORCED,
            SimpleStorageItemStacks.CUPRONICKEL_REINFORCED, SimpleStorageItemStacks.SHORTITE,                   SimpleStorageItemStacks.CUPRONICKEL_REINFORCED
    };
    protected static final ItemStack[] CHEST_IRON = new ItemStack[] {
            new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK),     new ItemStack(Material.IRON_BLOCK),
            new ItemStack(Material.IRON_BLOCK), SimpleStorageItemStacks.CHEST_CORE_BASIC,                   new ItemStack(Material.IRON_BLOCK),
            new ItemStack(Material.IRON_BLOCK), SlimefunItems.REINFORCED_ALLOY_INGOT,   new ItemStack(Material.IRON_BLOCK)
    };
    protected static final ItemStack[] CHEST_GOLD = new ItemStack[] {
            SlimefunItems.GOLD_24K_BLOCK, SimpleStorageItemStacks.CHEST_IRON,                  SlimefunItems.GOLD_24K_BLOCK,
            SlimefunItems.GOLD_24K_BLOCK, SimpleStorageItemStacks.CHEST_CORE_REINFORCED,        SlimefunItems.GOLD_24K_BLOCK,
            SlimefunItems.GOLD_24K_BLOCK, SlimefunItems.BLISTERING_INGOT_3,   SlimefunItems.GOLD_24K_BLOCK
    };
    protected static final ItemStack[] CHEST_DIAMOND = new ItemStack[] {
            new ItemStack(Material.DIAMOND_BLOCK), SimpleStorageItemStacks.CHEST_GOLD,                new ItemStack(Material.DIAMOND_BLOCK),
            new ItemStack(Material.DIAMOND_BLOCK), SimpleStorageItemStacks.CHEST_CORE_ADVANCED,        new ItemStack(Material.DIAMOND_BLOCK),
            new ItemStack(Material.DIAMOND_BLOCK), SlimefunItems.BOOSTED_URANIUM,  new ItemStack(Material.DIAMOND_BLOCK)
    };
    protected static final ItemStack[] INVENTORY_CELL = new ItemStack[] {
            SimpleStorageItemStacks.CHEST_DIAMOND, new ItemStack(Material.ENDER_PEARL, 16), null,
            null,               null,                                           null,
            null,               null,                                           null,
    };
    protected static final ItemStack[] CHEST_MASTER = new ItemStack[] {
            SimpleStorageItemStacks.COMBINED_PLATE, SimpleStorageItemStacks.DISPLAY_PANEL,        SimpleStorageItemStacks.COMBINED_PLATE,
            SimpleStorageItemStacks.COMBINED_PLATE, SimpleStorageItemStacks.CHEST_DIAMOND,        SimpleStorageItemStacks.COMBINED_PLATE,
            SimpleStorageItemStacks.COMBINED_PLATE, SimpleStorageItemStacks.COMMUNICATION_BLOCK,  SimpleStorageItemStacks.COMBINED_PLATE
    };
    protected static final ItemStack[] AUGMENT_BARREL = new ItemStack[] {
            new ItemStack(Material.OAK_PLANKS), SimpleStorageItemStacks.COMBINED_PLATE,         new ItemStack(Material.OAK_PLANKS),
            new ItemStack(Material.OAK_PLANKS), SimpleStorageItemStacks.PROUSTITE,              new ItemStack(Material.OAK_PLANKS),
            new ItemStack(Material.OAK_PLANKS), SimpleStorageItemStacks.COMMUNICATION_BLOCK,    new ItemStack(Material.OAK_PLANKS)
    };
}
