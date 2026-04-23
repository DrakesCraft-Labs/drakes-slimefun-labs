package io.github.addoncommunity.slimechem.implementation.attributes;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;

import javax.annotation.Nonnull;

/**
 * This interface marks classes that have a {@link SlimefunItemStack} representation
 *
 * @author Seggan
 */
public interface Itemable {

    @Nonnull
    SlimefunItemStack getItem();
}
