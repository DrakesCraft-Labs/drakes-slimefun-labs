package me.voper.slimeframe.implementation.items.multiblocks;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.multiblocks.MultiBlockMachine;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.ItemUtils;
import com.github.drakescraft_labs.slimefun4.libraries.paperlib.PaperLib;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;

import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.utils.Keys;

public class Foundry extends MultiBlockMachine {

    public static final RecipeType RECIPE_TYPE = new RecipeType(
            Keys.createKey("foundry_recipe"),
            SFrameStacks.FOUNDRY
    );

    public Foundry() {
        super(Groups.MULTIBLOCKS, SFrameStacks.FOUNDRY, new ItemStack[]{
                null, null, null,
                new ItemStack(Material.STONECUTTER), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.STONECUTTER),
                new ItemStack(Material.POLISHED_BLACKSTONE), new ItemStack(Material.DISPENSER), new ItemStack(Material.POLISHED_BLACKSTONE)
        }, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block dispBlock = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(dispBlock, false).getState();

        if (state instanceof Dispenser disp) {
            Inventory inv = disp.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (int i = 0; i < inputs.size(); i++) {
                if (canCraft(inv, inputs.get(i))) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, inputs.get(i)).clone();

                    if (SlimefunUtils.canPlayerUseItem(p, output, true)) {
                        craft(dispBlock, p, b, inv, inputs.get(i), output);
                    }

                    return;
                }
            }

            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
            } else {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }

    private boolean canCraft(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                return false;
            }
        }

        return true;
    }

    protected void craft(Block dispenser, Player p, Block b, Inventory inv, ItemStack[] recipe, ItemStack output) {
        Inventory fakeInv = createVirtualInventory(inv);
        Inventory outputInv = findOutputInventory(output, dispenser, inv, fakeInv);

        craftItem(inv, recipe, p, b);
        if (outputInv != null) {
            // craftItem(inv, recipe, p, b);
            outputInv.addItem(output);
        } else {
            // craftItem(inv, recipe, p, b);
            dispenser.getWorld().dropItem(b.getLocation(), output);
            Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
            p.sendMessage("&dCrafted item has been dropped instead");
        }
    }

    public void craftItem(Inventory inv, ItemStack[] recipe, Player p, Block b) {
        for (int j = 0; j < 9; j++) {
            ItemStack item = inv.getContents()[j];

            if (item != null && item.getType() != Material.AIR && SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                ItemUtils.consumeItem(item, recipe[j].getAmount(), true);
            }
        }

        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_HIT, 1, 1);
        p.getWorld().playEffect(b.getLocation(), Effect.BONE_MEAL_USE, 1);
    }

    protected @Nonnull Inventory createVirtualInventory(@Nonnull Inventory inv) {
        Inventory fakeInv = Bukkit.createInventory(null, 9, "Fake Inventory");

        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack stack = inv.getContents()[j];

            if (stack != null) {
                stack = stack.clone();
                ItemUtils.consumeItem(stack, true);
            }

            fakeInv.setItem(j, stack);
        }

        return fakeInv;
    }


}
