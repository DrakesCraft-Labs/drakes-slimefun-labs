package com.github.drakescraft_labs.gcereborn.items.chicken;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;

import lombok.Getter;

@Getter
public enum ExpandedChickenSpecies {

    // === TIER 7: ALEACIONES INDUSTRIALES SLIMEFUN ===
    REINFORCED_ALLOY("REINFORCED_ALLOY", "Gallina de Aleacion Reforzada", "Reinforced Alloy Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.REINFORCED_ALLOY_INGOT.clone(), () -> SlimefunItems.REINFORCED_ALLOY_INGOT.clone(), false),
    
    CARBONADO("CARBONADO", "Gallina de Carbonado", "Carbonado Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.CARBONADO.clone(), () -> SlimefunItems.CARBONADO.clone(), false),
        
    BLISTERING("BLISTERING", "Gallina Ardiente", "Blistering Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.BLISTERING_INGOT_3.clone(), () -> SlimefunItems.BLISTERING_INGOT_3.clone(), false),
        
    DAMASCUS_STEEL("DAMASCUS_STEEL", "Gallina de Acero Damasco", "Damascus Steel Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.DAMASCUS_STEEL_INGOT.clone(), () -> SlimefunItems.DAMASCUS_STEEL_INGOT.clone(), false),
        
    REDSTONE_ALLOY("REDSTONE_ALLOY", "Gallina de Aleacion Redstone", "Redstone Alloy Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.REDSTONE_ALLOY.clone(), () -> SlimefunItems.REDSTONE_ALLOY.clone(), false),
        
    HARDENED_METAL("HARDENED_METAL", "Gallina de Metal Endurecido", "Hardened Metal Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.HARDENED_METAL_INGOT.clone(), () -> SlimefunItems.HARDENED_METAL_INGOT.clone(), false),
        
    CORINTHIAN_BRONZE("CORINTHIAN_BRONZE", "Gallina de Bronce Corintio", "Corinthian Bronze Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.CORINTHIAN_BRONZE_INGOT.clone(), () -> SlimefunItems.CORINTHIAN_BRONZE_INGOT.clone(), false),
        
    GOLD_24K("GOLD_24K", "Gallina de Oro 24 Quilates", "24K Gold Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.GOLD_24K.clone(), () -> SlimefunItems.GOLD_24K.clone(), false),
        
    DURALUMIN("DURALUMIN", "Gallina de Duraluminio", "Duralumin Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.DURALUMIN_INGOT.clone(), () -> SlimefunItems.DURALUMIN_INGOT.clone(), false),
        
    BRONZE("BRONZE", "Gallina de Bronce", "Bronze Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.BRONZE_INGOT.clone(), () -> SlimefunItems.BRONZE_INGOT.clone(), false),
        
    SYNTHETIC_DIAMOND("SYNTHETIC_DIAMOND", "Gallina de Diamante Sintetico", "Synthetic Diamond Chicken", 7,
        SpeciesCategory.ALLOY, () -> SlimefunItems.SYNTHETIC_DIAMOND.clone(), () -> SlimefunItems.SYNTHETIC_DIAMOND.clone(), false),

    // === TIER 8: CEPA RADIACTIVA & NUCLEAR ===
    URANIUM("URANIUM", "Gallina de Uranio", "Uranium Chicken", 8,
        SpeciesCategory.RADIOACTIVE, () -> SlimefunItems.URANIUM.clone(), () -> SlimefunItems.URANIUM.clone(), true),
        
    NEPTUNIUM("NEPTUNIUM", "Gallina de Neptunio", "Neptunium Chicken", 8,
        SpeciesCategory.RADIOACTIVE, () -> SlimefunItems.NEPTUNIUM.clone(), () -> SlimefunItems.NEPTUNIUM.clone(), true),
        
    PLUTONIUM("PLUTONIUM", "Gallina de Plutonio", "Plutonium Chicken", 8,
        SpeciesCategory.RADIOACTIVE, () -> SlimefunItems.PLUTONIUM.clone(), () -> SlimefunItems.PLUTONIUM.clone(), true),
        
    BOOSTED_URANIUM("BOOSTED_URANIUM", "Gallina de Uranio Enriquecido", "Boosted Uranium Chicken", 8,
        SpeciesCategory.RADIOACTIVE, () -> SlimefunItems.BOOSTED_URANIUM.clone(), () -> SlimefunItems.BOOSTED_URANIUM.clone(), true),

    // === TIER 9: CEPA COSMICA & MISTICA ===
    NETHER_STAR("NETHER_STAR", "Gallina del Nether Star", "Nether Star Chicken", 9,
        SpeciesCategory.MYSTIC, () -> new ItemStack(Material.NETHER_STAR), () -> new ItemStack(Material.NETHER_STAR), false),
        
    DRAGON_BREATH("DRAGON_BREATH", "Gallina del Aliento de Dragon", "Dragon Breath Chicken", 9,
        SpeciesCategory.MYSTIC, () -> new ItemStack(Material.DRAGON_BREATH), () -> new ItemStack(Material.DRAGON_BREATH), false),
        
    ECHO_SHARD("ECHO_SHARD", "Gallina del Eco Profundo", "Echo Shard Chicken", 9,
        SpeciesCategory.MYSTIC, () -> new ItemStack(Material.ECHO_SHARD), () -> new ItemStack(Material.ECHO_SHARD), false),
        
    ANCIENT_DEBRIS("ANCIENT_DEBRIS", "Gallina de Escombros Ancestrales", "Ancient Debris Chicken", 9,
        SpeciesCategory.MYSTIC, () -> new ItemStack(Material.ANCIENT_DEBRIS), () -> new ItemStack(Material.ANCIENT_DEBRIS), false),
        
    SHULKER("SHULKER", "Gallina de Caparazon de Shulker", "Shulker Chicken", 9,
        SpeciesCategory.MYSTIC, () -> SlimefunItems.SYNTHETIC_SHULKER_SHELL.clone(), () -> new ItemStack(Material.SHULKER_SHELL), false),
        
    MAGIC_LUMP("MAGIC_LUMP", "Gallina de Terron Magico", "Magic Lump Chicken", 9,
        SpeciesCategory.MYSTIC, () -> SlimefunItems.MAGIC_LUMP_3.clone(), () -> SlimefunItems.MAGIC_LUMP_3.clone(), false);

    public enum SpeciesCategory {
        ALLOY("&6[Aleacion Slimefun]", "&6[Slimefun Alloy]"),
        RADIOACTIVE("&a[Cepa Radiactiva \u2622]", "&a[Radioactive Strain \u2622]"),
        MYSTIC("&d[Cosmica & Mistica \u2728]", "&d[Cosmic & Mystic \u2728]");

        @Getter
        private final String tagEs;
        @Getter
        private final String tagEn;

        SpeciesCategory(String tagEs, String tagEn) {
            this.tagEs = tagEs;
            this.tagEn = tagEn;
        }
    }

    private final String id;
    private final String displayNameEs;
    private final String displayNameEn;
    private final int tier;
    private final SpeciesCategory category;
    private final Supplier<ItemStack> productSupplier;
    private final Supplier<ItemStack> catalystSupplier;
    private final boolean radioactive;

    private static final Map<String, ExpandedChickenSpecies> BY_ID;

    static {
        Map<String, ExpandedChickenSpecies> map = new LinkedHashMap<>();
        for (ExpandedChickenSpecies s : values()) {
            map.put(s.id.toUpperCase(), s);
        }
        BY_ID = Collections.unmodifiableMap(map);
    }

    ExpandedChickenSpecies(String id, String displayNameEs, String displayNameEn, int tier,
                          SpeciesCategory category, Supplier<ItemStack> productSupplier,
                          Supplier<ItemStack> catalystSupplier, boolean radioactive) {
        this.id = id;
        this.displayNameEs = displayNameEs;
        this.displayNameEn = displayNameEn;
        this.tier = tier;
        this.category = category;
        this.productSupplier = productSupplier;
        this.catalystSupplier = catalystSupplier;
        this.radioactive = radioactive;
    }

    @Nullable
    public static ExpandedChickenSpecies getById(@Nullable String id) {
        if (id == null) {
            return null;
        }
        return BY_ID.get(id.trim().toUpperCase());
    }

    @Nonnull
    public ItemStack getProduct() {
        return productSupplier.get();
    }

    @Nonnull
    public ItemStack getCatalyst() {
        return catalystSupplier.get();
    }

    @Nullable
    public static ExpandedChickenSpecies matchCatalyst(@Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        for (ExpandedChickenSpecies s : values()) {
            ItemStack cat = s.getCatalyst();
            if (cat.isSimilar(item)) {
                return s;
            }
        }
        return null;
    }
}
