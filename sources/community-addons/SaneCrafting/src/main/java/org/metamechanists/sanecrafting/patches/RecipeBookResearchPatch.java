package org.metamechanists.sanecrafting.patches;

import com.github.drakescraft-labs.slimefun4.api.events.ResearchUnlockEvent;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.implementation.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.sanecrafting.SaneCrafting;
import org.metamechanists.sanecrafting.Util;

import java.util.UUID;


public final class RecipeBookResearchPatch implements Listener {
    private RecipeBookResearchPatch() {}

    public static void apply() {
        Bukkit.getServer().getPluginManager().registerEvents(new RecipeBookResearchPatch(), SaneCrafting.getInstance());
        SaneCrafting.getInstance().getLogger().info("Applied RecipeBookResearch patch");
    }

    @EventHandler
    public static void onJoin(@NotNull PlayerJoinEvent e) {
        if (Slimefun.getRegistry().isResearchingEnabled()) {
            return;
        }

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            e.getPlayer().discoverRecipe(new NamespacedKey(SaneCrafting.getInstance(), Util.generateRecipeId(item.getItem())));
        }
    }

    @EventHandler
    public static void onResearch(@NotNull ResearchUnlockEvent e) {
        for (SlimefunItem item : e.getResearch().getAffectedItems()) {
            e.getPlayer().discoverRecipe(new NamespacedKey(SaneCrafting.getInstance(), Util.generateRecipeId(item.getItem())));
        }
    }

    @EventHandler
    public static void onCraft(@NotNull CraftItemEvent e) {
        SlimefunItem result = SlimefunItem.getByItem(e.getInventory().getResult());
        if (result == null) {
            return;
        }

        if (!result.hasResearch()) {
            return;
        }

        UUID uuid = e.getWhoClicked().getUniqueId();
        if (Slimefun.getRegistry().getPlayerProfiles().get(uuid).hasUnlocked(result.getResearch())) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public static void onPrepareCraft(@NotNull PrepareItemCraftEvent e) {
        SlimefunItem result = SlimefunItem.getByItem(e.getInventory().getResult());
        if (result == null) {
            return;
        }

        if (!result.hasResearch()) {
            return;
        }

        UUID uuid = e.getView().getPlayer().getUniqueId();
        if (Slimefun.getRegistry().getPlayerProfiles().get(uuid).hasUnlocked(result.getResearch())) {
            return;
        }

        e.getInventory().setResult(null);
    }
}
