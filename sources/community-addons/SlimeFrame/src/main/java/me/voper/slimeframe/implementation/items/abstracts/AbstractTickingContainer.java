package me.voper.slimeframe.implementation.items.abstracts;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

/*
Credits to ProfElements
https://github.com/ProfElements/DynaTech/blob/990c942e4302869fe80cce33c49fa2e458a3af99/src/main/java/me/profelements/dynatech/items/abstracts/AbstractTickingContainer.java#L15
*/
public abstract class AbstractTickingContainer extends AbstractContainer {

    public AbstractTickingContainer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return synchronous();
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                BlockMenu menu = BlockStorage.getInventory(block);
                if (menu != null) {
                    AbstractTickingContainer.this.tick(menu, block);
                }
            }

        });
    }

    protected abstract void tick(BlockMenu menu, Block b);

    protected boolean synchronous() {
        return false;
    }

}
