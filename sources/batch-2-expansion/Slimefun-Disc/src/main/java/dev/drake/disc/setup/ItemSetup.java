package dev.drake.disc.setup;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;

import dev.drake.disc.NBSParser.NBSong;
import dev.drake.disc.SlimefunDisc;
import dev.drake.disc.items.DiscMold;
import dev.drake.disc.items.MusicDisc;
import dev.drake.disc.setup.DiscConfig.DiscRecipe;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;

public final class ItemSetup {

    public static final ItemSetup INSTANCE = new ItemSetup();

    public static final ItemGroup DISC_GROUP = new ItemGroup(
        new NamespacedKey(SlimefunDisc.getInstance(), "slimefun_disc"),
        new CustomItemStack(Material.JUKEBOX, "&6Slimefun Discs")
    );

    public static SlimefunItemStack DISC_MOLD;

    private boolean initialised;

    private ItemSetup() {}

    public void init() {
        if (initialised) return;
        initialised = true;

        DiscConfig.load();

        registerMold();

        Map<String, DiscRecipe> recipes = DiscConfig.getAllRecipes();

        if (recipes.isEmpty()) {
            SlimefunDisc.getInstance().getLogger().warning("No disc entries found in discs.yml — no discs will be registered.");
            return;
        }

        for (Map.Entry<String, DiscRecipe> entry : recipes.entrySet()) {
            String key = entry.getKey();
            DiscRecipe recipe = entry.getValue();

            NBSong song = SongLoader.loadSong(key, recipe.getFile());
            if (song != null) {
                registerDisc(key, song);
            }
        }
    }

    private void registerMold() {
        DISC_MOLD = new SlimefunItemStack(
            "DISC_MOLD",
            Material.MUSIC_DISC_CAT,
            "&6Disc Mold",
            "",
            "&7Used to craft custom music discs",
            "&7Place in the center of the recipe",
            "",
            "&e\u2728 Required for all disc recipes"
        );

        DiscMold mold = new DiscMold(
            DISC_GROUP,
            DISC_MOLD,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.MUSIC_DISC_CAT), new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)
            }
        );

        mold.register(SlimefunDisc.getInstance());
    }

    private void registerDisc(String key, NBSong song) {
        String discId = "SF_DISC_" + key.toUpperCase().replaceAll("[^A-Z0-9]", "_");

        DiscRecipe recipeConfig = DiscConfig.getRecipe(key);
        String displayName = recipeConfig.getNameOverride();
        if (displayName == null || displayName.isEmpty()) {
            String rawName = song.getName();
            if (rawName == null || rawName.isEmpty()) {
                rawName = formatKeyAsName(key);
            }
            displayName = "&6" + rawName;
        }

        SlimefunItemStack stack = new SlimefunItemStack(
            discId,
            Material.MUSIC_DISC_CAT,
            displayName,
            "",
            "&7Duration: &f" + String.format("%.1f", song.getDurationSeconds()) + "s",
            "",
            "&ePlace in a Jukebox to play!"
        );

        RecipeType recipeType;
        ItemStack[] recipe;

        if (recipeConfig.isUncraftable()) {
            recipeType = RecipeType.NULL;
            recipe = null;
        } else {
            recipeType = parseRecipeType(recipeConfig.getRecipeType());
            recipe = buildRecipe(recipeConfig.getRecipe(), discId);
        }

        MusicDisc disc = new MusicDisc(DISC_GROUP, stack, recipeType, recipe, key);
        disc.register(SlimefunDisc.getInstance());
    }

    private String formatKeyAsName(String key) {
        String name = key.replace("_", " ").replace("-", " ").trim();
        StringBuilder result = new StringBuilder();
        boolean nextUpper = true;
        for (char c : name.toCharArray()) {
            if (c == ' ') {
                result.append(' ');
                nextUpper = true;
            } else {
                result.append(nextUpper ? Character.toUpperCase(c) : c);
                nextUpper = false;
            }
        }
        return result.toString();
    }

    private ItemStack[] buildRecipe(String[] slots, String discId) {
        ItemStack[] recipe = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            String entry = (i < slots.length) ? slots[i] : null;
            if (entry == null || entry.isEmpty() || entry.equalsIgnoreCase("none")) {
                recipe[i] = null;
                continue;
            }
            if (entry.equalsIgnoreCase("DISC_MOLD")) {
                if (DISC_MOLD != null) {
                    recipe[i] = DISC_MOLD.clone();
                }
                continue;
            }
            Material material = Material.getMaterial(entry.toUpperCase());
            if (material != null) {
                recipe[i] = new ItemStack(material);
                continue;
            }
            SlimefunItem sfItem = SlimefunItem.getById(entry.toUpperCase());
            if (sfItem != null) {
                recipe[i] = sfItem.getItem().clone();
                continue;
            }
            recipe[i] = null;
        }
        return recipe;
    }

    private RecipeType parseRecipeType(String name) {
        try {
            return (RecipeType) RecipeType.class.getField(name.toUpperCase()).get(null);
        } catch (Exception e) {
            return RecipeType.ENHANCED_CRAFTING_TABLE;
        }
    }
}