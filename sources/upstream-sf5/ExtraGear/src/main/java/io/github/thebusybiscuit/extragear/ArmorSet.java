package io.github.thebusybiscuit.extragear;

import javax.annotation.Nonnull;

import org.bukkit.Material;

/**
 * Represents the different base armor material sets that can be used
 * when registering new armor items in ExtraGear.
 *
 * @author TheBusyBiscuit
 */
enum ArmorSet {

    LEATHER(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS),
    IRON(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS),
    GOLD(Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS),
    DIAMOND(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);

    private final Material helmet;
    private final Material chestplate;
    private final Material leggings;
    private final Material boots;

    ArmorSet(@Nonnull Material helmet, @Nonnull Material chestplate, @Nonnull Material leggings, @Nonnull Material boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    @Nonnull
    public Material getHelmet() {
        return helmet;
    }

    @Nonnull
    public Material getChestplate() {
        return chestplate;
    }

    @Nonnull
    public Material getLeggings() {
        return leggings;
    }

    @Nonnull
    public Material getBoots() {
        return boots;
    }

}
