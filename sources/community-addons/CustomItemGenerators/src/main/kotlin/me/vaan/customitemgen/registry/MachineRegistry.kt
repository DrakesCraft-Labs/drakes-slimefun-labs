package com.github.drakescraft_labs.customitemgen.registry

import com.github.drakescraft_labs.customitemgen.generator.ItemGenerator

/**
 * Map id to slimefun item generator
 */
object MachineRegistry : MutableMap<String, ItemGenerator> by HashMap()