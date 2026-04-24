package me.voper.slimeframe.implementation.items.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock;
import dev.drake.dough.items.CustomItemStack;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class Component extends UnplaceableBlock implements RecipeDisplayItem {

    public static final RecipeType RECIPE_TYPE = new RecipeType(Keys.createKey("prime_component_reward"), new CustomItemStack(Material.DIAMOND, ChatColor.AQUA + "This item is dropped by the relics below:"));

    private final List<ItemStack> relics = new ArrayList<>();

    public Component(SlimefunItemStack item) {
        super(Groups.PRIME_COMPONENTS, item, RECIPE_TYPE, Utils.NULL_ITEMS_ARRAY);
    }

    public void setRelics(List<ItemStack> relics) {
        this.relics.addAll(relics);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return relics.stream().flatMap(relic -> Stream.of(relic, null)).toList();
    }

    @Nonnull
    @Override
    public String getRecipeSectionLabel(@Nonnull Player p) {
        return "&7\u21E9 Dropped by \u21E9";
    }
}
