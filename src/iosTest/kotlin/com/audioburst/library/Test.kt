package com.audioburst.library

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

actual val testCoroutineContext: CoroutineContext = Dispatchers.Unconfined

actual fun runTest(block: suspend () -> Unit) {
    runBlocking { block() }
}
