package org.metamechanists.sanecrafting;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import lombok.NonNull;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.sanecrafting.patches.CraftingTablePatch;
import org.metamechanists.sanecrafting.patches.RecipeBookResearchPatch;
import org.metamechanists.sanecrafting.patches.RecipeLorePatch;
import org.metamechanists.sanecrafting.patches.UsableInWorkbenchPatch;
import com.github.drakescraft_labs.slimefun4.libraries.dough.updater.BlobBuildUpdater;


public final class SaneCrafting extends JavaPlugin implements SlimefunAddon {
    private static final int BSTATS_ID = 22737;
    @Getter
    private static SaneCrafting instance;

    @Override
    public void onEnable() {
        instance = this;

        if (getConfig().getBoolean("auto-update") && !getPluginVersion().contains("MODIFIED")) {
            new BlobBuildUpdater(this, getFile(), "SaneCrafting").start();
        }

        new Metrics(this, BSTATS_ID);

        // Patches applied on first tick to ensure everything has loaded
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            UsableInWorkbenchPatch.apply();
            CraftingTablePatch.apply();
            RecipeBookResearchPatch.apply();
            RecipeLorePatch.apply();
        }, 1);

    }

    @Override
    public void onDisable() {

    }

    @NonNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }
}
