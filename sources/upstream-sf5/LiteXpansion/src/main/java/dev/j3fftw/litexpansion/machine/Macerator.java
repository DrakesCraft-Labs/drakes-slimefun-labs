package dev.j3fftw.litexpansion.machine;

import dev.j3fftw.litexpansion.Items;
import dev.j3fftw.litexpansion.machine.api.PoweredMachine;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Represents the {@link Macerator} class.
 *
 * @author j3fftw
 */
public class Macerator extends AContainer implements PoweredMachine {

    public static final int TIME = 5;

    public Macerator() {
        super(Items.LITEXPANSION, Items.MACERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
            new ItemStack[] {
                new ItemStack(Material.FLINT), new ItemStack(Material.FLINT), new ItemStack(Material.FLINT),
                new ItemStack(Material.COBBLESTONE), Items.MACHINE_BLOCK.item(), new ItemStack(Material.COBBLESTONE),
                null, Items.ELECTRONIC_CIRCUIT.item(), null
            });
    }

    @Override
    protected void registerDefaultRecipes() {
        addRecipe(new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.BLAZE_POWDER, 5));
        addRecipe(new ItemStack(Material.COAL_BLOCK), Items.COAL_DUST.item().asQuantity(9));
        addRecipe(new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.REDSTONE, 9));
        addRecipe(new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ, 4));
        addRecipe(new ItemStack(Material.BONE_BLOCK), new ItemStack(Material.BONE_MEAL, 9));
        addRecipe(new ItemStack(Material.BONE), new ItemStack(Material.BONE_MEAL, 4));
        addRecipe(new ItemStack(Material.CLAY), new ItemStack(Material.CLAY_BALL, 4));
        addRecipe(new ItemStack(Material.COAL), Items.COAL_DUST.item().asQuantity(1));
        addRecipe(new ItemStack(Material.COBBLESTONE), new ItemStack(Material.SAND));
        addRecipe(new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GLOWSTONE_DUST, 4));
        addRecipe(new ItemStack(Material.GRAVEL), new ItemStack(Material.FLINT, 2));
        addRecipe(new ItemStack(Material.ICE), new ItemStack(Material.SNOWBALL, 4));
        addRecipe(new ItemStack(Material.QUARTZ_STAIRS), new ItemStack(Material.QUARTZ, 4));
        addRecipe(new ItemStack(Material.SANDSTONE), new ItemStack(Material.SAND, 4));
        addRecipe(new ItemStack(Material.STONE), new ItemStack(Material.COBBLESTONE));
        addRecipe(new ItemStack(Material.GRANITE), new ItemStack(Material.GRAVEL));
        addRecipe(new ItemStack(Material.ANDESITE), new ItemStack(Material.GRAVEL));
        addRecipe(new ItemStack(Material.DIORITE), new ItemStack(Material.GRAVEL));
        addRecipe(new ItemStack(Material.IRON_ORE), SlimefunItems.IRON_DUST.item().asQuantity(2));
        addRecipe(new ItemStack(Material.COAL_ORE), CustomItemStack.create(Items.COAL_DUST.item(), 2));
        addRecipe(new ItemStack(Material.LAPIS_ORE), Items.LAPIS_DUST.item().asQuantity(2));
        addRecipe(new ItemStack(Material.GOLD_ORE), SlimefunItems.GOLD_DUST.item().asQuantity(2));
        addRecipe(new ItemStack(Material.REDSTONE_ORE), Items.REDSTONE_DUST.item().asQuantity(2));
        addRecipe(new ItemStack(Material.DIAMOND_ORE), Items.DIAMOND_DUST.item().asQuantity(2));
        addRecipe(new ItemStack(Material.EMERALD_ORE), Items.EMERALD_DUST.item().asQuantity(2));
        addRecipe(new ItemStack(Material.NETHER_QUARTZ_ORE), Items.QUARTZ_DUST.item().asQuantity(2));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            addRecipe(new ItemStack(Material.NETHER_GOLD_ORE), SlimefunItems.GOLD_DUST.item().asQuantity(2));
            addRecipe(new ItemStack(Material.ANCIENT_DEBRIS), Items.ANCIENT_DEBRIS_DUST.item().asQuantity(2));
        }

        for (Material mat : Tag.WOOL.getValues()) {
            addRecipe(new ItemStack(mat), new ItemStack(Material.STRING, 4));
        }
    }

    private void addRecipe(ItemStack input, ItemStack output) {
        registerRecipe(Macerator.TIME, new ItemStack[] {input}, new ItemStack[] {output});
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FIRE_CHARGE);
    }

    @Nonnull
    @Override
    public String getInventoryTitle() {
        return "\u00a76Macerator";
    }

    @Nonnull
    @Override
    public String getMachineIdentifier() {
        return "MACERATOR";
    }

    @Override
    public int getCapacity() {
        return getDefaultEnergyConsumption() * 5;
    }

    @Override
    public int getDefaultEnergyConsumption() {
        return 20_000 / 26;
    }

    @Override
    public int getEnergyConsumption() {
        return this.getFinalEnergyConsumption();
    }

    @Override
    public int getSpeed() {
        return 1;
    }
}
