package com.github.drakescraft_labs.slimefun4.core.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow.Subscription;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import dev.drake.dough.common.CommonPatterns;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.utils.JsonUtils;

/**
 * This Class represents a Metrics Service that sends data to https://bstats.org/
 * This data is used to analyse the usage of this {@link Plugin}.
 * <p>
 * You can find more info in the README file of this Project on GitHub. <br>
 * <b>Note:</b> To start the metrics you will need to be calling {@link #start()}
 *
 * @author WalshyDev
 */
public class MetricsService {

    /**
     * The URL pointing towards the GitHub API.
     */
    private static final String API_URL = "https://api.github.com/";

    /**
     * The Name of our repository - Version 2 of this repo (due to big breaking changes)
     */
    private static final String REPO_NAME = "MetricsModule2";

    /**
     * The name of the metrics jar file.
     */
    private static final String JAR_NAME = "MetricsModule";

    /**
     * The URL pointing towards the /releases/ endpoint of our
     * Metrics repository
     */
    private static final String RELEASES_URL = API_URL + "repos/Slimefun/" + REPO_NAME + "/releases/latest";

    /**
     * The URL pointing towards the download location for a
     * GitHub release of our Metrics repository
     */
    private static final String DOWNLOAD_URL = "https://github.com/Slimefun/" + REPO_NAME + "/releases/download";

    /** Si la API de GitHub falla, intentar esta revisión conocida (debe existir en /releases/download). */
    private static final int FALLBACK_METRICS_BUILD = 29;

    private final Slimefun plugin;
    private final File parentFolder;
    private final File metricsModuleFile;
    private final HttpClient client = HttpClient.newHttpClient();

    private URLClassLoader moduleClassLoader;
    private String metricVersion = null;
    private boolean hasDownloadedUpdate = false;

    /**
     * This constructs a new instance of our {@link MetricsService}.
     * 
     * @param plugin
     *            Our {@link Slimefun} instance
     */
    public MetricsService(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
        this.parentFolder = new File(plugin.getDataFolder(), "cache" + File.separatorChar + "modules");

        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        this.metricsModuleFile = new File(parentFolder, JAR_NAME + ".jar");
    }

    /**
     * This method loads the metric module and starts the metrics collection.
     */
    public void start() {
        if (!metricsModuleFile.exists()) {
            plugin.getLogger().info(JAR_NAME + " does not exist, downloading...");

            int latest = getLatestVersion();
            if (latest <= 0) {
                plugin.getLogger().info("No se pudo leer la última versión de MetricsModule desde GitHub; se intenta la revisión " + FALLBACK_METRICS_BUILD + ".");
                latest = FALLBACK_METRICS_BUILD;
            }
            if (!download(latest) && latest != FALLBACK_METRICS_BUILD) {
                plugin.getLogger().info("Reintento de métricas con revisión " + FALLBACK_METRICS_BUILD + "...");
                download(FALLBACK_METRICS_BUILD);
            }
            if (!metricsModuleFile.exists()) {
                plugin.getLogger().info("Métricas de Slimefun omitidas (descarga desde GitHub no disponible en este entorno).");
                return;
            }
        }

        try {
            /*
             * Load the jar file into a child class loader using the Slimefun
             * PluginClassLoader as a parent.
             */
            moduleClassLoader = URLClassLoader.newInstance(new URL[] { metricsModuleFile.toURI().toURL() }, plugin.getClass().getClassLoader());
            Class<?> metricsClass = moduleClassLoader.loadClass("dev.walshy.sfmetrics.MetricsModule");

            metricVersion = metricsClass.getPackage().getImplementationVersion();

            /*
             * If it has not been newly downloaded, auto-updates are enabled
             * AND there's a new version then cleanup, download and start
             */
            if (!hasDownloadedUpdate && hasAutoUpdates() && checkForUpdate(metricVersion)) {
                plugin.getLogger().info("Cleaned up, now re-loading Metrics-Module!");
                start();
                return;
            }

            // Finally, we're good to start this.
            Method start = metricsClass.getDeclaredMethod("start");
            String version = metricsClass.getPackage().getImplementationVersion();

            // This is required to be sync due to bStats.
            Slimefun.runSync(() -> {
                try {
                    start.invoke(null);
                    plugin.getLogger().info("Metrics build #" + version + " started.");
                } catch (InvocationTargetException e) {
                    plugin.getLogger().log(Level.WARNING, "An exception was thrown while starting the metrics module", e.getCause());
                } catch (Exception | LinkageError e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to start metrics.", e);
                }
            });
        } catch (Exception | LinkageError e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load the metrics module. Maybe the jar is corrupt?", e);
        }
    }

    /**
     * This will close the child {@link ClassLoader} and mark all the resources held under this no longer
     * in use, they will be cleaned up the next GC run.
     */
    public void cleanUp() {
        try {
            if (moduleClassLoader != null) {
                moduleClassLoader.close();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not clean up module class loader. Some memory may have been leaked.");
        }
    }

    /**
     * Checks for a new update and compares it against the current version.
     * If there is a new version available then this returns true.
     *
     * @param currentVersion
     *            The current version which is being used.
     * 
     * @return if there is an update available.
     */
    public boolean checkForUpdate(@Nullable String currentVersion) {
        if (currentVersion == null || !CommonPatterns.NUMERIC.matcher(currentVersion).matches()) {
            return false;
        }

        int latest = getLatestVersion();

        if (latest > Integer.parseInt(currentVersion)) {
            return download(latest);
        }

        return false;
    }

    /**
     * Gets the latest version available as an int.
     * This is an internal method used by {@link #checkForUpdate(String)}.
     * If it cannot get the version for whatever reason this will return 0, effectively always
     * being behind.
     *
     * @return The latest version as an integer or -1 if it failed to fetch.
     */
    private int getLatestVersion() {
        try {
            HttpResponse<String> response = client.send(buildBaseRequest(URI.create(RELEASES_URL)), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return -1;
            }

            JsonElement element = JsonUtils.parseString(response.body());

            var tag = element.getAsJsonObject().get("tag_name");
            if (tag == null || tag.isJsonNull()) {
                return -1;
            }
            return parseMetricsReleaseTag(tag.getAsString());
        } catch (IOException | InterruptedException | JsonParseException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to fetch latest builds for Metrics: {0}", e.getMessage());
            return -1;
        }
    }

    private static int parseMetricsReleaseTag(@Nonnull String tagName) {
        String t = tagName.trim();
        if (t.startsWith("v") || t.startsWith("V")) {
            t = t.substring(1).trim();
        }
        int end = 0;
        while (end < t.length() && Character.isDigit(t.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return -1;
        }
        try {
            return Integer.parseInt(t.substring(0, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Downloads the version specified to Slimefun's data folder.
     *
     * @param version
     *            The version to download.
     */
    private boolean download(int version) {
        if (version <= 0) {
            return false;
        }
        URI uri = URI.create(DOWNLOAD_URL + "/" + version + "/" + JAR_NAME + ".jar");
        File file = new File(parentFolder, "Metrics-" + version + ".jar");

        for (int attempt = 1; attempt <= 3; attempt++) {
            if (attempt > 1) {
                plugin.getLogger().info("Reintentando descarga de MetricsModule (" + attempt + "/3)...");
                try {
                    Thread.sleep(2000L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
            try {
                plugin.getLogger().log(Level.INFO, "# Starting download of MetricsModule build: #{0}", version);

                if (file.exists()) {
                    Files.delete(file.toPath());
                }

                HttpResponse<Path> response = client.send(
                    buildDownloadRequest(uri),
                    downloadMonitor(HttpResponse.BodyHandlers.ofFile(file.toPath()))
                );

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    plugin.getLogger().log(Level.INFO, "Successfully downloaded {0} build: #{1}", new Object[] { JAR_NAME, version });

                    cleanUp();
                    Files.move(file.toPath(), metricsModuleFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    metricVersion = String.valueOf(version);
                    hasDownloadedUpdate = true;
                    return true;
                } else {
                    plugin.getLogger().log(Level.WARNING, "MetricsModule: HTTP {0} al descargar {1}", new Object[] { response.statusCode(), uri });
                }
            } catch (InterruptedException | JsonParseException e) {
                plugin.getLogger().log(Level.WARNING, "MetricsModule: error de red o respuesta inválida: {0}", e.getMessage());
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "MetricsModule: I/O ({0}): {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
            }
        }

        return false;
    }

    /**
     * Returns the currently downloaded metrics version.
     * This <strong>can change</strong>! It may be null or an
     * older version before it has downloaded a newer one.
     *
     * @return The current version or null if not loaded.
     */
    @Nullable
    public String getVersion() {
        return metricVersion;
    }

    /**
     * Returns if the current server has metrics auto-updates enabled.
     *
     * @return True if the current server has metrics auto-updates enabled.
     */
    public boolean hasAutoUpdates() {
        return Slimefun.instance().getConfig().getBoolean("metrics.auto-update");
    }

    private HttpRequest buildBaseRequest(@Nonnull URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(5))
                .header("User-Agent", "MetricsModule Auto-Updater")
                .header("Accept", "application/vnd.github.v3+json")
                .build();
    }

    /** Descarga del JAR: timeout más largo (hosts con latencia o egress limitado). */
    private HttpRequest buildDownloadRequest(@Nonnull URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(45))
                .header("User-Agent", "MetricsModule Auto-Updater")
                .GET()
                .build();
    }

    private <T> BodyHandler<T> downloadMonitor(BodyHandler<T> h) {
        return info -> new BodySubscriber<T>() {

            private BodySubscriber<T> delegateSubscriber = h.apply(info);
            private int lastPercentPosted = 0;
            private long bytesWritten = 0;

            @Override
            public void onSubscribe(Subscription subscription) {
                delegateSubscriber.onSubscribe(subscription);
            }

            @Override
            public void onNext(List<ByteBuffer> item) {
                bytesWritten += item.stream().mapToLong(ByteBuffer::capacity).sum();
                long totalBytes = info.headers().firstValue("Content-Length").map(Long::parseLong).orElse(-1L);

                int percent = (int) (20 * (Math.round((((double) bytesWritten / totalBytes) * 100) / 20)));

                if (percent != 0 && percent != lastPercentPosted) {
                    plugin.getLogger().info("# Downloading... " + percent + "% " + "(" + bytesWritten + "/" + totalBytes + " bytes)");
                    lastPercentPosted = percent;
                }

                delegateSubscriber.onNext(item);
            }

            @Override
            public void onError(Throwable throwable) {
                delegateSubscriber.onError(throwable);

            }

            @Override
            public void onComplete() {
                delegateSubscriber.onComplete();
            }

            @Override
            public CompletionStage<T> getBody() {
                return delegateSubscriber.getBody();
            }
        };
    }
}
