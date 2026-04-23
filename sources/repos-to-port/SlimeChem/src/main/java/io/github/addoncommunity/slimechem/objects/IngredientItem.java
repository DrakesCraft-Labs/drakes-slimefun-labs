package io.github.addoncommunity.slimechem.objects;

import io.github.addoncommunity.slimechem.implementation.atomic.Molecule;
import io.github.addoncommunity.slimechem.implementation.attributes.Ingredient;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft-labs.slimefun4.core.handlers.EntityInteractHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import lombok.Getter;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.api.items.Category;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import dev.drake.dough.items.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link SlimefunItem} to hold any class implementing {@link Ingredient}
 *
 * @author Seggan
 */

public class IngredientItem extends SlimefunItem implements NotPlaceable {

    @Getter
    private final Ingredient ingredient;

    private static final Map<Ingredient, IngredientAction> interactActions = getActions();

    @FunctionalInterface
    private interface IngredientAction {
        void apply(LivingEntity e);
    }

    public IngredientItem(Category category, Ingredient ingredient, RecipeType recipeType, ItemStack[] recipe) {
        super(category, ingredient.getItem(), recipeType, recipe);

        this.ingredient = ingredient;
        addItemHandler(onUse());
        addItemHandler(onEntityUse());
    }

    private EntityInteractHandler onEntityUse() {
        return (e, i, b) -> {
            e.setCancelled(true);
            Player p = e.getPlayer();
            IngredientAction run = interactActions.get(this.ingredient);
            if (run != null) {
                if (p.getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(i, false);
                }
                if (e.getRightClicked() instanceof LivingEntity) {
                    run.apply((LivingEntity) e.getRightClicked());
                }
            }
        };
    }

    private ItemUseHandler onUse() {
        return e -> {
            e.cancel();
            Player p = e.getPlayer();
            IngredientAction run = interactActions.get(this.ingredient);
            if (run != null) {
                if (p.getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(e.getItem(), false);
                }
                run.apply(p);
            }
        };
    }

    private static Map<Ingredient, IngredientAction> getActions() {
        Map<Ingredient, IngredientAction> actions = new HashMap<>();

        actions.put(Molecule.CAFFEINE, e -> e.addPotionEffect(new PotionEffect(
            PotionEffectType.SPEED,
            600,
            1
        )));

        return actions;
    }
}
