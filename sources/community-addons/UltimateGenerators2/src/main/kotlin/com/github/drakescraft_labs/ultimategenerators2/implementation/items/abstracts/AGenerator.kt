@file:Suppress("DEPRECATION")

package net.guizhanss.ultimategenerators2.implementation.items.abstracts

import com.google.common.base.Preconditions
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.ItemState
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetProvider
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineProcessHolder
import com.github.drakescraft_labs.slimefun4.core.machines.MachineProcessor
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems
import com.github.drakescraft_labs.slimefun4.implementation.operations.FuelOperation
import com.github.drakescraft_labs.slimefun4.libraries.dough.items.CustomItemStack
import com.github.drakescraft_labs.slimefun4.utils.NumberUtils
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import com.github.drakescraft_labs.slimefun4.utils.itemstack.ItemStackWrapper
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu
import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil
import net.guizhanss.ultimategenerators2.core.recipes.MachineFuel
import net.guizhanss.ultimategenerators2.implementation.UGItems
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * The basic class of a generator.
 */
abstract class AGenerator(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AHopper(itemGroup, item, recipeType, recipe), EnergyNetProvider, MachineProcessHolder<FuelOperation> {
    @JvmField
    protected val fuelTypes: MutableSet<MachineFuel> = HashSet()

    @JvmField
    protected val processor: MachineProcessor<FuelOperation> = MachineProcessor(this)

    private var _energyCapacity = -1
    private var _energyProduction = -1

    @get:JvmName("getEnergyCapacity")
    val capacity: Int get() = _energyCapacity
    val energyProduction: Int get() = _energyProduction

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

    protected abstract fun registerDefaultFuelTypes()

    protected fun registerFuel(ticks: Int, fuel: ItemStack, output: ItemStack? = null) {
        fuelTypes.add(MachineFuel(ticks, fuel, output))
    }

    override fun getDisplayRecipes(): List<ItemStack> {
        val list: MutableList<ItemStack> = ArrayList()
        for (fuel in fuelTypes) {
            val item: ItemStack = CustomItemStack(fuel.input.clone()) { im: ItemMeta ->
                val lore: List<String> = listOf(
                    "&8\u21E8 &7\u23F2 ${NumberUtils.getTimeLeft(fuel.ticks / 2)}",
                    "&8\u21E8 &e\u26A1 &7${energyProduction} J/t",
                    "&8\u21E8 &e\u26A1 &7${NumberUtils.getCompactDouble(fuel.ticks.toDouble() * energyProduction)} J in total"
                )
                im.lore = ChatUtil.color(lore)
            }
            list.add(item)
        }
        return list
    }

    fun setCapacity(capacity: Int): AGenerator {
        Preconditions.checkArgument(capacity > 0, "Capacity must be greater than 0")

        if (state == ItemState.UNREGISTERED) {
            this._energyCapacity = capacity
            return this
        } else {
            error("You cannot modify the capacity after the Item was registered.")
        }
    }

    fun setEnergyProduction(energyProduction: Int): AGenerator {
        Preconditions.checkArgument(energyProduction > 0, "Energy production must be greater than 0")

        if (state == ItemState.UNREGISTERED) {
            this._energyProduction = energyProduction
            return this
        } else {
            error("You cannot modify the energy production after the Item was registered.")
        }
    }

    override fun getGeneratedOutput(l: Location, data: Config): Int {
        val inv = BlockStorage.getInventory(l)
        val operation = processor.getOperation(l)

        if (operation != null) {
            if (!operation.isFinished) {
                processor.updateProgressBar(inv, STATUS_SLOT, operation)

                if (isChargeable) {
                    val charge = getCharge(l, data)

                    if (capacity - charge >= energyProduction) {
                        operation.addProgress(1)
                        return energyProduction
                    }

                    return 0
                } else {
                    operation.addProgress(1)
                    return energyProduction
                }
            } else {
                val fuel = operation.ingredient

                if (isBucket(fuel)) {
                    inv.pushItem(ItemStack(Material.BUCKET), *outputSlots)
                }

                inv.replaceExistingItem(STATUS_SLOT, NOT_WORKING_ITEM)

                processor.endOperation(l)
                return 0
            }
        } else {
            val found: MutableMap<Int, Int> = HashMap()
            val fuel = findRecipe(inv, found)

            if (fuel != null) {
                for ((key, value) in found) {
                    inv.consumeItem(key, value)
                }

                processor.startOperation(l, FuelOperation(fuel.toSf()))
            }

            return 0
        }
    }

    private fun isBucket(item: ItemStack?): Boolean {
        if (item == null) {
            return false
        }

        val wrapper = ItemStackWrapper.wrap(item)
        return (item.type == Material.LAVA_BUCKET ||
            SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.FUEL_BUCKET, false)
            || SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.OIL_BUCKET, false)
            || SlimefunUtils.isItemSimilar(wrapper, UGItems.BIOFUEL_BUCKET, false)
            || SlimefunUtils.isItemSimilar(wrapper, UGItems.BIOMASS_BUCKET, false)
            || SlimefunUtils.isItemSimilar(wrapper, UGItems.DIESEL_BUCKET, false))
    }

    protected fun findRecipe(menu: BlockMenu, found: MutableMap<Int, Int>): MachineFuel? {
        for (fuel in fuelTypes) {
            for (slot in inputSlots) {
                if (fuel.test(menu.getItemInSlot(slot))) {
                    found[slot] = fuel.input.amount
                    return fuel
                }
            }
        }

        return null
    }

    override fun register(addon: SlimefunAddon) {
        Preconditions.checkArgument(_energyCapacity > 0, "Energy capacity must be greater than 0")
        Preconditions.checkArgument(_energyProduction > 0, "Energy production must be greater than 0")
        Preconditions.checkArgument(
            _energyCapacity >= energyProduction,
            "Energy capacity must be greater than or equal to energy production"
        )
        super.register(addon)
        registerDefaultFuelTypes()
    }

    override fun tick(block: Block, menu: BlockMenu) {
        // generator ticker is handled in EnergyNet, no need to do anything here.
    }
}
