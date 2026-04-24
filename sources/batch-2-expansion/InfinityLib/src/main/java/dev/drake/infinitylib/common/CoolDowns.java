package dev.drake.infinitylib.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An object for holding cool downs based on uuids
 *
 * @author Mooy1
 */
public final class CoolDowns {

    private final Map<UUID, Long> map = new HashMap<>();
    private final long cd;

    public CoolDowns(long cd) {
        this.cd = cd;
    }

    public boolean check(UUID uuid) {
        return System.currentTimeMillis() - map.getOrDefault(uuid, 0L) >= cd;
    }

    public void reset(UUID uuid) {
        map.put(uuid, System.currentTimeMillis());
    }

    public boolean checkAndReset(UUID uuid) {
        boolean check = check(uuid);
        if (check) {
            reset(uuid);
        }
        return check;
    }

}
