package me.voper.slimeframe.implementation.items.resources;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.core.attributes.NotPlaceable;

import me.voper.slimeframe.core.attributes.FreezingItem;
import me.voper.slimeframe.implementation.SFrameStacks;
import me.voper.slimeframe.implementation.groups.Groups;
import me.voper.slimeframe.implementation.items.machines.CryoticExtractor;
import me.voper.slimeframe.utils.Utils;

public class Cryotic extends SlimefunItem implements NotPlaceable, FreezingItem {

    public Cryotic() {
        super(Groups.RESOURCES, SFrameStacks.CRYOTIC, CryoticExtractor.RECIPE_TYPE, Utils.NULL_ITEMS_ARRAY);
        this.disenchantable = false;
        this.enchantable = false;
    }

    @Override
    public double getFreezingDamage() {
        return 3;
    }
}
