package com.github.drakescraft-labs.gcereborn.items.common;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.handlers.EntityInteractHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.implementation.items.SimpleSlimefunItem;
import dev.drake.dough.protection.Interaction;

import com.github.drakescraft-labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft-labs.gcereborn.utils.ChickenUtils;

public class ChickenNet extends SimpleSlimefunItem<EntityInteractHandler> implements NotPlaceable {

    public ChickenNet(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(getItemUsehandler());
    }

    @Override
    @Nonnull
    public EntityInteractHandler getItemHandler() {
        return (e, item, offHand) -> {
            if (e.getRightClicked().getType() != EntityType.CHICKEN) {
                return;
            }
            Chicken chicken = (Chicken) e.getRightClicked();

            if (!Slimefun.getProtectionManager().hasPermission(e.getPlayer(), chicken.getLocation(), Interaction.INTERACT_ENTITY)) {
                GeneticChickengineering.getLocalization().sendMessage(e.getPlayer(), "no-permission");
                return;
            }

            Location l = chicken.getLocation().toCenterLocation();
            ItemStack pocketChicken = ChickenUtils.capture(chicken);
            l.getWorld().dropItemNaturally(l, pocketChicken);
            if (GeneticChickengineering.getConfigService().isSoundsEnabled()) {
                l.getWorld().playSound(l, Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
            }
        };
    }

    @Nonnull
    public ItemUseHandler getItemUsehandler() {
        return PlayerRightClickEvent::cancel;
    }
}
