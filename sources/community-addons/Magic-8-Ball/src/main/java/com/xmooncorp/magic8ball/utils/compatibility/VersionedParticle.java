package com.xmooncorp.magic8ball.utils.compatibility;

import com.github.drakescraft-labs.slimefun4.libraries.paperlib.PaperLib;
import org.bukkit.Particle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class VersionedParticle {

    public static final Particle PORTAL;
    public static final Particle ENCHANTED_HIT;
    public static final Particle TOTEM_OF_UNDYING;
    public static final Particle CRIT;

    static {

        int version = PaperLib.getMinecraftVersion();
        int patchVersion = PaperLib.getMinecraftPatchVersion();
        boolean IS_MINECRAFT_1_20_5_OR_6 = (version == 20 && (patchVersion == 5 || patchVersion == 6));

        // PORTAL was not renamed in 1.20.5
        PORTAL = Particle.PORTAL;

        // CRIT was not renamed in 1.20.5
        CRIT = Particle.CRIT;

        // CRIT_MAGIC is renamed to ENCHANTED_HIT in 1.20.5
        ENCHANTED_HIT = IS_MINECRAFT_1_20_5_OR_6
                ? getKey("ENCHANTED_HIT")
                : getKey("CRIT_MAGIC");

        // TOTEM is renamed to TOTEM_OF_UNDYING in 1.20.5
        TOTEM_OF_UNDYING = IS_MINECRAFT_1_20_5_OR_6
                ? getKey("TOTEM_OF_UNDYING")
                : getKey("TOTEM");
    }

    @Nullable
    private static Particle getKey(@Nonnull String key) {
        try {
            Field field = Particle.class.getDeclaredField(key);
            return (Particle) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
