@file:Suppress("DEPRECATION")

package net.guizhanss.ultimategenerators2.implementation.items.abstracts

import com.google.common.base.Preconditions
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.ItemState
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType
import com.github.drakescraft_labs.slimefun4.implementation.items.SimpleSlimefunItem
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

/**
 * A ticking machine that does not have gui.
 */
abstract class SimpleTickingMachine(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : SimpleSlimefunItem<BlockTicker>(itemGroup, item, recipeType, recipe), EnergyNetComponent {
    private var _energyCapacity = -1
    private var _energyConsumption = -1

    @get:JvmName("getEnergyCapacity")
    val capacity: Int get() = _energyCapacity
    val energyConsumption: Int get() = _energyConsumption
    open var isSync = false

    override fun getItemHandler() = object : BlockTicker() {
        override fun tick(b: Block, item: SlimefunItem, data: Config) {
            this@SimpleTickingMachine.tick(b)
        }

        override fun isSynchronized() = isSync
    }

    abstract fun tick(b: Block)

    override fun getEnergyComponentType() = EnergyNetComponentType.CONSUMER

    override fun getCapacity() = capacity

    fun setCapacity(capacity: Int): SimpleTickingMachine {
        Preconditions.checkArgument(capacity > 0, "Capacity must be greater than 0")

        if (state == ItemState.UNREGISTERED) {
            this._energyCapacity = capacity
            return this
        } else {
            error("You cannot modify the capacity after the Item was registered.")
        }
    }

    fun setEnergyConsumption(energyConsumption: Int): SimpleTickingMachine {
        Preconditions.checkArgument(energyConsumption > 0, "Energy consumption must be greater than 0")

        if (state == ItemState.UNREGISTERED) {
            this._energyConsumption = energyConsumption
            return this
        } else {
            error("You cannot modify the energy consumption after the Item was registered.")
        }
    }
}
