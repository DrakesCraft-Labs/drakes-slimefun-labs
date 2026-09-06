/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package me.poma123.globalwarming.eventos;

import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import me.poma123.globalwarming.eventos.RegistroClimatizadores;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

final class Nevada {
    private static final int RADIO = 12;

    private Nevada() {
    }

    static void copos(@Nonnull Player p, int intentos) {
        World mundo = p.getWorld();
        Location centro = p.getLocation();
        ThreadLocalRandom azar = ThreadLocalRandom.current();
        for (int i = 0; i < intentos; ++i) {
            Block encima;
            Block suelo;
            Block hueco;
            int z;
            int x = centro.getBlockX() + azar.nextInt(-12, 13);
            if (!mundo.isChunkLoaded(x >> 4, (z = centro.getBlockZ() + azar.nextInt(-12, 13)) >> 4) || !(hueco = (suelo = (encima = mundo.getHighestBlockAt(x, z)).getType().isAir() ? encima.getRelative(0, -1, 0) : encima).getRelative(0, 1, 0)).getType().isAir() || !Nevada.esSueloValido(suelo.getType()) || mundo.getHighestBlockYAt(x, z) > suelo.getY() + 1 || RegistroClimatizadores.estaCubierto(hueco.getLocation())) continue;
            hueco.setType(Material.SNOW, false);
        }
    }

    private static boolean esSueloValido(@Nonnull Material tipo) {
        switch (tipo) {
            case GRASS_BLOCK: 
            case DIRT: 
            case COARSE_DIRT: 
            case ROOTED_DIRT: 
            case PODZOL: 
            case MYCELIUM: 
            case SAND: 
            case RED_SAND: 
            case GRAVEL: 
            case CLAY: 
            case MUD: 
            case MOSS_BLOCK: 
            case SNOW_BLOCK: 
            case POWDER_SNOW: {
                return true;
            }
        }
        return false;
    }
}

