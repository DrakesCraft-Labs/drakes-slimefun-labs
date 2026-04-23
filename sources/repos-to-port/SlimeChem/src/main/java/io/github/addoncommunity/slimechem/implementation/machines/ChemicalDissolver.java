package io.github.addoncommunity.slimechem.implementation.machines;

import io.github.addoncommunity.slimechem.implementation.atomic.Element;
import io.github.addoncommunity.slimechem.implementation.atomic.Molecule;
import io.github.addoncommunity.slimechem.implementation.atomic.MoleculeIngredient;
import io.github.addoncommunity.slimechem.implementation.atomic.isotopes.Isotope;
import io.github.addoncommunity.slimechem.lists.Categories;
import io.github.addoncommunity.slimechem.lists.Items;
import com.github.drakescraft_labs.infinitylib.abstracts.AbstractMachine;
import com.github.drakescraft_labs.infinitylib.presets.MenuPreset;
import com.github.drakescraft_labs.infinitylib.recipes.normal.RandomRecipeMap;
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import dev.drake.dough.collections.RandomizedSet;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Dissolves {@link Molecule}s, {@link Material}s, and {@link SlimefunItemStack}s
 * into {@link Element}s, {@link Molecule}s, {@link Isotope}s
 * 
 * @author Mooy1
 *
 */
public class ChemicalDissolver extends AbstractMachine {
    
    public static final int ENERGY = 36;
    private static final int[] inputSlots = {2, 3, 4, 5, 6};
    private static final int[] outputSlots = {38, 39, 40, 41, 42, 47, 48, 49, 50, 51};
    private static final int[] inputBorder = {1, 10, 11, 12, 13, 14, 15, 16, 7, 22};
    private static final int[] outputBorder = {28, 29, 30, 31, 32, 33, 34, 37, 43, 46, 52};
    private static final int[] background = {0, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 35, 36, 44, 45, 53};
    
    private final RandomRecipeMap recipes = new RandomRecipeMap();

    private static RandomizedSet<ItemStack> makeRecipe(int[] chances, MoleculeIngredient... ingredients) {
        RandomizedSet<ItemStack> map = new RandomizedSet<>();
        for (int i = 0 ; i < chances.length ; i++) {
            map.add(ingredients[i].getNewItems()[0], chances[i]);
        }
        return map;
    }
    
    private void addRecipe(Material mat, int[] chances, MoleculeIngredient... ingredients) {
        this.recipes.put(new ItemStack(mat), makeRecipe(chances, ingredients));
    }

    private void addRecipe(@Nonnull ItemStack itemStack, int[] chances, MoleculeIngredient... ingredients) {
        this.recipes.put(itemStack, makeRecipe(chances, ingredients));
    }

    public ChemicalDissolver() {
        super(Categories.MACHINES, Items.CHEMICAL_DISSOLVER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        }, 0, ENERGY);

        // molecules
        for (Molecule molecule : Molecule.values()) {
            int[] chances = new int[molecule.getIngredients().length];
            Arrays.fill(chances, 100);
            addRecipe(molecule.getItem(), chances, molecule.getIngredients());
        }
        
        // Ores
        addRecipe(Material.COAL_ORE, new int[]{90, 10},
                Element.CARBON.asIngredient(12),
                Molecule.SILICON_DIOXIDE.asIngredient(5)
        );
        addRecipe(Material.IRON_ORE, new int[]{50, 30, 10, 6, 4},
                Molecule.IRON_III_OXIDE.asIngredient(4),
                Molecule.IRON_II_OXIDE.asIngredient(4),
                Molecule.SILICON_DIOXIDE.asIngredient(5),
                Molecule.IRON_PERSULFIDE.asIngredient(7),
                Molecule.COPPER_IRON_SULFIDE.asIngredient(6)
        );
        addRecipe(Material.GOLD_ORE, new int[]{70, 20, 10},
                Molecule.GOLD_TELLURIDE.asIngredient(4),
                Molecule.GOLD_ANTIMONIDE.asIngredient(4),
                Molecule.SILICON_DIOXIDE.asIngredient(5)
        );
        addRecipe(Material.REDSTONE_ORE, new int[]{60, 20, 10, 10},
                Molecule.COPPER_I_OXIDE.asIngredient(4),
                Molecule.PENTACOPPER_IRON_TETRASULFIDE.asIngredient(3),
                Molecule.COPPER_II_OXIDE.asIngredient(3),
                Molecule.SILICON_DIOXIDE.asIngredient(5)
        );
        addRecipe(Material.EMERALD_ORE, new int[]{90, 10},
                Molecule.BERYLLIUM_ALUMINUM_CYCLOSILICATE.asIngredient(6),
                Molecule.SILICATE.asIngredient(9)
        );
        addRecipe(Material.ANCIENT_DEBRIS, new int[]{80, 20},
                Molecule.SEABORGIUM_III_OXIDE.asIngredient(4),
                Molecule.SILICON_DIOXIDE.asIngredient(25)
        );

        // Plant matter

        // Wood
        // Standard: 1 plank = 2 cellulose
        // So 1 stick = 1 cellulose, 1 log = 8 cellulose, etc
        for (Material mat : Tag.LOGS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(8));
        }
        for (Material mat : Tag.PLANKS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(2));
        }
        // Loss of 1 cellulose
        for (Material mat : Tag.SIGNS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(4));
        }
        for (Material mat : Tag.ITEMS_BOATS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(10));
        }
        for (Material mat : Tag.WOODEN_BUTTONS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(2));
        }
        for (Material mat : Tag.WOODEN_SLABS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient());
        }
        for (Material mat : Tag.WOODEN_DOORS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(2));
        }
        for (Material mat : Tag.WOODEN_PRESSURE_PLATES.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(4));
        }
        for (Material mat : Tag.WOODEN_STAIRS.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(3));
        }
        // loss of 1 cellulose
        for (Material mat : Tag.WOODEN_FENCES.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(3));
        }
        for (Material mat : Tag.FENCE_GATES.getValues()) {
            addRecipe(mat, new int[]{100}, Molecule.CELLULOSE.asIngredient(8));
        }
        addRecipe(Material.STICK, new int[]{100}, Molecule.CELLULOSE.asIngredient());

        // Crops
        addRecipe(Material.WHEAT, new int[]{90, 10},
                Molecule.CELLULOSE.asIngredient(2),
                Molecule.PROTEIN.asIngredient(2)
        );
    }

    @Override
    public void setupMenu(@Nonnull BlockMenuPreset preset) {
        for (int i : inputBorder) {
            preset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            preset.addItem(i, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : background) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    public int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        if (itemTransportFlow == ItemTransportFlow.INSERT) {
            return inputSlots;
        } else if (itemTransportFlow == ItemTransportFlow.WITHDRAW) {
            return outputSlots;
        } else {
            return new int[0];
        }
    }

    @Override
    protected boolean process(@Nonnull BlockMenu blockMenu, @Nonnull Block block, @Nonnull Config config) {
        ItemStack input = null;
        for (int i : inputSlots) {
            if (blockMenu.getItemInSlot(i) != null) {
                input = blockMenu.getItemInSlot(i);
                break;
            }
        }
        if (input == null) {
            return false;
        }
        int max = Math.min(16, input.getAmount());
        for (int i = 0; i < max ; i++) {
            ItemStack output = this.recipes.get(input);
            if (output == null || !blockMenu.fits(output, outputSlots)) {
                break;
            }
            blockMenu.pushItem(output.clone(), outputSlots);
            input.setAmount(input.getAmount() - 1);
        }
        return true;
    }

    @Override
    public int getCapacity() {
        return ENERGY * 4;
    }

}
