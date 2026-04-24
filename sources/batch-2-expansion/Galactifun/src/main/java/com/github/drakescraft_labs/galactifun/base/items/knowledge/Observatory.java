package com.github.drakescraft_labs.galactifun.base.items.knowledge;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.galactifun.Galactifun;
import com.github.drakescraft_labs.galactifun.api.worlds.PlanetaryWorld;
import com.github.drakescraft_labs.galactifun.core.CoreItemGroup;
import com.github.drakescraft_labs.galactifun.core.WorldSelector;
import com.github.drakescraft_labs.infinitylib.common.Scheduler;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.multiblocks.MultiBlockMachine;
import dev.drake.dough.data.persistent.PersistentDataAPI;

public final class Observatory extends MultiBlockMachine {

    public Observatory(SlimefunItemStack item, ItemStack[] recipe) {
        super(CoreItemGroup.MACHINES, item, recipe, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        NamespacedKey key = Galactifun.createKey("discovering_" + p.getUniqueId());

        PlanetaryWorld world = Galactifun.worldManager().getWorld(p.getWorld());
        if (world == null) {
            p.sendMessage(ChatColor.RED + "You must be on a planet to use this!");
            return;
        }

        if (PersistentDataAPI.getBoolean(world.worldStorage(), key)) {
            p.sendMessage(ChatColor.RED + "Already observing!");
            return;
        }

        new WorldSelector((pl, w, l) -> {
            if (w instanceof PlanetaryWorld pw) {
                if (KnowledgeLevel.get(pl, pw) == KnowledgeLevel.ADVANCED) return false;
                return world.distanceTo(w) <= 0.25;
            }
            return true;
        }, (pl, w) -> {
            pl.sendMessage(ChatColor.GREEN + "Observing planet " + w.name());
            PersistentDataAPI.setBoolean(world.worldStorage(), key, true);
            Scheduler.run(30 * 60 * 20, () -> {
                PersistentDataAPI.setBoolean(world.worldStorage(), key, false);
                KnowledgeLevel.BASIC.set(pl, w);
            });
        }).open(p);
    }

}
