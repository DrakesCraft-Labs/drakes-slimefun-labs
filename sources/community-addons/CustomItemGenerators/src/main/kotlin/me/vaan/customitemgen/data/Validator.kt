package com.github.drakescraft_labs.customitemgen.data

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import org.bukkit.block.Block

typealias SFMachine = Pair<SlimefunItem, Block>
typealias Validator = (SFMachine) -> Boolean

operator fun <K> HashMap<K, Validator>.get(key: K, arg: SFMachine) : Boolean {
    val validator = this[key] ?: return true
    return validator.invoke(arg)
}

fun <K> Map<K, Validator>.validate(arg: SFMachine) : Boolean {
    val validated = this.values.map { it.invoke(arg) }
    return validated.all { it }
}