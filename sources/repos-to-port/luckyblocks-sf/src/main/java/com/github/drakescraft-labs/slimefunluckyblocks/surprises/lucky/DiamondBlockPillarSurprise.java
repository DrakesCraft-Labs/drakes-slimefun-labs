package com.github.drakescraft-labs.slimefunluckyblocks.surprises.lucky;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.drakescraft-labs.slimefun4.utils.ColoredMaterial;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.LuckLevel;
import com.github.drakescraft-labs.slimefunluckyblocks.surprises.Surprise;

public final class DiamondBlockPillarSurprise implements Surprise {

    @Override
    public String getName() {
        return "Stained Clay Pillar with Diamond Block on top";
    }

    @Override
    public void activate(Random random, Player p, Location l) {
        for (int i = 0; i < 8; i++) {
            l.getWorld().spawnFallingBlock(l.clone().add(0.5, (i + 1) * 4.0, 0.5), ColoredMaterial.TERRACOTTA.get(i).createBlockData());
        }

        l.getWorld().spawnFallingBlock(l.clone().add(0.5, 9 * 4.0, 0.5), Material.DIAMOND_BLOCK.createBlockData());
    }

    @Override
    public LuckLevel getLuckLevel() {
        return LuckLevel.LUCKY;
    }

}
