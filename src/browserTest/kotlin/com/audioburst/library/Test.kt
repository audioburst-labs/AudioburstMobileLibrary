package com.audioburst.library

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext

private val mainScope = MainScope()
actual val testCoroutineContext: CoroutineContext = mainScope.coroutineContext

actual fun runTest(block: suspend () -> Unit): dynamic =
    mainScope.promise {
        block()
    }
