package io.github.addoncommunity.galactifun.util.items

import io.github.addoncommunity.galactifun.Galactifun2
import io.github.addoncommunity.galactifun.util.bukkit.legacyDefaultColor
import io.github.addoncommunity.galactifun.util.bukkit.miniMessageToLegacy
import io.github.addoncommunity.galactifun.util.general.RequiredProperty
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType
import com.github.drakescraft_labs.slimefun4.utils.SlimefunUtils
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Constructor

class ItemBuilder {

    var name: String by RequiredProperty()
    var material: MaterialType by RequiredProperty()
    var id: String by RequiredProperty(setter = { "GF2_" + it.uppercase() })

    var category: ItemGroup by RequiredProperty()
    var recipeType: RecipeType by RequiredProperty()
    var recipe: Array<out ItemStack?> by RequiredProperty()

    private val lore = mutableListOf<String>()

    operator fun String.unaryPlus() {
        lore += this.miniMessageToLegacy()
    }

    fun build(clazz: Class<out SlimefunItem>, vararg otherArgs: Any?): SlimefunItemStack {
        val sfi = SlimefunItemStack(
            id,
            material.convert(),
            name.miniMessageToLegacy().legacyDefaultColor('f'),
            *lore.map { it.legacyDefaultColor('7') }.toTypedArray()
        )
        val ctor = findStandardSlimefunConstructor(clazz, otherArgs.size)
            ?: error("No constructor (ItemGroup, SlimefunItemStack, RecipeType, ItemStack[], …) for ${clazz.name} with ${otherArgs.size} trailing args")
        ctor.isAccessible = true
        try {
            val item = ctor.newInstance(category, sfi, recipeType, recipe, *otherArgs) as SlimefunItem
            item.register(Galactifun2)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Failed to construct ${clazz.name}", e)
        }
        return sfi
    }

    private fun findStandardSlimefunConstructor(
        clazz: Class<out SlimefunItem>,
        trailingArgCount: Int
    ): Constructor<out SlimefunItem>? {
        val expected = 4 + trailingArgCount
        @Suppress("UNCHECKED_CAST")
        return clazz.declaredConstructors.asSequence()
            .filter { it.parameterCount == expected && !it.isSynthetic }
            .map { it as Constructor<out SlimefunItem> }
            .firstOrNull { ctor ->
                val p = ctor.parameterTypes
                ItemGroup::class.java.isAssignableFrom(p[0]) &&
                    SlimefunItemStack::class.java.isAssignableFrom(p[1]) &&
                    RecipeType::class.java.isAssignableFrom(p[2]) &&
                    p[3].isArray &&
                    org.bukkit.inventory.ItemStack::class.java.isAssignableFrom(p[3].componentType)
            }
    }
}

sealed interface MaterialType {

    fun convert(): org.bukkit.inventory.ItemStack

    class Material(private val material: org.bukkit.Material) : MaterialType {
        override fun convert() = ItemStack(material)
    }

    class ItemStack(private val itemStack: org.bukkit.inventory.ItemStack) : MaterialType {
        override fun convert() = itemStack
    }

    class Head(private val texture: String) : MaterialType {
        override fun convert() = SlimefunUtils.getCustomHead(texture)
    }
}

val Material.materialType get() = MaterialType.Material(this)
val ItemStack.materialType get() = MaterialType.ItemStack(this)

inline fun <reified I : SlimefunItem> buildSlimefunItem(
    vararg otherArgs: Any?,
    builder: ItemBuilder.() -> Unit
): SlimefunItemStack {
    return ItemBuilder().apply(builder).build(I::class.java, *otherArgs)
}

inline fun buildSlimefunItem(
    builder: ItemBuilder.() -> Unit
): SlimefunItemStack {
    return ItemBuilder().apply(builder).build(SlimefunItem::class.java)
}