package me.schntgaispock.wildernether.slimefun.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.handlers.ItemUseHandler;
import me.schntgaispock.wildernether.util.GeneralUtil;

public class WildernetherCuisine extends SlimefunItem {

    /**
     * Dataclass for effects granted by eating Wildernether cuisine
     */

    int hunger = 1;
    float saturation = 1;
    List<PotionEffect> effects = new ArrayList<>();

    @ParametersAreNonnullByDefault
    public WildernetherCuisine(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc) {
        super(ig, is, rt, rc);
    }

    @ParametersAreNonnullByDefault
    public WildernetherCuisine(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc, int hunger) {
        this(ig, is, rt, rc);
        this.hunger = hunger;
    }

    @ParametersAreNonnullByDefault
    public WildernetherCuisine(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc, int hunger, double saturation) {
        this(ig, is, rt, rc, hunger);
        this.saturation = (float) saturation;
    }

    @ParametersAreNonnullByDefault
    public WildernetherCuisine(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc, int hunger, double saturation, PotionEffect... effects) {
        this(ig, is, rt, rc, hunger, saturation);
        this.effects.addAll(Arrays.asList(effects));
    }

    @Override
    public void preRegister() {
        addItemHandler((ItemUseHandler) this::onRightClick);
    }

    @EventHandler
    public void onRightClick(@Nonnull PlayerRightClickEvent e) {
        e.cancel();

        Player p = e.getPlayer();

        // Full
        if (p.getFoodLevel() > 19) {
            return;
        }

        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1, 1);

        p.setFoodLevel(Math.min(p.getFoodLevel() + this.hunger, 20));
        p.setSaturation(p.getSaturation() + this.saturation);
        if (!this.effects.isEmpty()) {
            for (PotionEffect effect : effects) {
                p.addPotionEffect(effect);
            }
        }

        ItemStack food = e.getItem();
        ItemStack returnItem = GeneralUtil.returnItemAfterUsing(food);

        food.setAmount(food.getAmount() - 1);
        if (returnItem != null) {
            p.getInventory().addItem(returnItem);
        }
        
    }
}
