package com.github.drakescraft-labs.slimefun4.implementation.items.androids;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

@FunctionalInterface
interface AndroidAction {

    void perform(ProgrammableAndroid android, Block b, BlockMenu inventory, BlockFace face);

}
