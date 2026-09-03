package com.github.drakescraft_labs.gcereborn.items.common;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.handlers.EntityInteractHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.utils.ChickenUtils;

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
            boolean allowed = hasPermissionCompat(e.getPlayer(), chicken.getLocation(), "INTERACT_ENTITY")
                    || hasPermissionCompat(e.getPlayer(), chicken.getLocation(), "INTERACT_BLOCK")
                    || e.getPlayer().hasPermission("slimefun.inventory.bypass");

            if (!allowed) {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static boolean hasPermissionCompat(org.bukkit.entity.Player player, Location location, String interactionName) {
        Object manager = Slimefun.getProtectionManager();
        for (Method method : manager.getClass().getMethods()) {
            if (!method.getName().equals("hasPermission") || method.getParameterCount() != 3) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (!parameterTypes[0].isAssignableFrom(player.getClass()) || !parameterTypes[2].isEnum()) {
                continue;
            }
            Object target = parameterTypes[1].isAssignableFrom(location.getClass()) ? location : location.getBlock();
            try {
                Object interaction = Enum.valueOf((Class<Enum>) parameterTypes[2], interactionName);
                return (boolean) method.invoke(manager, player, target, interaction);
            } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                // Try the next compatible overload.
            }
        }
        return true;
    }
}
