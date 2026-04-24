package net.guizhanss.ultimategenerators2.core.services

import net.guizhanss.slimefuntranslation.api.config.TranslationConfiguration
import net.guizhanss.slimefuntranslation.api.config.TranslationConfigurationDefaults
import net.guizhanss.slimefuntranslation.api.config.TranslationConfigurationFields
import net.guizhanss.slimefuntranslation.utils.FileUtils
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class IntegrationService(private val plugin: UltimateGenerators2) {
    var slimefunTranslationEnabled = false
        private set

    init {
        slimefunTranslationEnabled = isPluginEnabled("SlimefunTranslation")
    }

    private fun isPluginEnabled(pluginName: String) = plugin.server.pluginManager.isPluginEnabled(pluginName)

    fun loadTranslations() {
        val fields = TranslationConfigurationFields.builder().items("items").build()
        val defaults = TranslationConfigurationDefaults.builder().name("UltimateGenerators2").build()
        val languages = FileUtils.listYamlFiles(File(plugin.dataFolder, "lang"))
        for (langFile in languages) {
            val file = File(plugin.dataFolder, "lang" + File.separator + langFile)
            val lang = langFile.replace(".yml", "")
            val fileConfig = YamlConfiguration.loadConfiguration(file)
            val cfg = TranslationConfiguration.fromFileConfiguration(lang, fileConfig, fields, defaults)
            cfg.ifPresent { it.register(plugin) }
        }
    }
}
