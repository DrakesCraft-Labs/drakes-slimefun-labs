package me.voper.slimeframe.implementation.items.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;

import me.voper.slimeframe.implementation.items.abstracts.AbstractSelectorMachine;
import me.voper.slimeframe.utils.MachineUtils;

import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import net.md_5.bungee.api.ChatColor;

@ParametersAreNonnullByDefault
public class ConcreteGenerator extends AbstractSelectorMachine implements RecipeDisplayItem {

    private static final String BLOCK_KEY = "concrete_selector";
    private static final Map<Material, ItemStack> OUTPUT_MAPPER;
    private static final List<ItemStack> CONCRETES = List.of(
            MachineUtils.SELECTOR,
            MachineUtils.selectorItem(Material.WHITE_CONCRETE),
            MachineUtils.selectorItem(Material.ORANGE_CONCRETE),
            MachineUtils.selectorItem(Material.MAGENTA_CONCRETE),
            MachineUtils.selectorItem(Material.LIGHT_BLUE_CONCRETE),
            MachineUtils.selectorItem(Material.YELLOW_CONCRETE),
            MachineUtils.selectorItem(Material.LIME_CONCRETE),
            MachineUtils.selectorItem(Material.PINK_CONCRETE),
            MachineUtils.selectorItem(Material.GRAY_CONCRETE),
            MachineUtils.selectorItem(Material.LIGHT_GRAY_CONCRETE),
            MachineUtils.selectorItem(Material.CYAN_CONCRETE),
            MachineUtils.selectorItem(Material.PURPLE_CONCRETE),
            MachineUtils.selectorItem(Material.BLUE_CONCRETE),
            MachineUtils.selectorItem(Material.BROWN_CONCRETE),
            MachineUtils.selectorItem(Material.GREEN_CONCRETE),
            MachineUtils.selectorItem(Material.RED_CONCRETE),
            MachineUtils.selectorItem(Material.BLACK_CONCRETE)
    );

    static {
        OUTPUT_MAPPER = new HashMap<>();
        CONCRETES.subList(1, CONCRETES.size()).forEach(item -> {
            OUTPUT_MAPPER.put(item.getType(), new ItemStack(item.getType()));
        });
    }

    public ConcreteGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.outputAmount = 6;
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.WATER_BUCKET);
    }

    @Override
    public void postRegister() {
        registerRecipe(6, new ItemStack[]{new ItemStack(Material.SAND, 2), new ItemStack(Material.GRAVEL, 2)}, new ItemStack[]{new ItemStack(Material.OBSERVER)});
    }

    @Nonnull
    @Override
    public List<ItemStack> selectionList() {
        return CONCRETES;
    }

    @Nonnull
    @Override
    public String getBlockKey() {
        return BLOCK_KEY;
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return menu.getItemInSlot(getSelectorSlot()).getType().name().endsWith("_CONCRETE");
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.BARRIER, ChatColor.RED + "Select a concrete to generate!"));
    }

    @Nonnull
    @Override
    protected ItemStack getOutput(@Nonnull ItemStack item) {
        return OUTPUT_MAPPER.get(item.getType());
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> displayRecipes = new ArrayList<>();
        for (ItemStack itemStack : CONCRETES) {
            if (!itemStack.getType().name().endsWith("_CONCRETE")) continue;
            displayRecipes.add(recipes.get(0).getInput()[0]);
            ItemStack clone = itemStack.clone();
            clone.setAmount(outputAmount * production);
            displayRecipes.add(clone);
            displayRecipes.add(recipes.get(0).getInput()[1]);
            displayRecipes.add(null);
        }
        return displayRecipes;
    }

}
