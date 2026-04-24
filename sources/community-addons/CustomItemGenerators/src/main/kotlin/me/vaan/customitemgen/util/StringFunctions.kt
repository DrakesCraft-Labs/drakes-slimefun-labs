package com.github.drakescraft_labs.customitemgen.util

import com.github.drakescraft_labs.slimefun4.implementation.Slimefun
import com.github.drakescraft_labs.customitemgen.CustomItemGenerators
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun String.isSlimefun() : Boolean {
    return this.startsWith('@')
}

fun String.getItemStack() : ItemStack {
    val split = this.split(" ")
    val amount = if (split.size <= 1) {
        1
    } else {
        try {
            split[1].toInt()
        } catch (e: NumberFormatException) {
            CustomItemGenerators.instance.getJavaPlugin().logger.warning("Item amount not parsed correctly, format is: '<@>ITEM AMOUNT'")
            1
        }
    }

    val stackEntry = split[0]
    if (isSlimefun()) {
        val sfItem = Slimefun.getRegistry().slimefunItemIds[stackEntry.drop(1)]
        sfItem ?: throw RuntimeException("Slimefun item $stackEntry not found")
        val returnSf = sfItem.item.clone()
        returnSf.amount = amount
        return returnSf
    }

    val material = Material.getMaterial(split[0])
    material ?: throw RuntimeException("Vanilla item $stackEntry not found")
    return ItemStack(material, amount)
}

fun String.component() : TextComponent {
    return Component.text(this)
}

fun Material.getDefaultName() : TextComponent {
    return this.toString()
        .lowercase()
        .replace('_', ' ')
        .capitalizeWords(' ')
        .component()
}

fun String.capitalizeWords(delimiter: Char) : String {
    return this.split(delimiter).joinToString(" ") { word ->
        word.replaceFirstChar { it.titlecase() }
    }
}

fun String.parseTimeRange() : IntRange {
    val integers = this.split("-")

    return integers[0].parseTime()..integers[1].parseTime()
}

fun String.parseTime() : Int {
    return when(this) {
        "day" -> 1000
        "noon" -> 6000
        "night" -> 13000
        "midnight" -> 18000
        else -> this.toInt()
    }
}