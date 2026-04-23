package me.voper.slimeframe.core.attributes;

import org.bukkit.entity.EntityType;

import com.github.drakescraft-labs.slimefun4.core.attributes.RandomMobDrop;

/**
 * An extension of the {@link RandomMobDrop} interface that allows for more flexibility
 *
 * @author VoperAD
 * @see RandomMobDrop
 */
public interface AdvancedMobDrop extends RandomMobDrop {
    @Override
    default int getMobDropChance() {
        return 0;
    }

    int getMobDropChance(EntityType entity);
}
