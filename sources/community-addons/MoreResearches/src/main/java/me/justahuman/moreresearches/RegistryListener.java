package me.justahuman.moreresearches;

import com.github.drakescraft-labs.slimefun4.api.events.SlimefunItemRegistryFinalizedEvent;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.researches.Research;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RegistryListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegistryFinalized(SlimefunItemRegistryFinalizedEvent event) {
        FileConfiguration config = MoreResearches.getInstance().getConfig();
        ConfigurationSection researches = config.getConfigurationSection("researches");
        if (researches != null) {
            int researchCount = 0;
            Set<String> uniqueItemIds = new HashSet<>();
            Utils.getLogger().info(Utils.translated("startup.load"));
            for (String researchId : researches.getKeys(false)) {
                ConfigurationSection researchConfig = researches.getConfigurationSection(researchId);
                if (researchConfig == null) {
                    Utils.getLogger().warning(Utils.translated("warnings.research.no-body", researchId));
                    continue;
                }

                Research research = Utils.createResearch(researchId, researchConfig);
                if (research != null) {
                    researchCount++;
                    uniqueItemIds.addAll(research.getAffectedItems().stream().map(SlimefunItem::getId).collect(Collectors.toSet()));
                }
            }
            Utils.getLogger().info(Utils.translated("startup.loaded", researchCount, uniqueItemIds.size()));
        }
    }
}
