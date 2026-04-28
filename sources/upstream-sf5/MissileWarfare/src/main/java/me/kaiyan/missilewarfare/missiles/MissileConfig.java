package me.kaiyan.missilewarfare.missiles;

import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import me.kaiyan.missilewarfare.items.MissileClass;
import me.kaiyan.missilewarfare.util.VariantsAPI;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and stores missile configuration data from the plugin config file.
 *
 * @author MissileWarfare contributors
 */
public class MissileConfig {

    /** Array of configured missile classes indexed by type. */
    public static MissileClass[] missiles;

    /**
     * Reads missile configuration from the given config and populates the
     * {@link #missiles} array.
     *
     * @param cfg the plugin configuration
     */
    public static void setup(@Nonnull Config cfg) {
        List<MissileClass> outMissiles = new ArrayList<>();
        for (String key : cfg.getKeys("missiles")) {
            outMissiles.add(new MissileClass(
                    cfg.getDouble("missiles." + key + ".speed"),
                    cfg.getInt("missiles." + key + ".range"),
                    cfg.getInt("missiles." + key + ".power"),
                    cfg.getInt("missiles." + key + ".accuracy"),
                    VariantsAPI.getIntTypeFromSlimefunitemID(key)));
        }
        missiles = outMissiles.toArray(new MissileClass[0]);
    }
}
