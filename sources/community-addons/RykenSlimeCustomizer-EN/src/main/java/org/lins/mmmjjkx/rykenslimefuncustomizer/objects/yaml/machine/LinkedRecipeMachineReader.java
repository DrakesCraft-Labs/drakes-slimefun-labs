package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.yaml.machine;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.libraries.dough.collections.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.ProjectAddon;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.CustomMenu;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.LinkedOutput;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.machine.CustomLinkedRecipeMachine;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.machine.CustomLinkedMachineRecipe;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.yaml.YamlReader;
import org.lins.mmmjjkx.rykenslimefuncustomizer.utils.CommonUtils;
import org.lins.mmmjjkx.rykenslimefuncustomizer.utils.ExceptionHandler;

/*
 * RSC_EXAMPLE_LINKED_RECIPE_MACHINE:
 *   item_group: RSC_EXAMPLE_NORMAL_GROUP
 *   item:
 *     material: EMERALD_BLOCK
 *   input: [19, 20]
 *   output: [24, 25]
 *   saveAmount: 1
 *   recipes:
 *     your_recipe_name:
 *       seconds: 10
 *       input:
 *         1:
 *           slot: 19
 *           material: IRON_INGOT
 *           amount: 64
 *         2:
 *           slot: 20
 *           material: IRON_INGOT
 *           amount: 64
 *       output:
 *         1:
 *           material: GOLD_INGOT
 *
 */
public class LinkedRecipeMachineReader extends YamlReader<CustomLinkedRecipeMachine> {
    public LinkedRecipeMachineReader(YamlConfiguration config, ProjectAddon addon) {
        super(config, addon);
    }

    @Override
    public CustomLinkedRecipeMachine readEach(String s) {
        ConfigurationSection section = configuration.getConfigurationSection(s);
        if (section == null) return null;
        String id = addon.getId(s, section.getString("id_alias"));

        ExceptionHandler.HandleResult result = ExceptionHandler.handleIdConflict(id);

        if (result == ExceptionHandler.HandleResult.FAILED) return null;

        String igId = section.getString("item_group");
        Pair<ExceptionHandler.HandleResult, ItemGroup> group = ExceptionHandler.handleItemGroupGet(addon, igId);
        if (group.getFirstValue() == ExceptionHandler.HandleResult.FAILED) return null;

        SlimefunItemStack slimefunItemStack = getPreloadItem(id);
        if (slimefunItemStack == null) return null;

        ItemStack[] recipe = CommonUtils.readRecipe(section.getConfigurationSection("recipe"), addon);
        String recipeType = section.getString("recipe_type", "NULL");

        Pair<ExceptionHandler.HandleResult, RecipeType> rt = ExceptionHandler.getRecipeType(
                "Failed to load advanced recipe machine " + s + " in addon " + addon.getAddonId() + ": Invalid recipe type " + recipeType + "!", recipeType);

        if (rt.getFirstValue() == ExceptionHandler.HandleResult.FAILED) return null;

        CustomMenu menu = CommonUtils.getIf(addon.getMenus(), m -> m.getID().equalsIgnoreCase(id));
        if (menu == null) {
            ExceptionHandler.handleWarning("Menu " + id + " not found, using default menu");
        }

        List<Integer> input = section.getIntegerList("input");
        List<Integer> output = section.getIntegerList("output");

        if (input.isEmpty()) {
            ExceptionHandler.handleError("Failed to load advanced recipe machine " + s + " in addon " + addon.getAddonId() + ": Input slots are empty");
            return null;
        }

        if (output.isEmpty()) {
            ExceptionHandler.handleError("Failed to load advanced recipe machine " + s + " in addon " + addon.getAddonId() + ": Output slots are empty");
            return null;
        }

        ConfigurationSection recipes = section.getConfigurationSection("recipes");

        int capacity = section.getInt("capacity");

        if (capacity < 0) {
            ExceptionHandler.handleError("Failed to load advanced recipe machine " + s + " in addon " + addon.getAddonId() + ": Energy capacity cannot be negative");
            return null;
        }

        int energy = section.getInt("energyPerCraft");

        if (energy <= 0) {
            ExceptionHandler.handleError(
                    "Failed to load advanced recipe machine " + s + " in addon " + addon.getAddonId() + ": Energy consumption per craft must be greater than 0");
            return null;
        }

        int speed = section.getInt("speed");

        if (speed <= 0) {
            ExceptionHandler.handleError("Failed to load advanced recipe machine " + s + " in addon " + addon.getAddonId() + ": Crafting speed must be greater than 0");
            return null;
        }

        boolean hideAllRecipes = section.getBoolean("hideAllRecipes", false);
        int saveAmount = section.getInt("saveAmount", 0);

        List<CustomLinkedMachineRecipe> mr = readRecipes(s, input.size(), output.size(), recipes, addon);

        return new CustomLinkedRecipeMachine(
                group.getSecondValue(),
                slimefunItemStack,
                rt.getSecondValue(),
                recipe,
                input.stream().mapToInt(x -> x).toArray(),
                output.stream().mapToInt(x -> x).toArray(),
                mr,
                energy,
                capacity,
                menu,
                speed,
                hideAllRecipes,
                saveAmount);
    }

    @Override
    public List<SlimefunItemStack> preloadItems(String s) {
        ConfigurationSection section = configuration.getConfigurationSection(s);
        if (section == null) return null;

        ConfigurationSection item = section.getConfigurationSection("item");
        ItemStack stack = CommonUtils.readItem(item, false, addon);

        if (stack == null) {
            ExceptionHandler.handleError("Failed to load linked recipe machine " + s + " in addon " + addon.getAddonId() +
                    ": Item is null or has invalid format");
            return null;
        }

        return List.of(new SlimefunItemStack(addon.getId(s, section.getString("id_alias")), stack));
    }

    private List<CustomLinkedMachineRecipe> readRecipes(
            String s, int inputSize, int outputSize, ConfigurationSection section, ProjectAddon addon) {
        List<CustomLinkedMachineRecipe> list = new ArrayList<>();
        if (section == null) {
            return list;
        }

        for (String key : section.getKeys(false)) {
            ConfigurationSection recipes = section.getConfigurationSection(key);
            if (recipes == null) continue;
            int seconds = recipes.getInt("seconds");
            if (seconds < 0) {
                ExceptionHandler.handleError(
                        "Failed to load recipe " + key + " for linked recipe machine " + s + " in addon " + addon.getAddonId() + ": Processing interval cannot be negative or unset");
                continue;
            }
            ConfigurationSection inputs = recipes.getConfigurationSection("input");
            if (inputs == null) {
                ExceptionHandler.handleError(
                        "Failed to load recipe " + key + " for linked recipe machine " + s + " in addon " + addon.getAddonId() + ": No input items specified");
                continue;
            }

            ConfigurationSection outputs = recipes.getConfigurationSection("output");
            if (outputs == null) {
                ExceptionHandler.handleError(
                        "Failed to load recipe " + key + " for linked recipe machine " + s + " in addon " + addon.getAddonId() + ": No output items specified");
                continue;
            }

            List<ItemStack> freeOutput = new ArrayList<>();
            List<Integer> freeChances = new ArrayList<>();

            Map<Integer, ItemStack> linkedOutput = new HashMap<>();
            Map<Integer, Integer> linkedChances = new HashMap<>();

            for (int i = 0; i < outputSize; i++) {
                ConfigurationSection section1 = outputs.getConfigurationSection(String.valueOf(i + 1));
                var item = CommonUtils.readItem(section1, true, addon);
                if (item != null) {
                    int chance = section1.getInt("chance", 100);

                    if (chance < 1) {
                        ExceptionHandler.handleError("Failed to load recipe " + key + " for linked recipe machine " + s + " in addon " + addon.getAddonId()
                                + ": Drop chance cannot be less than 1 (auto-corrected to 1)");
                        chance = 1;
                    }

                    int slot = section1.getInt("slot", -1);
                    if (slot == -1) {
                        freeOutput.add(item);
                        freeChances.add(chance);
                    } else {
                        linkedOutput.put(slot, item);
                        linkedChances.put(slot, chance);
                    }
                }
            }

            boolean chooseOne = recipes.getBoolean("chooseOne", false);
            boolean forDisplay = recipes.getBoolean("forDisplay", false);
            boolean hide = recipes.getBoolean("hide", false);

            Map<Integer, ItemStack> finalInput = new HashMap<>();
            for (int i = 0; i < inputSize; i++) {
                ConfigurationSection section1 = inputs.getConfigurationSection(String.valueOf(i + 1));
                if (section1 == null) {
                    continue;
                }

                ItemStack itemStack = CommonUtils.readItem(section1, true, addon);
                if (itemStack == null) {
                    continue;
                }

                int slot = section1.getInt("slot", -1);
                if (slot == -1) {
                    ExceptionHandler.handleError(
                            "Failed to load recipe " + key + " for linked recipe machine " + s + " in addon " + addon.getAddonId() +
                                    ": Input slot must be specified (cannot be empty)");
                    continue;
                }

                if (slot < 0 || slot > 53) {
                    ExceptionHandler.handleError(
                            "Failed to load recipe " + key + " for linked recipe machine " + s + " in addon " + addon.getAddonId() +
                                    ": Input slot " + slot + " is out of valid range (0-53)");
                    continue;
                }

                finalInput.put(slot, itemStack);
            }

            int[] array = new int[freeChances.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = i;
            }
            list.add(new CustomLinkedMachineRecipe(
                    seconds,
                    finalInput,
                    new LinkedOutput(freeOutput.toArray(new ItemStack[0]), linkedOutput, array, linkedChances),
                    chooseOne,
                    forDisplay,
                    hide));
        }
        return list;
    }
}
