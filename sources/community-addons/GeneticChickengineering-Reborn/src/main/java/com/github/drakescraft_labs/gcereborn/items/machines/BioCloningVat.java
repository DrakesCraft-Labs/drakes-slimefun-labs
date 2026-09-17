package com.github.drakescraft_labs.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.items.chicken.ExpandedChickenSpecies;
import com.github.drakescraft_labs.gcereborn.items.chicken.PocketChicken;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.Keys;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;

public class BioCloningVat extends AbstractMachine {

    public BioCloningVat(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return new ItemStack(Material.POTION);
    }

    @Override
    protected void tick(@Nonnull Block b) {
        super.tick(b);
        if (GeneticChickengineering.getConfigService().isParticlesEnabled() && Math.random() < 0.2) {
            b.getWorld().spawnParticle(Particle.SPLASH, b.getLocation().add(0.5, 0.8, 0.5), 4, 0.2, 0.2, 0.2, 0.05);
        }
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        ItemStack chickenItem = null;
        ItemStack nutrientGel = null;
        int gelSlot = -1;

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (ChickenUtils.isPocketChicken(item) && ChickenUtils.isAdult(item) && chickenItem == null) {
                chickenItem = item;
            } else if (item.isSimilar(GCEItems.NUTRIENT_GEL)) {
                nutrientGel = item;
                gelSlot = slot;
            }
        }

        if (chickenItem == null || nutrientGel == null) {
            return null;
        }

        PocketChickenData data = PocketChickenData.fromItem(chickenItem);
        if (data == null) {
            return null;
        }

        // Create a baby cloned chicken with identical DNA
        DNA originalDna = data.getDNA();
        ItemStack babyClone = ChickenUtils.fromDNA(originalDna, true);

        // Inherit expanded species if present
        ExpandedChickenSpecies exp = data.getExpandedSpecies();
        if (exp != null) {
            JsonObject json = new JsonObject();
            json.addProperty("baby", true);
            json.addProperty("_age", -24000);
            json.addProperty("_breedable", false);
            json.addProperty("_health", 4d);
            ChickenUtils.setExpandedPocketChicken(babyClone, json, originalDna, exp);
        }

        ItemStack returnedParent = chickenItem.clone();
        returnedParent.setAmount(1);

        MachineRecipe recipe = new MachineRecipe(
            GeneticChickengineering.getConfigService().isTest() ? 1 : 20,
            new ItemStack[] {chickenItem, nutrientGel},
            new ItemStack[] {returnedParent, babyClone}
        );

        if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
            return null;
        }

        // Consume 1 parent from input and 1 nutrient gel
        ItemUtils.consumeItem(chickenItem, 1, false);
        menu.consumeItem(gelSlot, 1);

        if (GeneticChickengineering.getConfigService().isSoundsEnabled()) {
            menu.getBlock().getWorld().playSound(menu.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1f, 1.2f);
        }

        return recipe;
    }
}
