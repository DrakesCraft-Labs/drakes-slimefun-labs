package net.guizhanss.ultimategenerators2.utils

import java.util.concurrent.ThreadLocalRandom

object RandomUtils {
    fun nextBoolean(): Boolean {
        return ThreadLocalRandom.current().nextBoolean()
    }

    fun nextFloat(origin: Float, bound: Float): Float {
        return ThreadLocalRandom.current().nextFloat(origin, bound)
    }
}
