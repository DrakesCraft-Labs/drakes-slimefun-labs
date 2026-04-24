package net.guizhanss.ultimategenerators2.core.recipes

import org.bukkit.inventory.ItemStack

/**
 * A [MachineRecipe] but only has one output.
 */
open class SingleOutputMachineRecipe(ticks: Int, input: Array<ItemStack>, output: ItemStack) :
    MachineRecipe(ticks, input, arrayOf(output))
