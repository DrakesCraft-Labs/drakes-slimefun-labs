package com.github.drakescraft-labs.slimefun4.implementation.items.cargo;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.api.events.PlayerRightClickEvent;
import com.github.drakescraft-labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft-labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft-labs.slimefun4.core.attributes.HologramOwner;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft-labs.slimefun4.core.handlers.BlockUseHandler;
import com.github.drakescraft-labs.slimefun4.core.networks.cargo.CargoNet;
import com.github.drakescraft-labs.slimefun4.implementation.handlers.SimpleBlockBreakHandler;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import com.github.drakescraft-labs.slimefun4.legacy.api.BlockStorage;

public class CargoManager extends SlimefunItem implements HologramOwner {

    @ParametersAreNonnullByDefault
    public CargoManager(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onBreak());
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

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                CargoNet.getNetworkFromLocationOrCreate(b.getLocation()).tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }

        }, new BlockUseHandler() {

            @Override
            public void onRightClick(PlayerRightClickEvent e) {
                Optional<Block> block = e.getClickedBlock();

                if (block.isPresent()) {
                    Player p = e.getPlayer();
                    Block b = block.get();

                    if (BlockStorage.getLocationInfo(b.getLocation(), "visualizer") == null) {
                        BlockStorage.addBlockInfo(b, "visualizer", "disabled");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&4\u2718"));
                    } else {
                        BlockStorage.addBlockInfo(b, "visualizer", null);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&2\u2714"));
                    }
                }
            }
        });
    }

}
