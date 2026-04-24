package com.github.drakescraft_labs.gcereborn.items.common;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;

public class ResourceEgg extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    private final Material resource;
    private final boolean allowInNether;

    public ResourceEgg(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, Material resource,
                       boolean allowInNether) {
        super(itemGroup, item, recipeType, makeRecipe(resource));
        this.resource = resource;
        this.allowInNether = allowInNether;
    }

    @Nonnull
    private static ItemStack[] makeRecipe(@Nonnull Material resource) {
        ItemStack[] recipe = new ItemStack[9];
        ItemStack fake = GCEItems.POCKET_CHICKEN.clone();
        DNA dna;
        if (resource == Material.WATER) {
            dna = new DNA(62);
        } else {
            dna = new DNA(41);
        }
        ChickenUtils.setPocketChicken(fake, null, dna);
        recipe[4] = fake;
        return recipe;
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();
            Optional<Block> block = e.getClickedBlock();
            if (block.isEmpty()) {
                return;
            }
            Block b = block.get();
            Block place = b.getRelative(e.getClickedFace());
            if (!Slimefun.getProtectionManager().hasPermission(e.getPlayer(), place.getLocation(), Interaction.PLACE_BLOCK)) {
                GeneticChickengineering.getLocalization().sendMessage(e.getPlayer(), "no-permission");
                return;
            }
            if (place.isReplaceable()) {
                if (resource == Material.WATER && !allowInNether && place.getWorld().getEnvironment() == World.Environment.NETHER) {
                    place.getWorld().spawnParticle(Particle.CLOUD, place.getLocation().add(0.5, 0, 0.5), 5);
                    if (GeneticChickengineering.getConfigService().isSoundsEnabled()) {
                        place.getWorld().playSound(place.getLocation().toCenterLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F);
                    }
                } else {
                    place.setType(resource);
                }

                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(e.getItem(), false);
                }
            }
        };
    }
}
