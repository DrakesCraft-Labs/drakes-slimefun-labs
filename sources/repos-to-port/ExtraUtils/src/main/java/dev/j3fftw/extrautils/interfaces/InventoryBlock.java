package dev.j3fftw.extrautils.interfaces;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;

/**
 *
 * Use this till it gets reworked in Slimefun.
 *
 * @author TheBusyBiscuit
 */
public interface InventoryBlock {

    /**
     * This method returns an {@link Array} of slots that serve as the input
     * for the {@link Inventory} of this block.
     *
     * @return The input slots for the {@link Inventory} of this block
     */
    int[] getInputSlots();

    /**
     * This method returns an {@link Array} of slots that serve as the output
     * for the {@link Inventory} of this block.
     *
     * @return The output slots for the {@link Inventory} of this block
     */
    int[] getOutputSlots();

    default void createPreset(SlimefunItem item, Consumer<BlockMenuPreset> setup) {
        createPreset(item, item.getItemName(), setup);
    }

    default void createPreset(SlimefunItem item, String title, Consumer<BlockMenuPreset> setup) {
        new BlockMenuPreset(item.getId(), title) {

            @Override
            public void init() {
                setup.accept(this);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                } else {
                    return getOutputSlots();
                }
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass")
                    || (hasPermissionCompat(player, block, Interaction.INTERACT_BLOCK)
                    && SlimefunUtils.canPlayerUseItem(player, item.getItem(), false)
                );
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static boolean hasPermissionCompat(@Nonnull Player player, @Nonnull Block block, @Nonnull Interaction interaction) {
        Object manager = Slimefun.getProtectionManager();
        for (Method method : manager.getClass().getMethods()) {
            if (!method.getName().equals("hasPermission") || method.getParameterCount() != 3) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (!parameterTypes[0].isAssignableFrom(player.getClass())
                    || !parameterTypes[1].isAssignableFrom(block.getLocation().getClass())
                    || !parameterTypes[2].isEnum()) {
                continue;
            }
            try {
                Object mappedInteraction = Enum.valueOf((Class<? extends Enum>) parameterTypes[2], interaction.name());
                Object result = method.invoke(manager, player, block.getLocation(), mappedInteraction);
                return result instanceof Boolean && (Boolean) result;
            } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                // try next overload
            }
        }
        return false;
    }

}
