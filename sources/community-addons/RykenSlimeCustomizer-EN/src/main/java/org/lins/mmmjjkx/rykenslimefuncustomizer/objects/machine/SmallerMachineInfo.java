package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.machine;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft-labs.slimefun4.core.machines.MachineProcessor;
import javax.annotation.Nullable;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft-labs.slimefun4.legacy.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.lins.mmmjjkx.rykenslimefuncustomizer.objects.customs.machine.CustomNoEnergyMachine;

public record SmallerMachineInfo(
        @Nullable BlockMenu blockMenu,
        Config data,
        CustomNoEnergyMachine machine,
        SlimefunItem machineItem,
        Block block,
        MachineProcessor<?> processor) {}
