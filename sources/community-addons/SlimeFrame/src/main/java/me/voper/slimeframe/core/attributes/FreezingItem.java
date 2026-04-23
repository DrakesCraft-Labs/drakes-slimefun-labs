package me.voper.slimeframe.core.attributes;

/**
 * Every {@link com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem} that implements this interface
 * will cause a freezing effect a specified amount of damage
 * per second defined by the {@link FreezingItem#getFreezingDamage()} method
 *
 * @author VoperAD
 */
public interface FreezingItem {
    double getFreezingDamage();
}
