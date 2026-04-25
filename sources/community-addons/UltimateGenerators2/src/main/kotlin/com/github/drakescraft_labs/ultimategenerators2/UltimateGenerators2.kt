package net.guizhanss.ultimategenerators2

import com.google.common.base.Preconditions
import dev.drake.dough.updater.BlobBuildUpdater
import net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater
import net.guizhanss.ultimategenerators2.core.services.ConfigurationService
import net.guizhanss.ultimategenerators2.core.services.IntegrationService
import net.guizhanss.ultimategenerators2.core.services.ListenerService
import net.guizhanss.ultimategenerators2.core.services.LocalizationService
import net.guizhanss.ultimategenerators2.implementation.setup.UGItemSetup
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.logging.Level

class UltimateGenerators2 : AbstractAddon(
    "ybw0014", "UltimateGenerators2", "master", "auto-update"
) {
    companion object {
        private const val DEFAULT_LANG = "en"

        private fun inst() = getInstance<UltimateGenerators2>()

        fun getInstance() = inst()

        @JvmStatic
        fun configService() = inst().configService

        @JvmStatic
        fun localization() = inst().localization

        @JvmStatic
        fun integrationService() = inst().integrationService

        @JvmStatic
        fun scheduler() = getScheduler()

        @JvmStatic
        fun debug(message: String, vararg args: Any?) {
            Preconditions.checkNotNull(message, "message cannot be null")

            if (inst().debugEnabled) {
                inst().logger.log(Level.INFO, "[DEBUG] $message", args)
            }
        }
    }

    lateinit var configService: ConfigurationService
        private set
    lateinit var localization: LocalizationService
        private set
    lateinit var integrationService: IntegrationService
        private set
    private var debugEnabled = false

    override fun enable() {
        log(Level.INFO, "=====================")
        log(Level.INFO, " UltimateGenerators2 ")
        log(Level.INFO, "      by ybw0014     ")
        log(Level.INFO, "=====================")

        // config
        configService = ConfigurationService(this)

        // debug
        debugEnabled = configService.debug

        // localization
        log(Level.INFO, "Loading language...")
        val lang = configService.lang
        localization = LocalizationService(this, file)
        localization.addLanguage(lang)
        if (lang != DEFAULT_LANG) {
            localization.addLanguage(DEFAULT_LANG)
        }
        log(Level.INFO, localization.getString("console.loaded-language"), lang)

        // items
        UGItemSetup.setup(this)

        // integrations
        integrationService = IntegrationService(this)

        // listeners
        ListenerService(this)

        // metrics
        setupMetrics()
    }

    override fun disable() {
        // do nothing here
    }

    private fun setupMetrics() {
        Metrics(this, 21567)
    }

    override fun autoUpdate() {
        if (pluginVersion.startsWith("Dev")) {
            BlobBuildUpdater(this, file, githubRepo).start()
        } else if (pluginVersion.startsWith("Build")) {
            try {
                // use updater in lib plugin
                val clazz = Class.forName("net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater")
                val updaterStart = clazz.getDeclaredMethod(
                    "start",
                    Plugin::class.java,
                    File::class.java,
                    String::class.java,
                    String::class.java,
                    String::class.java
                )
                updaterStart.invoke(null, this, file, githubUser, githubRepo, githubBranch)
            } catch (ignored: Exception) {
                // use updater in lib
                GuizhanBuildsUpdater.start(this, file, githubUser, githubRepo, githubBranch)
            }
        }
    }
}
