package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.machine;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.core.machines.MachineOperation;
import com.github.drakescraft-labs.slimefun4.core.machines.MachineProcessor;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.machine.CustomMachine;

/**
 * This data class provides access to machine data for JavaScript integration.
 *
 * @param blockMenu the menu of the machine, may be null if the machine has no GUI.
 * @param data the persistent data of the machine.
 * @param machineItem the {@link SlimefunItem} instance of the machine.
 * @param block the physical {@link Block} instance of the machine.
 * @param processor the {@link MachineProcessor} handling the machine's logic.
 * @param operation the current {@link MachineOperation}, ALWAYS be null so far...
 * @param machine the {@link CustomMachine} instance defining the machine's behavior.
 * @author lijinhong11
 *
 * <h3>JavaScript Usage Example:</h3>
 * <pre>{@code
 * function tick(info) {
 *     // May be null
 *     var menu = info.blockMenu();
 *     // Access Java fields in JavaScript should like this:
 *     // var yourObjectName = info.<recordName>();
 *     // For example:
 *     // var block = info.block();
 *
 *     if (menu !== null) {
 *         // See Slimefun API for more details
 *         var item = blockMenu.getItemInSlot(0);
 *     }
 *
 *     // Displayed name
 *     var itemName = info.machineItem().getItemName();
 * }
 * }</pre>
 */
public record MachineInfo(
        @Nullable BlockMenu blockMenu,
        Config data,
        SlimefunItem machineItem,
        Block block,
        MachineProcessor<?> processor,
        MachineOperation operation,
        CustomMachine machine) {}
