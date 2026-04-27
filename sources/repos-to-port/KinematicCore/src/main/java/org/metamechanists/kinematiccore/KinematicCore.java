package org.metamechanists.kinematiccore;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.internal.addon.AddonStorage;
import org.metamechanists.kinematiccore.internal.entity.EntityListener;
import org.metamechanists.kinematiccore.internal.entity.EntityStorage;
import org.metamechanists.kinematiccore.api.addon.KinematicAddon;
import org.metamechanists.kinematiccore.internal.entity.EntityTicker;
import org.metamechanists.kinematiccore.internal.command.KinematicCommand;
import org.metamechanists.kinematiccore.internal.entity.EntityStorageListener;


@Accessors(fluent = true)
public class KinematicCore extends JavaPlugin implements KinematicAddon {
    @Getter
    private static KinematicCore instance;

    @Override
    public void onEnable() {
        instance = this;
        AddonStorage.init();
        EntityStorage.init();
        EntityStorageListener.init();
        EntityTicker.init();
        EntityListener.init();
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("help");
        manager.registerCommand(new KinematicCommand());
    }

    @Override
    public void onDisable() {
        for (KinematicAddon addon : AddonStorage.loadedAddons()) {
            cleanup(addon);
        }
        EntityStorage.instance().close();
    }

    @Override
    public String name() {
        return "KinematicCore";
    }

    public static void cleanup(@NotNull KinematicAddon addon) {
        instance.getLogger().info("Cleaning up addon " + addon.getClass().getSimpleName());
        EntityStorage.instance().cleanup(addon);
    }
}
