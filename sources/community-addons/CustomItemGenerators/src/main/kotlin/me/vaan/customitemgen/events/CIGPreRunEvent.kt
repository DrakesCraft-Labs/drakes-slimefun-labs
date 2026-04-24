package com.github.drakescraft_labs.customitemgen.events

import com.github.drakescraft_labs.customitemgen.data.SFMachine
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CIGPreRunEvent(val machine: SFMachine, val validatorResult: Boolean) : Event(), Cancellable {

    private var cancelled = false

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    override fun isCancelled(): Boolean = cancelled

    override fun setCancelled(p0: Boolean) {
        cancelled = p0
    }
}