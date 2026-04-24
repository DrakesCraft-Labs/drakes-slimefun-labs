package com.github.drakescraft_labs.customitemgen.file

import org.bukkit.configuration.file.FileConfiguration

object DisplayLoader {
    lateinit var INPUT_SLOT: IntArray
    lateinit var OUTPUT_SLOTS: IntArray
    @JvmField // kotlin be trippin, it tought this was a property
    var PROGRESS_SLOT: Int = 0

    lateinit var BORDER: IntArray
    lateinit var BORDER_IN: IntArray
    lateinit var BORDER_OUT: IntArray

    fun loadFiles(config: FileConfiguration) {
        val section = config.getConfigurationSection("display") ?: throw RuntimeException("Display not found")

        for (field in this.javaClass.fields) {
            val name = field.name
            if (name == "INSTANCE") continue

            if (field.type.isArray) {
                val array = section.getIntegerList(name).toIntArray()
                field.set(null, array)
            } else {
                val integer = section.getInt(name)
                field.set(null, integer)
            }
        }
    }
}