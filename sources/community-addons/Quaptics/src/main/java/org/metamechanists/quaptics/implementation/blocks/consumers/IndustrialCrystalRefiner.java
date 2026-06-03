package org.metamechanists.quaptics.implementation.blocks.consumers;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class IndustrialCrystalRefiner extends CrystalRefiner {
    public static final Settings INDUSTRIAL_CRYSTAL_REFINER_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .timePerItem(10)
            .minPower(1200)
            .minFrequency(260)
            .build();
    public static final SlimefunItemStack INDUSTRIAL_CRYSTAL_REFINER = new SlimefunItemStack(
            "QP_INDUSTRIAL_CRYSTAL_REFINER",
            Material.QUARTZ_BLOCK,
            "&bIndustrial Crystal Refiner",
            Lore.create(CRYSTAL_REFINER_SETTINGS,
                    "&7● Refines Phase Crystals",
                    "&7● Can refine up to 16 items at once",
                    "&7● &eRight Click &7with an item to start refining"));

    public IndustrialCrystalRefiner(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 1.10F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("wall1", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .translate(0.3F, -0.21F, -0.3F)
                        .scale(0.2F, 0.6F, 1.1F)
                        .rotateY(Math.PI / 4))
                .add("wall2", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .translate(-0.3F, -0.21F, 0.3F)
                        .scale(0.2F, 0.6F, 1.1F)
                        .rotateY(Math.PI / 4))
                .add("wall3", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .translate(0.3F, -0.21F, 0.3F)
                        .scale(1.1F, 0.6F, 0.2F)
                        .rotateY(Math.PI / 4))
                .add("wall4", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .translate(-0.3F, -0.21F, -0.3F)
                        .scale(1.1F, 0.6F, 0.2F)
                        .rotateY(Math.PI / 4))

                .add("panel1", new ModelCuboid()
                        .material(Material.RED_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .translate(0.4F, -0.21F, -0.4F)
                        .scale(0.05F, 0.4F, 0.5F)
                        .rotateY(Math.PI / 4))
                .add("panel2", new ModelCuboid()
                        .material(Material.RED_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .translate(-0.4F, -0.21F, 0.4F)
                        .scale(0.05F, 0.4F, 0.5F)
                        .rotateY(Math.PI / 4))
                .add("panel3", new ModelCuboid()
                        .material(Material.RED_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .translate(0.4F, -0.21F, 0.4F)
                        .scale(0.5F, 0.4F, 0.05F)
                        .rotateY(Math.PI / 4))
                .add("panel4", new ModelCuboid()
                        .material(Material.RED_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .translate(-0.4F, -0.21F, -0.4F)
                        .scale(0.5F, 0.4F, 0.05F)
                        .rotateY(Math.PI / 4))

                .add("water", new ModelCuboid()
                        .material(Material.BLUE_CONCRETE)
                        .translate(0, -0.3F, 0)
                        .scale(1.0F, 0.4F, 1.0F)
                        .rotateY(Math.PI / 4))
                .add("concrete", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .translate(0, -0.2F, 0)
                        .scale(0.4F)
                        .rotateY(Math.PI / 4))

                .add("item", new ModelItem()
                        .lookAlong(player.getFacing())
                        .scale(0.5F)
                        .translate(0, 0.2F, 0))

                .buildAtBlockCenter(location);
    }

    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "concrete", powered);
        brightnessAnimation(location, "panel1", powered);
        brightnessAnimation(location, "panel2", powered);
        brightnessAnimation(location, "panel3", powered);
        brightnessAnimation(location, "panel4", powered);
    }

    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        final Map<ItemStack, ItemStack> recipes = new HashMap<>();
        for (final Entry<ItemStack, ItemStack> recipe : super.getRecipes().entrySet()) {
            for (int i = 0; i < 16; i++) {
                final ItemStack input = recipe.getKey().clone();
                final ItemStack output = recipe.getValue().clone();
                input.add(i);
                output.add(i);
                recipes.put(input, output);
            }
        }
        return recipes;
    }
}
