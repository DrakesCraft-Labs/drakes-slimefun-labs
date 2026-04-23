package me.pranavverma.advancedtech.general.items.firecake;

import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockUseHandler;
import org.bukkit.inventory.ItemStack;

public class FireCake extends SlimefunItem implements Radioactive {

    public FireCake(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        BlockUseHandler blockUseHandler = this::onBlockRightClick;
        addItemHandler(blockUseHandler);
    }

    private void onBlockRightClick(PlayerRightClickEvent event) {
        event.cancel();
        event.getPlayer().sendMessage("Uh Oh! You Right Clicked the FIRE CAKE!");
        event.getPlayer().setFireTicks(5 * 20);
        
    }

    @Override
    public Radioactivity getRadioactivity() {
        return Radioactivity.HIGH;
    }
}