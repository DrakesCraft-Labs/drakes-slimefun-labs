/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.MinecraftVersion
 *  com.github.drakescraft_labs.slimefun4.api.exceptions.BiomeMapException
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
 *  com.github.drakescraft_labs.slimefun4.api.researches.Research
 *  com.github.drakescraft_labs.slimefun4.implementation.Slimefun
 *  com.github.drakescraft_labs.slimefun4.libraries.dough.config.Config
 *  com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeDataConverter
 *  com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeMap
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.World
 *  org.bukkit.block.Biome
 *  org.bukkit.plugin.Plugin
 */
package me.poma123.globalwarming;

import com.github.drakescraft_labs.slimefun4.api.MinecraftVersion;
import com.github.drakescraft_labs.slimefun4.api.exceptions.BiomeMapException;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.researches.Research;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.config.Config;
import com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeDataConverter;
import com.github.drakescraft_labs.slimefun4.utils.biomes.BiomeMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.api.WorldFilterType;
import me.poma123.globalwarming.api.biomes.BiomeTemperature;
import me.poma123.globalwarming.api.biomes.BiomeTemperatureDataConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.Plugin;

public class Registry {
    private final List<String> news = new ArrayList<String>();
    private BiomeMap<BiomeTemperature> biomeMap;
    private final Set<String> enabledWorlds = new HashSet<String>();
    private final Map<String, Config> worldConfigs = new HashMap<String, Config>();
    private final Map<Material, Double> pollutedVanillaItems = new EnumMap<Material, Double>(Material.class);
    private final Map<String, Double> pollutedSlimefunItems = new HashMap<String, Double>();
    private final Map<String, Double> pollutedSlimefunMachines = new HashMap<String, Double>();
    private final Map<String, Double> absorbentSlimefunMachines = new HashMap<String, Double>();
    private final Set<String> worlds = new HashSet<String>();
    private WorldFilterType worldFilterType;
    private double pollutionMultiply;
    private double stormTemperatureDrop;
    private double treeGrowthAbsorption;
    private double animalBreedPollution;
    private Research researchNeededForPlayerMechanics = null;

    public void load(Config cfg, Config messages) {
        List oldDisabledWorlds;
        try {
            this.biomeMap = this.loadBiomeMap(false);
        }
        catch (BiomeMapException | FileNotFoundException x) {
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, x, () -> "No se pudieron cargar los mapas de bioma de /plugins/GlobalWarming/biome-maps/; se usan los de por defecto");
        }
        if (this.biomeMap == null) {
            try {
                this.biomeMap = this.loadBiomeMap(true);
            }
            catch (BiomeMapException | FileNotFoundException x) {
                GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, x, () -> "No se pudieron aplicar los mapas de bioma por defecto; reinstala GlobalWarming.");
                GlobalWarmingPlugin.getInstance().getServer().getPluginManager().disablePlugin((Plugin)GlobalWarmingPlugin.getInstance());
            }
        }
        ArrayList<String> missingBiomes = new ArrayList<String>();
        for (Biome biome : Biome.values()) {
            if (this.biomeMap.containsKey(biome)) continue;
            missingBiomes.add(biome.toString());
        }
        if (!missingBiomes.isEmpty()) {
            String path = this.biomeMap.getKey().getKey().replace("globalwarming_biomemap_", "");
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Mapa de biomas ({0}): estos biomas no tienen temperatura asignada: {1}; se usan los valores por defecto (temp=15, max-temp-drop-at-night=0).", new Object[]{path, String.join((CharSequence)", ", missingBiomes)});
        }
        if (!(oldDisabledWorlds = cfg.getStringList("disabled-worlds")).isEmpty()) {
            cfg.setValue("worlds", (Object)oldDisabledWorlds);
            cfg.setValue("disabled-worlds", null);
            cfg.setValue("world-filter-type", (Object)"blacklist");
            cfg.save();
        }
        try {
            this.worldFilterType = WorldFilterType.valueOf(((String)cfg.getOrSetDefault("world-filter-type", (Object)"blacklist")).toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException ex) {
            this.worldFilterType = WorldFilterType.BLACKLIST;
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "\"{0}\" no es un tipo de filtro de mundo v\u00e1lido; se usa el de por defecto (blacklist)", new Object[]{cfg.getString("world-filter-type")});
        }
        this.worlds.addAll(cfg.getStringList("worlds"));
        for (World w : Bukkit.getWorlds()) {
            this.registerWorld(w, w.getName());
        }
        Bukkit.getScheduler().runTaskLater((Plugin)GlobalWarmingPlugin.getInstance(), () -> {
            double value;
            for (String id : cfg.getKeys("pollution.production.machine-recipe-input-items")) {
                value = cfg.getDouble("pollution.production.machine-recipe-input-items." + id);
                if (value <= 0.0) {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "No se pudo cargar el valor de contaminaci\u00f3n \"{1}\" un objeto contaminante no v\u00e1lido \"{0}\"", new Object[]{id, value});
                    continue;
                }
                if (Material.getMaterial((String)id) != null) {
                    this.pollutedVanillaItems.put(Material.getMaterial((String)id), value);
                    continue;
                }
                if (SlimefunItem.getById((String)id) != null) {
                    this.pollutedSlimefunItems.put(id, value);
                    continue;
                }
                GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "No se pudo cargar el valor de contaminaci\u00f3n \"{1}\" un objeto contaminante que no existe \"{0}\"", new Object[]{id, value});
            }
            for (String id : cfg.getKeys("pollution.production.machines")) {
                value = cfg.getDouble("pollution.production.machines." + id);
                if (value <= 0.0) {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "No se pudo cargar el valor de contaminaci\u00f3n \"{1}\" una m\u00e1quina contaminante no v\u00e1lida \"{0}\"", new Object[]{id, value});
                    continue;
                }
                if (SlimefunItem.getById((String)id) != null) {
                    this.pollutedSlimefunMachines.put(id, value);
                    continue;
                }
                GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "No se pudo cargar el valor de contaminaci\u00f3n \"{1}\" una m\u00e1quina contaminante que no existe \"{0}\"", new Object[]{id, value});
            }
            for (String id : cfg.getKeys("pollution.absorption.machines")) {
                value = cfg.getDouble("pollution.absorption.machines." + id);
                if (value <= 0.0) {
                    GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "No se pudo cargar el valor de absorci\u00f3n \"{1}\" una m\u00e1quina absorbedora no v\u00e1lida \"{0}\"", new Object[]{id, value});
                    continue;
                }
                if (SlimefunItem.getById((String)id) != null) {
                    this.absorbentSlimefunMachines.put(id, value);
                    continue;
                }
                GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "No se pudo cargar el valor de absorci\u00f3n \"{1}\" una m\u00e1quina absorbedora que no existe \"{0}\"", new Object[]{id, value});
            }
        }, 100L);
        this.news.addAll(messages.getStringList("messages.news"));
        this.pollutionMultiply = (Double)cfg.getOrSetDefault("temperature-options.pollution-multiply", (Object)0.002);
        this.stormTemperatureDrop = ((Integer)cfg.getOrSetDefault("temperature-options.temperature-drop-during-storms", (Object)8)).intValue();
        this.treeGrowthAbsorption = (Double)cfg.getOrSetDefault("pollution.absorption.tree-growth", (Object)0.01);
        this.animalBreedPollution = (Double)cfg.getOrSetDefault("pollution.production.animal-breed", (Object)0.007);
        String researchKey = cfg.getString("needed-research-for-player-mechanics");
        Optional tempResearch = Research.getResearch((NamespacedKey)new NamespacedKey((Plugin)Slimefun.instance(), researchKey));
        if (tempResearch.isPresent() && ((Research)tempResearch.get()).isEnabled()) {
            this.researchNeededForPlayerMechanics = (Research)tempResearch.get();
        } else {
            GlobalWarmingPlugin.getInstance().getLogger().log(Level.WARNING, "Could not load research \"{0}\"", new Object[]{researchKey});
        }
    }

    public BiomeMap<BiomeTemperature> loadBiomeMap(boolean internalResource) throws BiomeMapException, FileNotFoundException {
        String path = Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_18) ? "post-1.18.json" : "pre-1.18.json";
        BufferedReader reader = internalResource ? new BufferedReader(new InputStreamReader(((Object)((Object)GlobalWarmingPlugin.getInstance())).getClass().getResourceAsStream("/biome-maps/" + path), StandardCharsets.UTF_8)) : new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(String.valueOf(GlobalWarmingPlugin.getInstance().getDataFolder()) + "/biome-maps/" + path), StandardCharsets.UTF_8));
        return BiomeMap.fromJson((NamespacedKey)new NamespacedKey((Plugin)GlobalWarmingPlugin.getInstance(), "globalwarming_biomemap_" + path), (String)reader.lines().collect(Collectors.joining("")), (BiomeDataConverter)new BiomeTemperatureDataConverter(), (boolean)true);
    }

    public BiomeMap<BiomeTemperature> getBiomeMap() {
        return this.biomeMap;
    }

    public boolean isWorldEnabled(@Nonnull String worldName) {
        if (Bukkit.getWorld((String)worldName) == null) {
            this.enabledWorlds.remove(worldName);
            return false;
        }
        return this.enabledWorlds.contains(worldName);
    }

    public void registerWorld(World w, String worldName) {
        if (this.worldFilterType == WorldFilterType.BLACKLIST) {
            if (!this.worlds.contains(worldName)) {
                this.enabledWorlds.add(worldName);
                this.getWorldConfig(w);
            }
        } else if (this.worlds.contains(worldName)) {
            this.enabledWorlds.add(worldName);
            this.getWorldConfig(w);
        }
    }

    public void unregisterWorld(String worldName) {
        this.enabledWorlds.remove(worldName);
    }

    public Set<String> getEnabledWorlds() {
        return this.enabledWorlds;
    }

    @Nullable
    public Config getWorldConfig(@Nullable World world) {
        if (world != null && this.isWorldEnabled(world.getName())) {
            if (!this.worldConfigs.containsKey(world.getName())) {
                this.worldConfigs.put(world.getName(), this.getNewWorldConfig(world));
            }
            return this.worldConfigs.get(world.getName());
        }
        return null;
    }

    public Config getNewWorldConfig(@Nonnull World world) {
        Config config = new Config((Plugin)GlobalWarmingPlugin.getInstance(), "worlds/" + world.getName() + ".yml");
        if (config.getValue("data.pollution") == null) {
            config.setValue("data.pollution", (Object)0.0);
            config.save();
        }
        return config;
    }

    public Map<Material, Double> getPollutedVanillaItems() {
        return this.pollutedVanillaItems;
    }

    public Map<String, Double> getPollutedSlimefunItems() {
        return this.pollutedSlimefunItems;
    }

    public Map<String, Double> getPollutedSlimefunMachines() {
        return this.pollutedSlimefunMachines;
    }

    public Map<String, Double> getAbsorbentSlimefunMachines() {
        return this.absorbentSlimefunMachines;
    }

    public List<String> getNews() {
        return this.news;
    }

    public double getPollutionMultiply() {
        return this.pollutionMultiply;
    }

    public double getStormTemperatureDrop() {
        return this.stormTemperatureDrop;
    }

    public double getTreeGrowthAbsorption() {
        return this.treeGrowthAbsorption;
    }

    public double getAnimalBreedPollution() {
        return this.animalBreedPollution;
    }

    public Research getResearchNeededForPlayerMechanics() {
        return this.researchNeededForPlayerMechanics;
    }
}

