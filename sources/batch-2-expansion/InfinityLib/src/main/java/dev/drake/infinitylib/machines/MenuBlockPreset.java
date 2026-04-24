package dev.drake.infinitylib.machines;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.drakescraft_labs.slimefun4.implementation.Slimefun;
import dev.drake.dough.protection.Interaction;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset;
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu;
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow;
import java.lang.reflect.Method;

@ParametersAreNonnullByDefault
final class MenuBlockPreset extends BlockMenuPreset {

    private final MenuBlock menuBlock;

    MenuBlockPreset(MenuBlock menuBlock) {
        super(menuBlock.getId(), menuBlock.getItemName());
        this.menuBlock = menuBlock;
        menuBlock.setup(this);
    }

    @Override
    public void newInstance(BlockMenu menu, Block b) {
        menuBlock.onNewInstance(menu, b);
    }

    @Override
    public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
        return menuBlock.getTransportSlots(menu, flow, item);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean canOpen(Block b, Player p) {
        return hasPermissionCompat(p, b, Interaction.INTERACT_BLOCK) && menuBlock.canUse(p, false);
    }

    @Override
    public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
        return new int[0];
    }

    @SuppressWarnings("unchecked")
    private static boolean hasPermissionCompat(Player player, Block block, Interaction interaction) {
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
