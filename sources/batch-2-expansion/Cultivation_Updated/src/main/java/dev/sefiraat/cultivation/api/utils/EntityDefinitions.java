package dev.sefiraat.cultivation.api.utils;

import dev.sefiraat.cultivation.Cultivation;
import dev.drake.sefilib.entity.LivingEntityCategory;
import dev.drake.sefilib.entity.LivingEntityDefinition;
import dev.drake.sefilib.entity.LivingEntitySelector;
import com.github.drakescraft_labs.slimefun4.libraries.dough.versions.SemanticVersion;
import com.github.drakescraft_labs.slimefun4.libraries.dough.versions.MinecraftVersion;
import com.github.drakescraft_labs.slimefun4.libraries.dough.versions.UnknownServerVersionException;
import org.bukkit.Server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class EntityDefinitions {

    private EntityDefinitions() {
        throw new IllegalStateException("Utility class");
    }

    private static Set<LivingEntityDefinition> passiveMobs;
    private static Set<LivingEntityDefinition> hostileMobs;
    private static Set<LivingEntityDefinition> bossMobs;
    private static Set<LivingEntityDefinition> flyingMobs;

    static {
        Server server = Cultivation.getInstance().getServer();

        try {
            SemanticVersion serverVersion = getServerVersion();
            passiveMobs = LivingEntitySelector.start()
                .includeCategories(LivingEntityCategory.PASSIVE)
                .setVersion(serverVersion)
                .process(LivingEntitySelector.MatchType.MATCH_ALL);
            hostileMobs = LivingEntitySelector.start()
                .includeCategories(LivingEntityCategory.HOSTILE)
                .setVersion(serverVersion)
                .process(LivingEntitySelector.MatchType.MATCH_ALL);
            bossMobs = LivingEntitySelector.start()
                .includeCategories(LivingEntityCategory.BOSS)
                .setVersion(serverVersion)
                .process(LivingEntitySelector.MatchType.MATCH_ALL);
            flyingMobs = LivingEntitySelector.start()
                .includeCategories(LivingEntityCategory.FLYING)
                .setVersion(serverVersion)
                .process(LivingEntitySelector.MatchType.MATCH_ALL);
        } catch (UnknownServerVersionException e) {
            passiveMobs = new HashSet<>();
            hostileMobs = new HashSet<>();
            bossMobs = new HashSet<>();
            flyingMobs = new HashSet<>();
            server.getLogger().severe(e.getMessage());
        }
    }

    public static Set<LivingEntityDefinition> getPassiveMobs() {
        return Collections.unmodifiableSet(passiveMobs);
    }

    public static Set<LivingEntityDefinition> getHostileMobs() {
        return Collections.unmodifiableSet(hostileMobs);
    }

    public static Set<LivingEntityDefinition> getBossMobs() {
        return Collections.unmodifiableSet(bossMobs);
    }

    public static Set<LivingEntityDefinition> getFlyingMobs() {
        return flyingMobs;
    }

    private static SemanticVersion getServerVersion() throws UnknownServerVersionException {
        MinecraftVersion version = MinecraftVersion.of(Cultivation.getInstance().getServer());
        return new SemanticVersion(
            version.getMajorVersion(),
            version.getMinorVersion(),
            version.getPatchVersion()
        );
    }
}
