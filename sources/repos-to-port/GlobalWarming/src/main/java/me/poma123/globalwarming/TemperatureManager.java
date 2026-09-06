/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeMap
 *  javax.annotation.Nonnull
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Biome
 *  org.bukkit.plugin.Plugin
 */
package me.poma123.globalwarming;

import com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeMap;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.api.PollutionManager;
import me.poma123.globalwarming.api.Temperature;
import me.poma123.globalwarming.api.TemperatureType;
import me.poma123.globalwarming.api.biomes.BiomeTemperature;
import me.poma123.globalwarming.eventos.RegistroClimatizadores;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.Plugin;

public class TemperatureManager {
    public static final String HOT = "\u2600";
    public static final String COLD = "\u2744";
    private final Map<String, Map<Biome, Double>> worldTemperatureChangeFactorMap = new HashMap<String, Map<Biome, Double>>();

    protected void runCalculationTask(long delay, long interval) {
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)GlobalWarmingPlugin.getInstance(), () -> {
            for (String w : GlobalWarmingPlugin.getRegistry().getEnabledWorlds()) {
                World world;
                if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(w) || (world = Bukkit.getWorld((String)w)) == null || world.getPlayers().isEmpty()) continue;
                HashMap<Biome, Double> map = new HashMap<Biome, Double>();
                boolean isNormalEnvironment = world.getEnvironment() == World.Environment.NORMAL;
                BiomeMap<BiomeTemperature> biomeMap = GlobalWarmingPlugin.getRegistry().getBiomeMap();
                for (Biome biome : Biome.values()) {
                    BiomeTemperature biomeTemperature = biomeMap.getOrDefault(biome, new BiomeTemperature(15.0, 0.0));
                    Temperature defaultTemperature = new Temperature(biomeTemperature.getTemperature());
                    Temperature newTemp = isNormalEnvironment ? this.addTemperatureChangeFactors(world, biome, defaultTemperature) : defaultTemperature;
                    map.put(biome, newTemp.getCelsiusValue());
                }
                this.worldTemperatureChangeFactorMap.put(w, map);
            }
        }, delay, interval);
    }

    public Temperature getTemperatureAtLocation(@Nonnull Location loc) {
        World world = loc.getWorld();
        Biome biome = loc.getBlock().getBiome();
        Double climatizado = RegistroClimatizadores.objetivoEn(loc);
        if (climatizado != null) {
            return new Temperature(climatizado);
        }
        Map<Biome, Double> map = this.worldTemperatureChangeFactorMap.get(world.getName());
        if (map == null) {
            return new Temperature(0.0);
        }
        return new Temperature(map.get(biome));
    }

    public String getTemperatureString(@Nonnull Location loc, @Nonnull TemperatureType tempType) {
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(loc.getWorld().getName())) {
            return "&cEse mundo no est\u00e1 disponible";
        }
        Temperature temp = this.getTemperatureAtLocation(loc);
        if (temp == null) {
            return "&7Midiendo...";
        }
        double celsiusValue = temp.getCelsiusValue();
        String prefix = celsiusValue <= 18.0 ? "&b\u2744" : (celsiusValue <= 24.0 ? "&a\u2600" : (celsiusValue <= 28.0 ? "&e\u2600" : (celsiusValue <= 36.0 ? "&6\u2600" : (celsiusValue <= 45.0 ? "&c\u2600" : "&4\u2600"))));
        temp.setTemperatureType(tempType);
        return prefix + " " + TemperatureManager.fixDouble(temp.getConvertedValue()) + " &7" + tempType.getSuffix();
    }

    public String getAirQualityString(@Nonnull World world, @Nonnull TemperatureType tempType) {
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName()) || world.getEnvironment() != World.Environment.NORMAL) {
            return "&cEse mundo no est\u00e1 disponible";
        }
        Temperature temp = new Temperature(15.0);
        double celsiusDifference = PollutionManager.getPollutionInWorld(world) * GlobalWarmingPlugin.getRegistry().getPollutionMultiply();
        double currentValue = temp.getCelsiusValue() + celsiusDifference;
        double defaultValue = temp.getCelsiusValue();
        Object prefix = celsiusDifference <= -1.5 || celsiusDifference >= 1.5 ? "&c" : (celsiusDifference <= -0.5 || celsiusDifference >= 0.5 ? "&e" : (celsiusDifference < 0.0 || celsiusDifference > 0.0 ? "&a" : "&f"));
        double difference = celsiusDifference;
        if (tempType != TemperatureType.CELSIUS) {
            difference = TemperatureManager.getDifference(currentValue, defaultValue, tempType);
        }
        prefix = (String)prefix + (difference > 0.0 ? "+" : "");
        return (String)prefix + TemperatureManager.fixDouble(difference) + " &7" + tempType.getSuffix();
    }

    public Temperature addTemperatureChangeFactors(@Nonnull World world, @Nonnull Biome biome, @Nonnull Temperature temperature) {
        BiomeMap<BiomeTemperature> biomeMap = GlobalWarmingPlugin.getRegistry().getBiomeMap();
        double celsiusValue = temperature.getCelsiusValue();
        double nightDrop = biomeMap.getOrDefault(biome, new BiomeTemperature(15.0, 0.0)).getMaxTemperatureDropAtNight();
        if (world.getEnvironment() == World.Environment.NORMAL) {
            if (!TemperatureManager.isDaytime(world)) {
                double nightTime = (float)world.getTime() - 12300.0f;
                if (nightTime > 5775.0) {
                    nightTime = 5775.0 - (nightTime - 5775.0);
                }
                double dropPercent = nightTime / 5775.0;
                celsiusValue -= nightDrop * dropPercent;
            } else if (world.hasStorm()) {
                celsiusValue -= GlobalWarmingPlugin.getRegistry().getStormTemperatureDrop();
            }
        }
        celsiusValue += PollutionManager.getPollutionInWorld(world) * GlobalWarmingPlugin.getRegistry().getPollutionMultiply();
        if (GlobalWarmingPlugin.getGestorEventos() != null) {
            celsiusValue += GlobalWarmingPlugin.getGestorEventos().getDesviacion(world);
        }
        return new Temperature(celsiusValue);
    }

    public static double getDifference(@Nonnull double currentValue, @Nonnull double defaultValue, @Nonnull TemperatureType type) {
        double convertedCurrent = new Temperature(currentValue, type).getConvertedValue();
        double convertedDefault = new Temperature(defaultValue, type).getConvertedValue();
        double difference = Math.abs(convertedCurrent - convertedDefault);
        if (convertedCurrent < convertedDefault) {
            difference *= -1.0;
        }
        return difference;
    }

    public static boolean isDaytime(@Nonnull World world) {
        long time = world.getTime();
        return time < 12300L || time > 23850L;
    }

    public static double fixDouble(double amount, int digits) {
        if (digits == 0) {
            return (int)amount;
        }
        StringBuilder format = new StringBuilder("##");
        for (int i = 0; i < digits; ++i) {
            if (i == 0) {
                format.append(".");
            }
            format.append("#");
        }
        return Double.valueOf(new DecimalFormat(format.toString()).format(amount).replace(",", "."));
    }

    public static double fixDouble(double amount) {
        return TemperatureManager.fixDouble(amount, 2);
    }
}

