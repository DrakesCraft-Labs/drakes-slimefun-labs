package com.github.drakescraft_labs.customitemgen.data

import org.bukkit.inventory.ItemStack

data class Options(
    val entryRandomizer: Boolean = false,
    val progressBar: ItemStack,
    val validators: MutableMap<String, Validator>
)
