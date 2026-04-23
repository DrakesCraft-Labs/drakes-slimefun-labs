package me.lucasgithuber.elementmanipulation.elements;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft-labs.slimefun4.core.attributes.Radioactivity;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;


public class RadioactiveElement extends SlimefunItem implements Radioactive, NotPlaceable {

    private final Radioactivity radioactivity;

    @ParametersAreNonnullByDefault
    public RadioactiveElement(ItemGroup itemGroup, Radioactivity radioactivity, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        this.radioactivity = radioactivity;
        addItemHandler(onRightClick());
    }

    @Nonnull
    private ItemUseHandler onRightClick() {
        return PlayerRightClickEvent::cancel;
    }

    @Override
    @Nonnull
    public Radioactivity getRadioactivity() {
        return radioactivity;
    }

}
