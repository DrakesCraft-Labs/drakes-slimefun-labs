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
public class WoolGenerator extends AbstractSelectorMachine implements RecipeDisplayItem {

    private static final String BLOCK_KEY = "wool_selector";
    private static final Map<Material, ItemStack> OUTPUT_MAPPER;
    private static final List<ItemStack> WOOLS = List.of(
            MachineUtils.SELECTOR,
            MachineUtils.selectorItem(Material.WHITE_WOOL),
            MachineUtils.selectorItem(Material.ORANGE_WOOL),
            MachineUtils.selectorItem(Material.MAGENTA_WOOL),
            MachineUtils.selectorItem(Material.LIGHT_BLUE_WOOL),
            MachineUtils.selectorItem(Material.YELLOW_WOOL),
            MachineUtils.selectorItem(Material.LIME_WOOL),
            MachineUtils.selectorItem(Material.PINK_WOOL),
            MachineUtils.selectorItem(Material.GRAY_WOOL),
            MachineUtils.selectorItem(Material.LIGHT_GRAY_WOOL),
            MachineUtils.selectorItem(Material.CYAN_WOOL),
            MachineUtils.selectorItem(Material.PURPLE_WOOL),
            MachineUtils.selectorItem(Material.BLUE_WOOL),
            MachineUtils.selectorItem(Material.BROWN_WOOL),
            MachineUtils.selectorItem(Material.GREEN_WOOL),
            MachineUtils.selectorItem(Material.RED_WOOL),
            MachineUtils.selectorItem(Material.BLACK_WOOL)
    );

    static {
        OUTPUT_MAPPER = new HashMap<>();
        WOOLS.subList(1, WOOLS.size()).forEach(item -> {
            OUTPUT_MAPPER.put(item.getType(), new ItemStack(item.getType()));
        });
    }

    public WoolGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        this.outputAmount = 4;
    }

    @Override
    public void postRegister() {
        registerRecipe(6, new ItemStack[]{new ItemStack(Material.STRING, 2)}, new ItemStack[]{new ItemStack(Material.WHITE_WOOL, 4)});
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.LOOM);
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return menu.getItemInSlot(getSelectorSlot()).getType().name().endsWith("_WOOL");
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        menu.replaceExistingItem(getStatusSlot(), new CustomItemStack(Material.BARRIER, ChatColor.RED + "Select a wool to generate!"));
    }

    @Nonnull
    @Override
    public List<ItemStack> selectionList() {
        return WOOLS;
    }

    @Nonnull
    @Override
    public String getBlockKey() {
        return BLOCK_KEY;
    }

    @Nonnull
    @Override
    protected ItemStack getOutput(@Nonnull ItemStack item) {
        return OUTPUT_MAPPER.get(item.getType());
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> recipes = new ArrayList<>();
        WOOLS.subList(1, WOOLS.size()).forEach(itemStack -> {
            recipes.add(new ItemStack(Material.STRING, 2));
            recipes.add(new ItemStack(itemStack.getType(), outputAmount * production));
        });
        return recipes;
    }
}
