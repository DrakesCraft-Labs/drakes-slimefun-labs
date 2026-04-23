package me.pranavverma.advancedtech.general.items.solargen.AdvancedSolarGen.lib;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.ItemSetting;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.EnergyNetProvider;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;
import me.pranavverma.advancedtech.general.BaseItems;


public class AdvancedSolarGenLib extends SlimefunItem implements EnergyNetProvider {

    private final ItemSetting<Boolean> useNightEnergyInOtherDimensions = new ItemSetting<>(this, "other-dimensions-use-night-energy", false);
    private final int dayEnergy;
    private final int nightEnergy;
    private final int capacity;

    private boolean commandHubFound;


    @ParametersAreNonnullByDefault
    public AdvancedSolarGenLib(ItemGroup itemGroup, int dayEnergy, int nightEnergy, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity) {
        super(itemGroup, item, recipeType, recipe);
        this.dayEnergy = dayEnergy;
        this.nightEnergy = nightEnergy;
        this.capacity = capacity; 

        addItemSetting(useNightEnergyInOtherDimensions);
        addItemHandler(onPlace());
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                if (scanForCommandHub(block)) {
                    commandHubFound = true;

                } else {
                    commandHubFound = false;
                } 
            }
        });


    }

    @ParametersAreNonnullByDefault
    public AdvancedSolarGenLib(ItemGroup itemGroup, int dayEnergy, int nightEnergy, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        this(itemGroup, dayEnergy, nightEnergy, item, recipeType, recipe, 0);
    }

    public int getDayEnergy() {
        if (commandHubFound) {
            return dayEnergy;
         }  else {
            return 0;
        }
        
    }

    public int getNightEnergy() {
        if (commandHubFound) {
            return nightEnergy;
         } else {
            return 0;
        }
    }

    @Override
    public int getCapacity() {
        if (commandHubFound) {
            return capacity;
         } else {
            return 0;
        }
    }

    @Override
    public int getGeneratedOutput(Location l, Config data) {
        World world = l.getWorld();

        if (world.getEnvironment() != Environment.NORMAL) {
            if (useNightEnergyInOtherDimensions.getValue()) {
                return getNightEnergy();
            }

            return 0;
        } else {
            boolean isDaytime = isDaytime(world);

            // Performance optimization for daytime-only solar generators
            if (!isDaytime && getNightEnergy() < 1) {
                return 0;
            } else if (!world.isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4) || l.getBlock().getLightFromSky() < 15) {
                return 0;
            } else {
                return isDaytime ? getDayEnergy() : getNightEnergy();
            }
        }
    }

    private boolean isDaytime(World world) {
        long time = world.getTime();
        return !world.hasStorm() && !world.isThundering() && (time < 12300 || time > 23850);
    }

    @Override
    public void preRegister() {
        super.preRegister();

        // This prevents Players from toggling the Daylight sensor
        BlockUseHandler handler = PlayerRightClickEvent::cancel;
        addItemHandler(handler);
    }

        private boolean scanForCommandHub (@Nonnull Block block){
            BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

            for (BlockFace face : faces) {
                Block relativeBlock = block.getRelative(face);


                if (BlockStorage.check(relativeBlock, BaseItems.command_hub_.getItemId())) {
                    return true;
                }
            }
            return false;

        }

        @Nonnull
        private BlockPlaceHandler onPlace() {

        return new BlockPlaceHandler(true) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                if (scanForCommandHub(e.getBlock())) {
                    commandHubFound = true;
                } else {
                    commandHubFound = false;
                }
            }

        };
    }

}