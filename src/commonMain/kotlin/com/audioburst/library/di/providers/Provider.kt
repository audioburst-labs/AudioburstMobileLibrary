package com.audioburst.library.di.providers

internal interface Provider<T> {

    fun get(): T
}

internal fun <T> provider(creator: () -> T): Provider<T> =
    object : Provider<T> {
        override fun get(): T = creator()
    }

internal abstract class Singleton<T : Any>: Provider<T> {
    private lateinit var t: T

    protected abstract fun creator(): T

    override fun get(): T =
        if (::t.isInitialized) {
            t
        } else {
            creator().apply {
                t = this
            }
        }
}

internal fun <T : Any> singleton(creator: () -> T): Singleton<T> =
    object : Singleton<T>() {
        override fun creator(): T = creator()
    }
