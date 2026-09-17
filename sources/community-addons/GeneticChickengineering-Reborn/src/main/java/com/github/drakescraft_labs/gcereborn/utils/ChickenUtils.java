package com.github.drakescraft_labs.gcereborn.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.guide.SlimefunGuide;
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem;
import com.github.drakescraft_labs.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.utils.ChatUtils;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;
import com.github.drakescraft_labs.gcereborn.core.adapters.AnimalsAdapter;
import com.github.drakescraft_labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft_labs.gcereborn.items.GCEItems;
import com.github.drakescraft_labs.gcereborn.items.chicken.ChickenTypes;
import com.github.drakescraft_labs.gcereborn.items.chicken.ExpandedChickenSpecies;
import com.github.drakescraft_labs.gcereborn.items.chicken.PocketChicken;
import com.github.drakescraft_labs.gcereborn.setup.Groups;
import com.github.drakescraft_labs.gcereborn.setup.RecipeTypes;

import lombok.experimental.UtilityClass;
import net.guizhanss.guizhanlib.minecraft.utils.MinecraftVersionUtil;

@UtilityClass
public final class ChickenUtils {

    public static boolean isPocketChicken(@Nullable ItemStack item) {
        return item != null && !item.getType().isAir() && item.hasItemMeta() && PersistentDataAPI.hasIntArray(item.getItemMeta(), Keys.POCKET_CHICKEN_DNA);
    }

    public static boolean isPocketChicken(@Nullable Chicken chicken) {
        if (chicken == null) {
            return false;
        }
        if (PersistentDataAPI.hasString(chicken, Keys.CHICKEN_DNA)) {
            String dnaStr = PersistentDataAPI.getString(chicken, Keys.CHICKEN_DNA);
            return DNA.isValidSequence(dnaStr);
        }
        return false;
    }

    @Nonnull
    public static JsonObject getChickenJson(boolean isBaby) {
        JsonObject json = new JsonObject();
        json.addProperty("_type", "CHICKEN");
        json.addProperty("_health", 4.0);
        json.addProperty("_absorption", 0.0);
        json.addProperty("_removeWhenFarAway", false);
        json.addProperty("_customName", (String) null);
        json.addProperty("_customNameVisible", false);
        json.addProperty("_ai", true);
        json.addProperty("_silent", false);
        json.addProperty("_glowing", false);
        json.addProperty("_invulnerable", false);
        json.addProperty("_collidable", true);
        json.addProperty("_gravity", true);
        json.addProperty("_fireTicks", 0);
        json.addProperty("baby", isBaby);
        json.addProperty("_age", isBaby ? -24000 : 6000);
        json.addProperty("_ageLock", false);
        json.addProperty("_breedable", false);
        json.addProperty("_loveModeTicks", 0);
        json.add("_attributes", new JsonObject());
        json.add("_effects", new JsonObject());
        json.add("_scoreboardTags", new com.google.gson.JsonArray());
        return json;
    }

    @Nonnull
    public static ItemStack capture(@Nonnull Chicken chicken) {
        GeneticChickengineering.getIntegrationService().captureChicken(chicken);
        JsonObject json = PocketChicken.ADAPTER.saveData(chicken);
        ItemStack item = GCEItems.POCKET_CHICKEN.clone();

        DNA dna;
        if (PersistentDataAPI.hasString(chicken, Keys.CHICKEN_DNA)) {
            String dnaStr = PersistentDataAPI.getString(chicken, Keys.CHICKEN_DNA);
            dna = new DNA(dnaStr);
        } else {
            dna = new DNA();
        }

        if (GeneticChickengineering.getConfigService().isDisplayResources() && json.get("_customNameVisible").getAsBoolean() && dna.isKnown()) {
            String name;
            if (!json.get("_customName").isJsonNull()) {
                name = json.get("_customName").getAsString();
            } else {
                name = "";
            }
            String replace = "(" + ChickenTypes.getDisplayName(dna.getTyping()) + ")";
            name = name.replace(replace, "");
            if (name.isEmpty()) {
                json.addProperty("_customName", (String) null);
                json.addProperty("_customNameVisible", false);
            } else {
                json.addProperty("_customName", name);
            }
        }

        setPocketChicken(item, json, dna);
        return item;
    }

    @Nullable
    public static ItemStack breed(@Nonnull ItemStack c1, @Nonnull ItemStack c2) {
        ItemMeta c1m = c1.getItemMeta();
        ItemMeta c2m = c2.getItemMeta();

        if (PersistentDataAPI.hasIntArray(c1m, Keys.POCKET_CHICKEN_DNA) && PersistentDataAPI.hasIntArray(c2m, Keys.POCKET_CHICKEN_DNA)) {
            DNA c1d = new DNA(PersistentDataAPI.getIntArray(c1m, Keys.POCKET_CHICKEN_DNA));
            DNA c2d = new DNA(PersistentDataAPI.getIntArray(c2m, Keys.POCKET_CHICKEN_DNA));

            DNA babyDNA = new DNA(c1d.split(), c2d.split());
            ItemStack baby = fromDNA(babyDNA, true);
            
            // If both parents are the same expanded species, child inherits it!
            if (PersistentDataAPI.hasString(c1m, Keys.EXPANDED_SPECIES) &&
                PersistentDataAPI.hasString(c2m, Keys.EXPANDED_SPECIES)) {
                String s1 = PersistentDataAPI.getString(c1m, Keys.EXPANDED_SPECIES);
                String s2 = PersistentDataAPI.getString(c2m, Keys.EXPANDED_SPECIES);
                if (s1 != null && s1.equals(s2)) {
                    ExpandedChickenSpecies exp = ExpandedChickenSpecies.getById(s1);
                    if (exp != null) {
                        setExpandedPocketChicken(baby, getChickenJson(true), babyDNA, exp);
                    }
                }
            }
            return baby;
        }
        return null;
    }

    public static void createProductDisplay(int typing) {
        ItemStack fake = GCEItems.POCKET_CHICKEN.clone();
        DNA dna = new DNA(typing);
        String productRawName = ChickenTypes.getName(typing);
        setPocketChicken(fake, null, dna);

        String itemIDType = productRawName.replace(" ", "_").toUpperCase();
        SlimefunItemStack displayItem = new SlimefunItemStack("GCE_" + itemIDType + "_CHICKEN_ICON", ChickenTypes.getProduct(typing));
        ItemMeta meta = displayItem.getItemMeta();
        PersistentDataAPI.setIntArray(meta, Keys.POCKET_CHICKEN_DNA, dna.getState());
        displayItem.setItemMeta(meta);

        new PocketChicken(
            Groups.DICTIONARY,
            displayItem,
            RecipeTypes.FROM_CHICKEN,
            new ItemStack[] {
                null, null, null,
                null, fake, null,
                null, null, null
            }
        ).register(GeneticChickengineering.getInstance());
    }

    public static void createExpandedProductDisplay(@Nonnull ExpandedChickenSpecies species) {
        DNA dna = new DNA(0); // pure genotype
        ItemStack fake = GCEItems.POCKET_CHICKEN.clone();
        JsonObject fakeJson = getChickenJson(false);
        setExpandedPocketChicken(fake, fakeJson, dna, species);

        SlimefunItemStack displayItem = new SlimefunItemStack("GCE_" + species.getId() + "_ICON", species.getProduct());
        ItemMeta meta = displayItem.getItemMeta();
        meta.setDisplayName(species.getCategory().getTagEs() + " " + ChatColor.WHITE + species.getDisplayNameEs());
        meta.setLore(List.of(
            ChatColor.GRAY + "Clasificacion: " + species.getCategory().getTagEs(),
            ChatColor.GRAY + "Nivel (Tier): " + ChatColor.LIGHT_PURPLE + "Tier " + species.getTier(),
            ChatColor.GRAY + "Catalizador: " + ChatColor.YELLOW + species.getCatalyst().getType().name(),
            "",
            ChatColor.GOLD + "\u21E8 Obtenible en el Empalmador Mutagenico"
        ));
        displayItem.setItemMeta(meta);

        new PocketChicken(
            Groups.DICTIONARY,
            displayItem,
            RecipeTypes.MUTAGENIC_SPLICING,
            new ItemStack[] {
                GCEItems.MUTAGENIC_SERUM, species.getCatalyst(), null,
                null, fake, null,
                null, null, null
            }
        ).register(GeneticChickengineering.getInstance());
    }

    @Nonnull
    public static ItemStack fromDNA(@Nonnull DNA dna, boolean isBaby) {
        JsonObject json = getChickenJson(isBaby);
        ItemStack item = GCEItems.POCKET_CHICKEN.clone();
        setPocketChicken(item, json, dna);
        return item;
    }

    @Nonnull
    public static DNA getDNA(@Nonnull ItemStack chicken) {
        ItemMeta meta = chicken.getItemMeta();
        return new DNA(PersistentDataAPI.getIntArray(meta, Keys.POCKET_CHICKEN_DNA));
    }

    public static int getDNAStrength(@Nonnull ItemStack chicken) {
        DNA dna = getDNA(chicken);
        int[] state = dna.getState();
        int str = 6 - dna.getTier();
        for (int i = 0; i < 6; i++) {
            if (state[i] == 1) {
                str--;
            }
        }
        return str;
    }

    @Nonnull
    private static List<String> getLore(@Nullable JsonObject json, @Nonnull DNA dna, @Nullable ExpandedChickenSpecies species) {
        List<String> lore = new LinkedList<>();
        var localization = GeneticChickengineering.getLocalization();
        if (json != null) {
            lore = PocketChicken.ADAPTER.getLore(json);
            if (GeneticChickengineering.getConfigService().isPainEnabled()) {
                double health = json.get("_health").getAsDouble();
                String status;
                if (health > 2.0) {
                    status = localization.getString("lores.chicken.status.healthy");
                } else if (health <= 0.50) {
                    status = localization.getString("lores.chicken.status.exhausted");
                } else {
                    status = localization.getString("lores.chicken.status.fatigued");
                }
                lore.add(localization.getString("lores.chicken.status.line", status));
            }
        }
        if (species != null) {
            lore.add(ChatColor.DARK_GRAY + "--------------------");
            lore.add(species.getCategory().getTagEs() + " " + ChatColor.YELLOW + species.getDisplayNameEs());
            lore.add(ChatColor.GRAY + "Rango: " + ChatColor.LIGHT_PURPLE + "Tier " + species.getTier());
            lore.add(ChatColor.GRAY + "Producto: " + ChatColor.WHITE + species.getProduct().getType().name());
            if (species.isRadioactive()) {
                lore.add(ChatColor.RED + "\u2622 \u00A1Emite radiacion activa! Usar proteccion.");
            }
            lore.add(ChatColor.DARK_GRAY + "--------------------");
        } else if (dna.isKnown()) {
            lore.add(localization.getString("lores.chicken.dna", dna));
            lore.add(localization.getString("lores.chicken.type", ChickenTypes.getDisplayName(dna.getTyping())));
        }
        return lore;
    }

    public static void setPocketChicken(@Nonnull ItemStack item, @Nullable JsonObject json, @Nonnull DNA dna) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataAPI.setIntArray(meta, Keys.POCKET_CHICKEN_DNA, dna.getState());
        ExpandedChickenSpecies species = null;
        if (PersistentDataAPI.hasString(meta, Keys.EXPANDED_SPECIES)) {
            species = ExpandedChickenSpecies.getById(PersistentDataAPI.getString(meta, Keys.EXPANDED_SPECIES));
        }
        if (json != null) {
            PersistentDataAPI.set(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER, json);
        }
        meta.setLore(getLore(json, dna, species));
        item.setItemMeta(meta);
    }

    public static void setExpandedPocketChicken(@Nonnull ItemStack item, @Nullable JsonObject json, @Nonnull DNA dna, @Nonnull ExpandedChickenSpecies species) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataAPI.setIntArray(meta, Keys.POCKET_CHICKEN_DNA, dna.getState());
        PersistentDataAPI.setString(meta, Keys.EXPANDED_SPECIES, species.getId());
        if (json != null) {
            PersistentDataAPI.set(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER, json);
        }
        meta.setDisplayName(species.getCategory().getTagEs() + " " + ChatColor.WHITE + species.getDisplayNameEs());
        meta.setLore(getLore(json, dna, species));
        item.setItemMeta(meta);
    }

    public double getHealth(@Nullable ItemStack chicken) {
        if (chicken == null || chicken.getType().isAir()) {
            return 0d;
        }
        ItemMeta meta = chicken.getItemMeta();
        JsonObject json = PersistentDataAPI.get(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
        if (json != null && json.has("_health")) {
            return json.get("_health").getAsDouble();
        }
        return 4d;
    }

    public boolean survivesPain(@Nullable ItemStack chicken) {
        return getHealth(chicken) > 0.25;
    }

    public boolean harm(@Nullable ItemStack chicken) {
        return harm(chicken, 0.25);
    }

    public boolean harm(@Nullable ItemStack chicken, double damage) {
        if (chicken == null || chicken.getType().isAir()) {
            return false;
        }
        ItemMeta meta = chicken.getItemMeta();
        JsonObject json = PersistentDataAPI.get(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
        if (json != null && json.has("_health")) {
            double oldHealth = json.get("_health").getAsDouble();
            double newHealth = Math.max(0d, Math.min(oldHealth - damage, 4d));
            json.addProperty("_health", newHealth);
            PersistentDataAPI.set(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER, json);
            if (GeneticChickengineering.getConfigService().isLiveLoreEnabled()) {
                setPocketChicken(chicken, json, getDNA(chicken));
            } else {
                chicken.setItemMeta(meta);
            }
            return true;
        }
        return false;
    }

    public boolean heal(@Nullable ItemStack chicken, double amount) {
        if (amount > 0) {
            amount *= -1;
        }
        return harm(chicken, amount);
    }

    public void possiblyHarm(@Nullable ItemStack chicken) {
        if (ThreadLocalRandom.current().nextInt(100) < GeneticChickengineering.getConfigService().getPainChance()) {
            harm(chicken);
        }
    }

    @Nonnull
    public ItemStack getResource(@Nonnull ItemStack chicken) {
        PocketChickenData data = PocketChickenData.fromItem(chicken);
        if (data != null) {
            return data.getResource();
        }
        DNA dna = getDNA(chicken);
        return ChickenTypes.getProduct(dna.getTyping());
    }

    public int getResourceTier(@Nonnull ItemStack chicken) {
        PocketChickenData data = PocketChickenData.fromItem(chicken);
        if (data != null) {
            return data.getResourceTier();
        }
        DNA dna = getDNA(chicken);
        return dna.getTier();
    }

    public boolean isFood(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        if (item.isSimilar(GCEItems.RAPID_GROWTH_FEED) || item.isSimilar(GCEItems.FORTIFIED_VITA_FEED)) {
            return true;
        }
        if (item.hasItemMeta()) {
            return false;
        }
        Material type = item.getType();
        if (type == Material.WHEAT_SEEDS || type == Material.BEETROOT_SEEDS || type == Material.MELON_SEEDS || type == Material.PUMPKIN_SEEDS) {
            return true;
        }
        if (MinecraftVersionUtil.isAtLeast(19, 4) && type == Material.TORCHFLOWER_SEEDS) {
            return true;
        }
        if (MinecraftVersionUtil.isAtLeast(20) && type == Material.PITCHER_POD) {
            return true;
        }
        return false;
    }

    public boolean isAdult(@Nonnull ItemStack chicken) {
        JsonObject json = PersistentDataAPI.get(chicken.getItemMeta(), Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
        if (json != null && json.has("baby")) {
            return !json.get("baby").getAsBoolean();
        }
        return true;
    }

    public boolean isLearned(@Nonnull ItemStack chicken) {
        DNA dna = getDNA(chicken);
        return dna.isKnown();
    }

    @Nonnull
    public ItemStack learnDNA(@Nonnull ItemStack chicken) {
        ItemStack item = chicken.clone();
        ItemMeta meta = item.getItemMeta();

        if (PersistentDataAPI.hasIntArray(meta, Keys.POCKET_CHICKEN_DNA)) {
            DNA dna = new DNA(PersistentDataAPI.getIntArray(meta, Keys.POCKET_CHICKEN_DNA));
            dna.learn();
            JsonObject json = PersistentDataAPI.get(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
            setPocketChicken(item, json, dna);
        }

        return item;
    }
}
