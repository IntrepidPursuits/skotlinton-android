package io.intrepid.skotlinton.utils

interface TimeProvider {
    val currentTimeMillis: Long
}

object SystemTimeProvider : TimeProvider {
    override val currentTimeMillis: Long
        get() = System.currentTimeMillis()
}
