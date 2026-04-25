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
        // Tras shade-plugin, bStats debe cargarse con el paquete relocado (checkRelocation en 3.x).
        // Paper puede mezclar otra variante de bStats en runtime (p. ej. NoSuchMethodError al invocar);
        // las métricas son opcionales: no bloquear el enabling del addon.
        try {
            val metricsClass = Class.forName(
                "com.github.drakescraft_labs.ultimategenerators2.libs.bstats.bukkit.Metrics",
            )
            val intType = Integer.TYPE
            val pluginArg: Any = this
            val paramVariants = listOf(
                arrayOf(Plugin::class.java, intType),
                arrayOf(org.bukkit.plugin.java.JavaPlugin::class.java, intType),
            )
            for (paramTypes in paramVariants) {
                try {
                    val ctor = metricsClass.getConstructor(*paramTypes)
                    ctor.newInstance(pluginArg, 21567)
                    return
                } catch (_: ReflectiveOperationException) {
                    // siguiente variante
                } catch (_: LinkageError) {
                    // NoSuchMethodError u otro enlace frente a otra copia de bStats en el classloader
                    return
                }
            }
        } catch (e: Throwable) {
            logger.log(Level.FINE, "bStats no disponible o incompatible (omitido).", e)
        }
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
