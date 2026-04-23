package me.justahuman.moreresearches;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.researches.Research;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft-labs.slimefun4.libraries.dough.common.ChatColors;
import com.github.drakescraft-labs.slimefun4.libraries.dough.items.CustomItemStack;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class Utils {
    private static final Map<SlimefunItem, Research> ORIGINAL_RESEARCHES = new HashMap<>();
    private static final Field RESEARCH_NAME_FIELD;
    static {
        try {
            RESEARCH_NAME_FIELD = Research.class.getDeclaredField("name");
            RESEARCH_NAME_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static YamlConfiguration langFile = null;

    public static void loadResearches() {
        FileConfiguration config = MoreResearches.getInstance().getConfig();
        ConfigurationSection researches = config.getConfigurationSection("researches");
        Map<String, Research> customResearches = new HashMap<>();
        for (Research research : new ArrayList<>(Slimefun.getRegistry().getResearches())) {
            if (!isCustomResearch(research)) {
                continue;
            }

            for (SlimefunItem slimefunItem : new ArrayList<>(research.getAffectedItems())) {
                slimefunItem.setResearch(ORIGINAL_RESEARCHES.get(slimefunItem));
            }

            String researchId = research.getKey().getKey();
            if (researches == null || !researches.contains(researchId)) {
                Slimefun.getRegistry().getResearches().remove(research);
            }
            customResearches.put(researchId, research);
        }

        if (researches != null) {
            for (String researchId : researches.getKeys(false)) {
                Research research = customResearches.get(researchId);
                ConfigurationSection researchConfig = researches.getConfigurationSection(researchId);
                if (research == null && researchConfig != null) {
                    Utils.createResearch(researchId, researchConfig);
                } else if (research != null && researchConfig != null){
                    Utils.updateResearch(research, researchId, researchConfig);
                }
            }
        }
    }

    public static Research createResearch(String researchId, ConfigurationSection researchConfig) {
        MoreResearches plugin = MoreResearches.getInstance();
        int legacyId = researchConfig.getInt("legacy-id", -113132);
        if (legacyId == -113132) {
            getLogger().warning("Invalid research, you need to set a legacy-id, negative numbers are recommended: " + researchId);
            return null;
        }

        String displayName = researchConfig.getString("display-name", "Error: No Display Name Provided");
        int expCost = researchConfig.getInt("exp-cost", 0);
        List<String> itemIds = researchConfig.getStringList("slimefun-items");

        if (itemIds.isEmpty()) {
            getLogger().warning("Invalid research, no items: " + researchId);
            return null;
        }

        SlimefunItem[] items = itemIds.stream()
                .map(SlimefunItem::getById)
                .filter(Objects::nonNull)
                .toArray(SlimefunItem[]::new);

        if (items.length == 0) {
            getLogger().warning("Invalid research, no valid items: " + researchId);
            return null;
        }

        for (SlimefunItem item : items) {
            if (!ORIGINAL_RESEARCHES.containsKey(item) && !isCustomResearch(item.getResearch())) {
                ORIGINAL_RESEARCHES.put(item, item.getResearch());
            }
        }

        Research research = new Research(new NamespacedKey(plugin, researchId), legacyId, displayName, expCost);
        research.addItems(items);
        research.register();

        return research;
    }

    public static void updateResearch(Research research, String researchId, ConfigurationSection researchConfig) {
        List<String> itemIds = researchConfig.getStringList("slimefun-items");
        if (itemIds.isEmpty()) {
            getLogger().warning("Invalid research, no items: " + researchId);
            return;
        }

        SlimefunItem[] items = itemIds.stream()
                .map(SlimefunItem::getById)
                .filter(Objects::nonNull)
                .toArray(SlimefunItem[]::new);

        if (items.length == 0) {
            getLogger().warning("Invalid research, no valid items: " + researchId);
            return;
        }

        for (SlimefunItem item : items) {
            if (!ORIGINAL_RESEARCHES.containsKey(item) && !isCustomResearch(item.getResearch())) {
                ORIGINAL_RESEARCHES.put(item, item.getResearch());
            }
        }

        try {
            RESEARCH_NAME_FIELD.set(research, researchConfig.getString("display-name", "Error: No Display Name Provided"));
        } catch (Exception e) {
            getLogger().severe("Ran into a problem updating " + researchId + "'s name!");
            getLogger().throwing("Utils", "updateResearch", e);
            return;
        }

        research.setCost(researchConfig.getInt("exp-cost", 0));
        research.addItems(items);
    }

    public static List<String> compressIds(List<String> words) {
        int totalWords = words.size();
        int numGroups = (int) Math.ceil(Math.sqrt(totalWords));
        int targetGroupSize = (int) Math.ceil((double) totalWords / numGroups);

        List<String> compressedWords = new ArrayList<>();
        StringBuilder currentGroup = new StringBuilder();

        for (int i = 0; i < totalWords; i++) {
            if (!currentGroup.isEmpty()) {
                currentGroup.append(", ");
            }

            currentGroup.append(words.get(i));
            if ((i + 1) % targetGroupSize == 0 || i == totalWords - 1) {
                compressedWords.add(ChatColor.YELLOW + currentGroup.toString().trim());
                currentGroup = new StringBuilder();
            }
        }

        return compressedWords;
    }

    public static void unchacheLangFile() {
        langFile = null;
    }

    public static void cacheLangFile() {
        if (langFile == null) {
            MoreResearches researches = MoreResearches.getInstance();
            FileConfiguration config = researches.getConfig();
            String language = config.getString("lang", "en_us");
            InputStream lang = researches.getResource("lang/" + language + ".yml");
            if (lang == null) {
                lang = researches.getResource("lang/en_us.yml");
            }

            if (lang == null) {
                return;
            }
            langFile = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(lang, StandardCharsets.UTF_8)));
        }
    }

    public static void send(Player player, String key, Object... args) {
        player.sendMessage(translated(key, args));
    }

    public static String translated(String key, Object... args) {
        cacheLangFile();
        if (langFile == null) {
            return "Error: No Language File Found!";
        }
        return ChatColors.color(langFile.getString(key, "Error: No Translation for \"" + key + "\" Found!").formatted(args));
    }

    public static List<String> translatedList(String key, Object... args) {
        cacheLangFile();
        if (langFile == null) {
            return List.of("Error: No Language File Found!");
        }
        return new ArrayList<>(langFile.getStringList(key).stream().map(s -> ChatColors.color(s.formatted(args))).toList());
    }

    public static ItemStack translatedStack(Material base, String key, Object... args) {
        String name = translated(key + ".name", args);
        List<String> lore = translatedList(key + ".lore", args);
        return new CustomItemStack(base, name, lore);
    }

    public static ItemStack translatedStack(ItemStack base, String key, Object... args) {
        String name = translated(key + ".name", args);
        List<String> lore = translatedList(key + ".lore", args);
        return new CustomItemStack(base, name, lore.toArray(new String[0]));
    }

    public static ItemStack updateStack(ItemStack stack, String key, Object... args) {
        String name = translated(key + ".name", args);
        List<String> lore = translatedList(key + ".lore", args);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public static boolean isCustomResearch(Research research) {
        return research != null && research.getKey().getNamespace().equalsIgnoreCase("moreresearches");
    }

    public static Logger getLogger() {
        return MoreResearches.getInstance().getLogger();
    }
}
