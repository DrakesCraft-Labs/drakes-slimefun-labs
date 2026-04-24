package net.guizhanss.ultimategenerators2.core.recipes

import org.bukkit.inventory.ItemStack

/**
 * A [MachineRecipe] but only has one input and one output.
 */
class SingleMachineRecipe(ticks: Int, input: ItemStack, output: ItemStack) :
    SingleOutputMachineRecipe(ticks, arrayOf(input), output)
