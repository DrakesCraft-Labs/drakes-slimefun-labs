/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeDataConverter
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package me.poma123.globalwarming.api.biomes;

import com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeDataConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.poma123.globalwarming.api.biomes.BiomeTemperature;

public class BiomeTemperatureDataConverter
implements BiomeDataConverter<BiomeTemperature> {
    public BiomeTemperature convert(JsonElement jsonElement) {
        JsonObject obj = jsonElement.getAsJsonObject();
        double temperature = obj.get("temperature").getAsDouble();
        double maxTempDrop = obj.get("max-temp-drop-at-night").getAsDouble();
        return new BiomeTemperature(temperature, maxTempDrop);
    }
}

