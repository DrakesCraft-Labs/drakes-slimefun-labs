package io.github.sefiraat.slimetinker.items.workstations.smeltery;

import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;

public abstract class AbstractCache {

    protected final BlockMenu blockMenu;

    protected AbstractCache(BlockMenu blockMenu) {
        this.blockMenu = blockMenu;
    }
}
