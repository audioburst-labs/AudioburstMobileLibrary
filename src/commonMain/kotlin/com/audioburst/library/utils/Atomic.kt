package com.audioburst.library.utils

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicLong
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import kotlin.reflect.KProperty

internal fun <T> atomic(initialValue: T): AtomicReference<T> = AtomicReference(initialValue)
internal fun <T> nullableAtomic(): AtomicReference<T?> = atomic(null)

internal operator fun <T> AtomicReference<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value
internal operator fun <T> AtomicReference<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) { this.value = value }

internal fun atomic(initialValue: Boolean): AtomicBoolean = AtomicBoolean(initialValue)

internal operator fun AtomicBoolean.getValue(thisRef: Any?, property: KProperty<*>): Boolean = value
internal operator fun AtomicBoolean.setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) { this.value = value }

internal fun atomic(initialValue: Long): AtomicLong = AtomicLong(initialValue)

internal operator fun AtomicLong.getValue(thisRef: Any?, property: KProperty<*>): Long = value
internal operator fun AtomicLong.setValue(thisRef: Any?, property: KProperty<*>, value: Long) { this.value = value }