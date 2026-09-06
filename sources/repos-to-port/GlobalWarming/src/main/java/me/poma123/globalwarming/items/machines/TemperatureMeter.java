/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
 *  com.github.drakescraft_labs.slimefun4.api.items.ItemHandler
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
 *  com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
 *  com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
 *  com.github.drakescraft_labs.slimefun4.core.attributes.HologramOwner
 *  com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler
 *  com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler
 *  com.github.drakescraft_labs.slimefun4.core.handlers.BlockUseHandler
 *  com.github.drakescraft_labs.slimefun4.implementation.handlers.SimpleBlockBreakHandler
 *  com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker
 *  com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
 *  com.github.drakescraft_labs.slimefun4.libraries.dough.common.ChatColors
 *  javax.annotation.Nonnull
 *  javax.annotation.ParametersAreNonnullByDefault
 *  me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.poma123.globalwarming.items.machines;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.ItemHandler;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.HologramOwner;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockUseHandler;
import com.github.drakescraft_labs.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage;
import com.github.drakescraft_labs.slimefun4.libraries.dough.common.ChatColors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.poma123.globalwarming.api.TemperatureType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public abstract class TemperatureMeter
extends SlimefunItem
implements HologramOwner {
    @ParametersAreNonnullByDefault
    protected TemperatureMeter(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler(){

            public void onBlockBreak(@Nonnull Block b) {
                TemperatureMeter.this.removeHologram(b);
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false){

            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlockPlaced();
                BlockStorage.addBlockInfo((Block)b, (String)"type", (String)TemperatureType.CELSIUS.name());
                TemperatureMeter.this.updateHologram(b, "&7Midiendo...");
            }
        };
    }

    @Nonnull
    private BlockUseHandler onRightClick() {
        return e -> {
            Player p = e.getPlayer();
            Block b = (Block)e.getClickedBlock().get();
            TemperatureType saved = TemperatureType.valueOf(BlockStorage.getLocationInfo((Location)b.getLocation(), (String)"type"));
            saved = saved == TemperatureType.CELSIUS ? TemperatureType.FAHRENHEIT : (saved == TemperatureType.FAHRENHEIT ? TemperatureType.KELVIN : TemperatureType.CELSIUS);
            BlockStorage.addBlockInfo((Block)b, (String)"type", (String)saved.name());
            p.sendMessage(ChatColors.color((String)("&7Unidad: &e" + saved.getName())));
            e.cancel();
        };
    }

    public void preRegister() {
        this.addItemHandler(new ItemHandler[]{this.onBreak()});
        this.addItemHandler(new ItemHandler[]{this.onPlace()});
        this.addItemHandler(new ItemHandler[]{this.onRightClick()});
        this.addItemHandler(new ItemHandler[]{new BlockTicker(){

            public boolean isSynchronized() {
                return false;
            }

            public void tick(Block b, SlimefunItem item, Config data) {
                TemperatureMeter.this.tick(b);
            }
        }});
    }

    public void tick(@Nonnull Block b) {
    }
}

