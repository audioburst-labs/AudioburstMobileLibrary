package com.audioburst.library.di.providers

import co.touchlab.stately.concurrency.AtomicReference

internal interface Provider<T> {

    fun get(): T
}

internal fun <T> provider(creator: () -> T): Provider<T> =
    object : Provider<T> {
        override fun get(): T = creator()
    }

internal abstract class Singleton<T : Any>: Provider<T> {
    private val t: AtomicReference<T?> = AtomicReference(null)

    protected abstract fun creator(): T

    override fun get(): T =
        t.get() ?: creator().apply {
            t.set(this)
        }
}

internal fun <T : Any> singleton(creator: () -> T): Singleton<T> =
    object : Singleton<T>() {
        override fun creator(): T = creator()
    }
