package org.mockbukkit.mockbukkit.tags;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.mockbukkit.mockbukkit.util.ResourceLoader;

/**
 * Loads MockBukkit tags while dropping materials absent from Paper 1.21.11.
 */
public final class TagFactory {

    private TagFactory() {
        throw new IllegalStateException("Utility class");
    }

    @NotNull
    public static Tag<?> createTag(@NotNull TagRegistry registry, @NotNull NamespacedKey key) {
        Preconditions.checkNotNull(registry, "registry cannot be null");
        Preconditions.checkNotNull(key, "key cannot be null");
        JsonArray source = ResourceLoader.loadResource(
                String.format("/tags/%s/%s.json", registry.getRegistry(), key.value()))
                .getAsJsonObject().get("values").getAsJsonArray();
        JsonArray values = new JsonArray();
        for (JsonElement value : source) {
            if (registry == TagRegistry.BLOCKS || registry == TagRegistry.ITEMS) {
                String material = value.getAsString().replace("minecraft:", "").toUpperCase();
                try {
                    Material.valueOf(material);
                } catch (IllegalArgumentException ignored) {
                    continue;
                }
            }
            values.add(value);
        }
        return createTag(registry, key, values);
    }

    static Tag<?> createTag(TagRegistry registry, NamespacedKey key, JsonArray values) {
        return switch (registry) {
            case BLOCKS, ITEMS -> MaterialTagMock.from(key, values);
            case ENTITY_TYPES -> EntityTypeTagMock.from(key, values);
            case FLUIDS -> FluidTagMock.from(key, values);
            case GAME_EVENTS -> GameEventTagMock.from(key, values);
            case DAMAGE_TYPES -> DamageTypeTagMock.from(key, values);
        };
    }
}
