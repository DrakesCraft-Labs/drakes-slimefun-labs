package io.github.sefiraat.simplestorage.items;

import io.github.sefiraat.simplestorage.SimpleStorage;
import io.github.sefiraat.simplestorage.categories.Categories;
import io.github.sefiraat.simplestorage.items.machines.DiePress;
import io.github.sefiraat.simplestorage.items.machines.MetalPressBasic;
import io.github.sefiraat.simplestorage.items.materials.GemAngelite;
import io.github.sefiraat.simplestorage.items.materials.GemProustite;
import io.github.sefiraat.simplestorage.items.materials.GemQuartz;
import io.github.sefiraat.simplestorage.items.materials.GemShortite;
import io.github.sefiraat.simplestorage.items.other.RareDropCoal;
import io.github.sefiraat.simplestorage.items.other.RareDropLapis;
import io.github.sefiraat.simplestorage.items.other.RareDropQuartz;
import io.github.sefiraat.simplestorage.items.other.RareDropRedstone;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.implementation.SlimefunItems;
import com.github.drakescraft-labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.inventory.ItemStack;

public final class Materials {

    private Materials() {
        throw new IllegalStateException("Utility class");
    }

    public static void set(SimpleStorage p) {

        new GemAngelite(p, Categories.MAT, SimpleStorageItemStacks.ANGELITE, RecipeType.GEO_MINER, new ItemStack[9]);
        new GemProustite(p, Categories.MAT, SimpleStorageItemStacks.PROUSTITE, RecipeType.GEO_MINER, new ItemStack[9]);
        new GemShortite(p, Categories.MAT, SimpleStorageItemStacks.SHORTITE, RecipeType.GEO_MINER, new ItemStack[9]);
        new GemQuartz(p, Categories.MAT, SimpleStorageItemStacks.CLEARQUARTZ, RecipeType.GEO_MINER, new ItemStack[9]);

        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.CHUNK_CHROMITE, RareDropCoal.TYPE, new ItemStack[9]).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.CHUNK_BASTNAESITE, RareDropQuartz.TYPE, new ItemStack[9]).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.CHUNK_ANTIMONY, RareDropLapis.TYPE, new ItemStack[9]).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.RHENIUM_CHUNK, RareDropRedstone.TYPE, new ItemStack[9]).register(p);

        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CHROMIUM_DUST, RecipeType.ORE_CRUSHER, SimpleStorageRecipes.CHROMIUM_DUST).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.BASTNAESITE_DUST, RecipeType.ORE_CRUSHER, SimpleStorageRecipes.BASTNAESITE_DUST).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.ANTIMONY_DUST, RecipeType.ORE_CRUSHER, SimpleStorageRecipes.ANTIMONY_DUST).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.RHENIUM_DUST, RecipeType.ORE_CRUSHER, SimpleStorageRecipes.RHENIUM_DUST).register(p);

        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CHROMIUM_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.CHROMIUM_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.BASTNAESITE_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.BASTNAESITE_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.ANTIMONY_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.ANTIMONY_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.RHENIUM_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.RHENIUM_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.ELECTRUM_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.ELECTRUM_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CUPRONICKEL_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.CUPRONICKEL_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.PEWTER_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.PEWTER_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.NICKEL_SUPER_INGOT, RecipeType.SMELTERY, SimpleStorageRecipes.NICKEL_SUPERALLOY_INGOT).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.ZAMAK_1, RecipeType.SMELTERY, SimpleStorageRecipes.ZAMAK_1).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.ZAMAK_2, RecipeType.SMELTERY, SimpleStorageRecipes.ZAMAK_2).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.ZAMAK_3, RecipeType.SMELTERY, SimpleStorageRecipes.ZAMAK_3).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.NISIL, RecipeType.SMELTERY, SimpleStorageRecipes.NISIL).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.NICROSIL, RecipeType.SMELTERY, SimpleStorageRecipes.NICROSIL).register(p);

        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CUPRONICKEL_REINFORCED, RecipeType.HEATED_PRESSURE_CHAMBER, SimpleStorageRecipes.CUPRONICKEL_REINFORCED).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.PEWTER_REINFORCED, RecipeType.HEATED_PRESSURE_CHAMBER, SimpleStorageRecipes.PEWTER_REINFORCED).register(p);

        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.ELECTRUM_PLATE, MetalPressBasic.TYPE, SimpleStorageRecipes.ELECTRUM_PLATE).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.CUPRONICKEL_PLATE, MetalPressBasic.TYPE, SimpleStorageRecipes.CUPRONICKEL_PLATE).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.PEWTER_PLATE, MetalPressBasic.TYPE, SimpleStorageRecipes.PEWTER_PLATE).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.NICKEL_SUPERALLOY_PLATE, MetalPressBasic.TYPE, SimpleStorageRecipes.NICKEL_SUPERALLOY_PLATE).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.COMBINED_PLATE, RecipeType.HEATED_PRESSURE_CHAMBER, SimpleStorageRecipes.COMBINED_PLATE).register(p);

        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.DIE_BASIC, DiePress.TYPE, SimpleStorageRecipes.DIE_BASIC).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.DIE_REINFORCED, DiePress.TYPE, SimpleStorageRecipes.DIE_REINFORCED).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.DIE_ADVANCED, DiePress.TYPE, SimpleStorageRecipes.DIE_ADVANCED).register(p);

        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.THERMOCOUPLE, RecipeType.HEATED_PRESSURE_CHAMBER, SimpleStorageRecipes.THERMOCOUPLE).register(p);

        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CHEST_CORE_BASIC, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_CORE_BASIC).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CHEST_CORE_REINFORCED, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_CORE_REINFORCED).register(p);
        new SlimefunItem(Categories.MAT, SimpleStorageItemStacks.CHEST_CORE_ADVANCED, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.CHEST_CORE_ADVANCED).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.COMMUNICATION_BLOCK, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.COMMUNICATION_BLOCK).register(p);
        new UnplaceableBlock(Categories.MAT, SimpleStorageItemStacks.DISPLAY_PANEL, RecipeType.ENHANCED_CRAFTING_TABLE, SimpleStorageRecipes.DISPLAY_PANEL).register(p);

        AContainer heatedPressureChamber = (AContainer) SlimefunItems.HEATED_PRESSURE_CHAMBER.getItem();
        heatedPressureChamber.registerRecipe(60, new ItemStack[]{SimpleStorageItemStacks.CUPRONICKEL_INGOT, SimpleStorageItemStacks.BASTNAESITE_INGOT}, new ItemStack[]{SimpleStorageItemStacks.CUPRONICKEL_REINFORCED});
        heatedPressureChamber.registerRecipe(100, new ItemStack[]{SimpleStorageItemStacks.PEWTER_INGOT, SimpleStorageItemStacks.ANTIMONY_INGOT}, new ItemStack[]{SimpleStorageItemStacks.PEWTER_REINFORCED});
        heatedPressureChamber.registerRecipe(150, new ItemStack[]{SimpleStorageItemStacks.PEWTER_PLATE, SimpleStorageItemStacks.NICKEL_SUPERALLOY_PLATE}, new ItemStack[]{SimpleStorageItemStacks.COMBINED_PLATE});
        heatedPressureChamber.registerRecipe(30, new ItemStack[]{SimpleStorageItemStacks.NISIL, SimpleStorageItemStacks.NICROSIL}, new ItemStack[]{SimpleStorageItemStacks.THERMOCOUPLE});

        AContainer heatedPressureChamber2 = (AContainer) SlimefunItems.HEATED_PRESSURE_CHAMBER_2.getItem();
        heatedPressureChamber2.registerRecipe(60, new ItemStack[]{SimpleStorageItemStacks.CUPRONICKEL_INGOT, SimpleStorageItemStacks.BASTNAESITE_INGOT}, new ItemStack[]{SimpleStorageItemStacks.CUPRONICKEL_REINFORCED});
        heatedPressureChamber2.registerRecipe(100, new ItemStack[]{SimpleStorageItemStacks.PEWTER_INGOT, SimpleStorageItemStacks.ANTIMONY_INGOT}, new ItemStack[]{SimpleStorageItemStacks.PEWTER_REINFORCED});
        heatedPressureChamber2.registerRecipe(150, new ItemStack[]{SimpleStorageItemStacks.PEWTER_PLATE, SimpleStorageItemStacks.NICKEL_SUPERALLOY_PLATE}, new ItemStack[]{SimpleStorageItemStacks.COMBINED_PLATE});
        heatedPressureChamber2.registerRecipe(30, new ItemStack[]{SimpleStorageItemStacks.NISIL, SimpleStorageItemStacks.NICROSIL}, new ItemStack[]{SimpleStorageItemStacks.THERMOCOUPLE});

    }

}
