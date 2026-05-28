package net.guizhanss.ultimategenerators2.implementation.items.generators.modular.ender_crystal

import com.google.common.base.Preconditions
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.libraries.dough.blocks.BlockPosition
import com.github.drakescraft_labs.slimefun4.legacy.api.BlockStorage
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.implementation.items.abstracts.SimpleTickingMachine
import net.guizhanss.ultimategenerators2.utils.BlockUtils
import net.guizhanss.ultimategenerators2.utils.RandomUtils
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class EnderCrystalStabilizer(
    itemGroup: ItemGroup,
    item: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<ItemStack?>
) : SimpleTickingMachine(itemGroup, item, recipeType, recipe) {
    companion object {
        private val FAILED_TIMES: MutableMap<BlockPosition, Int> = HashMap()
    }

    override fun tick(b: Block) {
        val pos = BlockPosition(b)
        if (!BlockUtils.takeCharge(this, pos, energyConsumption)) {
            FAILED_TIMES[pos] = FAILED_TIMES.getOrPut(pos) { 0 } + 1

            if (FAILED_TIMES.getValue(pos) >= 5) {
                UltimateGenerators2.scheduler().run {
                    FAILED_TIMES.remove(pos)
                    BlockStorage.clearBlockInfo(b)
                    b.type = Material.AIR
                    b.world.createExplosion(BlockUtils.getBlockCenter(b), 0f, false)
                    if (RandomUtils.nextBoolean()) {
                        b.type = Material.LAVA
                    }
                    if (RandomUtils.nextBoolean()) {
                        b.world.createExplosion(
                            BlockUtils.getBlockCenter(b),
                            RandomUtils.nextFloat(1f, 8f),
                            RandomUtils.nextBoolean()
                        )
                    }
                }
            }
        }
    }

    override fun register(addon: SlimefunAddon) {
        Preconditions.checkArgument(capacity > 0, "Energy capacity must be greater than 0")
        Preconditions.checkArgument(energyConsumption > 0, "Energy consumption must be greater than 0")
        Preconditions.checkArgument(
            capacity >= energyConsumption,
            "Energy capacity must be greater than or equal to energy consumption"
        )
        super.register(addon)
    }
}
