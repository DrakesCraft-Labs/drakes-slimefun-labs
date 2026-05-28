package me.voper.slimeframe.implementation.items.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.abstracts.AbstractSelectorMachine;
import me.voper.slimeframe.implementation.items.multiblocks.Foundry;
import me.voper.slimeframe.utils.MachineUtils;

import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import net.md_5.bungee.api.ChatColor;

public class TerracottaGenerator extends AbstractSelectorMachine implements RecipeDisplayItem {

    private static final String BLOCK_KEY = "terracota_selector";
    private static final Map<Material, ItemStack> OUTPUT_MAPPER;
    private static final List<ItemStack> TERRACOTTAS = List.of(
            MachineUtils.SELECTOR,
            MachineUtils.selectorItem(Material.TERRACOTTA),
            MachineUtils.selectorItem(Material.WHITE_TERRACOTTA),
            MachineUtils.selectorItem(Material.ORANGE_TERRACOTTA),
            MachineUtils.selectorItem(Material.MAGENTA_TERRACOTTA),
            MachineUtils.selectorItem(Material.LIGHT_BLUE_TERRACOTTA),
            MachineUtils.selectorItem(Material.YELLOW_TERRACOTTA),
            MachineUtils.selectorItem(Material.LIME_TERRACOTTA),
            MachineUtils.selectorItem(Material.PINK_TERRACOTTA),
            MachineUtils.selectorItem(Material.GRAY_TERRACOTTA),
            MachineUtils.selectorItem(Material.LIGHT_GRAY_TERRACOTTA),
            MachineUtils.selectorItem(Material.CYAN_TERRACOTTA),
            MachineUtils.selectorItem(Material.PURPLE_TERRACOTTA),
            MachineUtils.selectorItem(Material.BLUE_TERRACOTTA),
            MachineUtils.selectorItem(Material.BROWN_TERRACOTTA),
            MachineUtils.selectorItem(Material.GREEN_TERRACOTTA),
            MachineUtils.selectorItem(Material.RED_TERRACOTTA),
            MachineUtils.selectorItem(Material.BLACK_TERRACOTTA)
    );

    static {
        OUTPUT_MAPPER = new HashMap<>();
        TERRACOTTAS.subList(1, TERRACOTTAS.size()).forEach(item -> {
            OUTPUT_MAPPER.put(item.getType(), new ItemStack(item.getType()));
        });
    }

    public TerracottaGenerator(SlimefunItemStack item, ItemStack[] recipe) {
        super(Groups.MACHINES, item, Foundry.RECIPE_TYPE, recipe);
        this.outputAmount = 6;
    }

    @Override
    protected ItemStack getProgressBar() {
        return new ItemStack(Material.WHITE_TERRACOTTA);
    }

    @Override
    public void postRegister() {
        registerRecipe(6, new ItemStack[]{new ItemStack(Material.DIRT, 2), new ItemStack(Material.CLAY, 2)}, new ItemStack[]{new ItemStack(Material.OBSERVER)});
    }

    @Nonnull
    @Override
    public List<ItemStack> selectionList() {
        return TERRACOTTAS;
    }

    @Nonnull
    @Override
    public String getBlockKey() {
        return BLOCK_KEY;
    }

    @Override
    protected boolean checkCraftConditions(BlockMenu menu) {
        return menu.getItemInSlot(getSelectorSlot()).getType().name().endsWith("TERRACOTTA");
    }

    @Override
    protected void onCraftConditionsNotMet(BlockMenu menu) {
        MachineUtils.replaceExistingItemViewer(menu, getStatusSlot(), new CustomItemStack(Material.BARRIER, ChatColor.RED + "Select a terracotta to generate!"));
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
        for (ItemStack itemStack : TERRACOTTAS) {
            if (!itemStack.getType().name().endsWith("TERRACOTTA")) continue;
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
