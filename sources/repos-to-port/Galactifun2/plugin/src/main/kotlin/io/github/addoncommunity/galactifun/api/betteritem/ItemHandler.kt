package io.github.addoncommunity.galactifun.api.betteritem

import com.github.drakescraft_labs.slimefun4.api.items.ItemHandler
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ItemHandler(val handler: KClass<out ItemHandler>)
