package net.guizhanss.ultimategenerators2.core.recipes

import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import com.github.drakescraft_labs.slimefun4.utils.itemstack.ItemStackWrapper
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineFuel as SfMachineFuel

/**
 * Just a wrapper for Slimefun's [SfMachineFuel].
 */
class MachineFuel(
    val ticks: Int,
    val input: ItemStack,
    val output: ItemStack?
) : Predicate<ItemStack?> {
    private val wrapper = ItemStackWrapper.wrap(input)

    override fun test(item: ItemStack?) = SlimefunUtils.isItemSimilar(item, wrapper, true)

    fun toSf() = SfMachineFuel(ticks / 2, input, output)
}
