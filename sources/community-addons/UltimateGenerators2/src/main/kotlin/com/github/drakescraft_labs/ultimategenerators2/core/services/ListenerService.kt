package net.guizhanss.ultimategenerators2.core.services

import net.guizhanss.ultimategenerators2.UltimateGenerators2
import net.guizhanss.ultimategenerators2.implementation.listeners.ClassifiedItemListener
import net.guizhanss.ultimategenerators2.implementation.listeners.EnderCrystalEnhancerListener
import net.guizhanss.ultimategenerators2.implementation.listeners.TranslationsLoadListener

class ListenerService(plugin: UltimateGenerators2) {
    init {
        ClassifiedItemListener(plugin)
        EnderCrystalEnhancerListener(plugin)
        TranslationsLoadListener(plugin)
    }
}

