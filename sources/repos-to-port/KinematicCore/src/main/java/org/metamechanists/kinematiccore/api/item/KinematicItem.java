package org.metamechanists.kinematiccore.api.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.addon.KinematicAddon;

import java.util.List;


public abstract class KinematicItem {
    private final String id;
    private final Class<? extends KinematicItem> kinematicClass;
    private final ItemStack stack;

    protected KinematicItem(
            @NotNull String id,
            @NotNull Class<? extends KinematicAddon> addonClass,
            @NotNull Class<? extends KinematicItem> kinematicClass,
            @NotNull ItemStack stack
    ) {
        this.id = addonClass.getSimpleName().toLowerCase() +":" + id.toLowerCase();
        this.kinematicClass = kinematicClass;
        this.stack = stack;
        stack.editMeta(meta -> meta.setPlaceableKeys(List.of()));
        stack.editMeta(meta -> meta.getPersistentDataContainer()
                .set(new NamespacedKey(KinematicCore.instance(), "id"), PersistentDataType.STRING, id));
    }

    public String id() {
        return id;
    }
}
