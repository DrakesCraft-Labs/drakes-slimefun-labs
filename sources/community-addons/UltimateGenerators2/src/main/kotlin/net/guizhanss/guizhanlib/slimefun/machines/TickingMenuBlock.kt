package net.guizhanss.guizhanlib.slimefun.machines

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.DirtyChestMenu
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

/**
 * Drake-compatible replacement for GuizhanLib's menu block base.
 *
 * The upstream GuizhanLib class is compiled against legacy Slimefun package
 * names. This local source version keeps UltimateGenerators2 on Drake types.
 */
abstract class TickingMenuBlock(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : SlimefunItem(itemGroup, item, recipeType, recipe), InventoryBlock {

    init {
        addItemHandler(
            object : BlockBreakHandler(false, false) {
                override fun onPlayerBreak(e: BlockBreakEvent, item: ItemStack, drops: MutableList<ItemStack>) {
                    BlockStorage.getInventory(e.block)?.let { onBreak(e, it) }
                }
            },
            object : BlockPlaceHandler(false) {
                override fun onPlayerPlace(e: BlockPlaceEvent) {
                    onPlace(e, e.blockPlaced)
                }
            },
            object : BlockTicker() {
                override fun isSynchronized() = false

                override fun tick(b: Block, item: SlimefunItem, data: Config) {
                    BlockStorage.getInventory(b)?.let { tick(b, it) }
                }
            }
        )
    }

    override fun postRegister() {
        object : BlockMenuPreset(id, itemName) {
            override fun init() {
                this@TickingMenuBlock.setup(this)
            }

            override fun newInstance(menu: BlockMenu, b: Block) {
                this@TickingMenuBlock.onNewInstance(menu, b)
            }

            override fun canOpen(b: Block, p: Player): Boolean {
                return this@TickingMenuBlock.canUse(p, false)
            }

            override fun getSlotsAccessedByItemTransport(flow: ItemTransportFlow): IntArray {
                return IntArray(0)
            }

            override fun getSlotsAccessedByItemTransport(
                menu: DirtyChestMenu,
                flow: ItemTransportFlow,
                item: ItemStack
            ): IntArray {
                return when (flow) {
                    ItemTransportFlow.INSERT -> this@TickingMenuBlock.getInputSlots(menu, item)
                    ItemTransportFlow.WITHDRAW -> this@TickingMenuBlock.outputSlots
                }
            }
        }
    }

    protected open fun onBreak(e: BlockBreakEvent, menu: BlockMenu) {
        val location = menu.location
        menu.dropItems(location, *inputSlots)
        menu.dropItems(location, *outputSlots)
    }

    protected open fun onPlace(e: BlockPlaceEvent, b: Block) {
        // default no-op
    }

    protected open fun onNewInstance(menu: BlockMenu, b: Block) {
        // default no-op
    }

    protected open fun getInputSlots(menu: DirtyChestMenu, item: ItemStack): IntArray {
        return inputSlots
    }

    protected abstract fun setup(preset: BlockMenuPreset)

    protected abstract fun tick(block: Block, menu: BlockMenu)
}
