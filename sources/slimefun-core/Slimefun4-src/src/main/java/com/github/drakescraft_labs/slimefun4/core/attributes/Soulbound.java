package com.github.drakescraft_labs.slimefun4.core.attributes;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.items.magical.SoulboundItem;

/**
 * This Interface, when attached to a class that inherits from {@link SlimefunItem}, marks
 * the Item as soulbound.
 * This Item will then not be dropped upon death.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SoulboundItem
 *
 */
public interface Soulbound extends ItemAttribute {

}
