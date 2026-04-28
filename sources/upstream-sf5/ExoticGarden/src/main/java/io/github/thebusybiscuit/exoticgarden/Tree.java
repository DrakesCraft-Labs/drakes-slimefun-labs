package io.github.thebusybiscuit.exoticgarden;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.exoticgarden.schematics.Schematic;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * Represents a custom tree that can be grown and harvested for fruit.
 *
 * @author TheBusyBiscuit
 */
public class Tree {

    private final String sapling;
    private final String texture;
    private final String fruit;
    private final List<Material> soils;

    private Schematic schematic;

    /**
     * Creates a new Tree with the given fruit ID, texture hash, and valid soil types.
     *
     * @param fruit
     *            the fruit identifier
     * @param texture
     *            the texture hash for the fruit head
     * @param soil
     *            the valid soil materials
     */
    public Tree(@Nonnull String fruit, @Nonnull String texture, @Nonnull Material... soil) {
        this.sapling = fruit + "_SAPLING";
        this.texture = texture;
        this.fruit = fruit;
        this.soils = Arrays.asList(soil);
    }

    /**
     * Returns the {@link Schematic} for this tree, loading it lazily.
     *
     * @return the schematic
     * @throws IOException
     *             if the schematic file cannot be read
     */
    @Nonnull
    public Schematic getSchematic() throws IOException {
        if (schematic == null) {
            schematic = Schematic.loadSchematic(new File(ExoticGarden.getInstance().getSchematicsFolder(), fruit + "_TREE.schematic"));
        }

        return schematic;
    }

    /**
     * Returns the sapling {@link ItemStack} for this tree.
     *
     * @return the sapling item
     */
    @Nonnull
    public ItemStack getItem() {
        return SlimefunItem.getById(sapling).getItem();
    }

    /**
     * Returns the texture hash for this tree's fruit head.
     *
     * @return the texture hash
     */
    @Nonnull
    public String getTexture() {
        return this.texture;
    }

    /**
     * Returns the fruit {@link ItemStack} for this tree.
     *
     * @return the fruit item
     */
    @Nonnull
    public ItemStack getFruit() {
        return SlimefunItem.getById(fruit).getItem();
    }

    /**
     * Returns the Slimefun ID of the fruit.
     *
     * @return the fruit ID
     */
    @Nonnull
    public String getFruitID() {
        return fruit;
    }

    /**
     * Returns the Slimefun ID of the sapling.
     *
     * @return the sapling ID
     */
    @Nonnull
    public String getSapling() {
        return this.sapling;
    }

    /**
     * Checks whether the given {@link Material} is a valid soil type for this tree.
     *
     * @param material
     *            the material to check
     * @return whether the material is valid soil
     */
    public boolean isSoil(@Nonnull Material material) {
        return soils.contains(material);
    }

}
