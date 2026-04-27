package io.github.addoncommunity.galactifun;

import com.google.gson.Gson;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class Galactifun2Loader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        PluginLibraries pluginLibraries = load();
        pluginLibraries.asDependencies().forEach(resolver::addDependency);
        pluginLibraries.asRepositories()
                .map(Galactifun2Loader::rewriteMavenCentralMirror)
                .forEach(resolver::addRepository);
        classpathBuilder.addLibrary(resolver);
    }

    /**
     * Paper rejects raw Maven Central URLs ({@code repo.maven.apache.org} / {@code repo1.maven.org}) in
     * {@link MavenLibraryResolver#addRepository}; use Paper's default mirror instead.
     */
    private static RemoteRepository rewriteMavenCentralMirror(RemoteRepository repository) {
        String url = repository.getUrl();
        if (url == null) {
            return repository;
        }
        String lower = url.toLowerCase(Locale.ROOT);
        if (lower.startsWith("https://repo.maven.apache.org/")
                || lower.startsWith("http://repo.maven.apache.org/")
                || lower.startsWith("https://repo1.maven.org/")
                || lower.startsWith("http://repo1.maven.org/")) {
            return new RemoteRepository.Builder(
                    repository.getId(),
                    repository.getContentType(),
                    paperDefaultCentralMirror()
            ).build();
        }
        return repository;
    }

    /** Same fallback order as {@code MavenLibraryResolver} on current Paper (without relying on compile-time constant). */
    private static String paperDefaultCentralMirror() {
        String central = System.getenv("PAPER_DEFAULT_CENTRAL_REPOSITORY");
        if (central == null || central.isBlank()) {
            central = System.getProperty("org.bukkit.plugin.java.LibraryLoader.centralURL");
        }
        if (central == null || central.isBlank()) {
            central = "https://maven-central.storage-download.googleapis.com/maven2";
        }
        return central;
    }

    private PluginLibraries load() {
        try (var in = getClass().getResourceAsStream("/paper-libraries.json")) {
            assert in != null;
            return new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), PluginLibraries.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record PluginLibraries(Map<String, String> repositories, List<String> dependencies) {
        public Stream<Dependency> asDependencies() {
            return dependencies.stream()
                    .map(d -> new Dependency(new DefaultArtifact(d), null));
        }

        public Stream<RemoteRepository> asRepositories() {
            return repositories.entrySet().stream()
                    .map(e -> new RemoteRepository.Builder(e.getKey(), "default", e.getValue()).build());
        }
    }
}
