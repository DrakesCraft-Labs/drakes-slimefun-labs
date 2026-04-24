package dev.sefiraat.cultivation.implementation.slimefun.machines;

import dev.sefiraat.cultivation.Cultivation;
import dev.sefiraat.cultivation.api.datatypes.instances.FloraLevelProfile;
import dev.sefiraat.cultivation.api.slimefun.items.plants.HarvestablePlant;
import dev.sefiraat.cultivation.implementation.slimefun.items.Machines;
import dev.drake.sefilib.string.Theme;
import dev.drake.dough.items.CustomItemStack;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GardenCloche extends SlimefunItem implements EnergyNetComponent {

    private static final String KEY_PLANT = "plant";
    private static final int PLANT_SLOT = 20;
    private static final int[] OUTPUT_SLOTS = new int[] {
            14, 15, 16, 23, 24, 25, 32, 33, 34
    };
    private static final int[] PLANT_SLOT_BACKGROUND = new int[] {
            10, 11, 12, 19, 21, 28, 29, 30
    };
    private static final int[] BACKGROUND = new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 17, 18, 22, 26, 27, 31, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int POWER_REQUIREMENT = 100;

    public GardenCloche(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(
                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                        Location location = e.getBlock().getLocation();
                        BlockMenu blockMenu = BlockStorage.getInventory(location);
                        if (blockMenu != null) {
                            blockMenu.dropItems(location, PLANT_SLOT);
                            blockMenu.dropItems(location, OUTPUT_SLOTS);
                        }
                    }
                },
                new BlockTicker() {
                    @Override
                    public boolean isSynchronized() {
                        return false;
                    }

                    @Override
                    public void tick(Block block, SlimefunItem item, Config data) {
                        BlockMenu blockMenu = BlockStorage.getInventory(block);
                        ItemStack possiblePlant = blockMenu.getItemInSlot(PLANT_SLOT);
                        SlimefunItem slimefunItem = SlimefunItem.getByItem(possiblePlant);
                        Location location = block.getLocation();
                        if (slimefunItem instanceof HarvestablePlant plant) {
                            if (getCharge(location) < POWER_REQUIREMENT) {
                                return;
                            }
                            FloraLevelProfile profile = FloraLevelProfile.fromItemStack(possiblePlant);
                            double growthRate = plant.getGrowthRate(profile);
                            double rand = ThreadLocalRandom.current().nextDouble();
                            if (rand < growthRate) {
                                ItemStack itemStack = plant.getRandomItemWithDropModifier(profile);
                                blockMenu.pushItem(itemStack, OUTPUT_SLOTS);
                                removeCharge(location, POWER_REQUIREMENT);
                            }
                        }
                    }
                });
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                ItemStack backgroundInput = new CustomItemStack(
                        Material.GREEN_STAINED_GLASS_PANE,
                        Theme.PASSIVE.apply("Insert Plant"));
                drawBackground(BACKGROUND);
                drawBackground(backgroundInput, PLANT_SLOT_BACKGROUND);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return Machines.GARDEN_CLOCHE.canUse(player, false)
                        && Slimefun.getProtectionManager()
                                .hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return OUTPUT_SLOTS;
                }
                return new int[0];
            }
        };
    }

    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 2500;
    }
}
