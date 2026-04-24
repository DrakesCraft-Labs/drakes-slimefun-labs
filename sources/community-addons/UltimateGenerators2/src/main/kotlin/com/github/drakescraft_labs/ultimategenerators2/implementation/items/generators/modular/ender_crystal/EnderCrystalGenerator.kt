@file:Suppress("DEPRECATION")

package net.guizhanss.ultimategenerators2.implementation.items.generators.modular.ender_crystal

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems
import com.github.drakescraft_labs.slimefun4.implementation.operations.FuelOperation
import dev.drake.dough.blocks.BlockPosition
import com.github.drakescraft_labs.slimefun4.utils.ChestMenuUtils
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenu
import com.github.drakescraft_labs.slimefun4.legacy.api.inventory.BlockMenuPreset
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.implementation.items.abstracts.AGenerator
import net.guizhanss.ultimategenerators2.utils.BlockUtils
import net.guizhanss.ultimategenerators2.utils.consts.Keys
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EnderCrystal
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

class EnderCrystalGenerator(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : AGenerator(itemGroup, item, recipeType, recipe) {
    companion object {
        private val CRYSTAL_COUNT: MutableMap<BlockPosition, Int> = HashMap()

        private const val INFO_SLOT = 18
    }

    override fun setup(preset: BlockMenuPreset) {
        super.setup(preset)
        preset.addItem(INFO_SLOT, NOT_WORKING_ITEM, ChestMenuUtils.getEmptyClickHandler())
    }

    override val progressBar = ItemStack(Material.END_CRYSTAL)

    override fun registerDefaultFuelTypes() {
        registerFuel(7200, UGItems.RUNE_COMPLEX_ENDER, SlimefunItems.STONE_CHUNK)
    }

    override fun onPlace(e: BlockPlaceEvent, b: Block) {
        super.onPlace(e, b)
        val loc = BlockPosition(b)
        CRYSTAL_COUNT[loc] = 0
    }

    override fun onBreak(e: BlockBreakEvent, menu: BlockMenu) {
        super.onBreak(e, menu)
        val loc: Location = menu.location
        CRYSTAL_COUNT.remove(BlockPosition(loc))
        loc.world!!
            .getNearbyEntities(loc, 4.0, 4.0, 4.0) { entity: Entity -> entity.type == EntityType.END_CRYSTAL }
            .stream()
            .map { obj: Entity -> obj as EnderCrystal }
            .forEachOrdered { crystal: EnderCrystal ->
                try {
                    if (!crystal.isShowingBottom
                        && crystal.hasMetadata(Keys.ENHANCED_ENDER_CRYSTAL)
                        && crystal.beamTarget!!.block.location
                            .distance(BlockUtils.getBlockCenter(loc.add(0.0, -1.0, 0.0))) == 0.0
                    ) {
                        crystal.beamTarget = null
                    }
                } catch (ex: Exception) {
                    crystal.beamTarget = null
                }
            }
    }

    private fun getEnergyProduction(pos: BlockPosition): Int {
        return super.energyProduction * CRYSTAL_COUNT.getOrPut(pos) { 0 }
    }

    override fun getGeneratedOutput(l: Location, data: Config): Int {
        val inv: BlockMenu = BlockStorage.getInventory(l)
        val pos = BlockPosition(l)
        val operation: FuelOperation? = processor.getOperation(l)

        // TODO: Implement this
        // check for ender crystals
//        BlockPosition basePos = new BlockPosition(l.add(0, -1, 0));
//        AtomicInteger count = new AtomicInteger(0);
//        l.getWorld().getNearbyEntities(l, 4D, 4D, 4D, entity -> entity.getType() == EntityType.ENDER_CRYSTAL)
//            .stream()
//            .map(EnderCrystal.class::cast)
//            .forEachOrdered(crystal -> {
//                if (crystal.isShowingBottom() && crystal.getBeamTarget().getBlock().getLocation().distance(LocationUtils.getBlockCenter(basePos)) == 0D) {
//                    count.incrementAndGet();
//                    crystal.setBeamTarget(basePos.toLocation());
//                }
//            });
//        CRYSTAL_COUNT.put(pos, count.get());

        // check for fuel
        if (operation != null) {
            if (!operation.isFinished) {
                processor.updateProgressBar(inv, STATUS_SLOT, operation)

                if (isChargeable) {
                    val charge = getCharge(l, data)

                    if (capacity - charge >= getEnergyProduction(pos)) {
                        operation.addProgress(1)
                        return getEnergyProduction(pos)
                    }

                    return 0
                } else {
                    operation.addProgress(1)
                    return getEnergyProduction(pos)
                }
            } else {
                inv.replaceExistingItem(STATUS_SLOT, NOT_WORKING_ITEM)

                processor.endOperation(l)
                return 0
            }
        } else if (CRYSTAL_COUNT.getValue(pos) > 0) {
            val found: MutableMap<Int, Int> = HashMap()
            val fuel = findRecipe(inv, found)

            if (fuel != null) {
                for ((key, value) in found) {
                    inv.consumeItem(key, value)
                }

                processor.startOperation(l, FuelOperation(fuel.toSf()))
            }

            return 0
        } else {
            return 0
        }
    }
}
