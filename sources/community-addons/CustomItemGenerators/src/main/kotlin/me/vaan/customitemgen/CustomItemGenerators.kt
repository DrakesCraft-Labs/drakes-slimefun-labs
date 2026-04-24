package com.github.drakescraft_labs.customitemgen

import com.github.drakescraft_labs.sf4k.AbstractAddon
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup
import com.github.drakescraft_labs.customitemgen.file.DisplayLoader
import com.github.drakescraft_labs.customitemgen.util.getBlock
import org.bstats.bukkit.Metrics
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class CustomItemGenerators : AbstractAddon() {

    companion object {
        lateinit var group: ItemGroup
            private set
        lateinit var instance: CustomItemGenerators
            private set
        lateinit var metrics: Metrics
            private set
        lateinit var pluginFolder: File
            private set
        lateinit var key: NamespacedKey
            private set
    }

    override suspend fun onEnableAsync() {
        instance = this
        pluginFolder = this.dataFolder
        metrics = Metrics(this, 23674)
        key = NamespacedKey(this, "mark")
        saveDefaultConfig()

        val stack = config.getBlock("GROUP.item")
        val key = NamespacedKey(this, "main_group")
        group = ItemGroup(key, stack)

        DisplayLoader.loadFiles(config)
        //Load after every addon item has been loaded
        server.pluginManager.registerEvents(FinalizeListener, this)
    }

    override suspend fun onDisableAsync() {

    }

    override fun getJavaPlugin(): JavaPlugin {
        return this
    }

    override fun getBugTrackerURL(): String {
        return "https://github.com/Intybyte/CustomItemGenerators/issues"
    }

    fun genFile(path: String): File {
        val file = File(this.dataFolder, path)
        if (!file.exists()) saveResource(path,false)
        return file
    }
}
