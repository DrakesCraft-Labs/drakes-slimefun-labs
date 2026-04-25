package net.guizhanss.guizhanlib.kt.slimefun.items.builder

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

open class ItemRegistry(
    val plugin: JavaPlugin,
    val prefix: String = ""
)

class MaterialType(private val item: ItemStack) {
    fun convert(): ItemStack = item.clone()
}

fun Material.asMaterialType(): MaterialType = MaterialType(ItemStack(this))

fun ItemStack.asMaterialType(): MaterialType = MaterialType(this)

class RecipeBuilder {
    private val rows = mutableListOf<String>()
    private val ingredients = mutableMapOf<Char, ItemStack?>()

    operator fun String.unaryPlus() {
        rows += padEnd(3).take(3)
    }

    infix fun Char.means(item: Material?) {
        ingredients[this] = item?.let { ItemStack(it) }
    }

    infix fun Char.means(item: ItemStack?) {
        ingredients[this] = item?.clone()
    }

    infix fun Char.means(item: SlimefunItemStack?) {
        ingredients[this] = item?.clone()
    }

    fun build(): Array<ItemStack?> {
        val normalized = (rows + List(3 - rows.size) { "   " }).take(3)
        return normalized
            .flatMap { row -> row.map { ingredients[it] } }
            .toTypedArray()
    }
}

fun buildRecipe(block: RecipeBuilder.() -> Unit): Array<ItemStack?> {
    return RecipeBuilder().apply(block).build()
}
