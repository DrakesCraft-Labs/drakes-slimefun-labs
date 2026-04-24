package net.guizhanss.ultimategenerators2.implementation.listeners

import com.github.drakescraft_labs.slimefun4.utils.FireworkUtils
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.implementation.UGItems
import net.guizhanss.ultimategenerators2.utils.BlockUtils
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

class ClassifiedItemListener(plugin: UltimateGenerators2) : Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun kapa(e: EntityDamageEvent) {
        if (e.cause == EntityDamageEvent.DamageCause.LAVA && e.entity is Item) {
            val itemEntity = e.entity as Item
            val item = itemEntity.itemStack
            if (item.amount == 8 && SlimefunUtils.isItemSimilar(item, UGItems.LAMBDA_ELECTRICITY_STORAGE, false)) {
                e.isCancelled = true
                val l: Location = BlockUtils.getBlockCenter(itemEntity.location)
                itemEntity.remove()

                generateItem(
                    l,
                    UGItems.KAPA_ELECTRICITY_STORAGE,
                    arrayOf(Color.BLACK, Color.BLACK, Color.GRAY, Color.SILVER)
                )
            }
        }
    }

    @EventHandler
    fun phi(e: EntityDamageEvent) {
        if (e.cause == EntityDamageEvent.DamageCause.LIGHTNING && e.entity is Item) {
            val itemEntity = e.entity as Item
            val item = itemEntity.itemStack
            if (item.amount == 8 && SlimefunUtils.isItemSimilar(item, UGItems.KAPA_ELECTRICITY_STORAGE, false)) {
                e.isCancelled = true
                val l: Location = BlockUtils.getBlockCenter(itemEntity.location)
                itemEntity.remove()

                generateItem(
                    l,
                    UGItems.PHI_ELECTRICITY_STORAGE,
                    arrayOf(Color.WHITE, Color.WHITE, Color.GRAY, Color.SILVER)
                )
            }
        }
    }

    private fun generateItem(l: Location, item: ItemStack, fireworkColors: Array<Color>) {
        l.world!!.playSound(l, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f)
        val newItem = l.world!!.dropItemNaturally(l, item)
        l.world!!.spawnParticle(Particle.EXPLOSION, l, 3)
        for (color in fireworkColors) {
            FireworkUtils.launchFirework(l, color)
        }
        newItem.isGlowing = true
        newItem.isInvulnerable = true
        newItem.isCustomNameVisible = true
        newItem.customName = item.itemMeta!!.displayName
    }
}
