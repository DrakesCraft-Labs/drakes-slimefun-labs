package dev.drake.dough.updater;

import java.io.File;
import javax.annotation.Nonnull;
import org.bukkit.plugin.Plugin;
import java.util.logging.Level;

/**
 * Stub implementation of GitHubBuildsUpdater after removal of auto-update functionality.
 * Provides constructors matching the original API but performs no actions.
 */
public class GitHubBuildsUpdater {
    private final Plugin plugin;
    private final File file;
    private final String repository;
    private final String prefix;

    public GitHubBuildsUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String repo) {
        this(plugin, file, repo, "DEV - ");
    }

    public GitHubBuildsUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String repo, @Nonnull String prefix) {
        this.plugin = plugin;
        this.file = file;
        this.repository = repo;
        this.prefix = prefix;
        if (plugin != null) {
            plugin.getLogger().log(Level.INFO, "GitHubBuildsUpdater stub init for repo {0} with prefix {1}", new Object[]{repo, prefix});
        }
    }

    public void start() {
        if (plugin != null) {
            plugin.getLogger().log(Level.INFO, "GitHubBuildsUpdater stub start() called; auto-update disabled.");
        }
    }
}
