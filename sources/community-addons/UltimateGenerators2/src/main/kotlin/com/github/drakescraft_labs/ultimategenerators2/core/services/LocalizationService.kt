package net.guizhanss.ultimategenerators2.core.services

import net.guizhanss.guizhanlib.slimefun.addon.SlimefunLocalization
import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.utils.FileUtils.listYmlFilesInJar
import java.io.File
import java.text.MessageFormat

class LocalizationService(
    private val plugin: UltimateGenerators2,
    private val jarFile: File
) : SlimefunLocalization(plugin) {
    companion object {
        private const val FOLDER_NAME = "lang"
    }

    init {
        extractTranslations()
    }

    private fun extractTranslations() {
        val translationsFolder = File(plugin.dataFolder, FOLDER_NAME)
        if (!translationsFolder.exists()) {
            translationsFolder.mkdirs()
        }
        val translationFiles = listYmlFilesInJar(jarFile, "${FOLDER_NAME}/")
        for (translationFile in translationFiles) {
            val filePath = FOLDER_NAME + File.separator + translationFile
            val file = File(plugin.dataFolder, filePath)
            if (file.exists()) {
                continue
            }
            plugin.saveResource(filePath, true)
        }
    }

    fun getString(key: String, vararg args: Any?): String {
        return MessageFormat.format(getString(key), *args)
    }
}
