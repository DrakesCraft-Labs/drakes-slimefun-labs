package net.guizhanss.ultimategenerators2.utils

import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition
import org.bukkit.Location
import org.bukkit.block.Block

object BlockUtils {
    fun takeCharge(block: EnergyNetComponent, location: Location, consumption: Int): Boolean {
        if (!block.isChargeable) {
            return false
        }

        val charge = block.getCharge(location)
        if (charge < consumption) {
            return false
        }

        block.setCharge(location, charge - consumption)
        return true
    }

    fun takeCharge(block: EnergyNetComponent, position: BlockPosition, consumption: Int): Boolean {
        return takeCharge(block, position.toLocation(), consumption)
    }

    fun getBlockCenter(l: Location): Location {
        return getBlockCenter(l.block)
    }

    fun getBlockCenter(block: Block): Location {
        val l = block.location
        return Location(l.world, l.blockX + 0.5, l.blockY + 0.5, l.blockZ + 0.5)
    }
}
