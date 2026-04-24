package com.github.drakescraft_labs.customitemgen.util

import com.github.drakescraft_labs.customitemgen.CustomItemGenerators
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


fun ItemStack.mark() : ItemStack {
    val copy = this.clone()
    val meta = copy.itemMeta
    meta.persistentDataContainer.set(CustomItemGenerators.key, PersistentDataType.BOOLEAN, true)
    copy.itemMeta = meta
    return copy
}

fun ItemStack.demark() : ItemStack {
    val meta = this.itemMeta
    meta.persistentDataContainer.remove(CustomItemGenerators.key)
    this.itemMeta = meta
    return this
}

fun ItemStack.addLore(vararg s : String) : ItemStack {
    val mapped = s.map(String::component).toTypedArray()
    return this.addLore(*mapped)
}

fun ItemStack.addLore(vararg s : TextComponent) : ItemStack {
    this.editMeta { m ->
        val base = m.lore() ?: emptyList()
        m.lore(base + s)
    }

    return this
}

fun ItemStack.displayOrDefault() : TextComponent {
     if (this.itemMeta.hasDisplayName()) {
        val display = this.itemMeta.displayName()!! as TextComponent
        if (display.content().isEmpty()) {
            return display.children()[0] as TextComponent
        }
        return display
     }

    return this.type.getDefaultName()
        .color(NamedTextColor.WHITE)
        .decoration(TextDecoration.ITALIC, false)
}