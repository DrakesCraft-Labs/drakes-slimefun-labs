package net.guizhanss.ultimategenerators2.implementation.listeners

import dev.drake.dough.items.ItemUtils
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.utils.consts.Keys
import org.bukkit.entity.EnderCrystal
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue

class EnderCrystalEnhancerListener(plugin: UltimateGenerators2) : Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onUse(e: PlayerInteractEntityEvent) {
        if (e.rightClicked !is EnderCrystal) {
            return
        }

        val crystal: EnderCrystal = e.rightClicked as EnderCrystal
        val slot: EquipmentSlot = e.hand
        val item: ItemStack = e.player.inventory.getItem(slot)!!
        if (!SlimefunUtils.isItemSimilar(item, UGItems.ENDER_CRYSTAL_ENHANCER, false)) {
            return
        }

        if (!crystal.isShowingBottom && !crystal.hasMetadata(Keys.ENHANCED_ENDER_CRYSTAL)) {
            crystal.setMetadata(
                Keys.ENHANCED_ENDER_CRYSTAL,
                FixedMetadataValue(UltimateGenerators2.getInstance(), true)
            )
            crystal.isGlowing = true
            ItemUtils.consumeItem(item, true)
        }
    }
}
