package dev.drake.disc.setup;

import dev.drake.disc.SlimefunDisc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DiscConfig {

    private static final Map<String, DiscRecipe> recipes = new HashMap<>();
    private static FileConfiguration config;

    private DiscConfig() {
    }

    public static void load() {
        SlimefunDisc plugin = SlimefunDisc.getInstance();
        File file = new File(plugin.getDataFolder(), "discs.yml");

        if (!file.exists()) {
            plugin.saveResource("discs.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection discs = config.getConfigurationSection("discs");
        if (discs == null) return;

        for (String key : discs.getKeys(false)) {
            ConfigurationSection sec = discs.getConfigurationSection(key);
            if (sec == null) continue;

            String nameOverride = sec.getString("name", null);
            String recipeType = sec.getString("recipe-type", "ENHANCED_CRAFTING_TABLE");
            boolean uncraftable = sec.getBoolean("uncraftable", false);
            List<String> recipeList = sec.getStringList("recipe");
            String[] recipe = recipeList.toArray(new String[0]);
            String lootTable = sec.getString("loot-table", null);
            String songFile = sec.getString("file", null);

            if (songFile == null) {
                plugin.getLogger().warning("Disc '" + key + "' is missing the 'file' field — skipping.");
                continue;
            }

            recipes.put(key.toLowerCase(), new DiscRecipe(nameOverride, recipeType, uncraftable, recipe, lootTable, songFile));
        }

        plugin.getLogger().info("Loaded " + recipes.size() + " disc recipe configurations.");
    }

    @Nonnull
    public static DiscRecipe getRecipe(String songKey) {
        return recipes.getOrDefault(songKey.toLowerCase(), DiscRecipe.DEFAULT);
    }

    @Nonnull
    public static Map<String, DiscRecipe> getAllRecipes() {
        return Map.copyOf(recipes);
    }

    public static final class DiscRecipe {
        public static final DiscRecipe DEFAULT = new DiscRecipe(null,
                "ENHANCED_CRAFTING_TABLE", false, new String[9], null, null);

        private final String nameOverride;
        private final String recipeType;
        private final boolean uncraftable;
        private final String[] recipe;
        private final String lootTable;
        private final String file;

        public DiscRecipe(String nameOverride, String recipeType, boolean uncraftable, String[] recipe, String lootTable, String file) {
            this.nameOverride = nameOverride;
            this.recipeType = recipeType;
            this.uncraftable = uncraftable;
            this.lootTable = lootTable;
            this.file = file;
            this.recipe = new String[9];
            if (recipe != null) {
                for (int i = 0; i < Math.min(recipe.length, 9); i++) {
                    this.recipe[i] = recipe[i];
                }
            }
        }

        public String getNameOverride() {
            return nameOverride;
        }

        public String getRecipeType() {
            return recipeType;
        }

        public boolean isUncraftable() {
            return uncraftable;
        }

        public String[] getRecipe() {
            return recipe;
        }

        public String getLootTable() {
            return lootTable;
        }

        public String getFile() {
            return file;
        }
    }
}