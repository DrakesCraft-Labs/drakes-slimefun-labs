package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Nombre de paquete compatible con plugins que siguen escuchando el API clásico de Slimefun
 * (p. ej. AuraSkills). Delegación sobre el evento real del fork Drake.
 *
 * @author TheBusyBiscuit / compat fork
 */
public class BlockPlacerPlaceEvent extends com.github.drakescraft_labs.slimefun4.api.events.BlockPlacerPlaceEvent {

    @ParametersAreNonnullByDefault
    public BlockPlacerPlaceEvent(Block blockPlacer, ItemStack placedItem, Block block) {
        super(blockPlacer, placedItem, block);
    }
}
