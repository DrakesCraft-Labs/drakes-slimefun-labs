package dev.drake.dough.updater;

import java.io.File;
import javax.annotation.Nonnull;
import org.bukkit.plugin.Plugin;
import java.util.logging.Level;

/**
 * Stub implementation of BlobBuildUpdater to satisfy references after removal of the auto-update system.
 * It does nothing when started.
 */
public class BlobBuildUpdater {
    private final Plugin plugin;
    private final File file;
    private final String project;
    private final String releaseChannel;

    public BlobBuildUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String project) {
        this(plugin, file, project, "Dev");
    }

    public BlobBuildUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String project, @Nonnull String releaseChannel) {
        this.plugin = plugin;
        this.file = file;
        this.project = project;
        this.releaseChannel = releaseChannel;
        // No auto-update functionality – just log for debugging
        if (plugin != null) {
            plugin.getLogger().log(Level.INFO, "BlobBuildUpdater stub instantiated for project {0}, channel {1}", new Object[]{project, releaseChannel});
        }
    }

    public void start() {
        // No operation – auto-update disabled.
        if (plugin != null) {
            plugin.getLogger().log(Level.INFO, "BlobBuildUpdater stub start() called; auto-update disabled.");
        }
    }
}
