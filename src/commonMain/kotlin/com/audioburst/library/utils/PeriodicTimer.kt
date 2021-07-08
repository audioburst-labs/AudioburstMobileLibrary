package com.audioburst.library.utils

import com.audioburst.library.models.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class PeriodicTimer(
    private val interval: Duration,
    private val scope: CoroutineScope,
) {

    private var currentTimerJob by nullableAtomic<Job>()
    private var isRunning by atomic(false)
    private val sharedFlow = MutableSharedFlow<Tick>()
    val timer: Flow<Tick> = sharedFlow.asSharedFlow()

    fun start() {
        if (!isRunning) {
            isRunning = true
            currentTimerJob = scope.launch {
                while (isRunning) {
                    delay(interval.milliseconds.toLong())
                    sharedFlow.emit(Tick)
                }
            }
        }
    }

    fun pause() {
        isRunning = false
        currentTimerJob?.cancel()
    }

    object Tick
}
