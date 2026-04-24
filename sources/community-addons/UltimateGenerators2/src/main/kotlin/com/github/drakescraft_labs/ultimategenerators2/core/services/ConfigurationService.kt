package net.guizhanss.ultimategenerators2.core.services

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig
import net.guizhanss.ultimategenerators2.UltimateGenerators2

class ConfigurationService(plugin: UltimateGenerators2) {
    private val config = AddonConfig(plugin, "config.yml")

    var autoUpdate = true
        private set
    var debug = false
        private set
    var lang = "en"
        private set
    var glassElectricityTransmitterCraftAmount = 12
        private set
    var enderCrystalGeneratorEnergyPerCrystal = 128
        private set

    init {
        reload()
    }

    fun reload() {
        config.reload()
        config.addMissingKeys()

        autoUpdate = config.getBoolean("auto-update", true)
        debug = config.getBoolean("debug", false)
        lang = config.getString("lang", "en")!!
        glassElectricityTransmitterCraftAmount = config.getInt("glass-electricity-transmitter.craft-amount", 12)
        enderCrystalGeneratorEnergyPerCrystal = config.getInt("ender-crystal-generator.energy-per-crystal", 128)

        config.save()
    }
}
