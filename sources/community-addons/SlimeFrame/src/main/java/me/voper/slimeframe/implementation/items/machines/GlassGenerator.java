package me.voper.slimeframe.implementation.items.machines;

import java.util.*;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.tags.SlimefunTag;

import me.voper.slimeframe.implementation.items.abstracts.AbstractSelectorMachine;
import me.voper.slimeframe.utils.MachineUtils;

import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import net.md_5.bungee.api.ChatColor;

public class GlassGenerator extends AbstractSelectorMachine implements RecipeDisplayItem {

    private static final String BLOCK_KEY = "glass_selector";
    private static final Map<Material, ItemStack> OUTPUT_MAPPER;
    private static final List<ItemStack> GLASSES = new ArrayList<>();

    static {
        GLASSES.add(MachineUtils.SELECTOR);
        GLASSES.addAll(SlimefunTag.GLASS_BLOCKS.stream()
                .sorted(Comparator.comparing(Enum::name))
                .filter(m -> !m.name().contains("TINTED"))
                .map(MachineUtils::selectorItem)
                .toList());

        OUTPUT_MAPPER = new HashMap<>();
        GLASSES.subList(1, GLASSES.size()).forEach(item -> {
            OUTPUT_MAPPER.put(item.getType(), new ItemStack(item.getType()));
        });
    }

    public GlassGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.outputAmount = 2;
    }

    @Override
    public void postRegister() {
        registerRecipe(5, new ItemStack(Material.SAND), new ItemStack(Material.GLASS));
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.FURNACE);
    }

    @Nonnull
    @Override
    public List<ItemStack> selectionList() {
        return GLASSES;
    }

    @Nonnull
    @Override
    public String getBlockKey() {
        return BLOCK_KEY;
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return menu.getItemInSlot(getSelectorSlot()).getType() != Material.FIREWORK_STAR;
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.BARRIER, ChatColor.RED + "Select a glass to generate!"));
    }

    @Nonnull
    @Override
    protected ItemStack getOutput(@Nonnull ItemStack item) {
        return OUTPUT_MAPPER.get(item.getType());
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return GLASSES.subList(1, GLASSES.size()).stream()
                .flatMap(item -> Stream.of(new ItemStack(Material.SAND), new ItemStack(item.getType(), outputAmount * production)))
                .toList();
    }
}
