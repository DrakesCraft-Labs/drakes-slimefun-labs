@file:Suppress("DEPRECATION")

package net.guizhanss.ultimategenerators2.implementation.items.abstracts

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset
import net.guizhanss.guizhanlib.slimefun.machines.TickingMenuBlock
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * A basic ticking block that the GUI looks like a hopper.
 */
abstract class AHopper protected constructor(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : TickingMenuBlock(itemGroup, item, recipeType, recipe), InventoryBlock, RecipeDisplayItem {
    companion object {
        // slots
        private val BACKGROUND_SLOTS = intArrayOf(18, 19, 20, 21, 23, 24, 25, 26)
        private val INPUT_BORDER = intArrayOf(0, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
        private val OUTPUT_BORDER = intArrayOf(27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 44)
        private val INPUT_SLOTS = intArrayOf(1, 2, 3, 4, 5, 6, 7)
        private val OUTPUT_SLOTS = intArrayOf(37, 38, 39, 40, 41, 42, 43)

        @JvmStatic
        protected val STATUS_SLOT = 22

        // items
        @JvmStatic
        protected val NOT_WORKING_ITEM = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
    }

    override fun setup(preset: BlockMenuPreset) {
        for (slot in BACKGROUND_SLOTS) {
            preset.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler())
        }
        for (slot in INPUT_BORDER) {
            preset.addItem(slot, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler())
        }
        for (slot in OUTPUT_BORDER) {
            preset.addItem(slot, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler())
        }
        preset.addItem(STATUS_SLOT, NOT_WORKING_ITEM, ChestMenuUtils.getEmptyClickHandler())
    }

    override fun getInputSlots() = INPUT_SLOTS

    override fun getOutputSlots() = OUTPUT_SLOTS
}
