package org.metamechanists.sanecrafting;


import com.github.drakescraft_labs.labupdate.DrakesLabsReleaseUpdate;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import org.bukkit.event.server.ServerLoadEvent;
import lombok.Getter;
import lombok.NonNull;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.sanecrafting.patches.CraftingTablePatch;
import org.metamechanists.sanecrafting.patches.RecipeBookResearchPatch;
import org.metamechanists.sanecrafting.patches.RecipeLorePatch;
import org.metamechanists.sanecrafting.patches.UsableInWorkbenchPatch;
import dev.drake.dough.updater.BlobBuildUpdater;


public final class SaneCrafting extends JavaPlugin implements SlimefunAddon {
    private static final int BSTATS_ID = 22737;
    @Getter
    private static SaneCrafting instance;

    @Override
    public void onEnable() {
        DrakesLabsReleaseUpdate.schedule(this, "SaneCrafting-drake");

        instance = this;

        if (getConfig().getBoolean("auto-update") && !getPluginVersion().contains("MODIFIED")) {
            new BlobBuildUpdater(this, getFile(), "SaneCrafting").start();
        }

        new Metrics(this, BSTATS_ID);

        Runnable applyPatches = () -> {
            UsableInWorkbenchPatch.apply();
            CraftingTablePatch.apply();
            RecipeBookResearchPatch.apply();
            RecipeLorePatch.apply();
        };

        // Tras STARTUP las recetas ya deben existir antes de que los jugadores sincronicen el libro de recetas
        // (delay 1 tick provocaba "unrecognized recipe" en el primer login).
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onServerLoad(ServerLoadEvent e) {
                if (e.getType() != ServerLoadEvent.LoadType.STARTUP
                        && e.getType() != ServerLoadEvent.LoadType.RELOAD) {
                    return;
                }
                applyPatches.run();
                if (e.getType() == ServerLoadEvent.LoadType.STARTUP) {
                    // Segundo pase: addons que registran recetas ECT algo más tarde
                    Bukkit.getScheduler().runTaskLater(SaneCrafting.this, () -> {
                        CraftingTablePatch.apply();
                        RecipeBookResearchPatch.apply();
                        RecipeLorePatch.apply();
                    }, 20L);
                }
            }
        }, this);

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
