package com.github.drakescraft_labs.coloredenderchests;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import com.github.drakescraft_labs.slimefun4.utils.ColoredMaterial;

final class ColorIndicator {

    private static double angle = Math.toRadians(345);
    private static double offset = -0.08;

    private ColorIndicator() {}

    static void updateIndicator(Block b, int c1, int c2, int c3, int yaw) {
        updateIndicator(b, c1, c2, c3, yaw, PrivateEnderStorage.isPrivate(b));
    }

    static void updateIndicator(Block b, int c1, int c2, int c3, int yaw, boolean isPrivate) {
        removeIndicator(b);
        EulerAngle euler = new EulerAngle(angle, 0F, 0F);

        Location l = b.getLocation().add(0.5D, 0.5D + offset, 0.5D);
        createArmorStand(l, new ItemStack(getWool(c1)), euler, (float) yaw);
        createArmorStand(translocate(l, 1, yaw), new ItemStack(getWool(c2)), euler, (float) yaw);
        createArmorStand(translocate(l, 1, yaw), new ItemStack(getWool(c3)), euler, (float) yaw);

        if (isPrivate) {
            Location latch = getLatchLocation(b.getLocation(), yaw);
            EulerAngle latchEuler = new EulerAngle(Math.toRadians(15), 0F, 0F);
            createArmorStand(latch, new ItemStack(Material.DIAMOND), latchEuler, (float) yaw);
        }
    }

    static void removeIndicator(Block b) {
        for (Entity n : b.getChunk().getEntities()) {
            if (n instanceof ArmorStand && b.getLocation().add(0.5D, 0.5D, 0.5D).distanceSquared(n.getLocation()) < 1.0D && n.getCustomName() == null) {
                n.remove();
            }
        }
    }

    public static ArmorStand createArmorStand(Location l, ItemStack item, EulerAngle arm, float yaw) {
        l.setYaw(yaw);
        ArmorStand armorStand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        armorStand.getEquipment().setItemInMainHand(item);
        armorStand.setVisible(false);
        armorStand.setSilent(true);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setArms(true);
        armorStand.setRightArmPose(arm);
        armorStand.setBasePlate(false);
        armorStand.setRemoveWhenFarAway(false);

        return armorStand;
    }

    private static Material getWool(int index) {
        return ColoredMaterial.WOOL.get(index);
    }

    private static Location translocate(Location l, int direction, int yaw) {
        if (yaw == 45) { // 0 (SOUTH)
            return l.add(0.275 * direction, 0, 0);
        } else if (yaw == 225) { // 180 (NORTH)
            return l.add(-0.275 * direction, 0, 0);
        } else if (yaw == -45) { // -90 (EAST)
            return l.add(0, 0, -0.275 * direction);
        } else { // 90 (WEST)
            return l.add(0, 0, 0.275 * direction);
        }
    }

    private static Location getLatchLocation(Location blockLoc, int yaw) {
        Location l = blockLoc.clone().add(0.5D, -0.15D, 0.5D);
        double dist = 0.42D;
        if (yaw == 45) { // SOUTH (+Z)
            return l.add(0.18D, 0, dist);
        } else if (yaw == 225) { // NORTH (-Z)
            return l.add(-0.18D, 0, -dist);
        } else if (yaw == -45) { // EAST (+X)
            return l.add(dist, 0, -0.18D);
        } else { // WEST (-X)
            return l.add(-dist, 0, 0.18D);
        }
    }
}
