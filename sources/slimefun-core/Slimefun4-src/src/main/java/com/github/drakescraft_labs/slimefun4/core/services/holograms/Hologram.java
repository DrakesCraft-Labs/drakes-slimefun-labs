package com.github.drakescraft_labs.slimefun4.core.services.holograms;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Entity;

/**
 * This represents a display entity that can expire and be renamed.
 * 
 * @author TheBusyBiscuit
 *
 */
class Hologram {

    /**
     * This is the minimum duration after which the {@link Hologram} will expire.
     */
    private static final long EXPIRES_AFTER = TimeUnit.MINUTES.toMillis(10);

    /**
     * The {@link UUID} of the display entity.
     */
    private final UUID uniqueId;

    /**
     * The timestamp of when the {@link ArmorStand} was last accessed.
     */
    private long lastAccess;

    /**
     * The label of this {@link Hologram}.
     */
    private String label;

    /**
     * This creates a new {@link Hologram} for the given {@link UUID}.
     * 
     * @param uniqueId
     *            The {@link UUID} of the corresponding {@link ArmorStand}
     */
    Hologram(@Nonnull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * This returns the corresponding display entity
     * and also updates the "lastAccess" timestamp.
     * <p>
     * If the {@link ArmorStand} was removed, it will return null.
     * 
     * @return The display entity or null.
     */
    @Nullable
    Entity getEntity() {
        Entity n = Bukkit.getEntity(uniqueId);

        if ((n instanceof ArmorStand || n instanceof TextDisplay) && n.isValid()) {
            this.lastAccess = System.currentTimeMillis();
            return n;
        } else {
            this.lastAccess = 0;
            return null;
        }
    }

    /**
     * This checks if the associated {@link ArmorStand} has despawned.
     * 
     * @return Whether the {@link ArmorStand} despawned
     */
    boolean hasDespawned() {
        return getEntity() == null;
    }

    /**
     * This returns whether this {@link Hologram} has expired.
     * The armorstand will expire if the last access has been more than 10
     * minutes ago.
     * 
     * @return Whether this {@link Hologram} has expired
     */
    boolean hasExpired() {
        return System.currentTimeMillis() - lastAccess > EXPIRES_AFTER;
    }

    /**
     * This method sets the label of this {@link Hologram}.
     * 
     * @param label
     *            The label to set
     */
    void setLabel(@Nullable String label) {
        if (Objects.equals(this.label, label)) {
            /*
             * Label is already set, no need to cause an entity
             * update. But we can update the lastAccess flag.
             */
            this.lastAccess = System.currentTimeMillis();
        } else {
            this.label = label;
            Entity entity = getEntity();

            if (entity != null) {
                if (label != null) {
                    if (entity instanceof TextDisplay display) {
                        display.setText(label);
                    } else if (entity instanceof ArmorStand armorStand) {
                        armorStand.setCustomNameVisible(true);
                        armorStand.setCustomName(label);
                    }
                } else {
                    if (entity instanceof TextDisplay display) {
                        display.setText("");
                    } else if (entity instanceof ArmorStand armorStand) {
                        armorStand.setCustomNameVisible(false);
                        armorStand.setCustomName(null);
                    }
                }
            }
        }
    }

    /**
     * This will remove the {@link ArmorStand} and expire this {@link Hologram}.
     */
    void remove() {
        Entity entity = getEntity();

        if (entity != null) {
            lastAccess = 0;
            entity.remove();
        }
    }

}
