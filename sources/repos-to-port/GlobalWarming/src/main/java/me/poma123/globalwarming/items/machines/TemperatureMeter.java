package me.poma123.globalwarming.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.HologramOwner;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import com.github.drakescraft-labs.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import dev.drake.dough.common.ChatColors;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.poma123.globalwarming.api.TemperatureType;

public abstract class TemperatureMeter extends SlimefunItem implements HologramOwner {

    @ParametersAreNonnullByDefault
    protected TemperatureMeter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                removeHologram(b);
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo(b,"type", TemperatureType.CELSIUS.name());
                updateHologram(b, "&7Measuring...");
            }
        };
    }

    @Nonnull
    private BlockUseHandler onRightClick() {
        return e -> {
            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();

            TemperatureType saved = TemperatureType.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "type"));

            if (saved == TemperatureType.CELSIUS) {
                saved = TemperatureType.FAHRENHEIT;
            } else if (saved == TemperatureType.FAHRENHEIT) {
                saved = TemperatureType.KELVIN;
            } else {
                saved = TemperatureType.CELSIUS;
            }

            BlockStorage.addBlockInfo(b, "type", saved.name());
            p.sendMessage(ChatColors.color("&7Temperature type: &e" + saved.getName()));

            e.cancel();
        };
    }

    @Override
    public void preRegister() {
        addItemHandler(onBreak());
        addItemHandler(onPlace());
        addItemHandler(onRightClick());
        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                TemperatureMeter.this.tick(b);
            }
        });
    }

    public void tick(@Nonnull Block b) {
    }
}
