/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.events.AsyncMachineOperationFinishEvent
 *  com.github.drakescraft_labs.slimefun4.implementation.items.electric.reactors.Reactor
 *  com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation
 *  com.github.drakescraft_labs.slimefun4.implementation.operations.FuelOperation
 *  com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer
 *  com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AGenerator
 *  com.github.drakescraft_labs.slimefun4.libraries.dough.common.ChatColors
 *  org.bukkit.Bukkit
 *  org.bukkit.Keyed
 *  org.bukkit.Material
 *  org.bukkit.Tag
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityBreedEvent
 *  org.bukkit.event.world.StructureGrowEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.poma123.globalwarming.listeners;

import com.github.drakescraft_labs.slimefun4.api.events.AsyncMachineOperationFinishEvent;
import com.github.drakescraft_labs.slimefun4.implementation.items.electric.reactors.Reactor;
import com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation;
import com.github.drakescraft_labs.slimefun4.implementation.operations.FuelOperation;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AContainer;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.AGenerator;
import com.github.drakescraft_labs.slimefun4.libraries.dough.common.ChatColors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import me.poma123.globalwarming.GlobalWarmingPlugin;
import me.poma123.globalwarming.TemperatureManager;
import me.poma123.globalwarming.api.PollutionManager;
import me.poma123.globalwarming.api.TemperatureType;
import me.poma123.globalwarming.api.events.AsyncWorldPollutionChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PollutionListener
implements Listener {
    private static final int BROADCAST_COOLDOWN = 60000;
    private final Map<String, Long> lastWorldBroadcasts = new HashMap<String, Long>();
    private final Map<String, Double> tempPollutionValues = new HashMap<String, Double>();

    @EventHandler
    public void onMachineOperationComplete(AsyncMachineOperationFinishEvent e) {
        World world = e.getPosition().getWorld();
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }
        if (e.getProcessor() == null) {
            return;
        }
        String id = null;
        ItemStack[] inputs = new ItemStack[]{};
        if ((e.getProcessor().getOwner() instanceof Reactor || e.getProcessor().getOwner() instanceof AGenerator) && e.getOperation() instanceof FuelOperation) {
            id = e.getProcessor().getOwner().getId();
            FuelOperation operation = (FuelOperation)e.getOperation();
            inputs = new ItemStack[]{operation.getIngredient()};
        } else if (e.getProcessor().getOwner() instanceof AContainer && e.getOperation() instanceof CraftingOperation) {
            id = e.getProcessor().getOwner().getId();
            CraftingOperation operation = (CraftingOperation)e.getOperation();
            inputs = operation.getIngredients();
        }
        if (id != null) {
            this.risePollutionTry(world, id, inputs);
            this.descendPollutionTry(world, id);
        }
    }

    @EventHandler
    public void onAnimalBreed(EntityBreedEvent e) {
        World world = e.getMother().getWorld();
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }
        double pollutionValue = GlobalWarmingPlugin.getRegistry().getAnimalBreedPollution();
        if (pollutionValue > 0.0) {
            PollutionManager.descendPollutionInWorld(world, pollutionValue);
        }
    }

    @EventHandler
    public void onTreeGrowth(StructureGrowEvent e) {
        World world = e.getWorld();
        if (!GlobalWarmingPlugin.getRegistry().isWorldEnabled(world.getName())) {
            return;
        }
        Bukkit.getScheduler().runTaskLater((Plugin)GlobalWarmingPlugin.getInstance(), () -> {
            double pollutionValue;
            Material type = e.getLocation().getBlock().getType();
            if (Tag.LOGS.isTagged(type) && (pollutionValue = GlobalWarmingPlugin.getRegistry().getTreeGrowthAbsorption()) > 0.0) {
                PollutionManager.descendPollutionInWorld(world, pollutionValue);
            }
        }, 2L);
    }

    @EventHandler
    public void onPollutionChange(AsyncWorldPollutionChangeEvent e) {
        Bukkit.getScheduler().runTaskLater((Plugin)GlobalWarmingPlugin.getInstance(), () -> {
            World world = e.getWorld();
            Long lastBroadcast = this.lastWorldBroadcasts.get(world.getName());
            if (lastBroadcast != null && System.currentTimeMillis() - lastBroadcast < 60000L) {
                return;
            }
            this.lastWorldBroadcasts.put(world.getName(), System.currentTimeMillis());
            double amount = TemperatureManager.fixDouble(e.getNewValue() * GlobalWarmingPlugin.getRegistry().getPollutionMultiply());
            if (!this.tempPollutionValues.containsKey(world.getName())) {
                this.tempPollutionValues.put(world.getName(), amount);
            } else if (this.tempPollutionValues.get(world.getName()) == amount) {
                return;
            }
            this.tempPollutionValues.replace(world.getName(), amount);
            this.sendNews(world);
        }, (long)ThreadLocalRandom.current().nextInt(1, 20));
    }

    private void sendNews(World world) {
        TemperatureType messageTempType = TemperatureType.valueOf(GlobalWarmingPlugin.getMessagesConfig().getString("temperature-scale"));
        String difference = GlobalWarmingPlugin.getTemperatureManager().getAirQualityString(world, messageTempType);
        String news = "";
        if (!GlobalWarmingPlugin.getRegistry().getNews().isEmpty()) {
            String base = GlobalWarmingPlugin.getMessagesConfig().getString("messages.breaking-news");
            List<String> newsList = GlobalWarmingPlugin.getRegistry().getNews();
            String random = newsList.get(ThreadLocalRandom.current().nextInt(newsList.size()));
            news = ChatColors.color((String)base.replace("%news%", random));
        }
        for (Player p : world.getPlayers()) {
            if (GlobalWarmingPlugin.getSilenciados() != null && GlobalWarmingPlugin.getSilenciados().estaSilenciado(p)) continue;
            p.sendMessage(ChatColors.color((String)GlobalWarmingPlugin.getMessagesConfig().getString("messages.climate-change").replace("%value%", difference)));
            if (news.length() <= 0) continue;
            p.sendMessage(news);
        }
    }

    private boolean risePollutionTry(World world, String id, ItemStack[] recipeInput) {
        double pollutionValue = this.calculatePollutionValue(id, recipeInput);
        if (pollutionValue > 0.0) {
            PollutionManager.risePollutionInWorld(world, pollutionValue);
            return true;
        }
        return false;
    }

    private boolean descendPollutionTry(World world, String id) {
        double absorptionValue = this.calculateAbsorptionValue(id);
        if (absorptionValue > 0.0) {
            PollutionManager.descendPollutionInWorld(world, absorptionValue);
            return true;
        }
        return false;
    }

    private double calculatePollutionValue(String id, ItemStack[] recipeInput) {
        double pollutionValue = 0.0;
        pollutionValue += PollutionManager.isPollutedMachine(id);
        for (ItemStack item : recipeInput) {
            pollutionValue += PollutionManager.isPollutedItem(item);
        }
        return pollutionValue;
    }

    private double calculateAbsorptionValue(String id) {
        double absorptionValue = 0.0;
        return absorptionValue += PollutionManager.isAbsorbentMachine(id);
    }
}

