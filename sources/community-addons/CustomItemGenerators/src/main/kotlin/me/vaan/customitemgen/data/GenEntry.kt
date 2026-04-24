package com.github.drakescraft_labs.customitemgen.data

import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe

data class GenEntry(
    val recipe: MachineRecipe,
    val energy: Int
)
