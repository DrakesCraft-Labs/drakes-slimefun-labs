package net.guizhanss.fastmachines.implementation.items.materials

import com.github.drakescraft_labs.slimefun4.api.events.PlayerRightClickEvent
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.core.handlers.ItemUseHandler
import com.github.drakescraft_labs.slimefun4.core.services.sounds.SoundEffect
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems
import com.github.drakescraft_labs.slimefun4.implementation.items.blocks.UnplaceableBlock
import dev.drake.dough.items.ItemUtils
import net.guizhanss.guizhanlib.minecraft.utils.InventoryUtil
import org.bukkit.inventory.ItemStack
import javax.annotation.Nonnull

class StackedAncientPedestal(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : UnplaceableBlock(itemGroup, itemStack, recipeType, recipe) {

    @Nonnull
    override fun getItemHandler(): ItemUseHandler {
        return ItemUseHandler { e: PlayerRightClickEvent ->
            e.cancel()
            val p = e.player
            val pedestal = SlimefunItems.ANCIENT_PEDESTAL
            if (pedestal.item!!.isDisabledIn(p.world)) {
                return@ItemUseHandler
            }
            ItemUtils.consumeItem(e.item, true)
            val output = pedestal.clone()
            output.amount = 4
            InventoryUtil.push(p, output)
            SoundEffect.ANCIENT_ALTAR_START_SOUND.playFor(p)
        }
    }
}
