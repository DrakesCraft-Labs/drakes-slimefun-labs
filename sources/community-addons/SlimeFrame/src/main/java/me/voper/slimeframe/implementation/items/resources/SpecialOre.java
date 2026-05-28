package me.voper.slimeframe.implementation.items.resources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.NotPlaceable;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.utils.Keys;
import me.voper.slimeframe.utils.Utils;

import lombok.Getter;

@Getter
@ParametersAreNonnullByDefault
public final class SpecialOre extends SlimefunItem implements NotPlaceable, Comparable<SpecialOre>, RecipeDisplayItem {

    public static final Map<Material, SpecialOre> SOURCE_ORE_MAP = new HashMap<>();
    public static final RecipeType RECIPE_TYPE = new RecipeType(Keys.createKey("ores_recipe"), new CustomItemStack(Material.DIAMOND_PICKAXE, "&bNosam Pickaxe", "&fYou must mine a specific ore", "&fusing a Nosam Pickaxe."));

    private final Material[] source;
    private final Rarity rarity;

    public SpecialOre(SlimefunItemStack item, Material[] source, Rarity rarity) {
        super(Groups.RESOURCES, item, RECIPE_TYPE, Utils.NULL_ITEMS_ARRAY);
        this.source = source;
        this.rarity = rarity;
        this.disenchantable = false;
        this.enchantable = false;
        for (Material m : source) {
            SOURCE_ORE_MAP.put(m, this);
        }
    }

    public SpecialOre(SlimefunItemStack item, Material source, Rarity rarity) {
        this(item, new Material[]{source}, rarity);
    }

    @Nullable
    public static SpecialOre getBySource(Material material) {
        return SOURCE_ORE_MAP.get(material);
    }

    @Override
    public int compareTo(@Nonnull SpecialOre o) {
        return o.rarity.value - this.rarity.value;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return Arrays.stream(this.source)
                .flatMap(m -> Stream.of(null, new ItemStack(m)))
                .toList();
    }

    @Getter
    public enum Rarity {
        COMMON(50),
        UNCOMMON(35),
        RARE(15);

        private final int value;

        Rarity(int value) {
            this.value = value;
        }
    }

}
