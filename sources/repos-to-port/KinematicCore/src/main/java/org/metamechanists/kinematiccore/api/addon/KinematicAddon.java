package org.metamechanists.kinematiccore.api.addon;

import org.bukkit.Bukkit;
import org.metamechanists.kinematiccore.KinematicCore;


@SuppressWarnings({"InterfaceMayBeAnnotatedFunctional", "unused"})
public interface KinematicAddon {
    String name();
    default void cleanup() {
        // If this doesn't fire, Kinematic already disabled so should have saved addon data already
        if (Bukkit.getPluginManager().isPluginEnabled("KinematicCore")) {
            KinematicCore.cleanup(this);
        }
    }
}
