package com.audioburst.library.models

import kotlinx.coroutines.CoroutineDispatcher

internal data class AppDispatchers(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher
)
