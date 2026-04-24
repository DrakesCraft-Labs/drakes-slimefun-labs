package net.guizhanss.ultimategenerators2.implementation.items.abstracts

import com.google.common.base.Preconditions
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.ItemState
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineProcessHolder
import com.github.drakescraft_labs.slimefun4.core.machines.MachineProcessor
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType
import com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation
import dev.drake.dough.inventory.InvUtils
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import com.github.drakescraft_labs.slimefun4.utils.itemstack.ItemStackWrapper
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu
import net.guizhanss.ultimategenerators2.core.recipes.MachineRecipe
import net.guizhanss.ultimategenerators2.core.recipes.SingleMachineRecipe
import net.guizhanss.ultimategenerators2.core.recipes.SingleOutputMachineRecipe
import net.guizhanss.ultimategenerators2.utils.BlockUtils
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.max

/**
 * The basic class of a machine.
 */
abstract class AMachine(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AHopper(itemGroup, item, recipeType, recipe), EnergyNetComponent, MachineProcessHolder<CraftingOperation> {
    @JvmField
    protected val recipes: MutableList<MachineRecipe> = ArrayList()

    @JvmField
    protected val processor: MachineProcessor<CraftingOperation> = MachineProcessor(this)

    private var _energyCapacity = -1
    private var _energyConsumption = -1

    @get:JvmName("getEnergyCapacity")
    val capacity: Int get() = _energyCapacity
    val energyConsumption: Int get() = _energyConsumption

    protected abstract val progressBar: ItemStack

    init {
        val lazyProgressBar by lazy { progressBar }
        processor.progressBar = lazyProgressBar
    }

    override fun getCapacity() = capacity

    override fun getMachineProcessor() = processor

    override fun onBreak(e: BlockBreakEvent, menu: BlockMenu) {
        super.onBreak(e, menu)
        processor.endOperation(menu.location)
    }

    override fun getEnergyComponentType() = EnergyNetComponentType.CONSUMER

    fun setCapacity(capacity: Int): AMachine {
        Preconditions.checkArgument(capacity > 0, "Capacity must be greater than 0")

        if (state == ItemState.UNREGISTERED) {
            this._energyCapacity = capacity
            return this
        } else {
            error("You cannot modify the capacity after the Item was registered.")
        }
    }

    fun setEnergyConsumption(energyConsumption: Int): AMachine {
        Preconditions.checkArgument(energyConsumption > 0, "Energy consumption must be greater than 0")

        if (state == ItemState.UNREGISTERED) {
            this._energyConsumption = energyConsumption
            return this
        } else {
            error("You cannot modify the energy consumption after the Item was registered.")
        }
    }

    protected abstract fun registerDefaultRecipes()

    override fun getDisplayRecipes(): List<ItemStack> {
        val list: MutableList<ItemStack> = ArrayList()
        for (recipe in recipes) {
            val length = max(recipe.input.size, recipe.output.size)
            val inputs: Array<ItemStack?> = recipe.input.copyOf(length)
            val outputs: Array<ItemStack?> = recipe.output.copyOf(length)
            for (i in 0 until length) {
                list.add(inputs[i] ?: ItemStack(Material.AIR))
                list.add(outputs[i] ?: ItemStack(Material.AIR))
            }
        }
        return list
    }

    protected fun registerRecipe(ticks: Int, inputs: Array<ItemStack>, outputs: Array<ItemStack>) {
        recipes.add(MachineRecipe(ticks, inputs, outputs))
    }

    protected fun registerRecipe(ticks: Int, inputs: Array<ItemStack>, output: ItemStack) {
        recipes.add(SingleOutputMachineRecipe(ticks, inputs, output))
    }

    protected fun registerRecipe(ticks: Int, input: ItemStack, output: ItemStack) {
        recipes.add(SingleMachineRecipe(ticks, input, output))
    }

    override fun tick(b: Block, menu: BlockMenu) {
        val inv: BlockMenu = BlockStorage.getInventory(b)
        val currentOperation: CraftingOperation? = processor.getOperation(b)

        if (currentOperation != null) {
            if (BlockUtils.takeCharge(this, b.location, energyConsumption)) {
                if (!currentOperation.isFinished) {
                    processor.updateProgressBar(inv, STATUS_SLOT, currentOperation)
                    currentOperation.addProgress(1)
                } else {
                    inv.replaceExistingItem(STATUS_SLOT, NOT_WORKING_ITEM)

                    for (output in currentOperation.results) {
                        inv.pushItem(output.clone(), *outputSlots)
                    }

                    processor.endOperation(b)
                }
            }
        } else {
            val next = findNextRecipe(inv)

            if (next != null) {
                processor.startOperation(b, CraftingOperation(next.toSf()))
            }
        }
    }

    protected fun findNextRecipe(inv: BlockMenu): MachineRecipe? {
        val inventory: MutableMap<Int, ItemStack> = HashMap()

        for (slot in inputSlots) {
            val item: ItemStack? = inv.getItemInSlot(slot)

            if (item != null) {
                inventory[slot] = ItemStackWrapper.wrap(item)
            }
        }

        val found: MutableMap<Int, Int> = HashMap()

        for (recipe in recipes) {
            for (input in recipe.input) {
                for (slot in inputSlots) {
                    if (SlimefunUtils.isItemSimilar(inventory[slot], input, false)) {
                        found[slot] = input.amount
                        break
                    }
                }
            }

            if (found.size == recipe.input.size) {
                if (!InvUtils.fitAll(inv.toInventory(), recipe.output, *outputSlots)) {
                    return null
                }

                for ((key, value) in found) {
                    inv.consumeItem(key, value)
                }

                return recipe
            } else {
                found.clear()
            }
        }

        return null
    }

    override fun register(addon: SlimefunAddon) {
        Preconditions.checkArgument(_energyCapacity > 0, "Energy capacity must be greater than 0")
        Preconditions.checkArgument(_energyConsumption > 0, "Energy consumption must be greater than 0")
        Preconditions.checkArgument(
            _energyConsumption <= _energyCapacity,
            "Energy consumption must be less than or equal to the energy capacity"
        )
        super.register(addon)
        registerDefaultRecipes()
    }
}
