package com.github.drakescraft_labs.gcereborn.core.adapters;

import java.util.List;

import com.google.gson.JsonObject;

import org.bukkit.entity.Animals;

import com.github.drakescraft_labs.gcereborn.GeneticChickengineering;

/**
 * This class is a wholesale copy of TheBusyBiscuit's MobCapturer.
 */
public class AnimalsAdapter<T extends Animals> implements MobAdapter<T> {

    private final Class<T> entityClass;

    public AnimalsAdapter(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<String> getLore(JsonObject json) {
        List<String> lore = MobAdapter.super.getLore(json);

        boolean isBaby = json.get("baby").getAsBoolean();

        if (isBaby) {
            lore.add(GeneticChickengineering.getLocalization().getString("lores.chicken.baby"));
        }

        return lore;
    }

    @Override
    public JsonObject saveData(T entity) {
        JsonObject json = MobAdapter.super.saveData(entity);

        json.addProperty("baby", !entity.isAdult());
        json.addProperty("_age", entity.getAge());
        json.addProperty("_ageLock", entity.getAgeLock());
        json.addProperty("_breedable", entity.canBreed());
        json.addProperty("_loveModeTicks", entity.getLoveModeTicks());

        return json;
    }

    @Override
    public void apply(T entity, JsonObject json) {
        MobAdapter.super.apply(entity, json);

        /*
         * Los Pocket Chickens obtenidos directamente del catálogo de pruebas no
         * contienen un espécimen serializado. MobAdapter ya deja la entidad en
         * valores vanilla cuando json es nulo; no debemos volver a accederlo aquí.
         */
        if (json == null) {
            return;
        }

        if (json.has("_age")) {
            entity.setAge(json.get("_age").getAsInt());
        }
        if (json.has("_loveModeTicks")) {
            entity.setLoveModeTicks(json.get("_loveModeTicks").getAsInt());
        }
        if (json.has("_ageLock")) {
            entity.setAgeLock(json.get("_ageLock").getAsBoolean());
        }
        if (json.has("_breedable")) {
            entity.setBreed(json.get("_breedable").getAsBoolean());
        }
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

}
