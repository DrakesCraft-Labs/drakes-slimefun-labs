package me.schntgaispock.wildernether.slimefun.items;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import dev.drake.infinitylib.machines.MenuBlock;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import me.schntgaispock.wildernether.slimefun.WildernetherRecipes;
import me.schntgaispock.wildernether.slimefun.recipes.RecipeCollection;
import me.schntgaispock.wildernether.slimefun.recipes.StoveRecipe;
import me.schntgaispock.wildernether.util.GeneralUtil;
import me.schntgaispock.wildernether.util.Theme;

/**
 * See the <a href="https://github.com/SchnTgaiSpock/Wildernether/wiki/Blackstone-Stove">Blackstone Stove wiki</a> for more details
 * 
 * @author SchnTgaiSpock
 */
public class BlackstoneStove extends MenuBlock {

    public enum Mode {
        Oven, Frying, Pot, Brewing
    }

    public static final int[] GUI_BACKGROUND_SLOTS = {
        0,  1,  2,  3,  4,  5,  6,  7,  8,
        9,      11,             15,     17,
        18,     20,             24,     26,
        27,     29,             33,     35,
        36, 37, 38, 39, 40, 41, 42, 43, 44,
        45, 46, 47, 48,     50, 51, 52, 53
    };
    public static final int[] GUI_BOWL_SLOTS = { 10, 28 };
    public static final int[] GUI_RECIPE_SLOTS = { 30, 31, 32 };
    public static final int[] GUI_OUTPUT_SLOTS = { 16, 34 };
    public static final int[] GUI_COOK_SLOTS = { 49 };

    public static final int BOWL_SLOT = 19;
    public static final int[] RECIPE_SLOTS = { 12, 13, 14, 21, 22, 23 };
    public static final int COOK_SLOT = GUI_COOK_SLOTS[0];
    public static final int OUTPUT_SLOT = 25;
    public static final String COOK_CONFIRM_NAME = "Click to cook!";

    public static final ItemStack BOWL_BORDER_ITEM = new CustomItemStack(Material.BLUE_STAINED_GLASS_PANE, "&9Container");
    public static final ItemStack RECIPE_BORDER_ITEM = new CustomItemStack(Material.CAMPFIRE, Theme.CUISINE.getColor() + "Recipe");

    @ParametersAreNonnullByDefault
    public BlackstoneStove(ItemGroup ig, SlimefunItemStack is, RecipeType rt, ItemStack[] rc) {
        super(ig, is, rt, rc);
    }

    public static ItemStack getCraftConfirmItem(@Nonnull Mode mode) {
        return new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&a" + COOK_CONFIRM_NAME, "&7Mode: &f" + mode.toString());
    }

    public static Mode getMode(Location l) {
        if (l.getBlockY() == 320) {
            return Mode.Oven;
        }

        Block blockOnTop = l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ());

        return switch (blockOnTop.getType()) {
            case CAULDRON, LAVA_CAULDRON, WATER_CAULDRON, POWDER_SNOW_CAULDRON -> Mode.Pot;
            case HEAVY_WEIGHTED_PRESSURE_PLATE -> Mode.Frying;
            case LIGHT_WEIGHTED_PRESSURE_PLATE -> Mode.Brewing;
            default -> Mode.Oven;
        };
    }

    public static Mode getMode(Block b) {
        return getMode(b.getLocation());
    }

    @Override
    protected void setup(BlockMenuPreset preset) {
        preset.drawBackground(GUI_BACKGROUND_SLOTS);
        preset.drawBackground(BOWL_BORDER_ITEM, GUI_BOWL_SLOTS);
        preset.drawBackground(RECIPE_BORDER_ITEM, GUI_RECIPE_SLOTS);
        preset.drawBackground(OUTPUT_BORDER, GUI_OUTPUT_SLOTS);
        preset.drawBackground(getCraftConfirmItem(Mode.Oven), GUI_COOK_SLOTS);
    }

    @Override
    protected int[] getInputSlots() {
        return new int[]{ 12, 13, 14, 21, 22, 23, 19 };
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{ 25 };
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    protected void onNewInstance(BlockMenu menu, Block b) {
        menu.addMenuOpeningHandler((Player player) -> {
            player.getOpenInventory().getTopInventory().setItem(49, getCraftConfirmItem(getMode(b)));
        });

        
        menu.addMenuClickHandler(COOK_SLOT, (Player player, int clickedSlot, ItemStack clickedItem, me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction action) -> {

            Inventory inv = player.getOpenInventory().getTopInventory();

            // Parse recipe, check if in existing recipes
            ItemStack[] currentRecipe = new ItemStack[9];
            for (int i = 0; i < 6; i++) {
                int slot = BlackstoneStove.RECIPE_SLOTS[i];
                currentRecipe[i] = inv.getItem(slot);
            }
            currentRecipe[7] = inv.getItem(BlackstoneStove.BOWL_SLOT);

            RecipeCollection<StoveRecipe> recipeCollection;
            switch (getMode(b)) {
                case Frying:
                    recipeCollection = WildernetherRecipes.RecipeCollections.BLACKSTONE_STOVE_FRYING;
                    break;
                    
                case Pot:
                    recipeCollection = WildernetherRecipes.RecipeCollections.BLACKSTONE_STOVE_POT;
                    break;
                    
                case Brewing:
                    recipeCollection = WildernetherRecipes.RecipeCollections.BLACKSTONE_STOVE_BREWING;
                    break;

                default:
                    recipeCollection = WildernetherRecipes.RecipeCollections.BLACKSTONE_STOVE_OVEN;
                    break;
            }

            ItemStack recipeOutput = recipeCollection.getOrNull(currentRecipe);
            if (recipeOutput == null) {
                player.sendMessage(Theme.CUISINE.getColor() + "Invalid " + getMode(b).toString().toLowerCase() + " recipe!");
                return false;
            }

            // Put output in output slot if there is space
            int outputSlot = BlackstoneStove.OUTPUT_SLOT;
            ItemStack currentlyInOutput = inv.getItem(outputSlot);

            
            if (currentlyInOutput == null) {
                // Success
                inv.setItem(outputSlot, recipeOutput.clone());
            } else if (recipeOutput.isSimilar(currentlyInOutput)) {
                // Failure
                if (currentlyInOutput.getMaxStackSize() == currentlyInOutput.getAmount()) {
                    player.sendMessage(Theme.CUISINE.getColor() + "Output is full!");
                    return false;
                }
                // Success
                currentlyInOutput.setAmount(currentlyInOutput.getAmount() + 1);
            
            // Failure
            } else {
                player.sendMessage(Theme.CUISINE.getColor() + "Output is occupied!");
                return false;
            }

            // Reduce input items by 1, gives buckets and bowls back
            for (int i = 0; i < 8; i++) {
                ItemStack item = currentRecipe[i];
                if (item != null) {
                    item.setAmount(item.getAmount() - 1);
                    ItemStack returnItem = (i != 7) ? GeneralUtil.returnItemAfterUsing(item) : null;

                    Map<Integer, ItemStack> leftOvers = new HashMap<Integer, ItemStack>();
                    if (returnItem != null &&
                        !(leftOvers = player.getInventory().addItem(returnItem)).isEmpty() &&
                        !(leftOvers = inv.addItem(leftOvers.get(0))).isEmpty()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), leftOvers.get(0));
                    }
                }
            }

            return false;
        });

        super.onNewInstance(menu, b);
    }
}
