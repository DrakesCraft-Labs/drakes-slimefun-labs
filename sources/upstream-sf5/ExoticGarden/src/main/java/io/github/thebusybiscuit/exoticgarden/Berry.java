package io.github.thebusybiscuit.exoticgarden;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * Represents a berry or plant that can be grown and harvested.
 *
 * @author TheBusyBiscuit
 */
public class Berry {

    private final ItemStack item;
    private final String id;
    private final String texture;
    private final PlantType type;

    @ParametersAreNonnullByDefault
    public Berry(String id, PlantType type, String texture) {
        this(null, id, type, texture);
    }

    @ParametersAreNonnullByDefault
    public Berry(@Nullable ItemStack item, String id, PlantType type, String texture) {
        this.item = item;
        this.id = id;
        this.texture = texture;
        this.type = type;
    }

    /**
     * Returns the identifier of this Berry.
     *
     * @return the identifier of this Berry
     *
     * @since 1.7.0, rename of {@link #getID()}.
     */
    @Nonnull
    public String getID() {
        return this.id;
    }

    /**
     * Returns the {@link ItemStack} associated with this Berry.
     *
     * @return the item for this Berry
     */
    @Nonnull
    public ItemStack getItem() {
        return type == PlantType.ORE_PLANT ? item : SlimefunItem.getById(id).getItem();
    }

    /**
     * Returns the texture hash for this Berry's player head skin.
     *
     * @return the texture hash string
     */
    @Nonnull
    public String getTexture() {
        return this.texture;
    }

    /**
     * Returns the {@link PlantType} of this Berry.
     *
     * @return the plant type
     */
    @Nonnull
    public PlantType getType() {
        return type;
    }

    /**
     * Returns the Slimefun ID of the bush or plant form of this Berry.
     *
     * @return the bush/plant ID
     */
    @Nonnull
    public String toBush() {
        return type == PlantType.ORE_PLANT ? this.id.replace("_ESSENCE", "_PLANT") : this.id + "_BUSH";
    }

    /**
     * Checks whether the given {@link Material} is a valid soil type for this Berry.
     *
     * @param type
     *            the material to check
     * @return whether the material is valid soil
     */
    public boolean isSoil(@Nonnull Material type) {
        List<Material> soils = Arrays.asList(Material.GRASS_BLOCK, Material.DIRT);
        return soils.contains(type);
    }

}
