package com.github.drakescraft_labs.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.inventory.InvUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.items.chicken.ExpandedChickenSpecies;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;
import com.github.drakescraft_labs.gcereborn.utils.PocketChickenData;

public class MutagenicSplicer extends AbstractMachine {

    public MutagenicSplicer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return new ItemStack(Material.ENCHANTING_TABLE);
    }

    @Override
    protected void tick(@Nonnull Block b) {
        super.tick(b);
        if (GeneticChickengineering.getConfigService().isParticlesEnabled() && Math.random() < 0.25) {
            b.getWorld().spawnParticle(Particle.PORTAL, b.getLocation().add(0.5, 0.9, 0.5), 6, 0.2, 0.2, 0.2, 0.1);
        }
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        ItemStack chickenItem = null;
        ItemStack serumItem = null;
        ItemStack catalystItem = null;
        ExpandedChickenSpecies matchedSpecies = null;
        int chickenSlot = -1;
        int serumSlot = -1;
        int catalystSlot = -1;

        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (ChickenUtils.isPocketChicken(item) && ChickenUtils.isAdult(item) && chickenItem == null) {
                chickenItem = item;
                chickenSlot = slot;
            } else if (item.isSimilar(GCEItems.MUTAGENIC_SERUM) && serumItem == null) {
                serumItem = item;
                serumSlot = slot;
            } else if (catalystItem == null) {
                ExpandedChickenSpecies s = ExpandedChickenSpecies.matchCatalyst(item);
                if (s != null) {
                    catalystItem = item;
                    catalystSlot = slot;
                    matchedSpecies = s;
                }
            }
        }

        if (chickenItem == null || serumItem == null || catalystItem == null || matchedSpecies == null) {
            return null;
        }

        PocketChickenData data = PocketChickenData.fromItem(chickenItem);
        if (data == null) {
            return null;
        }

        // Mutate the chicken into the new expanded species
        ItemStack mutated = chickenItem.clone();
        mutated.setAmount(1);
        DNA originalDna = data.getDNA();
        JsonObject json = data.getAdapter() != null ? data.getAdapter().deepCopy() : new JsonObject();
        json.addProperty("baby", false);
        json.addProperty("_health", 4d);

        ChickenUtils.setExpandedPocketChicken(mutated, json, originalDna, matchedSpecies);

        MachineRecipe recipe = new MachineRecipe(
            GeneticChickengineering.getConfigService().isTest() ? 1 : 30,
            new ItemStack[] {chickenItem, serumItem, catalystItem},
            new ItemStack[] {mutated}
        );

        if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
            return null;
        }

        menu.consumeItem(chickenSlot, 1);
        menu.consumeItem(serumSlot, 1);
        menu.consumeItem(catalystSlot, 1);

        if (GeneticChickengineering.getConfigService().isSoundsEnabled()) {
            menu.getBlock().getWorld().playSound(menu.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 0.8f);
        }

        return recipe;
    }
}
