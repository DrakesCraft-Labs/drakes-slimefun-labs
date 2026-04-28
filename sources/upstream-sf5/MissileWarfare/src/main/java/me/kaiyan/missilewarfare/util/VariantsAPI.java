package me.kaiyan.missilewarfare.util;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.kaiyan.missilewarfare.items.MissileClass;
import me.kaiyan.missilewarfare.missiles.MissileConfig;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Utility class for missile variant type mappings, inventory scanning,
 * and missile trail particle effects.
 *
 * @author MissileWarfare contributors
 */
public class VariantsAPI {

    public static Random rand = new Random();

    /**
     * Returns the short string code for the given missile type integer.
     *
     * @param type the missile type integer
     * @return the string variant code, or {@code "NONE"} if unrecognised
     */
    @Nonnull
    public static String getStrVariantFromInt(int type) {
        switch (type) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "SMR";
            case 2:
                return "SMHE";
            case 3:
                return "SMLR";
            case 4:
                return "SMAC";
            case 5:
                return "GAAM";
            case 6:
                return "MR";
            case 7:
                return "MHE";
            case 8:
                return "MLR";
            case 9:
                return "MAC";
            case 10:
                return "MAPT1";
            case 11:
                return "MAPT2";
            case 12:
                return "MAPT3";
            case 13:
                return "MGAS";
            case 14:
                return "EXCV";
            case 15:
                return "STCK";
            case 16:
                return "ICBM";
            case 17:
                return "CLST";
            case 18:
                return "NPLM";
            case 19:
                return "AVMS";
            case 20:
                return "AVHE";
            case 21:
                return "AVLR";
            case 22:
                return "AVAC";
            case 23:
                return "ELYT";
        }
        return "NONE";
    }

    /**
     * Returns the integer type identifier for the given Slimefun item.
     *
     * @param item the Slimefun item
     * @return the type integer, or {@code 0} if unrecognised
     */
    public static int getIntTypeFromSlimefunitem(@Nonnull SlimefunItem item) {
        return getIntTypeFromSlimefunitemID(item.getId());
    }

    /**
     * Returns the integer type identifier for the given Slimefun item ID string.
     *
     * @param id the Slimefun item ID
     * @return the type integer, or {@code 0} if unrecognised
     */
    public static int getIntTypeFromSlimefunitemID(@Nonnull String id) {
        switch (id) {
            case "SMALLMISSILE":
                return 1;
            case "SMALLMISSILEHE":
                return 2;
            case "SMALLMISSILELR":
                return 3;
            case "SMALLMISSILEAC":
                return 4;
            case "ANTIAIRMISSILE":
                return 5;
            case "MISSILE":
                return 6;
            case "MISSILEHE":
                return 7;
            case "MISSILELR":
                return 8;
            case "MISSILEAC":
                return 9;
            case "MISSILEAPONE":
                return 10;
            case "MISSILEAPTWO":
                return 11;
            case "MISSILEAPTHR":
                return 12;
            case "MISSILEGAS":
                return 13;
            case "MISSILEEXCAV":
                return 14;
            case "MISSILESTICK":
                return 15;
            case "MISSILEICBM":
                return 16;
            case "MISSILECLUSTER":
                return 17;
            case "MISSILENAPALM":
                return 18;
            case "MISSILEADV":
                return 19;
            case "MISSILEHEADV":
                return 20;
            case "MISSILELRADV":
                return 21;
            case "MISSILEACADV":
                return 22;
            case "ANTIELYTRAMISSILE":
                return 23;
        }
        return 0;
    }

    /**
     * Returns the first missile item found in the given inventory.
     *
     * @param inv the inventory to search
     * @return the first missile ItemStack, or {@code null} if none found
     */
    @Nullable
    public static ItemStack getFirstMissile(@Nonnull Inventory inv) {
        for (ItemStack item : inv) {
            SlimefunItem slimefun_item = SlimefunItem.getByItem(item);
            if (slimefun_item != null && getIntTypeFromSlimefunitem(slimefun_item) != 0) {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns the first missile item in the inventory that matches the given Slimefun item type.
     *
     * @param inv          the inventory to search
     * @param slimefunItem the Slimefun item to match against
     * @return the first matching ItemStack, or {@code null} if none found
     */
    @Nullable
    public static ItemStack getOtherFirstMissile(@Nonnull Inventory inv, @Nonnull SlimefunItem slimefunItem) {
        for (ItemStack item : inv) {
            SlimefunItem _item = SlimefunItem.getByItem(item);
            if (_item != null && _item.getId().equals(slimefunItem.getId())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns the {@link MissileClass} stats for the given missile type.
     *
     * @param type the missile type integer
     * @return the missile class, or {@code null} if unrecognised
     */
    @Nullable
    public static MissileClass missileStatsFromType(int type) {
        switch (type) {
            case 1:
                return MissileConfig.missiles[0];
            case 2:
                return MissileConfig.missiles[1];
            case 3:
                return MissileConfig.missiles[2];
            case 4:
                return MissileConfig.missiles[3];
            case 5:
                return MissileConfig.missiles[4];
            case 6:
                return MissileConfig.missiles[5];
            case 7:
                return MissileConfig.missiles[6];
            case 8:
                return MissileConfig.missiles[7];
            case 9:
                return MissileConfig.missiles[8];
            case 10:
                return MissileConfig.missiles[9];
            case 11:
                return MissileConfig.missiles[10];
            case 12:
                return MissileConfig.missiles[11];
            case 13:
                return MissileConfig.missiles[12];
            case 14:
                return MissileConfig.missiles[13];
            case 15:
                return MissileConfig.missiles[14];
            case 16:
                return MissileConfig.missiles[15];
            case 17:
                return MissileConfig.missiles[16];
            case 18:
                return MissileConfig.missiles[17];
            case 19:
                return MissileConfig.missiles[18];
            case 20:
                return MissileConfig.missiles[19];
            case 21:
                return MissileConfig.missiles[20];
            case 22:
                return MissileConfig.missiles[21];
            case 23:
                return MissileConfig.missiles[22];
        }
        return null;
    }

    /**
     * Checks whether the given distance exceeds the range of the specified missile type.
     *
     * @param dist the squared distance to check
     * @param type the missile type integer
     * @return {@code true} if the distance is out of range
     */
    public static boolean isInRange(int dist, int type) {
        MissileClass missile = missileStatsFromType(type);
        return dist >= missile.range;
    }

    /**
     * Spawns particle trail effects for a missile at the given position.
     *
     * @param world    the world to spawn particles in
     * @param type     the missile type integer
     * @param pos      the current missile position
     * @param velocity the current missile velocity
     */
    public static void spawnMissileTrail(@Nonnull World world, int type, @Nonnull Vector pos,
                                         @Nonnull Vector velocity) {
        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, pos.toLocation(world), 0, 0, 0, 0, 0.1, null, true);
        world.spawnParticle(Particle.FLAME, pos.toLocation(world), 0, -velocity.getX() + ((rand.nextDouble() - 0.5) * 0.5), -velocity.getY() + ((rand.nextDouble() - 0.5) * 0.5), -velocity.getZ() + ((rand.nextDouble() - 0.5) * 0.5), 0.25, null, true);
        world.spawnParticle(Particle.FLAME, pos.toLocation(world), 0, -velocity.getX() + ((rand.nextDouble() - 0.5) * 0.5), -velocity.getY() + ((rand.nextDouble() - 0.5) * 0.5), -velocity.getZ() + ((rand.nextDouble() - 0.5) * 0.5), 0.25, null, true);
        world.spawnParticle(Particle.FLAME, pos.toLocation(world), 0, -velocity.getX() + ((rand.nextDouble() - 0.5) * 0.5), -velocity.getY() + ((rand.nextDouble() - 0.5) * 0.5), -velocity.getZ() + ((rand.nextDouble() - 0.5) * 0.5), 0.25, null, true);
        if (type == 2 || type == 7) {
            // HE missiles
            world.spawnParticle(Particle.ANGRY_VILLAGER, pos.toLocation(world), 0, 0, 0, 0, 0.1, null, true);
        } else if (type == 3 || type == 8) {
            // Long range
            world.spawnParticle(Particle.END_ROD, pos.toLocation(world), 0, -velocity.getX() + ((rand.nextDouble() - 0.5) * 0.25), -velocity.getY() + ((rand.nextDouble() - 0.5) * 0.25), -velocity.getZ() + ((rand.nextDouble() - 0.5) * 0.25), 0.3, null, true);
        } else if (type == 4 || type == 9) {
            // Accurate missiles
            world.spawnParticle(Particle.CRIT, pos.toLocation(world), 0, -velocity.getX() + ((rand.nextDouble() - 0.5) * 0.25), -velocity.getY() + ((rand.nextDouble() - 0.5) * 0.25), -velocity.getZ() + ((rand.nextDouble() - 0.5) * 0.25), 0.3, null, true);
        }
        if (type == 6 || type == 7 || type == 8 || type == 9 || type == 10) {
            // 'Missile' types
            world.spawnParticle(Particle.HAPPY_VILLAGER, pos.toLocation(world), 1);
        }
        if (type == 10 || type == 11 || type == 12) {
            world.spawnParticle(Particle.DRAGON_BREATH, pos.toLocation(world), 1);
        }
    }
}
