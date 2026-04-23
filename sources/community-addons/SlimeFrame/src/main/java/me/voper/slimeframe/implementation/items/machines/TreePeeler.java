package me.voper.slimeframe.implementation.items.machines;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;

import me.voper.slimeframe.implementation.items.abstracts.AbstractProcessorMachine;

public class TreePeeler extends AbstractProcessorMachine implements RecipeDisplayItem {

    private static final Map<XMaterial, XMaterial> STRIPPED_WOODS_LOGS = Arrays.stream(XMaterial.values())
            .filter(m -> !m.name().startsWith("STRIPPED_") && (m.name().endsWith("LOG") || m.name().endsWith("WOOD")))
            .filter(XMaterial::isSupported)
            .map(m -> Map.entry(m, XMaterial.matchXMaterial("STRIPPED_" + m.name())))
            .filter(e -> e.getValue().isPresent() && e.getValue().get().isSupported())
            .map(e -> Map.entry(e.getKey(), e.getValue().get()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private static final int TIME = 5;

    public TreePeeler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void postRegister() {
        STRIPPED_WOODS_LOGS
                .forEach((wood, stripped) -> registerRecipe(TIME, wood.parseItem(), stripped.parseItem()));
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.IRON_AXE);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return STRIPPED_WOODS_LOGS.entrySet().stream()
                .flatMap(entry -> Stream.of(entry.getKey().parseItem(), entry.getValue().parseItem()))
                .toList();
    }

    static {
        STRIPPED_WOODS_LOGS.put(XMaterial.CRIMSON_STEM, XMaterial.STRIPPED_CRIMSON_STEM);
        STRIPPED_WOODS_LOGS.put(XMaterial.CRIMSON_HYPHAE, XMaterial.STRIPPED_CRIMSON_HYPHAE);
        STRIPPED_WOODS_LOGS.put(XMaterial.WARPED_STEM, XMaterial.STRIPPED_WARPED_STEM);
        STRIPPED_WOODS_LOGS.put(XMaterial.WARPED_HYPHAE, XMaterial.STRIPPED_WARPED_HYPHAE);
    }

}
