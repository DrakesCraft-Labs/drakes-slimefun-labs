package com.epicplayera10.potionexpansion.multiblocks.alchemic;

import com.epicplayera10.potionexpansion.PotionExpansion;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.multiblocks.MultiBlockMachine;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.libraries.paperlib.PaperLib;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;

public class AlchemicStation extends MultiBlockMachine {
    private List<AlchemicRecipe> recipes = new ArrayList<>();
    private final int INGREDIENT_SLOT = 1;
    private final int POTION_1_SLOT = 6;
    private final int POTION_2_SLOT = 7;
    private final int POTION_3_SLOT = 8;

    @ParametersAreNonnullByDefault
    public AlchemicStation(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[]{null, new ItemStack(Material.WHITE_STAINED_GLASS), null,
                new ItemStack(Material.WHITE_STAINED_GLASS), new ItemStack(Material.GLOWSTONE), new ItemStack(Material.WHITE_STAINED_GLASS),
                new ItemStack(Material.CAULDRON), new ItemStack(Material.DISPENSER), new ItemStack(Material.CAULDRON)}, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player player, Block block) {
        Block dispBlock = block.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(dispBlock, false).getState();

        if (state instanceof Dispenser) {
            Dispenser disp = (Dispenser) state;
            Inventory inv = disp.getInventory();

            List<Block> glassBlocks = getWhiteGlassBlocks(block);

            AlchemicRecipe recipe = checkRecipe(inv);

            if (recipe != null) {
                if (SlimefunUtils.canPlayerUseItem(player, recipe.getOutput(), true)) {
                    ItemUtils.consumeItem(inv.getItem(INGREDIENT_SLOT), true);
                    startAnimation(recipe, inv, block, glassBlocks);
                }
            } else {
                Slimefun.getLocalization().sendMessage(player, "machines.pattern-not-found", true);
            }
        }
    }

    public List<AlchemicRecipe> getAlchemicRecipes() {
        return recipes;
    }

    @Nullable
    private AlchemicRecipe checkRecipe(@Nonnull Inventory input) {
        ItemStack ingredient = input.getItem(INGREDIENT_SLOT);

        if (ingredient != null) {
            for (AlchemicRecipe recipe : recipes) {
                if (SlimefunUtils.isItemSimilar(ingredient, recipe.getIngredient(), true)) {
                    if (anyValidInputPotion(recipe, input)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }

    private List<Block> getWhiteGlassBlocks(@Nonnull Block block) {
        List<Block> list = new ArrayList<>();
        if (block.getRelative(BlockFace.NORTH).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.NORTH));
        } else if (block.getRelative(BlockFace.EAST).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.EAST));
        }

        if (block.getRelative(BlockFace.UP).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.UP));
        }

        if (block.getRelative(BlockFace.SOUTH).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.SOUTH));
        } else if (block.getRelative(BlockFace.WEST).getType() == Material.WHITE_STAINED_GLASS) {
            list.add(block.getRelative(BlockFace.WEST));
        }

        return list;
    }

    @ParametersAreNonnullByDefault
    private void startAnimation(AlchemicRecipe recipe, Inventory dispInv, Block b, List<Block> glassBlocks) {
        for (int i = 0; i < 10; i++) {
            int j = i;
            PotionExpansion.runSync(() -> {
                World world = b.getWorld();
                if (j < 9) {
                    world.playSound(b.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1f, 2f);
                    glassBlocks.forEach(glassBlock -> {
                        world.playEffect(glassBlock.getLocation().clone().add(0.5, 0, 0.5), Effect.SMOKE, BlockFace.UP);
                    });
                } else {
                    if (anyValidInputPotion(recipe, dispInv)) {
                        world.playSound(b.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1f, 0.1f);
                        brew(recipe, dispInv);
                    } else {
                        world.playSound(b.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_HURT, 1f, 0.5f);
                        world.dropItemNaturally(b.getLocation(), recipe.getIngredient());
                    }
                }
            }, i * 20L);
        }
    }

    @ParametersAreNonnullByDefault
    private void brew(AlchemicRecipe recipe, Inventory dispInv) {
        for (int i = POTION_1_SLOT; i < POTION_3_SLOT + 1; i++) {
            if (isInputPotionValid(recipe, dispInv.getItem(i))) {
                dispInv.setItem(i, recipe.getOutput());
            }
        }
    }

    @ParametersAreNonnullByDefault
    private boolean anyValidInputPotion(AlchemicRecipe recipe, Inventory dispInv) {
        for (int i = POTION_1_SLOT; i < POTION_3_SLOT + 1; i++) {
            if (isInputPotionValid(recipe, dispInv.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isInputPotionValid(@Nonnull AlchemicRecipe recipe, @Nullable ItemStack item) {
        if (item != null && item.getType() == Material.POTION && item.hasItemMeta()) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            return meta.getBasePotionType() == recipe.getInputPotion();
        }
        return false;
    }
}
