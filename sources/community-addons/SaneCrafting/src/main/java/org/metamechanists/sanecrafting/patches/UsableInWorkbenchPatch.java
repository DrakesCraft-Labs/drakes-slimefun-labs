package org.metamechanists.sanecrafting.patches;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.sanecrafting.SaneCrafting;

import java.util.HashSet;
import java.util.Set;


public final class UsableInWorkbenchPatch implements Listener {
    private static final Set<String> usableInWorkbench = new HashSet<>();

    private UsableInWorkbenchPatch() {}

    public static void apply() {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (item.isUseableInWorkbench()) {
                usableInWorkbench.add(item.getId());
            }
            item.setUseableInWorkbench(true);
        }

        Bukkit.getServer().getPluginManager().registerEvents(new UsableInWorkbenchPatch(), SaneCrafting.getInstance());
        SaneCrafting.getInstance().getLogger().info("Applied UsableInWorkbench patch");
    }

    @SuppressWarnings("Convert2streamapi")
    @EventHandler
    public static void onCraft(@NotNull CraftItemEvent e) {
        // If we are crafting a slimefun item, allow the craft
        if (SlimefunItem.getByItem(e.getInventory().getResult()) != null) {
            return;
        }

        // Otherwise go through with standard checks to make sure we're not trying to craft a vanilla item with SF ingredients
        for (ItemStack item : e.getInventory().getContents()) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null && !usableInWorkbench.contains(sfItem.getId())) {
                e.setResult(Result.DENY);
                break;
            }
        }
    }

    @SuppressWarnings("Convert2streamapi")
    @EventHandler
    public static void onPrepareCraft(@NotNull PrepareItemCraftEvent e) {
        ItemStack result = e.getInventory().getResult();
        if (result == null) {
            return;
        }

        // If we are crafting a slimefun item, allow the craft
        if (SlimefunItem.getByItem(result) != null) {
            return;
        }

        // Otherwise go through with standard checks to make sure we're not trying to craft a vanilla item with SF ingredients
        for (ItemStack item : e.getInventory().getContents()) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null && !usableInWorkbench.contains(sfItem.getId())) {
                e.getInventory().setResult(null);
                break;
            }
        }
    }
}
