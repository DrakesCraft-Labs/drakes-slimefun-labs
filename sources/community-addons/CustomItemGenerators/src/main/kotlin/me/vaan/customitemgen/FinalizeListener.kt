package com.github.drakescraft_labs.customitemgen

import com.github.drakescraft_labs.slimefun4.api.events.SlimefunItemRegistryFinalizedEvent
import com.github.drakescraft_labs.customitemgen.file.MachineLoader
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object FinalizeListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun registerStuff(e: SlimefunItemRegistryFinalizedEvent) {
        val plugin = CustomItemGenerators.instance
        val machines = plugin.genFile("machines.yml")

        plugin.server.consoleSender.sendMessage("§aEnabling CustomItemGenerators!")
        MachineLoader.load(machines)
    }
}