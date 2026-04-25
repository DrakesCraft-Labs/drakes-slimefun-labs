package com.github.drakescraft_labs.customitemgen.generator

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.ItemState
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent
import com.github.drakescraft_labs.slimefun4.core.attributes.MachineProcessHolder
import com.github.drakescraft_labs.slimefun4.core.attributes.RecipeDisplayItem
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler
import com.github.drakescraft_labs.slimefun4.core.machines.MachineProcessor
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType
import com.github.drakescraft_labs.slimefun4.implementation.Slimefun
import com.github.drakescraft_labs.slimefun4.implementation.handlers.SimpleBlockBreakHandler
import com.github.drakescraft_labs.slimefun4.implementation.operations.CraftingOperation
import dev.drake.dough.inventory.InvUtils
import dev.drake.dough.items.CustomItemStack
import com.github.drakescraft_labs.slimefun4.libraries.dough.protection.Interaction
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils
import com.github.drakescraft_labs.slimefun4.utils.LoreBuilder
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.abstractItems.MachineRecipe
import com.github.drakescraft_labs.slimefun4.legacy.Objects.SlimefunItem.interfaces.InventoryBlock
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset
import com.github.drakescraft_labs.slimefun4.legacy.api.item_transport.ItemTransportFlow
import com.github.drakescraft_labs.customitemgen.CustomItemGenerators
import com.github.drakescraft_labs.customitemgen.data.GenEntry
import com.github.drakescraft_labs.customitemgen.data.Options
import com.github.drakescraft_labs.customitemgen.data.SFMachine
import com.github.drakescraft_labs.customitemgen.data.validate
import com.github.drakescraft_labs.customitemgen.events.CIGPreRunEvent
import com.github.drakescraft_labs.customitemgen.file.DisplayLoader
import com.github.drakescraft_labs.customitemgen.util.*
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.Validate
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Suppress("DEPRECATION")
class ItemGenerator(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>,
    val options: Options,
    val production: MutableList<GenEntry>
) : SlimefunItem(itemGroup, item, recipeType!!, recipe),
    InventoryBlock, EnergyNetComponent, MachineProcessHolder<CraftingOperation>, RecipeDisplayItem {

    companion object {
        const val KEY_POSITION = "current-position"
        const val KEY_CONSUMPTION = "current-consumption"
    }

    private val processor = MachineProcessor(this)

    var energyCapacity = -1
        private set

    val blockMenuPreset = object : BlockMenuPreset(this.id, inventoryTitle) {
        override fun init() = constructMenu(this)

        override fun getSlotsAccessedByItemTransport(flow: ItemTransportFlow): IntArray {
            return if (flow == ItemTransportFlow.INSERT) {
                inputSlots
            } else {
                outputSlots
            }
        }

        override fun newInstance(menu: BlockMenu, block: Block) {
            if (options.entryRandomizer) return

            val position = BlockStorage.getLocationInfo(block.location, KEY_POSITION) ?: "0"
            val baseItem = production[position.toInt()].recipe.input[0]
            menu.addItem(inputSlots[0], baseItem) { _: Player?, slot: Int, _: ItemStack?, _: ClickAction? ->
                val startPositionString = BlockStorage.getLocationInfo(block.location, KEY_POSITION) ?: "0"
                val startPosition = (startPositionString.toInt() + 1) % production.size
                BlockStorage.addBlockInfo(block, KEY_POSITION, startPosition.toString())

                menu.replaceExistingItem(slot, production[startPosition].recipe.input[0].clone().mark())
                false
            }
        }

        override fun canOpen(b: Block, p: Player): Boolean {
            if (p.hasPermission("slimefun.inventory.bypass")) {
                return true
            }

            return this@ItemGenerator.canUse(p, false) && (// Protection manager doesn't exist in unit tests
                    Slimefun.instance()!!.isUnitTest
                            || Slimefun.getProtectionManager()
                        .hasPermission(p, b, Interaction.INTERACT_BLOCK))
        }
    }

    init {
        processor.progressBar = options.progressBar

        addItemHandler(onBlockBreak(), onBlockPlace())
    }

    private fun createRandomizerItem(): ItemStack {
        val displayItem = ItemStack(Material.LIGHT)

        val allRecipes = production.map {
            val ditem = it.recipe.output[0]
            var name = ditem.displayOrDefault()

            val currentStyle = name.style()
            if (ditem.amount != 1) {
                name = name.append(" ${ditem.amount}".component()).style(currentStyle)
            }

            name = name.append(" §7".component())

            val powerPerSecond = Component.text(" " + LoreBuilder.powerPerSecond(it.energy * 2).replace('&', '§'))
            val duration = Component.text(" §8⇨ §7" + it.recipe.ticks / 2 + "s")

            "§8⇨ §7".component()
                .append(name)
                .append(powerPerSecond)
                .append(duration)
        }

        displayItem.editMeta {
            it.displayName("§eRandom Entries".component())
            it.lore(allRecipes)
        }

        return displayItem
    }

    private fun onBlockBreak(): BlockBreakHandler {
        return object : SimpleBlockBreakHandler() {
            override fun onBlockBreak(b: Block) {
                val inv = BlockStorage.getInventory(b)

                inv?.dropItems(b.location, *outputSlots)

                processor.endOperation(b)
            }
        }
    }

    private fun onBlockPlace(): BlockPlaceHandler {
        return object : BlockPlaceHandler(true) {
            override fun onPlayerPlace(p0: BlockPlaceEvent) {
                this@ItemGenerator.tick(p0.block)
            }
        }
    }

    override fun getMachineProcessor(): MachineProcessor<CraftingOperation> {
        return processor
    }

    private fun constructMenu(preset: BlockMenuPreset) {
        for (i in DisplayLoader.BORDER) {
            preset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler())
        }

        for (i in DisplayLoader.BORDER_IN) {
            preset.addItem(i, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler())
        }

        for (i in DisplayLoader.BORDER_OUT) {
            preset.addItem(i, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler())
        }

        preset.addItem(
            DisplayLoader.PROGRESS_SLOT,
            CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "),
            ChestMenuUtils.getEmptyClickHandler()
        )


        preset.addItem(
            inputSlots[0],
            if (options.entryRandomizer) {
                createRandomizerItem().mark()
            } else {
                ItemStack(Material.AIR)
            },
            ChestMenuUtils.getEmptyClickHandler()
        )


        val outputHandler = object : AdvancedMenuClickHandler {
            override fun onClick(p: Player, slot: Int, cursor: ItemStack, action: ClickAction): Boolean {
                return false
            }

            override fun onClick(
                e: InventoryClickEvent,
                p: Player,
                slot: Int,
                cursor: ItemStack?,
                action: ClickAction
            ): Boolean {
                return cursor?.type == null || cursor.type == Material.AIR
            }
        }
        for (i in outputSlots) {
            preset.addMenuClickHandler(i, outputHandler)
        }
    }

    val inventoryTitle: String
        get() = itemName


    fun setCapacity(capacity: Int): ItemGenerator {
        Validate.isTrue(capacity > 0, "The capacity must be greater than zero!")

        check(state == ItemState.UNREGISTERED) { "You cannot modify the capacity after the Item was registered." }

        this.energyCapacity = capacity
        return this
    }

    override fun register(addon: SlimefunAddon) {
        this.addon = addon
        super.register(addon)

        if (capacity <= 0) {
            warn("The capacity has not been configured correctly. The Item was disabled.")
            warn("Make sure to call '" + this.id + "#setEnergyCapacity(...)' before registering!")
        }
    }

    override fun getDisplayRecipes(): List<ItemStack> {
        return production.map { entry ->
            val clone = entry.recipe.output[0].clone()
            clone.addLore(
                "",
                LoreBuilder.powerPerSecond(entry.energy * 2).replace('&', '§'),
                "§8⇨ §eTime required: §7${entry.recipe.ticks / 2} s"
            )
        }
    }

    override fun getInputSlots(): IntArray {
        return DisplayLoader.INPUT_SLOT
    }

    override fun getOutputSlots(): IntArray {
        return DisplayLoader.OUTPUT_SLOTS
    }

    override fun getEnergyComponentType(): EnergyNetComponentType {
        return EnergyNetComponentType.CONSUMER
    }

    override fun getCapacity(): Int {
        return energyCapacity
    }

    fun addRecipe(seconds: Int, energy: Int, item: ItemStack) = registerRecipe(seconds, energy, item)

    fun registerRecipe(seconds: Int, energy: Int, item: ItemStack) {
        val recipe = MachineRecipe(seconds, arrayOf(item), arrayOf(item))
        val temp = GenEntry(recipe, energy)
        registerRecipe(temp)
    }

    private fun registerRecipe(entry: GenEntry) {
        Validate.isTrue(entry.recipe.input.size == 1, "ItemGenerator must have only 1 input")
        Validate.isTrue(entry.recipe.output.size == 1, "ItemGenerator must have only 1 output")
        Validate.isTrue(
            SlimefunUtils.isItemSimilar(entry.recipe.input[0], entry.recipe.output[0], true),
            "ItemGenerator must have same input and output"
        )

        Validate.isTrue(
            entry.energy < this.capacity,
            "$id the energy-capacity must always be above the energy production cost"
        )
        Validate.isTrue(
            entry.recipe.ticks > 0,
            "$id the required seconds of a recipe must be a positive non-zero integer"
        )

        production.add(entry)
    }


    override fun preRegister() {
        addItemHandler(object : BlockTicker() {
            override fun tick(b: Block, sf: SlimefunItem, data: Config) {
                this@ItemGenerator.tick(b)
            }

            override fun isSynchronized(): Boolean {
                return false
            }
        })
    }

    private fun checkValidators(b: Block): Boolean {
        val sfMachine = SFMachine(this, b)
        val execute = options.validators.validate(sfMachine)

        val event = CIGPreRunEvent(sfMachine, execute)
        Bukkit.getPluginManager().callEvent(event)

        return execute && !event.isCancelled
    }

    private fun tick(b: Block) {
        if (!checkValidators(b)) {
            return
        }

        val inv = BlockStorage.getInventory(b)
        var currentOperation = processor.getOperation(b)

        if (currentOperation == null) {
            val next = findNextRecipe(inv, b) ?: return
            currentOperation = CraftingOperation(next)
            processor.startOperation(b, currentOperation)

            // Fixes #3534 - Update indicator immediately
            processor.updateProgressBar(inv, DisplayLoader.PROGRESS_SLOT, currentOperation)
            return
        }

        if (!takeCharge(b.location)) return

        if (!currentOperation.isFinished) {
            processor.updateProgressBar(inv, DisplayLoader.PROGRESS_SLOT, currentOperation)
            currentOperation.addProgress(1)
            return
        }

        inv.replaceExistingItem(DisplayLoader.PROGRESS_SLOT, CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " "))
        for (output in currentOperation.results) {
            inv.pushItem(output.clone().demark(), *outputSlots)
        }

        processor.endOperation(b)
    }

    /**
     * This method will remove charge from a location if it is chargeable.
     *
     * @param l
     * location to try to remove charge from
     * @return Whether charge was taken if its chargeable
     */
    private fun takeCharge(l: Location): Boolean {
        if (!isChargeable) {
            return true
        }

        val charge = getCharge(l)
        val consumption = l.energyConsumption

        if (consumption == 0) {
            return true
        }

        if (charge < consumption) {
            return false
        }

        setCharge(l, charge - consumption)
        return true
    }

    private fun findNextRecipe(inv: BlockMenu, b: Block): MachineRecipe? {
        val location = b.location
        val recipe = if(options.entryRandomizer) {
            location.currentPosition = Random.nextInt(production.size)
            production[location.currentPosition].recipe
        } else {
            production[location.currentPosition].recipe
        }

        if (!InvUtils.fitAll(inv.toInventory(), recipe.output, *outputSlots)) {
            return null
        }

        location.energyConsumption = production[location.currentPosition].energy
        return recipe
    }

    var Location.energyConsumption : Int
        get() = BlockStorage.getLocationInfo(this, KEY_CONSUMPTION)?.toInt() ?: production[0].energy
        set(value) = BlockStorage.addBlockInfo(this, KEY_CONSUMPTION, value.toString())


    var Location.currentPosition: Int
        get() = BlockStorage.getLocationInfo(this, KEY_POSITION)?.toInt() ?: 0
        set(value) = BlockStorage.addBlockInfo(this, KEY_POSITION, value.toString())
}