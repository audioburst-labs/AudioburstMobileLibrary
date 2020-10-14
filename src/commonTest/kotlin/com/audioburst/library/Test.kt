package com.audioburst.library

import kotlin.coroutines.CoroutineContext

expect val testCoroutineContext: CoroutineContext

expect fun runTest(block: suspend () -> Unit)
