package com.github.drakescraft-labs.gcereborn.utils;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.drake.dough.data.persistent.PersistentDataAPI;

import com.github.drakescraft-labs.gcereborn.core.genetics.DNA;
import com.github.drakescraft-labs.gcereborn.items.chicken.PocketChicken;
import com.github.drakescraft-labs.gcereborn.items.chicken.ChickenTypes;

/**
 * Lightweight view of a pocket chicken's stored data to avoid repeated PDC/ItemMeta
 * reads when inspecting the same ItemStack multiple times in hot paths.
 */
public final class PocketChickenData {

    private final ItemStack item;
    private final JsonObject adapter;
    private final DNA dna;
    private final double health;
    private final boolean adult;

    private PocketChickenData(ItemStack item, JsonObject adapter, DNA dna, double health, boolean adult) {
        this.item = item;
        this.adapter = adapter;
        this.dna = dna;
        this.health = health;
        this.adult = adult;
    }

    @Nullable
    public static PocketChickenData fromItem(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (!PersistentDataAPI.hasIntArray(meta, Keys.POCKET_CHICKEN_DNA)) {
            return null;
        }

        JsonObject adapter = PersistentDataAPI.get(meta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
        int[] state = PersistentDataAPI.getIntArray(meta, Keys.POCKET_CHICKEN_DNA);
        DNA dna = state != null ? new DNA(state) : new DNA();

        double health = 0d;
        boolean adult = false;
        if (adapter != null) {
            if (adapter.has("_health")) {
                health = adapter.get("_health").getAsDouble();
            }
            if (adapter.has("baby")) {
                adult = !adapter.get("baby").getAsBoolean();
            }
        }

        return new PocketChickenData(item, adapter, dna, health, adult);
    }

    public DNA getDNA() {
        return dna;
    }

    public double getHealth() {
        return health;
    }

    public boolean isAdult() {
        return adult;
    }

    public int getResourceTier() {
        return dna.getTier();
    }

    public ItemStack getResource() {
        return ChickenTypes.getProduct(dna.getTyping());
    }

    public int getDNAStrength() {
        int[] state = dna.getState();
        int str = 6 - dna.getTier();
        for (int i = 0; i < 6; i++) {
            if (state[i] == 1) {
                str--;
            }
        }
        return str;
    }

    public JsonObject getAdapter() {
        return adapter;
    }

    public boolean isKnown() {
        return dna.isKnown();
    }

    public int[] getState() {
        return dna.getState();
    }

}
